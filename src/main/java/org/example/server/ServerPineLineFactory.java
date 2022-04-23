package org.example.server;

import org.example.server.handler.DuplexHandler;
import org.example.server.handler.PingMessageSimpleChannelInboundHandler;
import org.example.server.handler.RpcRequestHandler;
import org.jetbrains.annotations.Contract;

public interface ServerPineLineFactory {

	PingMessageSimpleChannelInboundHandler PING_HANDLER = new PingMessageSimpleChannelInboundHandler();
	DuplexHandler DUPLEX_HANDLER = new DuplexHandler();
	RpcRequestHandler RPC_REQUEST_HANDLER = new RpcRequestHandler();

	@Contract(pure = true)
	static PingMessageSimpleChannelInboundHandler getPingHandler(){
		return PING_HANDLER;
	}

	@Contract(pure = true)
	static DuplexHandler getDuplex(){
		return DUPLEX_HANDLER;
	}

	static RpcRequestHandler getRpcRequestHandler(){
		return RPC_REQUEST_HANDLER;
	}
}

