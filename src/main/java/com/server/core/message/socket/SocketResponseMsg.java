package com.server.core.message.socket;

import java.util.List;

public class SocketResponseMsg {
	private String code;
	
	private String userId;
	
	private String roomNum;

	private List<String> sendUserId;
	
	private boolean isDraw;
	
	private String successResult;
	
	private String param;
	
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}
	
	public String getRoomNum() {
		return roomNum;
	}

	public void setRoomNum(String roomNum) {
		this.roomNum = roomNum;
	}

	public List<String> getSendUserId() {
		return sendUserId;
	}

	public void setSendUserId(List<String> sendUserId) {
		this.sendUserId = sendUserId;
	}

	public boolean isDraw() {
		return isDraw;
	}

	public void setDraw(boolean isDraw) {
		this.isDraw = isDraw;
	}

	public String getSuccessResult() {
		return successResult;
	}

	public void setSuccessResult(String successResult) {
		this.successResult = successResult;
	}
	
	public String getParam() {
		return param;
	}

	public void setParam(String param) {
		this.param = param;
	}

}
