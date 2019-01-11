package com.server.core.netty;

import org.springframework.stereotype.Component;

import com.server.core.netty.protocol.ProtocloFctory;
import com.server.core.netty.protocol.Protocol;

import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;

@Component
@Sharable
public class NettyServerInHandle extends ChannelInboundHandlerAdapter{

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) {		
		Protocol protocol = ProtocloFctory.create(ctx, msg);
		
		if(protocol != null) {
			protocol.response();
		}
		
		ReferenceCountUtil.release(msg);
	}
	
	@Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
            throws Exception {
        ctx.fireExceptionCaught(cause);
    }
}