package com.firstlinecode.basalt.oxm.android;

import com.firstlinecode.basalt.oxm.parsing.BadMessageException;
import org.xmlpull.v1.XmlPullParser;

public final class ParsingUtils {
	
	private ParsingUtils() {}

	public static BadMessageException newParsingException(XmlPullParser xmlPullParser, String info) {
		return new BadMessageException(String.format("%s. offset: %d", info, xmlPullParser.getColumnNumber()));
	}

	public static StringBuilder appendText(XmlPullParser xmlPullParser, StringBuilder text) {
		String textFragment = xmlPullParser.getText().trim();
		// if the characters are white space, ignore them
		if ("".equals(textFragment)) {
			return text;
		}

		if (text == null) {
			text = new StringBuilder();
		}

		text.append(textFragment);

		return text;
	}
}
