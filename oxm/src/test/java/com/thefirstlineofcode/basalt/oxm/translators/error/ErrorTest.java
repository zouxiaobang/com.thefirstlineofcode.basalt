package com.thefirstlineofcode.basalt.oxm.translators.error;

import org.junit.Before;
import org.junit.Test;

import com.thefirstlineofcode.basalt.oxm.IOxmFactory;
import com.thefirstlineofcode.basalt.oxm.OxmService;
import com.thefirstlineofcode.basalt.oxm.TestData;
import com.thefirstlineofcode.basalt.oxm.convention.NamingConventionParserFactory;
import com.thefirstlineofcode.basalt.oxm.convention.NamingConventionTranslatorFactory;
import com.thefirstlineofcode.basalt.oxm.convention.annotations.ProtocolObject;
import com.thefirstlineofcode.basalt.protocol.core.IError;
import com.thefirstlineofcode.basalt.protocol.core.LangText;
import com.thefirstlineofcode.basalt.protocol.core.Protocol;
import com.thefirstlineofcode.basalt.protocol.core.ProtocolChain;
import com.thefirstlineofcode.basalt.protocol.core.stanza.error.BadRequest;
import com.thefirstlineofcode.basalt.protocol.core.stanza.error.StanzaError;
import com.thefirstlineofcode.basalt.protocol.core.stream.error.StreamError;

import junit.framework.Assert;

public class ErrorTest {
	private IOxmFactory oxmFactory;
	
	@Before
	public void before() {
		oxmFactory = OxmService.createStandardOxmFactory();
		
		oxmFactory.register(
				ProtocolChain.
					first(StreamError.PROTOCOL).
					next(EscapeYourData.PROTOCOL),
				new NamingConventionParserFactory<>(
						EscapeYourData.class)
				);
		
		oxmFactory.register(
				ProtocolChain.
					first(StanzaError.PROTOCOL).
					next(TooManyParameters.PROTOCOL),
				new NamingConventionParserFactory<>(
						TooManyParameters.class)
				);
		
		oxmFactory.register(EscapeYourData.class,
				new NamingConventionTranslatorFactory<>(
						EscapeYourData.class)
				);
		oxmFactory.register(TooManyParameters.class,
				new NamingConventionTranslatorFactory<>(
						TooManyParameters.class)
				);
	}
	
	@Test
	public void parseStreamError() {
		IError error = new StreamError();
		error.setDefinedCondition("not-well-formed");
		error.setText(new LangText("Some special application diagnostic information!", "en"));
		error.setApplicationSpecificCondition(new EscapeYourData());
		
		String errorMessage = oxmFactory.translate(error);
		
		error = (IError)oxmFactory.parse(errorMessage);
		Assert.assertNotNull(error);
		Assert.assertTrue(error instanceof StreamError);
		
		Assert.assertNotNull(error.getDefinedCondition());
		Assert.assertEquals("not-well-formed", error.getDefinedCondition());
		Assert.assertNotNull(error.getText());
		Assert.assertEquals("en", error.getText().getLang());
		Assert.assertEquals("Some special application diagnostic information!", error.getText().getText());
		Assert.assertTrue(error.getApplicationSpecificCondition() instanceof EscapeYourData);
	}
	
	@ProtocolObject(namespace="application-ns", localName="escape-your-data")
	public static class EscapeYourData {
		public static final Protocol PROTOCOL = new Protocol("application-ns", "escape-your-data");
	}
	
	@Test
	public void parseStanzaError() {
		String originalMessage = TestData.getData(this.getClass(), "originalMessage");
		String senderMessage = TestData.getData(this.getClass(), "senderMessage");
		
		StanzaError error = new BadRequest();
		error.setId("some-id");
		error.setApplicationSpecificCondition(new TooManyParameters());
		error.setOriginalMessage(originalMessage);
		
		String errorMessage = oxmFactory.translate(error);
		
		Object obj = oxmFactory.parse(errorMessage);
		Assert.assertNotNull(obj);
		Assert.assertTrue(obj instanceof StanzaError);
		
		error = (StanzaError)obj;
		Assert.assertEquals("some-id", error.getId());
		Assert.assertEquals(StanzaError.Kind.IQ, error.getKind());
		Assert.assertEquals(senderMessage, error.getSenderMessage());
		Assert.assertNotNull(error.getDefinedCondition());
		Assert.assertEquals("bad-request", error.getDefinedCondition());
		Assert.assertNull(error.getText());
		Assert.assertTrue(error.getApplicationSpecificCondition() instanceof TooManyParameters);
	}
	
	@ProtocolObject(namespace="application-ns", localName="too-many-parameters")
	public static class TooManyParameters {
		public static final Protocol PROTOCOL = new Protocol("application-ns", "too-many-parameters");
	}
}
