package com.server.core.netty.protocol.impl;

import com.server.core.message.ResponseMessage.gps_data.Builder;
import com.server.core.netty.protocol.Protocol;
import com.server.core.spring.action.Action;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.FullHttpRequest;

public class HttpProtocol implements Protocol {

	private FullHttpRequest msg;
	
	public HttpProtocol(ChannelHandlerContext ctx, FullHttpRequest msg) {
		this.msg = msg;
	}
	
	@Override
	public Builder response() {
		Builder responseMsg = Action.toRequest(msg);
		responseMsg.setConnectType(1);
		return responseMsg;
	}
}
