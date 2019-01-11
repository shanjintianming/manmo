package com.server.user.controller;

import org.springframework.beans.factory.annotation.Autowired;

import com.server.core.annotation.HttpController;
import com.server.core.annotation.HttpRequest;
import com.server.user.entity.OauthUser;
import com.server.user.service.UserService;

@HttpController
public class UserController {

	@Autowired
	private UserService userService;
	
	@HttpRequest("login")
	public void login(OauthUser user) {
		userService.search(user);
	}
	
	@HttpRequest("register")
	public void register(OauthUser user) {
		userService.insert(user);
	}
}
