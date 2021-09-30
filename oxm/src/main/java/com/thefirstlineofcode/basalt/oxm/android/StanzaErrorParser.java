package com.thefirstlineofcode.basalt.oxm.android;

import com.thefirstlineofcode.basalt.oxm.parsing.BadMessageException;
import com.thefirstlineofcode.basalt.oxm.parsing.IParsingContext;
import com.thefirstlineofcode.basalt.oxm.parsing.IParsingFactory;
import com.thefirstlineofcode.basalt.protocol.core.Protocol;
import com.thefirstlineofcode.basalt.protocol.core.stanza.Iq;
import com.thefirstlineofcode.basalt.protocol.core.stanza.Stanza;
import com.thefirstlineofcode.basalt.protocol.core.stanza.error.StanzaError;
import com.thefirstlineofcode.basalt.protocol.im.stanza.Message;
import com.thefirstlineofcode.basalt.protocol.im.stanza.Presence;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;

public class StanzaErrorParser {
    public StanzaError parse(IParsingFactory parsingFactory, IParsingContext<?> context,
                             XmlPullParser xmlPullParser) throws IOException, XmlPullParserException {
        return doParse(parsingFactory, context, xmlPullParser);
    }

    private StanzaError doParse(IParsingFactory parsingFactory, IParsingContext<?> context,
                                XmlPullParser xmlPullParser) throws IOException, XmlPullParserException {
        String senderXml = null, errorXml;

        int senderXmlStart = xmlPullParser.getColumnNumber();

        int eventType = xmlPullParser.next();

        if (eventType != XmlPullParser.START_TAG) {
            throw new BadMessageException("Should be a start tag here.");
        }

        Protocol protocol = new Protocol(xmlPullParser.getNamespace(), xmlPullParser.getName());

        int errorXmlStart = senderXmlStart;

        while (!StanzaError.PROTOCOL.equals(protocol)) {
            // sender message found
            senderXml = parseSenderMessage(xmlPullParser, protocol, context.getMessage(), senderXmlStart);

            errorXmlStart = xmlPullParser.getColumnNumber();

            eventType = xmlPullParser.next();
            if (eventType != XmlPullParser.START_TAG) {
                throw new BadMessageException("Start tag expected.");
            }

            protocol = new Protocol(xmlPullParser.getNamespace(), xmlPullParser.getName());
        }

        errorXml = getErrorXml(xmlPullParser, context.getMessage(), errorXmlStart);

        eventType = xmlPullParser.next();

        Protocol startTagProtocol = context.getProtocolChain().get(context.getProtocolChain().size() - 1);
        Protocol endTagProtocol = new Protocol(xmlPullParser.getNamespace(), xmlPullParser.getName());
        if (eventType != XmlPullParser.END_TAG || !startTagProtocol.equals(endTagProtocol)) {
            throw new BadMessageException(String.format("End tag of protocol %s expected.", startTagProtocol));
        }

        Object object = parsingFactory.parse(errorXml);
        if (!(object instanceof StanzaError)) {
            throw new BadMessageException("Not a valid stanza error.");
        }

        Stanza stanza = (Stanza) context.getObject();
        StanzaError error = (StanzaError) object;
        if (stanza instanceof Presence) {
            error.setKind(StanzaError.Kind.PRESENCE);
        } else if (stanza instanceof Message) {
            error.setKind(StanzaError.Kind.MESSAGE);
        } else if (stanza instanceof Iq) {
            error.setKind(StanzaError.Kind.IQ);
        } else {
            throw new BadMessageException("Invalid stanza kind.");
        }

        error.setFrom(stanza.getFrom());
        error.setTo(stanza.getTo());
        error.setId(stanza.getId());
        error.setLang(stanza.getLang());

        error.setSenderMessage(senderXml);

        return error;
    }

    private String getErrorXml(XmlPullParser xmlPullParser, String message, int errorXmlStart) throws IOException, XmlPullParserException {
        while (true) {
            int eventType = xmlPullParser.next();

            if (eventType == XmlPullParser.END_TAG) {
                if (StanzaError.PROTOCOL.equals(new Protocol(xmlPullParser.getNamespace(), xmlPullParser.getName()))) {
                    int errorXmlEnd = xmlPullParser.getColumnNumber();

                    return message.substring(errorXmlStart, errorXmlEnd);
                }
            }
        }
    }

    private String parseSenderMessage(XmlPullParser xmlPullParser, Protocol protocol,
                                      String message, int senderXmlStart) throws IOException, XmlPullParserException {
        while (true) {
            int eventType = xmlPullParser.next();

            if (eventType == XmlPullParser.END_TAG) {
                if (protocol.equals(new Protocol(xmlPullParser.getNamespace(), xmlPullParser.getName()))) {
                    int senderXmlEnd = xmlPullParser.getColumnNumber();

                    return message.substring(senderXmlStart, senderXmlEnd);
                }
            }
        }
    }
}
