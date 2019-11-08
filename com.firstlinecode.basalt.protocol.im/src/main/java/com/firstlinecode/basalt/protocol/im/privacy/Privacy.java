package com.firstlinecode.basalt.protocol.im.privacy;

import java.util.ArrayList;
import java.util.List;

public class Privacy {
	private List<PrivacyList> lists;
	private String eDefault;
	private String active;
	
	public List<PrivacyList> getLists() {
		if (lists == null)
			lists = new ArrayList<>();
		
		return lists;
	}
	
	public void setLists(List<PrivacyList> lists) {
		this.lists = lists;
	}
	
	public String geteDefault() {
		return eDefault;
	}
	
	public void seteDefault(String eDefault) {
		this.eDefault = eDefault;
	}
	
	public String getActive() {
		return active;
	}
	
	public void setActive(String active) {
		this.active = active;
	}
	
}
