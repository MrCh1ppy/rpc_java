package org.example.utils.regerster.service;

import java.net.InetSocketAddress;

public interface ServiceRegister {
	/**
	 * 将服务的名称和地址注册进服务注册中心
	 * @param serviceName
	 * @param inetSocketAddress
	 */
	void register(String serviceName, InetSocketAddress inetSocketAddress);

	/**
	 * 根据服务名称从注册中心获取到服务提供者的地址
	 * @param serviceName
	 * @return
	 */
	InetSocketAddress getService(String serviceName);
}
