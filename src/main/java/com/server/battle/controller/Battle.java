package com.server.battle.controller;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;

import com.server.core.annotation.WebSocketController;
import com.server.core.annotation.WebSocketRequest;
import com.server.core.message.socket.SocketRequestMsg;
import com.server.core.message.socket.SocketResponseMsg;
import com.server.core.netty.protocol.impl.WebSocketProtocol;

import io.netty.channel.Channel;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

@WebSocketController
public class Battle {

	@Autowired
	private JedisPool jedisPool;
	
	/**
	 * 简单石头布游戏
	 * @param requestMsg
	 * @return
	 */
	@WebSocketRequest("101")
	public SocketResponseMsg battleCreate(SocketRequestMsg requestMsg) {
		
		SocketResponseMsg response = new SocketResponseMsg();
		
		Jedis jedis = jedisPool.getResource();
		
		response = getBattleUser(jedis, requestMsg);
		
		jedis.close();
		return response;
	}
	
	@WebSocketRequest("104")
	public SocketResponseMsg battleResult(SocketRequestMsg requestMsg) {
		List<String> sendUserId = new ArrayList<String>();
		SocketResponseMsg response = new SocketResponseMsg();
		
		Jedis jedis = jedisPool.getResource();
		String key = requestMsg.getRoomNum();
		
		jedis.hset(key, requestMsg.getUserId(), requestMsg.getParam());
		
		if(jedis.hlen(key) > 1) {
			Map<String,String> battleMap = jedis.hgetAll(key);
			
			response.setCode("204");
			
			response.setDraw(false);
			
			Collection<String> values = battleMap.values();
			
			if(values.contains("1") && values.contains("2") && !values.contains("3")) {
				response.setSuccessResult("2");
			} else if(values.contains("1") && values.contains("3") && !values.contains("2")) {
				response.setSuccessResult("1");
			} else if(values.contains("2") && values.contains("3") && !values.contains("1")) {
				response.setSuccessResult("3");
			} else {
				response.setDraw(true);
			}
			
			sendUserId.addAll(battleMap.keySet());
			jedis.del(key);
			jedis.close();
		}
		response.setSendUserId(sendUserId);
		return response;
	}
	
	/**
	 * 简单石头布游戏
	 * @param requestMsg
	 * @return
	 */
	@WebSocketRequest("112")
	public SocketResponseMsg WarChessBattleCreate(SocketRequestMsg requestMsg) {
		
		SocketResponseMsg response = new SocketResponseMsg();
		
		Jedis jedis = jedisPool.getResource();
		
		jedis.hset(requestMsg.getRoomNum(), requestMsg.getUserId(), requestMsg.getParam());
		
		jedis.close();
		return response;
	}
	
	/**
	 * 简单石头布游戏
	 * @param requestMsg
	 * @return
	 */
	@WebSocketRequest("114")
	public SocketResponseMsg WarChessBattle(SocketRequestMsg requestMsg) {
		List<String> sendUserId = new ArrayList<String>();
		SocketResponseMsg response = new SocketResponseMsg();
		
		Jedis jedis = jedisPool.getResource();
		String key = requestMsg.getRoomNum();
		
		response.setDraw(true);
		response.setCode("213");
		response.setUserId(requestMsg.getUserId());
		response.setParam(requestMsg.getParam());
		
		if((jedis.hexists(key, requestMsg.getUserId()) && jedis.hlen(key) > 1)
				|| (!jedis.hexists(key, requestMsg.getUserId()) && jedis.hlen(key) > 0)) {
			Map<String,String> battleMap = jedis.hgetAll(key);
			
			Collection<String> values = battleMap.values();
			
			if(values.contains(requestMsg.getParam())) {
				response.setCode("214");
				response.setSuccessResult(requestMsg.getUserId());
				response.setDraw(false);
				jedis.del(key);
			}

			sendUserId.addAll(battleMap.keySet());	
		}
		
		if(!sendUserId.contains(requestMsg.getUserId())) {
			sendUserId.add(requestMsg.getUserId());
		}
		
		if(response.isDraw()) {
			jedis.hset(key, requestMsg.getUserId(), requestMsg.getParam());	
		}
		
		response.setSendUserId(sendUserId);
		
		jedis.close();
		return response;
	}
	
	private SocketResponseMsg getBattleUser(Jedis jedis, SocketRequestMsg requestMsg) {
		SocketResponseMsg response = new SocketResponseMsg();
		
		List<String> sendUserId = new ArrayList<String>();

		if (jedis.exists(requestMsg.getLevel()) && jedis.llen(requestMsg.getLevel()) > 0) {
			String userId = jedis.rpop(requestMsg.getLevel());
			
			boolean falseFlg = false;
			
			if(!userId.equals(requestMsg.getUserId()) && WebSocketProtocol.channelMap.containsKey(userId)) {
				
				Channel channel = WebSocketProtocol.channelMap.get(userId);
				
				if(channel.isOpen()) {
					String key = UUID.randomUUID().toString();			
					sendUserId.add(userId);
					sendUserId.add(requestMsg.getUserId());
					response.setCode("201");
					response.setRoomNum(key);
					response.setSendUserId(sendUserId);
				} else {
					channel.disconnect();
					WebSocketProtocol.channelMap.remove(userId);
					falseFlg = true;
				}
			} else {
				falseFlg = true;
			}
			
			if(falseFlg) {
				response = getBattleUser(jedis, requestMsg);
			}
			
		} else {
			sendUserId.add(requestMsg.getUserId());
			response.setCode("200");
			response.setSendUserId(sendUserId);
			jedis.lpush(requestMsg.getLevel(), requestMsg.getUserId());
		}
		
		return response;
	}
}
