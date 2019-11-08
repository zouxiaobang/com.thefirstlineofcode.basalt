package com.firstlinecode.basalt.protocol.im.privacy;

import java.util.ArrayList;
import java.util.List;

public class PrivacyList {
	private String name;
	private List<Item> items;
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public List<Item> getItems() {
		if (items == null)
			items = new ArrayList<>();
		
		return items;
	}
	
	public void setItems(List<Item> items) {
		this.items = items;
	}
	
}
