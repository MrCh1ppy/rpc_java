package org.example.utils;

import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.IdleStateHandler;
import org.example.utils.codec.Codec;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public interface BasePineLineFactory {

	Codec CODEC = new Codec();
	LoggingHandler LOGGING_HANDLER = new LoggingHandler(LogLevel.DEBUG);

	@Contract(" -> new")
	static @NotNull LengthFieldBasedFrameDecoder getFrameDecoder(){
		return new LengthFieldBasedFrameDecoder(10024,12,4,0,0);
	}

	@Contract(value = "_, _, _ -> new", pure = true)
	static @NotNull IdleStateHandler getIdleStateHandler(int readerIdleTimeSeconds,
	                                                     int writerIdleTimeSeconds,
	                                                     int allIdleTimeSeconds){
		return new IdleStateHandler(readerIdleTimeSeconds,writerIdleTimeSeconds,allIdleTimeSeconds);
	}

	@Contract(" -> new")
	static @NotNull LoggingHandler getLoggingHandler(){
		return LOGGING_HANDLER;
	}

	@Contract(pure = true)
	static Codec getCodec(){
		return CODEC;
	}
}
