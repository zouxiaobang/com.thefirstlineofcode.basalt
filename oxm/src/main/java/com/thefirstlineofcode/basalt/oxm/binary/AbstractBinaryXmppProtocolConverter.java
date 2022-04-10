package com.thefirstlineofcode.basalt.oxm.binary;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.thefirstlineofcode.basalt.oxm.Attribute;
import com.thefirstlineofcode.basalt.oxm.OxmService;
import com.thefirstlineofcode.basalt.oxm.binary.Element.NameAndValue;
import com.thefirstlineofcode.basalt.oxm.binary.Element.NextPart;
import com.thefirstlineofcode.basalt.oxm.parsing.BadMessageException;
import com.thefirstlineofcode.basalt.oxm.translating.IProtocolWriter;
import com.thefirstlineofcode.basalt.protocol.core.Protocol;

public abstract class AbstractBinaryXmppProtocolConverter<T> implements IBinaryXmppProtocolConverter {
	private static final int INDEX_LOCAL_NAME_START = 1;
	private static final int DEFAULT_INDEX_NAMESPACE_START = 7;
	
	private Map<ReplacementBytes, BxmppExtension> replacementBytesToBxmppExtensions = new HashMap<>();
	private Map<String, BxmppExtension> keywordToBxmppExtensions = new HashMap<>();
	private BxmppExtension defaultBxmppExtension;
	
	protected abstract Element readDocument(T reader);
	protected abstract Element readElement(T reader, Element parent) throws Exception;
	protected abstract T createXmlParser(String message) throws Exception;
	
	public AbstractBinaryXmppProtocolConverter() {}
	
	public void register(BxmppExtension bxmppExtension) {
		if (bxmppExtension instanceof DefaultBxmppExtension) {
			defaultBxmppExtension = bxmppExtension;
		} else {
			for (BxmppExtension aBxmppExtension : replacementBytesToBxmppExtensions.values()) {
				if (aBxmppExtension.getNamespace().equals(bxmppExtension.getNamespace()))
					throw new RuntimeException(String.format("Try to register same BXMPP extension multiple times. The extension is %s .", bxmppExtension));
			}
			
			replacementBytesToBxmppExtensions.put(bxmppExtension.getNamespace().getReplacementBytes(), bxmppExtension);
			keywordToBxmppExtensions.put(bxmppExtension.getNamespace().getKeyword(), bxmppExtension);
		}
	}
	
	public void unregister(Namespace namespace) {
		replacementBytesToBxmppExtensions.remove(namespace.getReplacementBytes());
		keywordToBxmppExtensions.remove(namespace.getKeyword());
	}
	
	@Override
	public byte[] toBinary(String message) {
		if (message.startsWith("<stream:stream")) {
			message = new String(message + "</stream:stream>");
		} else if (message.equals("</stream:stream>")) {
			message = new String("<stream:stream xmlns:stream=\"http://etherx.jabber.org/streams\"/>");
		}

		T xmlParser = null;
		try {
			xmlParser = createXmlParser(message);

			Element doc = readDocument(xmlParser);
			if (DefaultBxmppExtension.STRING_STREAM_STREAM.equals(doc.localName)
					&& DefaultBxmppExtension.STRING_STREAM_NAMESPACE.equals(doc.namespace)) {
				// It's a close stream message.
				if (doc.attributes.size() == 0) {
					doc.localName = DefaultBxmppExtension.STRING_STREAM_STREAM;
					doc.namespace = null;
				} else {
					// It's a open stream message.
					doc.attributes.add(0,
							new NameAndValue("xmlns:stream", DefaultBxmppExtension.STRING_STREAM_NAMESPACE));
					doc.namespace = DefaultBxmppExtension.STRING_DEFAULT_NAMESPACE;
				}
			}
			
			return toBinary(doc);
		} catch (Exception e) {
			throw new RuntimeException("Can't parse xml document.", e);
		}
	}
	
	private byte[] toBinary(Element doc) {
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		output.write(Constants.FLAG_DOC_BEGINNING_END);
		
		BxmppExtension currentExtension = null;
		try {
			writeElement(output, doc, currentExtension);
		} catch (IOException e) {
			throw new RuntimeException("IO exception.", e);
		}
		
		byte[] bytes = output.toByteArray();
		bytes[bytes.length - 1] = Constants.FLAG_DOC_BEGINNING_END;
		
		return bytes;
	}
	
