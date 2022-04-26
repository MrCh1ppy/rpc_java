package org.example.client.handler;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.concurrent.Promise;
import lombok.extern.slf4j.Slf4j;
import org.example.dao.message.RpcResponseMsg;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@ChannelHandler.Sharable
@Slf4j
public class RpcResponseHandler extends SimpleChannelInboundHandler<RpcResponseMsg> {
	private static final ConcurrentHashMap<Integer, Promise<Object>> map=new ConcurrentHashMap<>();

	public static ConcurrentMap<Integer,Promise<Object>> getMap(){
		return map;
	}

	/**
	 * Is called for each message of type {@link }.
	 *
	 * @param ctx the {@link ChannelHandlerContext} which this {@link SimpleChannelInboundHandler}
	 *            belongs to
	 * @param msg the message to handle
	 * @throws Exception is thrown if an error occurred
	 */
	@Override
	protected void channelRead0(ChannelHandlerContext ctx, RpcResponseMsg msg) throws Exception {
		log.debug("{}",msg);
		//用完删除
		Promise<Object> promise = map.remove(msg.getSequenceId());
		if(promise!=null){
			Object value = msg.getReturnValue();
			Exception exceptionValue = msg.getExceptionValue();
			if(exceptionValue!=null){
				promise.setFailure(exceptionValue);
			}else {
				promise.setSuccess(value);
			}
		}
	}
}
