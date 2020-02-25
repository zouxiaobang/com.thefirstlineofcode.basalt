package com.firstlinecode.basalt.xeps.xdata;

import java.util.ArrayList;
import java.util.List;

import com.firstlinecode.basalt.oxm.convention.annotations.Array;

public class Item {
	@Array(type=Field.class, elementName="field")
	private List<Field> fields;

	public List<Field> getFields() {
		if (fields == null) {
			fields = new ArrayList<>();
		}
		
		return fields;
	}

	public void setFields(List<Field> fields) {
		this.fields = fields;
	}
	
}
