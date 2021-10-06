package com.thefirstlineofcode.basalt.oxm.binary;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.thefirstlineofcode.basalt.oxm.Attribute;
import com.thefirstlineofcode.basalt.oxm.OxmService;
import com.thefirstlineofcode.basalt.oxm.binary.Element.NameAndValue;
import com.thefirstlineofcode.basalt.oxm.binary.Element.NextPart;
import com.thefirstlineofcode.basalt.oxm.parsing.BadMessageException;
import com.thefirstlineofcode.basalt.oxm.translating.IProtocolWriter;
import com.thefirstlineofcode.basalt.protocol.Constants;
import com.thefirstlineofcode.basalt.protocol.core.Protocol;

public abstract class AbstractBinaryXmppProtocolConverter<T> implements IBinaryXmppProtocolConverter {
	private static final ReplacementBytes REPLACEMENT_BYTES_XMLNS = BxmppExtension.REPLACEMENT_BYTES_XMLNS;
	private static final byte[] BYTES_XMLNS = BxmppExtension.BYTES_XMLNS;
	
	private static final byte FLAG_DOC_BEGINNING_END = (byte) 0xff;
	private static final byte FLAG_UNIT_SPLLITER = (byte) 0xfe;
	private static final byte FLAG_ESCAPE = (byte)0xfd;
	
	private static final int INDEX_LOCAL_NAME_START = 1;
	private static final int DEFAULT_INDEX_NAMESPACE_START = 7;
	
	private List<BxmppExtension> bxmppExtensions = new ArrayList<>();
	private BxmppExtension defaultBxmppExtension;
	
	protected abstract Element readDocument(T reader);
	protected abstract Element readElement(T reader, Element parent) throws Exception;
	protected abstract T createXmlParser(String message) throws Exception;
	
	public AbstractBinaryXmppProtocolConverter() {}
	
	public void registerBxmppExtension(BxmppExtension bxmppExtension) {
		if (bxmppExtension instanceof DefaultBxmppExtension ||
				bxmppExtension.getNamespace().equals(new Namespace(null, null))) {
			defaultBxmppExtension = bxmppExtension;
		} else {
			for (BxmppExtension aBxmppExtension : bxmppExtensions) {
				if (aBxmppExtension.getNamespace().equals(bxmppExtension.getNamespace()))
					throw new RuntimeException("Same namespace BXMPP extension has alread existed.");
			}
			
			bxmppExtensions.add(bxmppExtension);
		}
	}
	
	public void unregisterBxmppExtension(Namespace namespace) {
		BxmppExtension extension = null;
		for (BxmppExtension anExtension : bxmppExtensions) {
			if (anExtension.getNamespace().equals(namespace)) {
				extension = anExtension;
				break;
			}
		}
		
		if (extension != null)
			bxmppExtensions.remove(extension);
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
		output.write(FLAG_DOC_BEGINNING_END);
		
		BxmppExtension currentExtension = null;
		try {
			writeElement(output, doc, currentExtension);
		} catch (IOException e) {
			throw new RuntimeException("IO exception.", e);
		}
		
		byte[] bytes = output.toByteArray();
		bytes[bytes.length - 1] = FLAG_DOC_BEGINNING_END;
		
		return bytes;
	}
	
	private void writeElement(ByteArrayOutputStream output, Element element, BxmppExtension currentExtension) throws IOException {
		if (isCurrentExtensionChanged(currentExtension, element.namespace)) {
			currentExtension = findExtension(element.namespace);
		}
		
		output.write(convertKeywordToBytes(element.localName, true, currentExtension));
		
		int attributeLength = element.attributes.size();
		if (!isEmpty(element.namespace)) {
			attributeLength++;
		}
		int childrenLength = element.children.size();
		boolean hasText = element.text != null;
		
		if (attributeLength == 0 && childrenLength == 0 && !hasText) {
			output.write(FLAG_UNIT_SPLLITER);
			return;
		} else {
			output.write(attributeLength);
			output.write(childrenLength);
			output.write(hasText ? 0x01 : 0x00);
		}
		
		if (attributeLength != 0) {
			if (!isEmpty(element.namespace)) {
				output.write(BYTES_XMLNS);
				output.write(FLAG_UNIT_SPLLITER);
				output.write(convertKeywordToBytes(element.namespace, true, currentExtension));
				output.write(FLAG_UNIT_SPLLITER);
			}
			
			for (int i = 0; i < element.attributes.size(); i++) {
				NameAndValue attribute = element.attributes.get(i);
				output.write(convertKeywordToBytes(attribute.name, true, currentExtension));
				output.write(FLAG_UNIT_SPLLITER);
				byte[] bytes = convertKeyworkToBytes(attribute.value, currentExtension);
				if (bytes == null)
					output.write(escape(attribute.value.getBytes()));
				else
					output.write(bytes);
				output.write(FLAG_UNIT_SPLLITER);
			}
		}
		
		if (element.children.size() != 0) {
			for (int i = 0; i < element.children.size(); i++) {
				writeElement(output, element.children.get(i), currentExtension);
			}
		}
		
		if (hasText) {
			byte[] replacementBytes = convertKeywordToBytes(element.text, false, currentExtension);
			if (replacementBytes != null) {
				output.write(replacementBytes);
			} else {
				output.write(escape(element.text.getBytes(Constants.DEFAULT_CHARSET)));
			}
			
			output.write(FLAG_UNIT_SPLLITER);
		}
	}
	
