package org.example.utils.serialize;

interface Serializer {
	<T> T deSerializer(Class<T> clazz,byte[] bytes)throws RuntimeException;
	<T> byte[] serialize(T object)throws RuntimeException;
}
