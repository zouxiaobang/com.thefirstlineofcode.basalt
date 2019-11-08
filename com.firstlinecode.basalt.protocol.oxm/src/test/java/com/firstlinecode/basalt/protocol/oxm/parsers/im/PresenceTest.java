package com.firstlinecode.basalt.protocol.oxm.parsers.im;

import junit.framework.Assert;

import com.firstlinecode.basalt.protocol.core.LangText;
import com.firstlinecode.basalt.protocol.im.stanza.Presence;
import com.firstlinecode.basalt.protocol.oxm.IOxmFactory;
import com.firstlinecode.basalt.protocol.oxm.OxmService;
import com.firstlinecode.basalt.protocol.oxm.TestData;

import org.junit.Before;
import org.junit.Test;

public class PresenceTest {
	private IOxmFactory oxmFactory;
	
	@Before
	public void before() {
		oxmFactory = OxmService.createStandardOxmFactory();
	}
	
	@Test
	public void parsePresence() {
		String presenceMessage = TestData.getData(this.getClass(), "presenceMessage1");
		Object obj = oxmFactory.parse(presenceMessage);
		Assert.assertTrue(obj instanceof Presence);
		
		Presence presence = (Presence)obj;
		Assert.assertNull(presence.getObject());
		
		presenceMessage = TestData.getData(this.getClass(), "presenceMessage2");
		obj = oxmFactory.parse(presenceMessage);
		Assert.assertTrue(obj instanceof Presence);
		
		presence = (Presence)obj;
		Assert.assertEquals(Presence.Show.DND, presence.getShow());
		
		presenceMessage = TestData.getData(this.getClass(), "presenceMessage3");
		obj = oxmFactory.parse(presenceMessage);
		Assert.assertTrue(obj instanceof Presence);
		
		presence = (Presence)obj;
		Assert.assertEquals(Presence.Show.DND, presence.getShow());
		Assert.assertEquals(2, presence.getStatuses().size());
		Assert.assertEquals(new LangText("Hello, world!", null),
				presence.getStatuses().get(0));
		Assert.assertEquals(new LangText("哈罗，世界！", "zh"),
				presence.getStatuses().get(1));
	}
}
