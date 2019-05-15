package com.server.core.cache;

import java.util.Map;
import java.util.concurrent.Callable;

import org.springframework.cache.Cache;

import com.google.gson.Gson;

import io.lettuce.core.RedisClient;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.sync.RedisCommands;

public class RedisCache implements Cache {

	private StatefulRedisConnection redisConnection;
	
	private String name;
	
	public RedisCache(RedisClient redisClient, String name) {
		this.redisConnection = redisClient.connect();
		this.name = name;
	}
	
	@Override
	public String getName() {
		return this.name;
	}

	@Override
	public Object getNativeCache() {
		return this;
	}

	@Override
	public ValueWrapper get(Object key) {
		
		String keyStr = "";
		
		if (key instanceof String) {
			keyStr = (String) key;
		} else {
			Gson gson = new Gson();
			keyStr = gson.toJson(key);
		}
		
		RedisValueWrapper result = new RedisValueWrapper();	
		RedisCommands command = redisConnection.sync();
		command.select(1);
		Map<String, String> valueMap = command.hgetall(keyStr);
		if(valueMap == null || valueMap.isEmpty()) {
			return null;
		}
		
		result.setValue(valueMap);
		return result;
	}

	@Override
	public <T> T get(Object key, Class<T> type) {
		
		Gson gson = new Gson();
		String keyStr = "";
		
		if (key instanceof String) {
			keyStr = (String) key;
		} else {
			keyStr = gson.toJson(key);
		}
		
		RedisCommands command = redisConnection.sync();
		command.select(1);
		String value = (String) command.hget(keyStr, type.getName());
		T result = gson.fromJson(value, type);
		return result;
	}

	@Override
	public <T> T get(Object key, Callable<T> valueLoader) {
		// TODO Auto-generated method stub
		try {
			return valueLoader.call();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}

	@Override
	public void put(Object key, Object value) {
		
		if(value == null) {
			return;
		}
		
		Gson gson = new Gson();
		String keyStr = "";
		String valueJson = "";
		
		if (key instanceof String) {
			keyStr = (String) key;
		} else {
			keyStr = gson.toJson(key);
		}
		
		if (value instanceof String) {
			valueJson = (String) value;
		} else {
			valueJson = gson.toJson(value);
		}
		
		RedisCommands command = redisConnection.sync();
		command.select(1);
		command.hset(keyStr, value.getClass().getName(), valueJson);
	}

	@Override
	public ValueWrapper putIfAbsent(Object key, Object value) {
		
		if(value == null) {
			return null;
		}
		
		Gson gson = new Gson();
		String keyStr = "";
		String valueJson = "";
		
		if (key instanceof String) {
			keyStr = (String) key;
		} else {
			keyStr = gson.toJson(key);
		}
		
		if (value instanceof String) {
			valueJson = (String) value;
		} else {
			valueJson = gson.toJson(value);
		}
		
		RedisCommands command = redisConnection.sync();
		command.select(1);
		command.hset(keyStr, value.getClass().getName(), valueJson);
		
		
		RedisValueWrapper result = new RedisValueWrapper();	
		Map<String, String> valueMap = command.hgetall(keyStr);
		result.setValue(valueMap);
		return result;
	}

	@Override
	public void evict(Object key) {
		
		String keyStr = "";
		
		if (key instanceof String) {
			keyStr = (String) key;
		} else {
			Gson gson = new Gson();
			keyStr = gson.toJson(key);
		}
		
		RedisCommands command = redisConnection.sync();
		command.select(1);
		command.del(keyStr);
	}

	@Override
	public void clear() {
		RedisCommands command = redisConnection.sync();
		command.select(1);
		command.flushdb();
	}	
}
