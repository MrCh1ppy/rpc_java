package org.example.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import lombok.extern.slf4j.Slf4j;
import org.example.utils.BasePineLineFactory;

import java.net.InetSocketAddress;

@Slf4j
public class RpcServer {

	public static void main(String[] args) {
		var boss = new NioEventLoopGroup();
		var worker = new NioEventLoopGroup();
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
