package org.example.dao.message;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RpcResponseMsg extends Message {
	private static final MsgType type=MsgType.RESPONSE;
	private Object returnValue;
	private Exception exceptionValue;


	public RpcResponseMsg(Object returnValue, Exception exceptionValue) {
		super(type);
		this.returnValue = returnValue;
		this.exceptionValue = exceptionValue;
	}

	public RpcResponseMsg() {
		super(type);
	}
}
