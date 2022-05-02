package org.example.utils.regerster.service;

import com.alibaba.nacos.api.exception.NacosException;

import java.net.InetSocketAddress;

public interface ServiceDiscovery {
	InetSocketAddress getService(String serviceName) throws NacosException;
}
