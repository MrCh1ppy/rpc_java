package org.example.client;

import io.netty.util.concurrent.DefaultPromise;
import io.netty.util.concurrent.Promise;
import lombok.extern.slf4j.Slf4j;
import org.example.client.channel.RpcChannelFactory;
import org.example.client.handler.RpcResponseHandler;
import org.example.dao.message.RpcRequestMsg;
import org.example.service.HelloService;

import java.lang.reflect.Proxy;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
public class ProxyClient {
	private static ConcurrentHashMap<Integer, Promise<?>>map=new ConcurrentHashMap<>();

	public static void main(String[] args) {
		HelloService service = getProxyService(HelloService.class);
		System.out.println("res==========="+service.hello("css"));
		System.out.println("res==========="+service.hello("lisi"));
		System.out.println("res==========="+service.hello("check"));
	}

	private static  <T> T getProxyService(Class<T> serviceClass){
		Object o = Proxy.newProxyInstance(serviceClass.getClassLoader(), new Class[]{serviceClass}, ((proxy, method, args) -> {
			RpcRequestMsg msg = new RpcRequestMsg(
				serviceClass.getName(),
					method.getName(),
					method.getReturnType(),
					method.getParameterTypes(),
					args
			);
			RpcChannelFactory.getChannel().writeAndFlush(msg);

			DefaultPromise<Object> promise = new DefaultPromise<>(RpcChannelFactory.getChannel().eventLoop());
			RpcResponseHandler.getMap().put(msg.getSequenceId(),promise);

			promise.await();

			if (promise.isSuccess()){
				return promise.get();
			}
			promise.cause().printStackTrace();
			throw new RuntimeException(promise.cause());
		}));
		return (T) o;
	}
}
