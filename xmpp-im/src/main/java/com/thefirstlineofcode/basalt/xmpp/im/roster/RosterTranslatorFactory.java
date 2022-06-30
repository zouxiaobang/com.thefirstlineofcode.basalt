package com.thefirstlineofcode.basalt.xmpp.im.roster;

import com.thefirstlineofcode.basalt.oxm.Attribute;
import com.thefirstlineofcode.basalt.oxm.Attributes;
import com.thefirstlineofcode.basalt.oxm.Value;
import com.thefirstlineofcode.basalt.oxm.translating.IProtocolWriter;
import com.thefirstlineofcode.basalt.oxm.translating.ITranslatingFactory;
import com.thefirstlineofcode.basalt.oxm.translating.ITranslator;
import com.thefirstlineofcode.basalt.oxm.translating.ITranslatorFactory;
import com.thefirstlineofcode.basalt.xmpp.core.Protocol;
import com.thefirstlineofcode.basalt.xmpp.im.roster.Item.Ask;
import com.thefirstlineofcode.basalt.xmpp.im.roster.Item.Subscription;

public class RosterTranslatorFactory implements ITranslatorFactory<Roster> {
	private static final ITranslator<Roster> translator = new RosterTranslator();

	@Override
	public Class<Roster> getType() {
		return Roster.class;
	}

	@Override
	public ITranslator<Roster> create() {
		return translator;
	}
	
	private static class RosterTranslator implements ITranslator<Roster> {

		@Override
		public Protocol getProtocol() {
			return Roster.PROTOCOL;
		}

		@Override
		public String translate(Roster roster, IProtocolWriter writer, ITranslatingFactory translatingFactory) {
			writer.writeProtocolBegin(Roster.PROTOCOL); // roster
			
			if (roster.getItems().length != 0) {
				for (Item item : roster.getItems()) {
					Subscription subscription = item.getSubscription();
					Ask ask = item.getAsk();
					writer.writeElementBegin("item"); // item
					writer.writeAttributes(new Attributes().
							add(new Attribute("jid", item.getJid().toString())).
							add(new Attribute("name", item.getName())).
							add(new Attribute("subscription", subscription == null ? null : subscription.toString().toLowerCase())).
							add(new Attribute("ask", ask == null ? null : ask.toString().toLowerCase())).
							get()
					);
					
					if (item.getGroups().size() != 0) {
						for (String group : item.getGroups()) {
							writer.writeTextOnly("group", Value.create(group)); //group
						}
					}
					
					writer.writeElementEnd(); // end of item
				}
			}
			
			writer.writeProtocolEnd(); // end of roster
			
			return writer.getDocument();
		}
		
	}

}
