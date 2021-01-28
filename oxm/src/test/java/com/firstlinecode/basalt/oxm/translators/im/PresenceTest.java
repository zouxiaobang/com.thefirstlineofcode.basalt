package com.firstlinecode.basalt.oxm.translators.im;

import junit.framework.Assert;

import com.firstlinecode.basalt.protocol.core.LangText;
import com.firstlinecode.basalt.protocol.im.stanza.Presence;
import com.firstlinecode.basalt.oxm.IOxmFactory;
import com.firstlinecode.basalt.oxm.OxmService;
import com.firstlinecode.basalt.oxm.TestData;

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
		String presenceMessage = oxmFactory.translate(new Presence());
		Assert.assertEquals(TestData.getData(this.getClass(), "translatingResult1"),
				presenceMessage);
		
		Presence presence = new Presence();
		presence.setShow(Presence.Show.DND);
		
		presenceMessage = oxmFactory.translate(presence);
		Assert.assertEquals(TestData.getData(this.getClass(), "translatingResult2"),
				presenceMessage);
		
		presence = new Presence();
		presence.setLang("en");
		presence.setShow(Presence.Show.DND);
		presence.getStatuses().add(new LangText("Hello, world!"));
		presence.getStatuses().add(new LangText("哈罗，世界！", "zh"));
		
		presenceMessage = oxmFactory.translate(presence);
		
		Object obj = oxmFactory.parse(presenceMessage);
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
