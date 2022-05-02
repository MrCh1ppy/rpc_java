package org.example.utils.regerster.service;

import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;

@Slf4j
public abstract class ServiceFactory {
	private static final HashMap<String, Object> map = new HashMap<>(16);

	private ServiceFactory() {
	}

	public static <T> void addServiceProvider(T service,String serviceName){
		if(map.containsKey(serviceName)){
			return;
		}
		map.put(serviceName,service);
		log.debug("服务类{}添加进工厂",serviceName);
	}

	public static Object getService(String interfaceName){
		return map.get(interfaceName);
	}

}