package com.server.core.netty;

import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpContentCompressor;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.stream.ChunkedWriteHandler;

@Component
public class ChildChannelHandler extends ChannelInitializer<SocketChannel> {

/*	@Autowired
	private NettyServerOutHandle nettyServerOutHandle;*/
	
	@Autowired
	private NettyServerInHandle nettyServerInHandle;
	
	@Override
	protected void initChannel(SocketChannel arg0) throws Exception {
		
		// 日志
		arg0.pipeline().addLast(new LoggingHandler(Logger.class));
		
		// http消息解码器
		// arg0.pipeline().addLast(new HttpRequestDecoder());
		
		// http消息解码器
		arg0.pipeline().addLast(new HttpServerCodec());
		
		// 将多个消息转换为单一的FullHttpRequest或FullHttpResponse
		// 原因是http解码器会在每个http消息中生成多个http对象(HttpRequest/HttpResponse,HttpContent,LstHttpContent)
		arg0.pipeline().addLast(new HttpObjectAggregator(65536));
		
		// http响应编码器
		// arg0.pipeline().addLast(new HttpResponseEncoder());
		
		arg0.pipeline().addLast("compressor", new HttpContentCompressor());
		
		// 支持异步发送打的码流（如文件传输），但不占用过多内存，防止内存溢出
		arg0.pipeline().addLast(new ChunkedWriteHandler());
				
		// arg0.pipeline().addLast(nettyServerOutHandle);
		arg0.pipeline().addLast(nettyServerInHandle);
	}

}
