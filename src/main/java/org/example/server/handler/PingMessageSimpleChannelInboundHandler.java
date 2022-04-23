package org.example.server.handler;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;
import org.example.dao.message.PingMessage;

@Slf4j
@ChannelHandler.Sharable
public class PingMessageSimpleChannelInboundHandler extends SimpleChannelInboundHandler<PingMessage> {
	/**
	 * Is called for each message of type {@link I}.
	 *
	 * @param ctx the {@link ChannelHandlerContext} which this {@link SimpleChannelInboundHandler}
	 *            belongs to
	 * @param msg the message to handle
	 * @throws Exception is thrown if an error occurred
	 */
	@Override
	public void channelRead0(ChannelHandlerContext ctx, PingMessage msg) throws Exception {
		log.debug("get heart beat");
	}
}
