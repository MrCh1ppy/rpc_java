package org.example.utils.codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageCodec;
import io.netty.handler.codec.MessageToMessageDecoder;
import io.netty.handler.codec.MessageToMessageEncoder;
import lombok.extern.slf4j.Slf4j;
import org.example.dao.message.Message;
import org.example.dao.message.MsgType;
import org.example.utils.serialize.SerializerFactory;

import java.util.List;

@ChannelHandler.Sharable
@Slf4j
public class Codec extends MessageToMessageCodec<ByteBuf,Message> {

	private static final byte[] magicNum={1,1,4,5,1};


	/**
	 * @param ctx
	 * @param msg
	 * @param out
	 * @see MessageToMessageEncoder(ChannelHandlerContext, Object, List)
	 */
	@Override
	protected void encode(ChannelHandlerContext ctx, Message msg, List<Object> out) throws Exception {
		var buf = ctx.alloc().buffer();
		var version = 1;
		var sWay = 1;

		var text=switch (sWay){
			case 0-> SerializerFactory.JDK.serialize(msg);
			case 1->SerializerFactory.JSON.serialize(msg);
			default -> throw new IllegalStateException("Unexpected value: " + sWay);
		};

		var len=text.length;

		buf.writeBytes(magicNum)
				//version
				.writeByte(version)
				//0jdk 1json read way
				.writeByte(sWay)
				//msg code
				.writeByte(msg.getMsgCode())
				//sequence id
				.writeInt(msg.getSequenceId())
				//len
				.writeInt(len)
				//text
				.writeBytes(text);
		out.add(buf);
	}

	/**
	 * @param ctx
	 * @param msg
	 * @param out
	 * @see MessageToMessageDecoder(ChannelHandlerContext, Object, List)
	 */
	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf msg, List<Object> out) throws Exception {
		msg.readBytes(5);
		msg.readByte();
		var readWay = msg.readByte();
		var msgCode = msg.readByte();
		msg.readInt();
		var len = msg.readInt();
		var text = new byte[len];

		log.debug("text is {}",new String(text));
		msg.readBytes(text,0,len);
		var object=switch (readWay){
			case 0-> SerializerFactory.JDK.deSerializer(Message.class,text);
			case 1->{
				var type = MsgType.values()[msgCode].getClazz();
				yield SerializerFactory.JSON.deSerializer(type, text);
			}
			default -> throw new IllegalStateException("Unexpected value: " + readWay);
		};
		System.out.println(object.toString());
		out.add(object);
	}
}
