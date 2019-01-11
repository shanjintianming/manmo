package com.server.user.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.server.user.dao.OauthUserMapper;
import com.server.user.entity.OauthUser;
import com.server.user.entity.OauthUserExample;
import com.server.user.entity.OauthUserExample.Criteria;
import com.server.user.service.UserService;

@Service("userService")
public class UserServiceImp implements UserService{

	@Autowired
	private OauthUserMapper userDao;
	
	@Override
	@Cacheable(value="rediscache", key="#user.getName()")
	public OauthUser search(OauthUser user) {
		
		OauthUserExample example = new OauthUserExample();
		
		Criteria criteria = example.createCriteria();
		criteria.andNameEqualTo(user.getName());
		criteria.andPasswordEqualTo(user.getPassword());
		
		List<OauthUser> userList = userDao.selectByExample(example);
		
		if(userList != null && userList.size() > 0) {
			return userList.get(0);
		}
		
		return null;
	}

	@Override
	@Transactional
	public void insert(OauthUser user) {
		userDao.insert(user);
	}
}
