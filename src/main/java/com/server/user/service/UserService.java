package com.server.user.service;

import com.server.user.entity.OauthUser;

public interface UserService {
	public OauthUser search(OauthUser user);
	
	public void insert(OauthUser user);
}
