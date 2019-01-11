package com.server.core.netty;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

@Component
public class NettyServer {

	// 负责网络事件的责任链，负责管理和执行channelhandler
	@Autowired
	private ChildChannelHandler childChannelHandler;
	
	public void start() {
		// bossGroup就是Reactor模型中的acceptor，负责处理客户端产生的TCP连接请求
		// workerGroup则是worker，真正负责IO读写操作
		EventLoopGroup bossGroup = new NioEventLoopGroup();
		EventLoopGroup workerGroup = new NioEventLoopGroup();

		// 辅助启动类
		ServerBootstrap b = new ServerBootstrap();

		b.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class).option(ChannelOption.SO_BACKLOG, 1024)
				.childHandler(childChannelHandler);

		ChannelFuture f;
		try {
			f = b.bind(8082).sync();
			f.channel().closeFuture().sync();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			bossGroup.shutdownGracefully();
			workerGroup.shutdownGracefully();
		}
	}

}