	private void writeElement(ByteArrayOutputStream output, Element element, BxmppExtension currentExtension) throws IOException {
		if (isCurrentExtensionChanged(currentExtension, element.namespace)) {
			currentExtension = findBxmppExtension(element.namespace);
		}
		
		output.write(convertKeywordToBytes(element.localName, true, currentExtension));
		
		int attributeLength = element.attributes.size();
		if (!isEmpty(element.namespace)) {
			attributeLength++;
		}
		int childrenLength = element.children.size();
		boolean hasText = element.text != null;
		
		if (attributeLength == 0 && childrenLength == 0 && !hasText) {
			output.write(Constants.FLAG_UNIT_SPLLITER);
			return;
		} else {
			output.write((byte)attributeLength);
			output.write((byte)childrenLength);
			output.write(hasText ? (byte)0x01 : (byte)0x00);
		}
		
		if (attributeLength != 0) {
			if (!isEmpty(element.namespace)) {
				output.write(Constants.BYTES_XMLNS);
				output.write(Constants.FLAG_UNIT_SPLLITER);
				output.write(convertKeywordToBytes(element.namespace, true, currentExtension));
				output.write(Constants.FLAG_UNIT_SPLLITER);
			}
			
			for (int i = 0; i < element.attributes.size(); i++) {
				NameAndValue attribute = element.attributes.get(i);
				output.write(convertKeywordToBytes(attribute.name, true, currentExtension));				
				output.write(Constants.FLAG_UNIT_SPLLITER);
				
				output.write(convertStringValueToBytes(attribute.value, currentExtension));				
				output.write(Constants.FLAG_UNIT_SPLLITER);
			}
		}
		
		if (element.children.size() != 0) {
			for (int i = 0; i < element.children.size(); i++) {
				writeElement(output, element.children.get(i), currentExtension);
			}
		}
		
		if (hasText) {
			output.write(convertStringValueToBytes(element.text, currentExtension));
			output.write(Constants.FLAG_UNIT_SPLLITER);
		}
	}
	
	private byte[] convertStringValueToBytes(String value, BxmppExtension currentExtension) throws UnsupportedEncodingException {
		if (BinaryUtils.isBase64Encoded(value))
			return BinaryUtils.decodeFromBase64(value);
		
		byte[] bytes = convertKeyworkToBytes(value, currentExtension);
		if (bytes != null)
			return bytes;
		
		return BinaryUtils.escape(value.getBytes(Constants.DEFAULT_CHARSET));
	}
	
	private byte[] convertKeyworkToBytes(String keyword, BxmppExtension currentExtension) throws UnsupportedEncodingException {
		return convertKeywordToBytes(keyword, false, currentExtension);
	}
	
	private byte[] convertKeywordToBytes(String keyword, boolean replaceRequired,
			BxmppExtension currentExtension) throws UnsupportedEncodingException {
		ReplacementBytes replacementBytes = keywordToReplacementBytes(keyword, currentExtension);
		
		if (replaceRequired && replacementBytes == null)
			throw new BadMessageException(String.format("Replacement bytes for keyword '%s' not found.", keyword));
		
		return replacementBytes == null ? null : replacementBytes.toBytes();
	}
	
	private ReplacementBytes keywordToReplacementBytes(String keyword, BxmppExtension currentExtension) {
		if (currentExtension != null)
			return currentExtension.getReplacementBytes(keyword);
		
		if (defaultBxmppExtension != null)
			return defaultBxmppExtension.getReplacementBytes(keyword);
		
		return null;
	}
	
	private BxmppExtension findBxmppExtension(String namespace) {
		if (namespace == null)
			return defaultBxmppExtension;
		
		return keywordToBxmppExtensions.get(namespace);
	}
	
