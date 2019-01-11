package com.server.core.netty;

import org.springframework.stereotype.Component;

import com.server.core.message.ResponseMessage;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.ChannelPromise;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;

@Component
@Sharable
public class NettyServerOutHandle extends ChannelOutboundHandlerAdapter{

	@Override
	public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) {		
		if(msg instanceof ByteBuf) {
			ByteBuf byteBuf = (ByteBuf)msg;
			
			FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK, byteBuf);
			
			ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
		} else if (msg instanceof ResponseMessage.gps_data.Builder) {
			ResponseMessage.gps_data.Builder responseMsg = (ResponseMessage.gps_data.Builder)msg;
			
			if(responseMsg.getConnectType() == 1) {
				ByteBuf byteBuf = Unpooled.copiedBuffer("dajiahao".getBytes());
				FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK, byteBuf);
				
				ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
			}
		}
		
		ByteBuf byteBuf = Unpooled.copiedBuffer("dajiahao".getBytes());
		
		ctx.writeAndFlush(byteBuf);
	}
	
	@Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
            throws Exception {
        ctx.fireExceptionCaught(cause);
    }
}
