package org.example.utils.regerster.service.nacos;

import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.pojo.Instance;
import org.example.utils.regerster.loadbalancer.LoadBalancer;
import org.example.utils.regerster.service.ServiceDiscovery;

import java.net.InetSocketAddress;
import java.util.List;

public class NacosDiscoveryImpl implements ServiceDiscovery {
	private final LoadBalancer.Rules loadBalancer;

	public NacosDiscoveryImpl(LoadBalancer.Rules loadBalancer) {
		this.loadBalancer = loadBalancer==null?LoadBalancer.Rules.ROUND_ROBIN_RULE:loadBalancer;
	}


	@Override
	public InetSocketAddress getService(String serviceName) throws NacosException {
		List<Instance> instances = NacosDealer.getAllInstance(serviceName);
		if(instances.isEmpty()){
			throw new RuntimeException("无服务被发现");
		}
		Instance instance = loadBalancer.getInstance(instances);
		return new InetSocketAddress(instance.getIp(), instance.getPort());
	}
}
