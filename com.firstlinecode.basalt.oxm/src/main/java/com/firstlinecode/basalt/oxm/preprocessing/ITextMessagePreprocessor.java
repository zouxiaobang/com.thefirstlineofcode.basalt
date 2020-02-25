package com.firstlinecode.basalt.oxm.preprocessing;

import com.firstlinecode.basalt.protocol.core.ProtocolException;

public interface ITextMessagePreprocessor {
	String[] process(char[] chars, int readChars) throws OutOfMaxBufferSizeException, ProtocolException;
	String[] getMessages();
	void setMaxBufferSize(int maxBufferSize);
	int getMaxBufferSize();
	char[] getBuffer();
	void resetBuffer();
	void clear();
}