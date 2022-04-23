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
	public static void main(String[] args) {
		var worker = new NioEventLoopGroup();
		try{
			var future = new Bootstrap()
					.group(worker)
					.channel(NioSocketChannel.class)
					.handler(new ChannelInitializer<SocketChannel>() {
						@Override
						protected void initChannel(SocketChannel ch) throws Exception {
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
			future.channel().writeAndFlush(new RpcRequestMsg(
					"org.example.service.HelloService",
					"hello",
					String.class,
					new Class[]{String.class},
					new Object[]{"css"}
					));
			future.channel().closeFuture().sync();
		}catch (InterruptedException e){
			throw new RuntimeException(e);
		}finally {
			worker.shutdownGracefully();
		}

	}

}
