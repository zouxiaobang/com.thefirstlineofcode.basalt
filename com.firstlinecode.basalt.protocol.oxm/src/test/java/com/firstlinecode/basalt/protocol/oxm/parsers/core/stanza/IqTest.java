package com.firstlinecode.basalt.protocol.oxm.parsers.core.stanza;

import junit.framework.Assert;

import com.firstlinecode.basalt.protocol.core.ProtocolChain;
import com.firstlinecode.basalt.protocol.core.ProtocolException;
import com.firstlinecode.basalt.protocol.core.stanza.Iq;
import com.firstlinecode.basalt.protocol.core.stanza.error.BadRequest;
import com.firstlinecode.basalt.protocol.core.stanza.error.StanzaError;
import com.firstlinecode.basalt.protocol.oxm.IOxmFactory;
import com.firstlinecode.basalt.protocol.oxm.OxmService;
import com.firstlinecode.basalt.protocol.oxm.TestData;
import com.firstlinecode.basalt.protocol.oxm.convention.NamingConventionParserFactory;
import com.firstlinecode.basalt.protocol.oxm.parsing.FlawedProtocolObject;
import com.firstlinecode.basalt.protocol.oxm.xep.ibb.TMessageData;

import org.junit.Before;
import org.junit.Test;

public class IqTest {
	private IOxmFactory oxmFactory;
	
	@Before
	public void before() {
		oxmFactory = OxmService.createStandardOxmFactory();
		
		oxmFactory.register(
				ProtocolChain.
					first(Iq.PROTOCOL).
					next(TMessageData.PROTOCOL),
				new NamingConventionParserFactory<>(
						TMessageData.class)
			);
	}
	
	@Test
	public void parseNoEmbeddedObjectFound() {
		String noEmbeddedObjectFoundIqMessage = TestData.getData(this.getClass(), "noEmbeddedObjectFoundIqMessage");
		try {
			oxmFactory.parse(noEmbeddedObjectFoundIqMessage);
			Assert.fail();
		} catch (ProtocolException e) {
			// should run to here
			Assert.assertTrue(e.getError() instanceof BadRequest);
			Assert.assertTrue(e.getError() instanceof StanzaError);
			Assert.assertEquals("some-id", ((StanzaError)e.getError()).getId());
			Assert.assertTrue("No embedded object found.".equals(e.getError().getText().getText()));
		}
		
		oxmFactory.unregister(ProtocolChain.first(Iq.PROTOCOL).next(TMessageData.PROTOCOL));
		String messageMessage = TestData.getData(this.getClass(), "messageMessage");
		
		Object obj = oxmFactory.parse(messageMessage);
		Assert.assertTrue(FlawedProtocolObject.isFlawed(obj));
	}
	
	@Test
	public void parseEmbedded() {
		String dataText = TestData.getData(this.getClass(), "dataText");
		String messageMessage = TestData.getData(this.getClass(), "messageMessage");
		
		Object obj = oxmFactory.parse(messageMessage);
		Assert.assertTrue(obj instanceof Iq);
		
		Iq iq = (Iq)obj;
		Assert.assertEquals("romeo@montague.net/orchard", iq.getFrom().toString());
		Assert.assertEquals("juliet@capulet.com/balcony", iq.getTo().toString());
		Assert.assertEquals("dsw71gj3", iq.getId());
		
		Assert.assertNotNull(iq.getObject());
		Assert.assertTrue(iq.getObject() instanceof TMessageData);
		TMessageData data = (TMessageData)iq.getObject();
		Assert.assertEquals("i781hf64", data.getSid());
		Assert.assertEquals(0, data.getSeq());
		Assert.assertEquals(dataText, data.getText());
	}
}
