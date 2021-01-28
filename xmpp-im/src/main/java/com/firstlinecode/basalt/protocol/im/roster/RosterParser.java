package com.firstlinecode.basalt.protocol.im.roster;

import java.util.List;

import com.firstlinecode.basalt.protocol.core.JabberId;
import com.firstlinecode.basalt.protocol.core.ProtocolException;
import com.firstlinecode.basalt.protocol.core.stanza.error.BadRequest;
import com.firstlinecode.basalt.protocol.im.roster.Item.Ask;
import com.firstlinecode.basalt.protocol.im.roster.Item.Subscription;
import com.firstlinecode.basalt.oxm.Attribute;
import com.firstlinecode.basalt.oxm.Value;
import com.firstlinecode.basalt.oxm.annotations.Parser;
import com.firstlinecode.basalt.oxm.annotations.ProcessAttributes;
import com.firstlinecode.basalt.oxm.annotations.ProcessText;
import com.firstlinecode.basalt.oxm.parsing.IParsingContext;

@Parser(namespace="query", localName="jabber:iq:roster", objectType=Roster.class)
public class RosterParser {
	@ProcessAttributes("/item")
	public void processItemAttributes(IParsingContext<Roster> context, List<Attribute> attributes) {
		Item item = new Item();
		
		for (Attribute attribute : attributes) {
			if ("jid".equals(attribute.getName())) {
				item.setJid(JabberId.parse(attribute.getValue().toString()));
			} else if ("name".equals(attribute.getName())) {
				item.setName(attribute.getValue().toString());
			} else if ("subscription".equals(attribute.getName())) {
				try {
					item.setSubscription(Subscription.valueOf(attribute.getValue().toString().toUpperCase()));
				} catch (Exception e) {
					throw new ProtocolException(new BadRequest(String.format("Invalid value '%s' for attribute 'subscription'.",
							attribute.getValue().toString())), e);
				}
			} else if ("ask".equals(attribute.getName())) {
				if ("subscribe".equals(attribute.getValue().toString())) {
					item.setAsk(Ask.SUBSCRIBE);
				} else {
					throw new ProtocolException(new BadRequest(String.format("Invalid value '%s' for attribute 'ask'.",
							attribute.getValue().toString())));
				}
			} else {
				throw new ProtocolException(new BadRequest(String.format("Unknown attribute %s.", attribute.getName())));
			}
		}
		
		context.getObject().addOrUpdate(item);
	}
	
	@ProcessText("/item/group")
	public void processGroup(IParsingContext<Roster> context, Value<?> text) {
		Item[] items = context.getObject().getItems();
		Item current = items[items.length - 1];
		
		current.getGroups().add(text.getString());
	}
}
