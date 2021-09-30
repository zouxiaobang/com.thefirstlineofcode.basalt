package com.thefirstlineofcode.basalt.xeps.muc.admin;

import java.util.ArrayList;
import java.util.List;

import com.thefirstlineofcode.basalt.oxm.convention.annotations.Array;
import com.thefirstlineofcode.basalt.oxm.convention.annotations.ProtocolObject;
import com.thefirstlineofcode.basalt.protocol.core.Protocol;
import com.thefirstlineofcode.basalt.xeps.muc.user.Item;

@ProtocolObject(namespace="http://jabber.org/protocol/muc#admin", localName="query")
public class MucAdmin {
	public static final Protocol PROTOCOL = new Protocol("http://jabber.org/protocol/muc#admin", "query");
	
	@Array(Item.class)
	private List<Item> items;

	public List<Item> getItems() {
		if (items == null)
			items = new ArrayList<>();
		
		return items;
	}

	public void setItems(List<Item> items) {
		this.items = items;
	}
	
}
