package org.example.dao.message;

public enum MsgType {
	RESPONSE((byte)0, RpcResponseMsg.class),
	REQUEST((byte)1,RpcRequestMsg.class),
	PING((byte) 2,PingMessage.class);

	private final byte code;
	private final Class<? extends Message> clazz;
	MsgType(byte code, Class<? extends Message> messageClass) {
		this.code=code;
		this.clazz=messageClass;
	}

	public int getCode() {
		return code;
	}

	public Class<? extends Message> getClazz() {
		return clazz;
	}
}
