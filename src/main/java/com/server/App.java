package com.server;

import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.server.core.netty.NettyServer;

/**
 * Hello world!
 *
 */
public class App {
	public static void main(String[] args) {
		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(
				"com/server/core/spring/config/spring.xml");
		NettyServer server = context.getBean(NettyServer.class);
		server.start();
		context.close();
	}
}
