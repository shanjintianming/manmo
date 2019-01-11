package com.server.core.spring.action;

import java.lang.reflect.Method;

public class ActionBean {
	private Method method;
	
	private Object obj;

	public Method getMethod() {
		return method;
	}

	public void setMethod(Method method) {
		this.method = method;
	}

	public Object getObj() {
		return obj;
	}

	public void setObj(Object obj) {
		this.obj = obj;
	}
}
