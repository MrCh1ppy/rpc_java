package org.example.dao.message;

import lombok.ToString;

@ToString
public class PingMessage extends Message{
	private static final MsgType type=MsgType.PING;

	public PingMessage() {
		super(type);
	}
}
