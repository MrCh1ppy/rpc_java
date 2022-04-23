package org.example.dao.message;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RpcRequestMsg extends Message{
	private static final MsgType type=MsgType.REQUEST;
	private String interfaceName;
	private String methodName;
	private Class<?> returnType;
	private Class<?>[] parameterTypes;
	private Object[] parameterValue;

	public RpcRequestMsg(String interfaceName, String methodName, Class<?> returnType, Class<?>[] parameterTypes, Object[] parameterValue) {
		super(type);
		this.interfaceName = interfaceName;
		this.methodName = methodName;
		this.returnType = returnType;
		this.parameterTypes = parameterTypes;
		this.parameterValue = parameterValue;
	}

	public RpcRequestMsg() {
		super(type);
	}
}
