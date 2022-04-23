package org.example.client.handler;

import io.netty.channel.*;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import lombok.extern.slf4j.Slf4j;
import org.example.dao.message.PingMessage;

@ChannelHandler.Sharable
@Slf4j
public class ClientDuplexHandler extends ChannelDuplexHandler {
	/**
	 * Calls {@link ChannelHandlerContext#fireChannelActive()} to forward
	 * to the next {@link ChannelInboundHandler} in the {@link ChannelPipeline}.
	 * <p>
	 * Sub-classes may override this method to change behavior.
	 *
	 * @param ctx
	 */
	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		log.debug("heartbeat is on the way");
		ctx.writeAndFlush(new PingMessage());
	}

	/**
	 * Calls {@link ChannelHandlerContext#fireUserEventTriggered(Object)} to forward
	 * to the next {@link ChannelInboundHandler} in the {@link ChannelPipeline}.
	 * <p>
	 * Sub-classes may override this method to change behavior.
	 *
	 * @param ctx
	 * @param evt
	 */
	@Override
	public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
		var event = (IdleStateEvent) evt;
		if (event.state() == IdleState.WRITER_IDLE) {
			ctx.writeAndFlush(new PingMessage());
		}
	}
}