	private BxmppExtension findBxmppExtension(ReplacementBytes namespace) {
		if (namespace == null)
			return defaultBxmppExtension;
		
		return replacementBytesToBxmppExtensions.get(namespace);
	}
	

	
	@Override
	public String toXml(byte[] message) {
		if (message.length < 3 || message[0] != Constants.FLAG_DOC_BEGINNING_END
				|| message[message.length - 1] != Constants.FLAG_DOC_BEGINNING_END) {
			throw new BadMessageException("Binary message must start and end with byte &0xff.");
		}
		
		BxmppExtension currentExtension = null;
		Element element = readElement(message, 1, currentExtension);
		
		if (isCloseStream(element)) {
			return "</stream:stream>";
		}
		
		IProtocolWriter writer = OxmService.createProtocolWriterFactory().create();
		if (isOpenStream(element)) {
			writeOpenStreamElement(element, writer);
		} else {
			writeElement(null, element, writer);
		}
		
		String document = writer.getDocument();
		
		return document;
	}
	
	@Override
	public Protocol readProtocol(byte[] data) {
		if (data.length < 9)
			throw new IllegalArgumentException("Bad binary data. Bytes size is too small.");
		
		ReplacementBytes localName = readLocalName(data);
		ReplacementBytes namespace = readNamespace(data, localName);
		
		BxmppExtension bxmppExtension = findBxmppExtension(namespace);
		
		String namespaceKeyword = replacementBytesToKeyword(namespace, bxmppExtension);
		String localNameKeyword = replacementBytesToKeyword(localName, bxmppExtension);
		
		if (namespaceKeyword == null || localNameKeyword == null)
			throw new RuntimeException("Can't get namespace or local name keywords for BXMPP extension protocol: " + namespace.toString());
		
		return new Protocol(namespaceKeyword, localNameKeyword);
	}
	
	private ReplacementBytes readNamespace(byte[] data, ReplacementBytes localName) {
		return new ReplacementBytes(data[DEFAULT_INDEX_NAMESPACE_START], data[DEFAULT_INDEX_NAMESPACE_START + 1]);
	}
	
	private ReplacementBytes readLocalName(byte[] data) {
		return new ReplacementBytes(data[INDEX_LOCAL_NAME_START]);
	}
	
	private void writeOpenStreamElement(Element element, IProtocolWriter writer) {
		writer.writeProtocolBegin(new Protocol(element.namespace, element.localName));
		
		List<Attribute> attributes = new ArrayList<Attribute>();
		for (int i = 0; i < element.attributes.size(); i++) {
			NameAndValue nameAndValue = element.attributes.get(i);
			attributes.add(new Attribute(nameAndValue.name, nameAndValue.value));
		}
		if (!attributes.isEmpty())
			writer.writeAttributes(attributes);
		
		writer.writeString("");
	}
	
	private boolean isOpenStream(Element element) {
		if (!DefaultBxmppExtension.STRING_DEFAULT_NAMESPACE.equals(element.namespace)
				|| !DefaultBxmppExtension.STRING_STREAM_STREAM.equals(element.localName)) {
			return false;
		}
		
		if (element.children.size() != 0 || element.text != null) {
			throw new BadMessageException("Open stream mustn't has children or text.");
		}
		
		return true;
	}
	
	private boolean isCloseStream(Element element) {
		if (!isEmpty(element.namespace) || !DefaultBxmppExtension.STRING_STREAM_STREAM.equals(element.localName))
			return false;
		
		if (element.attributes.size() != 0 || element.children.size() != 0 || element.text != null) {
			return false;
		}
		
		return true;
	}
	
	private void writeElement(Element parent, Element element, IProtocolWriter writer) {
		if (isNewProtocol(parent, element)) {
			writer.writeProtocolBegin(new Protocol(element.namespace, element.localName));
		} else {
			writer.writeElementBegin(element.localName);
		}
		
		List<Attribute> attributes = new ArrayList<Attribute>();
		for (int i = 0; i < element.attributes.size(); i++) {
			NameAndValue nameAndValue = element.attributes.get(i);
			attributes.add(new Attribute(nameAndValue.name, nameAndValue.value));
		}
		if (!attributes.isEmpty())
			writer.writeAttributes(attributes);
		
		for (int i = 0; i < element.children.size(); i++) {
			writeElement(element, element.children.get(i), writer);
		}
		
		if (element.text != null) {
			writer.writeText(element.text);
		}
		
		if (isNewProtocol(parent, element)) {
			writer.writeProtocolEnd();
		} else {
			writer.writeElementEnd();
		}
	}
	
