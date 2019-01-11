package com.server.core.netty.protocol.impl;

import java.util.HashMap;
import java.util.Map;

import com.google.gson.Gson;
import com.server.core.message.socket.SocketRequestMsg;
import com.server.core.message.socket.SocketResponseMsg;
import com.server.core.netty.protocol.ProtocloFctory;
import com.server.core.netty.protocol.Protocol;
import com.server.core.spring.action.Action;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.websocketx.CloseWebSocketFrame;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;

public class WebSocketProtocol implements Protocol {

	public static Map<String, Channel> channelMap = new HashMap<String, Channel>();
	private ChannelHandlerContext ctx;
	private WebSocketFrame msg;

	public WebSocketProtocol(ChannelHandlerContext ctx, WebSocketFrame msg) {
		this.ctx = ctx;
		this.msg = msg;
	}

	@Override
	public void response() {

		SocketResponseMsg response = null;

		if (msg instanceof CloseWebSocketFrame) {	
			CloseWebSocketFrame closeMsg = (CloseWebSocketFrame)msg;
			closeMsg.reasonText();
			ProtocloFctory.handshaker.close(ctx.channel(), (CloseWebSocketFrame) msg.retain());
		}

		if (msg instanceof TextWebSocketFrame) {
			TextWebSocketFrame data = (TextWebSocketFrame) msg;

			Gson gson = new Gson();
			SocketRequestMsg requestMsg = gson.fromJson(data.text(), SocketRequestMsg.class);

			if("101".equals(requestMsg.getCode())) {
				if (channelMap.containsKey(requestMsg.getUserId())) {
					channelMap.remove(requestMsg.getUserId());
				}

				channelMap.put(requestMsg.getUserId(), ctx.channel());
			}
			
			response = Action.toBattle(requestMsg);

			if(response != null && response.getSendUserId() != null && response.getSendUserId().size() > 0) {
				for (String userId : response.getSendUserId()) {
					if(channelMap.get(userId).isOpen()) {
						channelMap.get(userId).writeAndFlush(new TextWebSocketFrame(gson.toJson(response)));	
					}
				}	
			}
		}
	}

}
