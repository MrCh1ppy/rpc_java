package org.example.server.handler;

import io.netty.channel.*;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import lombok.extern.slf4j.Slf4j;

@ChannelHandler.Sharable
@Slf4j
public class DuplexHandler extends ChannelDuplexHandler {

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
		if (event.state() == IdleState.READER_IDLE) {
			log.debug("need to lost=============>");
			System.out.println("need to lost=============>");
			ctx.channel().close();
		}
	}
}
