package com.firstlinecode.basalt.oxm.android;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

/**
 * @author xb.zou
 * @date 2020/2/19
 * @option
 */
public class XmlParserFactory implements IXmlParserFactory {
    private org.xmlpull.v1.XmlPullParserFactory xmlPullParserFactory;

    public XmlParserFactory() throws XmlPullParserException {
        xmlPullParserFactory = createXmlPullParserFactory();
    }

    private org.xmlpull.v1.XmlPullParserFactory createXmlPullParserFactory() throws XmlPullParserException {
        org.xmlpull.v1.XmlPullParserFactory factory = org.xmlpull.v1.XmlPullParserFactory.newInstance();
        return factory;
    }

    @Override
    public XmlPullParser createParserWrapper(String message) throws XmlPullParserException {
        xmlPullParserFactory.setNamespaceAware(true);
        XmlPullParser parser = xmlPullParserFactory.newPullParser();
        // 转换成输入流形式进行解析
        InputStream msgInputStream = parseInputStream(message);
        parser.setInput(msgInputStream, String.valueOf(StandardCharsets.UTF_8));

        return parser;
    }

    private InputStream parseInputStream(String message) {
        return new ByteArrayInputStream(message.getBytes(StandardCharsets.UTF_8));
    }
}
