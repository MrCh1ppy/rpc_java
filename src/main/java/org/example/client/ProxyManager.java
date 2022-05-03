package org.example.client;

import com.alibaba.nacos.api.exception.NacosException;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.concurrent.DefaultPromise;
import lombok.extern.slf4j.Slf4j;
import org.example.client.channel.ClientPineLineFactory;
import org.example.dao.message.RpcRequestMsg;
import org.example.utils.BasePineLineFactory;
import org.example.utils.regerster.loadbalancer.LoadBalancer;
import org.example.utils.regerster.service.ServiceDiscovery;
import org.example.utils.regerster.service.nacos.NacosDiscoveryImpl;

import java.lang.reflect.Proxy;
import java.net.InetSocketAddress;
import io.netty.channel.Channel;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
public class ProxyManager {

	private static final ServiceDiscovery serviceDiscovery;

	private static final NioEventLoopGroup group;

	private static final Map<String, Channel> channels;

	private static final Bootstrap bootstarp;



	static {
		group=new NioEventLoopGroup();
		bootstarp=initChannel();
		channels=new ConcurrentHashMap<>(64);
		serviceDiscovery=new NacosDiscoveryImpl(LoadBalancer.Rules.ROUND_ROBIN_RULE);
	}

	private static Channel getChannel(InetSocketAddress inetSocketAddress){
		String key = inetSocketAddress.toString();
		if(channels.containsKey(key)){
			Channel channel = channels.get(key);
			if(channel!=null&&channel.isActive()){
				return channel;
			}
			channels.remove(key);
		}
		Channel channel=null;
		try{
			channel = bootstarp.connect(inetSocketAddress).sync().channel();
			channel.closeFuture().addListener(future -> log.debug("connect done"));
		}catch (InterruptedException e){
			Thread.currentThread().interrupt();
			assert channel != null;
			channel.close();
			log.debug("connect error");
			return null;
		}
		channels.put(key,channel);
		return channel;
	}

	private static Bootstrap initChannel() {
		return new Bootstrap()
				.group(ProxyManager.group)
				.channel(NioSocketChannel.class)
				.handler(new ChannelInitializer<SocketChannel>() {
					@Override
					protected void initChannel(SocketChannel ch) {
						var pipeline = ch.pipeline();
						pipeline
								.addLast(BasePineLineFactory.getIdleStateHandler(0, 3, 0))
								.addLast(BasePineLineFactory.getFrameDecoder())
								.addLast(BasePineLineFactory.getLoggingHandler())
								.addLast(BasePineLineFactory.getCodec())
								.addLast(ClientPineLineFactory.getClientDuplexHandler())
								.addLast(ClientPineLineFactory.getRpcResponseHandler())
						;
					}
				});
	}

	private void sendRpcRequest(RpcRequestMsg msg) throws NacosException {
		InetSocketAddress service = serviceDiscovery.getService(msg.getInterfaceName());
		Channel channel = getChannel(service);
		assert channel != null;
		if((!channel.isActive()||!channel.isRegistered())){
			group.shutdownGracefully();
			return;
		}
		channel.writeAndFlush(msg).addListener((ChannelFutureListener)future->{
			if(future.isSuccess()){
				log.debug("send msg success");
			}
		});
	}

	public <T> T getProxyService(Class<T> serviceClass){
		ClassLoader loader = serviceClass.getClassLoader();
		Class<?>[] interfaces = serviceClass.getInterfaces();
		Object proxyInstance = Proxy.newProxyInstance(loader, interfaces, (proxy, method, args) -> {
			RpcRequestMsg msg = new RpcRequestMsg(
					serviceClass.getName(),
					method.getName(),
					method.getReturnType(),
					method.getParameterTypes(),
					args
					);
			DefaultPromise<Object> promise = new DefaultPromise<>(group.next());
			ClientPineLineFactory.getRpcResponseHandler().addRequest(msg.getSequenceId(), promise);
			sendRpcRequest(msg);
			promise.await();
			if(promise.isSuccess()){
				return promise.getNow();
			}else {
				throw new RuntimeException(promise.cause());
			}
		});
		return (T)proxyInstance;
	}
}
