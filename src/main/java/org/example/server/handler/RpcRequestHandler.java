package org.example.server.handler;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.example.dao.message.RpcRequestMsg;
import org.example.dao.message.RpcResponseMsg;
import org.example.utils.service.ServiceFactory;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.InvocationTargetException;

@ChannelHandler.Sharable
public class RpcRequestHandler extends SimpleChannelInboundHandler<RpcRequestMsg> {
	/**
	 * Is called for each message of type {@link }.
	 *
	 * @param ctx the {@link ChannelHandlerContext} which this {@link SimpleChannelInboundHandler}
	 *            belongs to
	 * @param request the message to handle
	 * @throws Exception is thrown if an error occurred
	 */
	@Override
	protected void channelRead0(ChannelHandlerContext ctx, @NotNull RpcRequestMsg request){
		Object res=null;
		Exception exception=null;
		try{
			var instance = ServiceFactory.getInstance(Class.forName(request.getInterfaceName()));
			var method = instance.getClass().getMethod(request.getMethodName(), request.getParameterTypes());
			res = method.invoke(instance, request.getParameterValue());
		}catch (ClassNotFoundException | NoSuchMethodException | IllegalAccessException | InvocationTargetException e){
			e.printStackTrace();
			exception=e;
		}finally {
			ctx.writeAndFlush(new RpcResponseMsg(res,exception).setSequenceId(request.getSequenceId()));
		}
	}
}
