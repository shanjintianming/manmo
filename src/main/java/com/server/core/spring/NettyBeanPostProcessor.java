package com.server.core.spring;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;

import com.server.core.annotation.WebSocketController;
import com.server.core.annotation.WebSocketRequest;
import com.server.core.annotation.HttpController;
import com.server.core.annotation.HttpRequest;
import com.server.core.spring.action.Action;
import com.server.core.spring.action.ActionBean;

public class NettyBeanPostProcessor implements BeanPostProcessor{
	
	@Override
	public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
		
		String requestStr = "";
		
		Class<?> controllerClass = bean.getClass();
		HttpController controller = controllerClass.getAnnotation(HttpController.class);
		if(controller != null) {
			HttpRequest request = controllerClass.getAnnotation(HttpRequest.class);
			if(request != null) {
				String[] locations = request.value().split("/");
				List<String> locationLst = Arrays.asList(locations).stream().filter((n) -> n.length() > 0).collect(Collectors.toList());
				if(locationLst != null && locationLst.size() > 0) {
					requestStr = requestStr + "/" + locationLst.stream().collect(Collectors.joining("/"));
				}	
			}
			
			Method[] nettyMethods = controllerClass.getDeclaredMethods();
			for(Method nettyMethod : nettyMethods) {
				HttpRequest methodRequest = nettyMethod.getAnnotation(HttpRequest.class);
				
				if(methodRequest != null) {
					String location = "";
					String[] locations = methodRequest.value().split("/");
					List<String> locationLst = Arrays.asList(locations).stream().filter((n) -> n.length() > 0).collect(Collectors.toList());
					if(locationLst != null && locationLst.size() > 0) {
						location = requestStr + "/" + locationLst.stream().collect(Collectors.joining("/"));
					}
					
					ActionBean action = new ActionBean();
					action.setMethod(nettyMethod);
					action.setObj(bean);
					Action.actionMap.put(location, action);
				}
			}
		}
		
		WebSocketController battle = controllerClass.getAnnotation(WebSocketController.class);
		
		if(battle != null) {			
			Method[] nettyMethods = controllerClass.getDeclaredMethods();
			for(Method nettyMethod : nettyMethods) {
				WebSocketRequest methodRequest = nettyMethod.getAnnotation(WebSocketRequest.class);
				
				if(methodRequest != null) {
					ActionBean action = new ActionBean();
					action.setMethod(nettyMethod);
					action.setObj(bean);
					Action.actionMap.put(methodRequest.value(), action);
				}
			}
		}
		
		return bean;
	}
}
