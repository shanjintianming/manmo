package com.server.core.netty;

import org.springframework.stereotype.Component;

import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.util.JsonFormat;
import com.server.core.message.ResponseMessage.gps_data.Builder;
import com.server.core.netty.protocol.ProtocloFctory;
import com.server.core.netty.protocol.Protocol;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.util.ReferenceCountUtil;

@Component
@Sharable
public class NettyServerInHandle extends ChannelInboundHandlerAdapter{

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) {		
		Protocol protocol = ProtocloFctory.create(ctx, msg);
		
		Object obj = null;
		if(protocol != null) {
			obj = protocol.response();
		}
		
		if(obj != null) {
			Builder responseData = (Builder)obj;
			String responseJsonData = "";
			try {
				responseJsonData = JsonFormat.printer().print(responseData);
			} catch (InvalidProtocolBufferException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			ByteBuf byteBuf = Unpooled.copiedBuffer(responseJsonData.getBytes());
			FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK);
			response.headers().set("Content-Type", "text/json; charset=UTF-8");
			response.content().writeBytes(byteBuf);  
			byteBuf.release();
			ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);	
		}
				
		ReferenceCountUtil.release(msg);
	}
	
	@Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
            throws Exception {
        ctx.fireExceptionCaught(cause);
    }
}