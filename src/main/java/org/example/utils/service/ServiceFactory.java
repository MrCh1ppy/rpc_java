package org.example.utils.service;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;

public class ServiceFactory {
	static HashMap<Class<?>, Object> map = new HashMap<>(16);

	static {
		try {
			Class<?> clazz = Class.forName("org.example.service.HelloService");
			Object instance = Class.forName("org.example.service.impl.HelloServiceImpl").getDeclaredConstructor().newInstance();

			// 放入 InterfaceClass -> InstanceObject 的映射
			map.put(clazz, instance);
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException | NoSuchMethodException |
		         InvocationTargetException e) {
			e.printStackTrace();
		}
	}

	public static Object getInstance(Class<?> interfaceClass) {
		return map.get(interfaceClass);
	}
}