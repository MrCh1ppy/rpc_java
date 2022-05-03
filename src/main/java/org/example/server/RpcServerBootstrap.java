package org.example.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import lombok.extern.slf4j.Slf4j;
import org.example.utils.BasePineLineFactory;
import org.example.utils.pak_scan.PackageScanUtils;
import org.example.utils.regerster.annotation.RpcBootStrap;
import org.example.utils.regerster.annotation.ServiceProvider;
import org.example.utils.regerster.service.ServiceFactory;
import org.example.utils.regerster.service.ServiceRegister;
import org.example.utils.regerster.service.nacos.NacosRegisterImpl;

import java.lang.reflect.InvocationTargetException;
import java.net.InetSocketAddress;
import java.util.Set;
@Slf4j
public class RpcServerBootstrap {
	private final ServiceRegister register=new NacosRegisterImpl();
	private final InetSocketAddress host;

	private final NioEventLoopGroup boss;

	private final NioEventLoopGroup worker;

	public RpcServerBootstrap(String host,int port) {
		this.host = new InetSocketAddress(host,port);
		autoRegistry();
		boss=new NioEventLoopGroup();
		worker=new NioEventLoopGroup();
	}

	private <T> void addServer(T server, String serverName){
		ServiceFactory.addServiceProvider(server,serverName);
		register.register(serverName,host);
	}

	public void autoRegistry(){
		String mainClassPath = PackageScanUtils.getStackTrace();
		Class<?> mainClass;
		try{
			mainClass=Class.forName(mainClassPath);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			throw new RuntimeException("main class not found");
		}
		if(!mainClass.isAnnotationPresent(RpcBootStrap.class)){
			throw new RuntimeException("main class need annotation \"@RpcBootStrap\"");
		}
		String value = mainClass.getAnnotation(RpcBootStrap.class).value();
		if(value.isBlank()){
			value=mainClassPath.substring(0,mainClassPath.lastIndexOf("."));
		}
		Set<Class<?>> set = PackageScanUtils.getClasses(value);
		log.debug("service size:{}",set.size());
		for (Class<?> cur : set) {
			if(cur.isAnnotationPresent(ServiceProvider.class)){
				String serviceName = cur.getAnnotation(ServiceProvider.class).name();
				Object object;
				try{
					log.debug("build class{}",cur.toString());
					object=cur.getDeclaredConstructor().newInstance();
				} catch (InstantiationException | IllegalAccessException | NoSuchMethodException |
				         InvocationTargetException e) {
					log.debug("create object:{},err:{}",cur,e.getCause());
					e.printStackTrace();
					continue;
				}
				if(serviceName.isBlank()){
					addServer(object,cur.getCanonicalName());
				}else{
					addServer(object,serviceName);
				}
			}
		}
	}

	public void start() {
		try {
			var channelFuture = new ServerBootstrap()
					.group(boss, worker)
					.channel(NioServerSocketChannel.class)
					.childHandler(new ChannelInitializer<SocketChannel>() {
						@Override
						protected void initChannel(SocketChannel ch) {
							var pipeline = ch.pipeline();
							pipeline
									.addLast(BasePineLineFactory.getIdleStateHandler(5,0,0))
									.addLast(BasePineLineFactory.getFrameDecoder())
									.addLast(BasePineLineFactory.getLoggingHandler())
									.addLast(BasePineLineFactory.getCodec())
									.addLast(ServerPineLineFactory.getPingHandler())
									.addLast(ServerPineLineFactory.getDuplex())
									.addLast(ServerPineLineFactory.getRpcRequestHandler())
							;
						}
					})
					.bind(new InetSocketAddress(9000)).sync();
			channelFuture.channel().closeFuture().sync();
		}catch (InterruptedException e){
			e.printStackTrace();
			throw new RuntimeException(e);
		}finally {
			boss.shutdownGracefully();
			worker.shutdownGracefully();
		}
	}
}
