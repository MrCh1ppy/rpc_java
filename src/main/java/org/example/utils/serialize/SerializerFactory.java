package org.example.utils.serialize;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Slf4j
public enum SerializerFactory implements Serializer {
	JDK{
		@Override
		public <T> T deSerializer(Class<T> clazz, byte[] bytes) throws RuntimeException {
			return null;
		}

		@Override
		public <T> byte[] serialize(T object) throws RuntimeException {
			return null;
		}
	},
	JSON{
		@Override
		public <T> T deSerializer(Class<T> clazz, byte[] bytes) throws RuntimeException {
			try {
				return OBJECT_MAPPER.readValue(bytes, clazz);
			} catch (IOException e) {
				e.printStackTrace();
				throw new RuntimeException(e);
			}
		}

		@Override
		public <T> byte[] serialize(T object) throws RuntimeException {
			try {
				var value = OBJECT_MAPPER.writeValueAsString(object);
				log.debug("value is : {}",value);
				return OBJECT_MAPPER.writeValueAsString(object).getBytes(StandardCharsets.UTF_8);
			} catch (JsonProcessingException e) {
				throw new RuntimeException(e);
			}
		}
	};
	public static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
}
