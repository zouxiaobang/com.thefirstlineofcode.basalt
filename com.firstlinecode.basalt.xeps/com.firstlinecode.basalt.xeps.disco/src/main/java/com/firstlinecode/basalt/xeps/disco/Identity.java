package com.firstlinecode.basalt.xeps.disco;

import com.firstlinecode.basalt.protocol.oxm.convention.validation.annotations.NotNull;

public class Identity {
	@NotNull
	private String category;
	private String name;
	@NotNull
	private String type;
	
	public Identity() {}
	
	public Identity(String category, String type) {
		this.category = category;
		this.type = type;
	}
	
	public String getCategory() {
		return category;
	}
	
	public void setCategory(String category) {
		this.category = category;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getType() {
		return type;
	}
	
	public void setType(String type) {
		this.type = type;
	}
	
	@Override
	public int hashCode() {
		int hash = 7;
		
		hash += 31 * hash + (category == null ? 0 : category.hashCode());
		hash += 31 * hash + (name == null ? 0 : name.hashCode());
		hash += 31 * hash + (type == null ? 0 : type.hashCode());
		
		return hash;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj == null)
			return false;
		
		if (obj == this)
			return true;
		
		if (obj instanceof Identity) {
			Identity other = (Identity)obj;
			
			if (category == null) {
				if (other.category != null)
					return false;
			} else {
				if (!category.equals(other.category))
					return false;
			}
			
			if (name == null) {
				if (other.name != null)
					return false;
			} else {
				if (!name.equals(other.name))
					return false;
			}
			
			if (type == null) {
				if (other.type != null)
					return false;
			} else {
				if (!type.equals(other.type))
					return false;
			}
			
			return true;
		}
		
		return false;
	}
	
}
