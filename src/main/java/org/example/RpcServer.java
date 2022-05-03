package org.example;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import lombok.extern.slf4j.Slf4j;
import org.example.server.RpcServerBootstrap;
import org.example.utils.BasePineLineFactory;
import org.example.utils.regerster.annotation.RpcBootStrap;

import java.net.InetSocketAddress;

@Slf4j
@RpcBootStrap
public class RpcServer {

	public static void main(String[] args) {
		new RpcServerBootstrap("127.0.0.1",9000).start();
	}

}
