package com.firstlinecode.basalt.protocol.core.stream;

import java.util.ArrayList;
import java.util.List;

import com.firstlinecode.basalt.protocol.core.Protocol;

public class Features {
	public static final Protocol PROTOCOL = new Protocol("http://etherx.jabber.org/streams", "features");
	
	private List<Feature> features;

	public List<Feature> getFeatures() {
		if (features == null) {
			features = new ArrayList<>();
		}
		
		return features;
	}

	public void setFeatures(List<Feature> features) {
		this.features = features;
	}
}
