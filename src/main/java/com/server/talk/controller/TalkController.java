package com.server.talk.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.google.gson.internal.LinkedTreeMap;
import com.server.core.annotation.WebSocketController;
import com.server.core.annotation.WebSocketRequest;
import com.server.core.message.socket.SocketRequestMsg;
import com.server.core.message.socket.SocketResponseMsg;

@WebSocketController
public class TalkController {

	@WebSocketRequest("501")
	public SocketResponseMsg talk(SocketRequestMsg requestMsg) {
		List<String> sendUserId = new ArrayList<String>();
		SocketResponseMsg response = new SocketResponseMsg();
		response.setCode("601");
		response.setUserId(requestMsg.getUserId());
		
		Map<?, ?> talkMap = (LinkedTreeMap<?, ?>)requestMsg.getTalk();
		sendUserId.add(String.valueOf(talkMap.get("sendUserId")));
		response.setParam(String.valueOf(talkMap.get("sendTalk")));
		response.setSendUserId(sendUserId);
		return response;
	}
	
}