	private boolean isNewProtocol(Element parent, Element element) {
		return parent == null || (element.namespace != null && !element.namespace.equals(parent.namespace));
	}
	
	private Element readElement(byte[] message, int position, BxmppExtension currentExtension) {
		Element element = new Element();
		
		if (position >= message.length - 1) {
			throw new BadMessageException("Unexpected end of document.");
		}
		
		ReplacementBytes localNameReplacementBytes = null;
		localNameReplacementBytes = new ReplacementBytes(message[position]);
		position++;
		
		if (isEndOfElement(message, position)) {
			element.localName = replacementBytesToKeyword(localNameReplacementBytes, currentExtension);
			element.endPosition = position;
			
			// It's an empty element.
			return element;
		}
		
		int attributesLength = 0;
		int childrenLength = 0;
		boolean hasText = false;
		if (message[position + 1] != Constants.FLAG_UNIT_SPLLITER &&
				message[position + 1] != Constants.FLAG_DOC_BEGINNING_END) {
			if (position + 3 > message.length - 1) {
				throw new BadMessageException("Unexpected end of document.");
			}
			
			attributesLength = message[position] & 0xff;
			childrenLength = message[position + 1] & 0xff;
			int hasTextByte = message[position + 2] & 0xff;
			if (hasTextByte != 0x00 && hasTextByte != 0x01) {
				throw new BadMessageException("Has text byte must be 0x00 or 0x01.");
			}
			hasText = (hasTextByte == 0x00) ? false : true;
			
			position += 3;
		}
		
		NextPart[][] attributes = new NextPart[attributesLength][2];
		for (int i = 0; i < attributesLength; i++) {
			NextPart[] attribute = new NextPart[2];
			
			NextPart nextPart = readNextPart(message, position, true);
			position = nextPart.endPostion + 1;
			attribute[0] = nextPart;
			
			nextPart = readNextPart(message, position, false);
			position = nextPart.endPostion + 1;
			attribute[1] = nextPart;
			
			attributes[i] = attribute;
		}
		
		ReplacementBytes elementNamespace = getElementNamespace(attributes);
		if (isCurrentExtensionChanged(currentExtension, elementNamespace)) {
			currentExtension = findBxmppExtension(elementNamespace);
		}
		
		String localName = replacementBytesToKeyword(localNameReplacementBytes, currentExtension);
		if (localName == null) {
			throw new BadMessageException(
					"Can't get local name by replacement bytes: " + localNameReplacementBytes + ".");
		}
		element.localName = localName;
		
		for (int i = 0; i < attributesLength; i++) {
			String attributeName = replacementBytesToKeyword(attributes[i][0].replacementBytes, currentExtension);
			if (attributeName == null) {
				throw new BadMessageException(
						"Can't get attribute name by replacement bytes: " + attributes[i][0].replacementBytes + ".");
			}
			
			String attributeValue = null;
			if (attributes[i][1].text != null)
				attributeValue = attributes[i][1].text;
			else
				attributeValue = replacementBytesToKeyword(attributes[i][1].replacementBytes, currentExtension);
			
			if (attributeValue == null) {
				throw new BadMessageException(
						"Can't get attribute value by replacement bytes: " + attributes[i][1].replacementBytes + ".");
			}
			
			if (Constants.KEYWORD_XMLNS.equals(attributeName)) {
				element.namespace = attributeValue;
			} else {
				element.attributes.add(new NameAndValue(attributeName, attributeValue));				
			}
		}
		
		for (int i = 0; i < childrenLength; i++) {
			Element child = readElement(message, position, currentExtension);
			element.children.add(child);
			position = child.endPosition + 1;
		}
		
		if (hasText) {
			NextPart nextPart = readNextPart(message, position, false);
			element.text = nextPart.text;
			position = nextPart.endPostion + 1;
		}
		
		element.endPosition = position - 1;
		return element;
	}
	
	private boolean isCurrentExtensionChanged(BxmppExtension currentExtension, ReplacementBytes elementNamespace) {
		if (elementNamespace == null)
			return false;
		
		if (currentExtension == null || currentExtension.getNamespace().getReplacementBytes() == null) {
			return elementNamespace != null;
		}
		
		return !currentExtension.getNamespace().getReplacementBytes().equals(elementNamespace);
	}
	
