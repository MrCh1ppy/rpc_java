package org.example.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import lombok.extern.slf4j.Slf4j;
import org.example.dao.message.RpcRequestMsg;
import org.example.utils.BasePineLineFactory;

import java.net.InetSocketAddress;

@Slf4j
public class PpcClient {

	private static Channel channel=null;
	private static final Object lock=new Object();
	public static Channel getChannel(){
		if(channel!=null){
			return channel;
		}
		synchronized (lock){
			if(channel!=null){
				return channel;
			}
			initChannel();
			return channel;
		}
	}
	public static void main(String[] args) {
		getChannel().writeAndFlush(new RpcRequestMsg(
				"org.example.service.HelloService",
				"hello",
				String.class,
				new Class[]{String.class},
				new Object[]{"css"}
		));
	}

	private static void initChannel() {
		var worker = new NioEventLoopGroup();
		try{
			var future = new Bootstrap()
					.group(worker)
					.channel(NioSocketChannel.class)
					.handler(new ChannelInitializer<SocketChannel>() {
						@Override
						protected void initChannel(SocketChannel ch) {
							var pipeline = ch.pipeline();
							pipeline
									.addLast(BasePineLineFactory.getIdleStateHandler(0,3,0))
									.addLast(BasePineLineFactory.getFrameDecoder())
									.addLast(BasePineLineFactory.getLoggingHandler())
									.addLast(BasePineLineFactory.getCodec())
									.addLast(ClientPineLineFactory.getClientDuplexHandler())
									;
						}
					})
					.connect(new InetSocketAddress("localhost",9000)).sync();
			channel=future.channel();
			future.channel().closeFuture().addListener(future1 -> worker.shutdownGracefully());
		}catch (InterruptedException e){
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

}
