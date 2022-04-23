package org.example.client;

import org.example.client.handler.ClientDuplexHandler;

public interface ClientPineLineFactory {

	ClientDuplexHandler DUPLEX_HANDLER = new ClientDuplexHandler();

	static ClientDuplexHandler getClientDuplexHandler(){
		return DUPLEX_HANDLER;
	}
}
