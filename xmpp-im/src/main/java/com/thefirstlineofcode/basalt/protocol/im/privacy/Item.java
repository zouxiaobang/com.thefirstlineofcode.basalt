package com.thefirstlineofcode.basalt.protocol.im.privacy;

public class Item {
	public enum Type {
		JID,
		GROUP,
		SUBSCRIPTION
	}
	
	public enum Action {
		ALLOW,
		DENY
	}
	
	private Type type;
	private String value;
	private Action action;
	private int order;
	
	private Message message;
	private PresenceIn presenceIn;
	private PresenceOut presenceOut;
	private Iq iq;
	
	public Type getType() {
		return type;
	}
	
	public void setType(Type type) {
		this.type = type;
	}
	
	public String getValue() {
		return value;
	}
	
	public void setValue(String value) {
		this.value = value;
	}
	
	public Action getAction() {
		return action;
	}
	
	public void setAction(Action action) {
		this.action = action;
	}
	
	public int getOrder() {
		return order;
	}
	
	public void setOrder(int order) {
		this.order = order;
	}

	public Message getMessage() {
		return message;
	}

	public void setMessage(Message message) {
		this.message = message;
	}

	public PresenceIn getPresenceIn() {
		return presenceIn;
	}

	public void setPresenceIn(PresenceIn presenceIn) {
		this.presenceIn = presenceIn;
	}

	public PresenceOut getPresenceOut() {
		return presenceOut;
	}

	public void setPresenceOut(PresenceOut presenceOut) {
		this.presenceOut = presenceOut;
	}

	public Iq getIq() {
		return iq;
	}

	public void setIq(Iq iq) {
		this.iq = iq;
	}
	
}
