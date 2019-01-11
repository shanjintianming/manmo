package com.server.core.spring.action;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;
import com.server.core.message.RequestMesssage;
import com.server.core.message.ResponseMessage;
import com.server.core.message.socket.SocketResponseMsg;

import io.netty.buffer.ByteBuf;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.QueryStringDecoder;

public class Action {
	public static Map<String, ActionBean> actionMap = new HashMap<String, ActionBean>();

	public static ResponseMessage.gps_data.Builder toRequest(FullHttpRequest msg) {
		return actionMethod(msg.uri(), msg.content());
	}
	
	public static SocketResponseMsg toBattle(RequestMesssage requestMsg) {
		return battleMethod(requestMsg);
	}
	
	private static ResponseMessage.gps_data.Builder actionMethod(String uri, ByteBuf requestBuf) {
		
		ResponseMessage.gps_data.Builder responseMessage = ResponseMessage.gps_data.newBuilder();
		
		byte[] requestBytes = new byte[requestBuf.readableBytes()];
		requestBuf.readBytes(requestBytes);
		
		Gson gson = new Gson();
		try {
			String messageBody = new String(requestBytes, "UTF-8");
			Map<?, ?> messageMap = null;
			try {
				messageMap = gson.fromJson(messageBody, Map.class);
			} catch (Exception e) {
				QueryStringDecoder decode = new QueryStringDecoder(messageBody, false);
				messageMap = decode.parameters();
			}
			
			if(uri.indexOf("/interview") == 0) {
				uri = uri.substring(10);
			}
			
			if (Action.actionMap.containsKey(uri)) {
				ActionBean action = Action.actionMap.get(uri);
				List<Parameter> paramLst = Arrays.asList(action.getMethod().getParameters());
				List<Object> objLst = new ArrayList<Object>();
				for (Parameter param : paramLst) {
					Object obj = null;
					
					if (messageMap.containsKey(param.getName())) {
						Object requestValue = messageMap.get(param.getName());
						if (requestValue != null) {							
							obj = castValue(requestValue, param.getType());
						}
					} else {
						if(!param.getType().isInterface()) {
							
							obj = param.getType().newInstance();
							
							Field[] fields = obj.getClass().getDeclaredFields();
							for (Field field : fields) {
								String fieldName = field.getName();
								Object requestValue = messageMap.get(fieldName);
								Object value = null;

								if (requestValue != null) {
									value = castValue(requestValue, field.getType());

									String setMethod = "set" + fieldName.toUpperCase().substring(0, 1)
											+ fieldName.substring(1, fieldName.length());
									Method setReadOnly = obj.getClass().getMethod(setMethod, field.getType());
									setReadOnly.invoke(obj, value);
								}
							}	
						}
					}
					objLst.add(obj);
				}
				action.getMethod().invoke(action.getObj(), objLst.toArray());
				responseMessage.setCodeId("0001");
			}
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return responseMessage;
	}
	
	private static SocketResponseMsg battleMethod(RequestMesssage requestMsg) {
		SocketResponseMsg response = null;
		try {
			if (Action.actionMap.containsKey(requestMsg.getCode())) {
				ActionBean action = Action.actionMap.get(requestMsg.getCode());
				Object obj = action.getMethod().invoke(action.getObj(), requestMsg);
				
				if(obj instanceof SocketResponseMsg) {
					response = 	(SocketResponseMsg)obj;
				}			
			}
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return response;
	}
	
	private static Object castValue(Object requestValue, Class<?> valueClass) throws InstantiationException, 
	IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {
		
		Object obj = null;
		List<?> valueLst = null;
		if(requestValue instanceof List) {
			valueLst = (List<?>) requestValue;
		} else {
			valueLst = Arrays.asList(requestValue);
		}
		
		if (valueClass.isArray()) {
			obj = valueLst.toArray();
		} else {
			if (valueClass.isInterface()) {
				if(valueClass.isInstance(valueLst)) {
					obj = valueLst;
				} else {
					obj = valueLst.get(0);
				}
			} else {
				if(valueClass.newInstance() instanceof List) {
					obj = valueClass.getConstructor(Collection.class).newInstance(valueLst);
				} else {
					obj = valueLst.get(0);
				}
			}
		}
		
		return obj;
	}
}