package com.firstlinecode.basalt.oxm.translating;

import java.util.List;

import org.junit.Test;

import com.firstlinecode.basalt.protocol.core.Protocol;
import com.firstlinecode.basalt.protocol.core.stanza.Iq;
import com.firstlinecode.basalt.protocol.im.stanza.Message;
import com.firstlinecode.basalt.oxm.Attribute;
import com.firstlinecode.basalt.oxm.Attributes;
import com.firstlinecode.basalt.oxm.OxmService;
import com.firstlinecode.basalt.oxm.TestData;
import com.firstlinecode.basalt.oxm.translating.IProtocolWriter;
import com.firstlinecode.basalt.oxm.xep.ibb.TMessageData;

import junit.framework.Assert;

public class ProtocolWriterTest {
	
	@Test
	public void testEmbedded() {
		String dataText = TestData.getData(this.getClass(), "dataText");
		String messageMessage = TestData.getData(this.getClass(), "messageMessage");
		
		IProtocolWriter writer = OxmService.createProtocolWriterFactory().create();
		
		writer.writeProtocolBegin(Message.PROTOCOL).
			writeAttributes(createMessageAttributes()).
			writeProtocolBegin(TMessageData.PROTOCOL).
			writeAttributes(createMessageDataAttributes()).
			writeText(dataText).
			writeProtocolEnd().
			writeProtocolEnd();
		
		Assert.assertEquals(messageMessage, writer.getDocument());
	}
	
	@Test
	public void testTextOnlyArray() {
		String iqMessage = TestData.getData(this.getClass(), "iqMessage");
		
		IProtocolWriter writer = OxmService.createProtocolWriterFactory().create();
		
		writer.writeProtocolBegin(Iq.PROTOCOL).
			writeAttributes(createIqAttributes()).
			writeProtocolBegin(new Protocol("jabber:iq:roster", "query")).
			writeElementBegin("item").
			writeAttributes(createItemAttributes()).
			writeTextOnly("group", "Friends").
			writeTextOnly("group", "Lovers").
			writeElementEnd().
			writeProtocolEnd().
			writeProtocolEnd();
		
		Assert.assertEquals(iqMessage, writer.getDocument());
	}

	private List<Attribute> createMessageDataAttributes() {
		return new Attributes().
			add("seq", 0).
			add("sid", "i781hf64").
			get();
	}

	private List<Attribute> createMessageAttributes() {
		return new Attributes().
				add("from", "romeo@montague.net/orchard").
				add("to", "juliet@capulet.com/balcony").
				add("id", "dsw71gj3").
				get();
	}
	
	private List<Attribute> createIqAttributes() {
		return new Attributes().
				add("from", "juliet@example.com/chamber").
				add("type", "set").
				add("id", "roster_3").
				get();
	}
	
	private List<Attribute> createItemAttributes() {
		return new Attributes().
			add("jid", "romeo@example.net").
			add("name", "Romeo").
			add("subscription", "both").
			get();
	}
}
