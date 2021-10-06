package com.thefirstlineofcode.basalt.oxm.binary;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;

import com.thefirstlineofcode.basalt.oxm.TestData;

import junit.framework.Assert;

public class BinaryXmppProtocolConverterTest {
	private IBinaryXmppProtocolConverter bxmppProtocolConverter;
	private String openStreamXmlMessage;
	private byte[] openStreamBinaryMessage;
	private String closeStreamXmlMessage;
	private byte[] closeStreamBinaryMessage;
	private String complexXmlMessage;
	private byte[] complexBinaryMessage;
	private String simpleXmlMessage;
	private byte[] simpleBinaryMessage;
	
	@Before
	public void before() throws ReduplicateBxmppReplacementException {
		bxmppProtocolConverter = BinaryXmppProtocolConverterFactory.getInstance();
		
		openStreamXmlMessage = TestData.getData(BinaryXmppProtocolConverterTest.class, "openStreamXmlMessage");
		String openStreamBinaryMessageString = TestData.getData(BinaryXmppProtocolConverterTest.class, "openStreamBinaryMessage");
		openStreamBinaryMessage = TestData.toBinaryBytes(openStreamBinaryMessageString);
		
		closeStreamXmlMessage = TestData.getData(BinaryXmppProtocolConverterTest.class, "closeStreamXmlMessage");
		String closeStreamBinaryMessageString = TestData.getData(BinaryXmppProtocolConverterTest.class, "closeStreamBinaryMessage");
		closeStreamBinaryMessage = TestData.toBinaryBytes(closeStreamBinaryMessageString);
		
		complexXmlMessage = TestData.getData(BinaryXmppProtocolConverterTest.class, "complexXmlMessage");
		String complexBinaryMessageString = TestData.getData(BinaryXmppProtocolConverterTest.class, "complexBinaryMessage");
		complexBinaryMessage = TestData.toBinaryBytes(complexBinaryMessageString);
		
		simpleXmlMessage = TestData.getData(BinaryXmppProtocolConverterTest.class, "simpleXmlMessage");
		String simpleBinaryMessageString = TestData.getData(BinaryXmppProtocolConverterTest.class, "simpleBinaryMessage");
		simpleBinaryMessage = TestData.toBinaryBytes(simpleBinaryMessageString);
	}
	
	@Test
	public void toXml() {
		Assert.assertEquals(complexXmlMessage, bxmppProtocolConverter.toXml(complexBinaryMessage));
		Assert.assertEquals(simpleXmlMessage, bxmppProtocolConverter.toXml(simpleBinaryMessage));
		Assert.assertEquals(openStreamXmlMessage, bxmppProtocolConverter.toXml(openStreamBinaryMessage));
		Assert.assertEquals(closeStreamXmlMessage, bxmppProtocolConverter.toXml(closeStreamBinaryMessage));
	}
	
	@Test
	public void toBinary() throws UnsupportedEncodingException {
		Assert.assertTrue(Arrays.equals(complexBinaryMessage, bxmppProtocolConverter.toBinary(complexXmlMessage)));
		Assert.assertTrue(Arrays.equals(simpleBinaryMessage, bxmppProtocolConverter.toBinary(simpleXmlMessage)));
		Assert.assertTrue(Arrays.equals(openStreamBinaryMessage, bxmppProtocolConverter.toBinary(openStreamXmlMessage)));
		Assert.assertTrue(Arrays.equals(closeStreamBinaryMessage, bxmppProtocolConverter.toBinary(closeStreamXmlMessage)));
	}
	
}