	private boolean isCurrentExtensionChanged(BxmppExtension currentExtension, String elementNamespace) {
		if (elementNamespace == null)
			return false;
		
		if (currentExtension == null || currentExtension.getNamespace().getKeyword() == null) {
			return elementNamespace != null;
		}
		
		return !currentExtension.getNamespace().getKeyword().equals(elementNamespace);
	}
	
	private ReplacementBytes getElementNamespace(NextPart[][] attributes) {
		if (attributes == null || attributes.length == 0)
			return null;
		
		for (NextPart[] attribute : attributes) {
			if (Constants.REPLACEMENT_BYTES_XMLNS.equals(attribute[0].replacementBytes))
				return attribute[1].replacementBytes;
		}
		
		return null;
	}
	
	private NextPart readNextPart(byte[] message, int position, boolean replaceIsRequired) {
		int endPosition = readToNextPart(message, position);
		
		String text = null;
		ReplacementBytes replacementBytes = null;
		if (endPosition - position == 1) {
			replacementBytes = new ReplacementBytes(message[endPosition - 1]);
		} else if (endPosition - position == 2) {
			if (isNoreplaceText(message, position, endPosition)) {
				text = getText(message, position, endPosition);
			} else if (ReplacementBytes.isFirstByteOfDoubleBytesNamespaceReplacementBytes(message[endPosition - 2])) {				
				replacementBytes = new ReplacementBytes(message[endPosition - 2], message[endPosition - 1]);
			} else {
				throw new IllegalArgumentException("Illegal element part: " +
						BinaryUtils.getHexStringFromBytes(Arrays.copyOfRange(message, position, endPosition)));
			}
		} else {
			if (replaceIsRequired)
				throw new IllegalArgumentException("Need a replacement bytes, but text found.");
			
			text = getText(message, position, endPosition);
		}
		
		if (replaceIsRequired && replacementBytes == null) {
			throw new BadMessageException("Can't read a replacement bytes.");
		}
		
		if (replacementBytes == null && "".equals(text)) {
			throw new BadMessageException("Illegal next part. Replacement bytes is null and text is blank.");
		}
		
		return new NextPart(replacementBytes, text, endPosition);
	}
	
	private boolean isNoreplaceText(byte[] message, int position, int endPosition) {
		if (endPosition - position > 4)
			return false;
		
		return message[position] == Constants.FLAG_NOREPLACE;
	}
	
	private String getText(byte[] message, int position, int endPosition) {		
		byte[] bytes = Arrays.copyOfRange(message, position, endPosition);
		if (BinaryUtils.isBase64Decoded(bytes)) {
			return BinaryUtils.encodeToBase64(bytes);
		} else {			
			if (bytes[0] == Constants.FLAG_NOREPLACE)
				bytes = Arrays.copyOfRange(bytes, 1, bytes.length);
			
			try {
				return new String(BinaryUtils.unescape(bytes), Constants.DEFAULT_CHARSET);
			} catch (Exception e) {
				throw new RuntimeException("Illegal text for element.", e);
			}
		}
	}
	
	private int readToNextPart(byte[] message, int position) {
		for (; position < message.length; position++) {
			if ((message[position] == Constants.FLAG_UNIT_SPLLITER ||
					message[position] == Constants.FLAG_DOC_BEGINNING_END)
					&& message[position - 1] != Constants.FLAG_ESCAPE)
				return position;
		}
		
		throw new BadMessageException("Unexpected end of document.");
	}
	
	private boolean isEndOfElement(byte[] message, int position) {
		return message[position] == Constants.FLAG_UNIT_SPLLITER ||
				message[position] == Constants.FLAG_DOC_BEGINNING_END;
	}
	
	private String replacementBytesToKeyword(ReplacementBytes replacementBytes, BxmppExtension currentExtension) {
		if (currentExtension != null)
			return currentExtension.getKeyword(replacementBytes);
		
		if (defaultBxmppExtension != null)
			return defaultBxmppExtension.getKeyword(replacementBytes);
		
		return null;
	}
	
	protected boolean isEmpty(String str) {
		return str == null || "".equals(str);
	}
}
