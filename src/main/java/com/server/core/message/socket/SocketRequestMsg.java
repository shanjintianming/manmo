package com.server.core.message.socket;

import com.server.core.message.RequestMesssage;

public class SocketRequestMsg extends RequestMesssage{
	
	private String level;
	
	private String roomNum;

	private String param;
	
	private Object talk;
	
	private int personCount;
	
	public String getLevel() {
		return level;
	}

	public void setLevel(String level) {
		this.level = level;
	}

	public String getRoomNum() {
		return roomNum;
	}

	public void setRoomNum(String roomNum) {
		this.roomNum = roomNum;
	}

	public String getParam() {
		return param;
	}

	public void setParam(String param) {
		this.param = param;
	}

	public int getPersonCount() {
		return personCount;
	}

	public void setPersonCount(int personCount) {
		this.personCount = personCount;
	}

	public Object getTalk() {
		return talk;
	}

	public void setTalk(Object talk) {
		this.talk = talk;
	}
}
