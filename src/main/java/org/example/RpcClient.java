package org.example;


import org.example.client.ProxyManager;
import org.example.service.HelloService;
import org.example.service.impl.HelloServiceImpl;

public class RpcClient {
	public static void main(String[] args)  {
		HelloService service = new ProxyManager().getProxyService(HelloServiceImpl.class);
		System.out.println(service.hello("css"));
	}
}