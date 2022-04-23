package org.example.dao.message;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.jetbrains.annotations.Contract;

import java.io.Serializable;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicInteger;
@JsonIgnoreProperties(ignoreUnknown = true)
public abstract class Message implements Serializable {

	@JsonIgnore
	private final MsgType type;
	private final int sequenceId;

	@JsonIgnore
	private static final AtomicInteger sequenceNum=new AtomicInteger(ThreadLocalRandom.current().nextInt(100));

	@Contract(pure = true)
	protected Message(MsgType type) {
		this.type = type;
		this.sequenceId = sequenceNum.addAndGet(1);
	}

	public byte getMsgCode() {
		return (byte) type.getCode();
	}

	public int getSequenceId() {
		return sequenceId;
	}
}