	private byte[] convertKeyworkToBytes(String keyword, BxmppExtension currentExtension) throws UnsupportedEncodingException {
		return convertKeywordToBytes(keyword, false, currentExtension);
	}
	
	private byte[] convertKeywordToBytes(String keyword, boolean replaceRequired,
			BxmppExtension currentExtension) throws UnsupportedEncodingException {
		ReplacementBytes replacementBytes = keywordToReplacementBytes(keyword, currentExtension);
		
		if (replacementBytes == null) {
			if (!replaceRequired)
				return escape(keyword.getBytes(Constants.DEFAULT_CHARSET));
			
			throw new BadMessageException(String.format("Replacement bytes for keyword '%s' not found.", keyword));
		}
		
		return replacementBytes.toBytes();
	}
	
	private ReplacementBytes keywordToReplacementBytes(String keyword, BxmppExtension currentExtension) {
		if (currentExtension != null)
			return currentExtension.getReplacementBytes(keyword);
		
		if (defaultBxmppExtension != null)
			return defaultBxmppExtension.getReplacementBytes(keyword);
		
		return null;
	}
	
	private BxmppExtension findExtension(String namespace) {
		for (BxmppExtension extension : bxmppExtensions) {
			if (extension.getNamespace().getKeyword().equals(namespace))
				return extension;
		}
		
		return defaultBxmppExtension;
	}
	
	private BxmppExtension findExtension(ReplacementBytes namespace) {
		for (BxmppExtension extension : bxmppExtensions) {
			if (extension.getNamespace().getReplacementBytes().equals(namespace))
				return extension;
		}
		
		return defaultBxmppExtension;
	}
	
