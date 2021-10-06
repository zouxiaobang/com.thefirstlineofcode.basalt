package com.thefirstlineofcode.basalt.oxm.binary;

/*public class IdentifyBytes {
	private ReplacementBytes namespace;
	private ReplacementBytes localName;
	
	public IdentifyBytes(ReplacementBytes namespace, ReplacementBytes localName) {
		this.namespace = namespace;
		this.localName = localName;
	}
	
	@Override
	public int hashCode() {
		return 31 * namespace.hashCode() + localName.hashCode();
	}
	
	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof IdentifyBytes)) {
			return false;
		}
		
		IdentifyBytes other = (IdentifyBytes)obj;
		
		return namespace.equals(other.namespace) && localName.equals(other.localName);
	}
	
	@Override
	public String toString() {
		return String.format("%s[%s:%s]", IdentifyBytes.class.getSimpleName(), namespace.toString(), localName.toString());
	}
}
*/