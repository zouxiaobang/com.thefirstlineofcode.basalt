package com.firstlinecode.basalt.protocol.oxm.conversion;

public interface IConverter<K, V> {
	V from(K obj) throws ConversionException;
	K to(V obj) throws ConversionException;
}
