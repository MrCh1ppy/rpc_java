package org.example.utils.regerster.service.nacos;

import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.NamingFactory;
import com.alibaba.nacos.api.naming.NamingService;
import com.alibaba.nacos.api.naming.pojo.Instance;

import java.net.InetSocketAddress;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public final class NacosDealer {
	private static final NamingService NAMING_SERVICE;
	private static final String SERVER_ADDRESS ="127.0.0.1:8848";

	private static final Set<String> services=new HashSet<>(64);

	private static InetSocketAddress address;
	static {
		try{
			NAMING_SERVICE =NamingFactory.createNamingService(SERVER_ADDRESS);
		}catch (NacosException e){
			throw new RuntimeException("connect nacos bad happended");
		}
	}

	private NacosDealer() {
	}

	public static void registerService(String servername, InetSocketAddress address)throws NacosException{
		NAMING_SERVICE.registerInstance(servername,address.getHostName(),address.getPort());
		NacosDealer.address=address;
		services.add(servername);
	}

	public static List<Instance> getAllInstance(String server) throws NacosException {
		return NAMING_SERVICE.getAllInstances(server);
	}

	public static void clearRegister()  {
		if(!services.isEmpty()&&address!=null){
			String hostName = address.getHostName();
			int port = address.getPort();
			for (String next : services) {
				try {
					NAMING_SERVICE.deregisterInstance(next, hostName, port);
				} catch (NacosException e) {
					throw new RuntimeException("注销服务失败");
				}
			}
		}
	}
}
