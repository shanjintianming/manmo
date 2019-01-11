package com.server.core.cache;

import java.util.Map;

import org.springframework.cache.Cache.ValueWrapper;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

public class RedisValueWrapper implements ValueWrapper {

	private Map<String,String> value;

	@Override
	public Object get() {
		Gson gson = new Gson();
		try {
			String valueJson = value.values().iterator().next();
			Class<?> type = Class.forName(value.keySet().iterator().next());
			Object obj = gson.fromJson(valueJson, type);
			return obj;
		} catch (JsonSyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}

	public void setValue(Map<String,String> value) {
		this.value = value;
	}
}
