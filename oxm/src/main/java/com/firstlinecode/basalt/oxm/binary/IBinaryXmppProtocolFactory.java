package com.firstlinecode.basalt.oxm.binary;

import com.firstlinecode.basalt.oxm.preprocessing.IMessagePreprocessor;

public interface IBinaryXmppProtocolFactory {
	public IBinaryXmppProtocolConverter createConverter();
	public IMessagePreprocessor createPreprocessor();
}
