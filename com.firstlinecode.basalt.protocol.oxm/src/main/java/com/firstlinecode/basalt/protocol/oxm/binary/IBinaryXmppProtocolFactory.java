package com.firstlinecode.basalt.protocol.oxm.binary;

import com.firstlinecode.basalt.protocol.oxm.preprocessing.IMessagePreprocessor;

public interface IBinaryXmppProtocolFactory {
	public IBinaryXmppProtocolConverter createConverter();
	public IMessagePreprocessor createPreprocessor();
}
