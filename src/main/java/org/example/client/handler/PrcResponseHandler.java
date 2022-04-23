package org.example.client.handler;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;
import org.example.dao.message.RpcResponseMsg;
@ChannelHandler.Sharable
@Slf4j
public class PrcResponseHandler extends SimpleChannelInboundHandler<RpcResponseMsg> {
	/**
	 * Is called for each message of type {@link I}.
	 *
	 * @param ctx the {@link ChannelHandlerContext} which this {@link SimpleChannelInboundHandler}
	 *            belongs to
	 * @param msg the message to handle
	 * @throws Exception is thrown if an error occurred
	 */
	@Override
	protected void channelRead0(ChannelHandlerContext ctx, RpcResponseMsg msg) throws Exception {
		log.debug("receive response...{}",msg);
	}
}
