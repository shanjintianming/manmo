package com.server.core.netty.protocol;

import org.springframework.stereotype.Component;

import com.server.core.netty.protocol.impl.HttpProtocol;
import com.server.core.netty.protocol.impl.WebSocketProtocol;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketServerHandshaker;
import io.netty.handler.codec.http.websocketx.WebSocketServerHandshakerFactory;

@Component
public class ProtocloFctory {
	
	public volatile static WebSocketServerHandshaker handshaker;

	public static Protocol create(ChannelHandlerContext ctx, Object msg) {

		if (msg instanceof FullHttpRequest) {
			FullHttpRequest req = (FullHttpRequest) msg;
			String upgrade = req.headers().get(HttpHeaderNames.UPGRADE);
			if (req.decoderResult().isSuccess()) {
				if (upgrade != null && upgrade.equals("websocket")) {
					WebSocketServerHandshakerFactory socketFactory = new WebSocketServerHandshakerFactory(
							"ws://localhost:8082/socket", null, false);
					handshaker = socketFactory.newHandshaker(req);

					if (handshaker == null) {
						WebSocketServerHandshakerFactory.sendUnsupportedVersionResponse(ctx.channel());
					} else {
						ProtocloFctory.handshaker.handshake(ctx.channel(), (FullHttpRequest) msg);
					}
				} else {
					return new HttpProtocol(ctx, req);
				}
			}
		} else if (msg instanceof WebSocketFrame) {
			return new WebSocketProtocol(ctx, (WebSocketFrame)msg);
		}

		return null;
	}

}
