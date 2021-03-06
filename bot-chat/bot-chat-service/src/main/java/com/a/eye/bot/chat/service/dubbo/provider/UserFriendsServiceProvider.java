package com.a.eye.bot.chat.service.dubbo.provider;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.a.eye.bot.chat.service.service.UserFriendsService;
import com.a.eye.bot.chat.share.dubbo.provider.IUserFriendsServiceProvider;
import com.alibaba.dubbo.config.annotation.Service;

@Service
public class UserFriendsServiceProvider implements IUserFriendsServiceProvider {

	@Autowired
	private UserFriendsService userFriendsService;

	@Override
	public String getUserFriends(Long userId) {
		return userFriendsService.getUserFriends(userId);
	}

	@Override
	public Map<Long, Long> getUserFriendIds(Long userId) {
		return userFriendsService.getUserFriendIds(userId);
	}
}
