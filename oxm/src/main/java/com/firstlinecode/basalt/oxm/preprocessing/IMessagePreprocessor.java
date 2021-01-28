package com.firstlinecode.basalt.oxm.preprocessing;

import com.firstlinecode.basalt.protocol.core.ProtocolException;

public interface IMessagePreprocessor {
	String[] process(byte[] bytes) throws OutOfMaxBufferSizeException, ProtocolException;
	void clear();
	void setMaxBufferSize(int maxBufferSize);
	int getMaxBufferSize();
	String[] getMessages();
}