	private byte[] escape(byte[] bytes) {
		if (bytes.length == 2) {
			return new byte[] {FLAG_ESCAPE, bytes[0], bytes[1]};
		}
		
		if (bytes.length == 1) {
			return new byte[] {FLAG_ESCAPE, bytes[0]};
		}
		
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		try {
			for (int i = 0; i < bytes.length; i++) {
				if (bytes[i] == FLAG_DOC_BEGINNING_END || bytes[i] == FLAG_UNIT_SPLLITER || bytes[i] == FLAG_ESCAPE) {
					output.write(FLAG_ESCAPE);
					output.write(bytes[i]);
				} else {
					output.write(bytes[i]);
				}
			}
			
			return output.toByteArray();
		} finally {
			if (output != null)
				try {
					output.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
		}
	}
	
	@Override
	public String toXml(byte[] message) {
		if (message.length < 3 || message[0] != FLAG_DOC_BEGINNING_END
				|| message[message.length - 1] != FLAG_DOC_BEGINNING_END) {
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
		// TODO
		return null;
/*		if (data.length < 9)
			throw new IllegalArgumentException("Bad binary data. Bytes size is too small.");
		
		ReplacementBytes localName = readLocalName(data);
		ReplacementBytes namespace = readNamespace(data, localName);
		
		String namespaceKeyword = replacementBytesToKeyword(namespace);
		String localNameKeyword = replacementBytesToKeyword(localName);
		
		if (namespaceKeyword == null || localNameKeyword == null)
			throw new RuntimeException("Can't get namespace or local name keywords for protocol.");
		
		return new Protocol(namespaceKeyword, localNameKeyword);*/
	}
	
	/*private ReplacementBytes readNamespace(byte[] data, ReplacementBytes localName) {
		int namespaceStartIndex = DEFAULT_INDEX_NAMESPACE_START;
		if (localName.getFirst() != (byte) 0xff) {
			namespaceStartIndex++;
		}
		
		byte firstByteOfNamespace = data[namespaceStartIndex];
		byte secondByteOfNamespace;
		if (firstByteOfNamespace > 0xf0 && firstByteOfNamespace < 0xfa) {
			secondByteOfNamespace = data[namespaceStartIndex + 1];
		} else {
			secondByteOfNamespace = firstByteOfNamespace;
			firstByteOfNamespace = (byte) 0xff;
		}
		
		return new ReplacementBytes(firstByteOfNamespace, secondByteOfNamespace);
	}
	
	private ReplacementBytes readLocalName(byte[] data) {
		byte firstByteOfLocalName = data[INDEX_LOCAL_NAME_START];
		byte secondByteOfLocalName;
		if (firstByteOfLocalName > 0xf0 && firstByteOfLocalName < 0xfa) {
			secondByteOfLocalName = data[INDEX_LOCAL_NAME_START + 1];
		} else {
			secondByteOfLocalName = firstByteOfLocalName;
			firstByteOfLocalName = (byte) 0xff;
		}
		
		return new ReplacementBytes(firstByteOfLocalName, secondByteOfLocalName);
	} */
	
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
		if (message[position + 1] != FLAG_UNIT_SPLLITER && message[position + 1] != FLAG_DOC_BEGINNING_END) {
			if (position + 3 > message.length - 1) {
				throw new BadMessageException("Unexpected end of document.");
			}
			
			attributesLength = message[position] & 0xff;
			childrenLength = message[position + 1] & 0xff;
			int hasTextByte = message[position + 2];
			if (hasTextByte != 0x00 && hasTextByte != 0x01) {
				throw new BadMessageException("Has text byte must be 0x00 or 0x01.");
			}
			hasText = message[position + 2] == 0x00 ? false : true;
			
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
			currentExtension = findExtension(elementNamespace);
		}
		
		String localName = replacementBytesToKeyword(localNameReplacementBytes, currentExtension);
		if (localName == null) {
			throw new BadMessageException(
					"Can't get local name replacement bytes " + localNameReplacementBytes + ".");
		}
		element.localName = localName;
		
		for (int i = 0; i < attributesLength; i++) {
			String attributeName = replacementBytesToKeyword(attributes[i][0].replacementBytes, currentExtension);
			String attributeValue = null;
			if (attributes[i][1].text != null)
				attributeValue = attributes[i][1].text;
			else
				attributeValue = replacementBytesToKeyword(attributes[i][1].replacementBytes, currentExtension);
			
			if (BxmppExtension.KEYWORD_XMLNS.equals(attributeName)) {
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
			if (REPLACEMENT_BYTES_XMLNS.equals(attribute[0].replacementBytes))
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
			if (ReplacementBytes.isNamespaceFirstByteOfReplacementBytes(message[endPosition - 2])) {				
				replacementBytes = new ReplacementBytes(message[endPosition - 2], message[endPosition - 1]);
			} else if (isOneCharText(message, position, endPosition)) {
				text = getText(message, position, endPosition, text);
			} else {
				throw new IllegalArgumentException("Illegal element part: " +
						BinaryUtils.getHexStringFromBytes(Arrays.copyOfRange(message, position, endPosition)));
			}
		} else if (endPosition - position == 3) {
			if (isOneCharText(message, position, endPosition))
				text = getText(message, position + 1, endPosition, text);
		} else {
			if (replaceIsRequired)
				throw new IllegalArgumentException("Need a replacement bytes, but text is found.");
			
			text = getText(message, position, endPosition, text);
		}
		
		if (text == null && isOneCharText(message, position, endPosition)) {
			text = getText(message, position + 1, endPosition, text);
			replacementBytes = null;
		}
		
		if (replaceIsRequired && replacementBytes == null) {
			throw new BadMessageException("Can't read a replacement bytes.");
		}
		
		if ("".equals(text)) {
			throw new BadMessageException("Next part text is blank.");
		}
		
		return new NextPart(replacementBytes, text, endPosition);
	}
	
	private boolean isOneCharText(byte[] message, int position, int endPosition) {
		if (endPosition - position > 3)
			return false;
		
		return message[position] == FLAG_ESCAPE;
	}
	
	private String getText(byte[] message, int position, int endPosition, String text) {
		try {
			text = new String(unescape(Arrays.copyOfRange(message, position, endPosition)),
					Constants.DEFAULT_CHARSET);
		} catch (Exception e) {
			throw new RuntimeException("Illegal text for element.", e);
		}
		return text;
	}
	
	private byte[] unescape(byte[] bytes) throws IOException {
		if (bytes.length == 3 && bytes[0] == FLAG_ESCAPE) {
			return new byte[] {bytes[1], bytes[2]};
		}
		
		if (bytes.length == 2 && bytes[0] == FLAG_ESCAPE) {
			return new byte[] {bytes[1]};
		}
		
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		try {
			for (int i = 0; i < bytes.length; i++) {
				if (bytes[i] == FLAG_ESCAPE && i < bytes.length && isEscapedByte(bytes[i + 1])) {
					continue;
				}
				
				output.write(bytes[i]);
			}
			
			return output.toByteArray();
		} finally {
			if (output != null)
				output.close();
		}
	}
	
	private boolean isEscapedByte(byte b) {
		return b == FLAG_DOC_BEGINNING_END || b == FLAG_UNIT_SPLLITER || b == FLAG_ESCAPE;
	}
	
	private int readToNextPart(byte[] message, int position) {
		for (; position < message.length; position++) {
			if ((message[position] == FLAG_UNIT_SPLLITER || message[position] == FLAG_DOC_BEGINNING_END)
					&& message[position - 1] != FLAG_ESCAPE)
				return position;
		}
		
		throw new BadMessageException("Unexpected end of document.");
	}
	
	private boolean isEndOfElement(byte[] message, int position) {
		return message[position] == FLAG_UNIT_SPLLITER || message[position] == FLAG_DOC_BEGINNING_END;
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
