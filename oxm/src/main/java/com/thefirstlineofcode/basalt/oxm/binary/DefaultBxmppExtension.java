package com.thefirstlineofcode.basalt.oxm.binary;

public class DefaultBxmppExtension extends BxmppExtension {
	public static final String STRING_STREAM_STREAM = "stream:stream";
	public static final String STRING_DEFAULT_NAMESPACE = "jabber:client";
	public static final String STRING_STREAM_NAMESPACE = "http://etherx.jabber.org/streams";
	public static final ReplacementBytes REPLACEMENT_BYTES_STREAM_STREAM = new ReplacementBytes((byte)0xff, (byte)0xfb);
	
	public DefaultBxmppExtension() {
		namespace = new Namespace(null, null);
		
		try {
			register(REPLACEMENT_BYTES_STREAM_STREAM, STRING_STREAM_STREAM);
			register(Constants.REPLACEMENT_BYTES_XMLNS, Constants.KEYWORD_XMLNS);			
		} catch (ReduplicateBxmppReplacementException e) {
			throw new RuntimeException("???Predefined BXMPP replacments hava existed?", e);
		}
	}
	
	@Override
	public void register(ReplacementBytes replacementBytes, String keyword)
			throws ReduplicateBxmppReplacementException {
		if (ReplacementBytes.isNamespaceReplacementBytes(replacementBytes)) {
			throw new IllegalArgumentException("Registering namespace not allowed.");
		}
		
		super.register(replacementBytes, keyword);
	}
}
