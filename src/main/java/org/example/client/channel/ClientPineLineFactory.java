package org.example.client.channel;

import org.example.client.handler.ClientDuplexHandler;
import org.example.client.handler.RpcResponseHandler;

public interface ClientPineLineFactory {

	ClientDuplexHandler DUPLEX_HANDLER = new ClientDuplexHandler();
	RpcResponseHandler RESPONSE_HANDLER = new RpcResponseHandler();

	static ClientDuplexHandler getClientDuplexHandler(){
		return DUPLEX_HANDLER;
	}

	static RpcResponseHandler getRpcResponseHandler(){
		return RESPONSE_HANDLER;
	}
}
