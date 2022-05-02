package org.example.utils.regerster.service.nacos;

import com.alibaba.nacos.api.exception.NacosException;
import org.example.utils.regerster.service.ServiceRegister;

import java.net.InetSocketAddress;

public class ServiceRegisterImpl implements ServiceRegister {
	@Override
	public void register(String serviceName, InetSocketAddress inetSocketAddress) {
		try{
			NacosDealer.registerService(serviceName,inetSocketAddress);
			System.out.println("service register" + serviceName);
		}catch (NacosException e){
			throw new RuntimeException("注册Nacos时出现异常");
		}
	}

	@Override
	public InetSocketAddress getService(String serviceName) {
		return null;
	}
}
