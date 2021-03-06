package com.a.eye.bot.user.service.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.a.eye.bot.chat.share.dubbo.provider.IUserFriendsDataServiceProvider;
import com.a.eye.bot.common.cache.redis.UserInfoJedisRepository;
import com.a.eye.bot.common.cache.user.entity.UserCacheInfo;
import com.a.eye.bot.common.consts.Constants;
import com.a.eye.bot.common.exception.MessageException;
import com.a.eye.bot.user.service.dao.UserMapper;
import com.a.eye.bot.user.service.dubbo.consumer.UserAuthDataServiceConsumer;
import com.a.eye.bot.user.service.entity.User;
import com.a.eye.bot.user.service.util.UserInfoParseUtil;
import com.alibaba.dubbo.config.annotation.Reference;

/**
 * 
 * @Title: UserDataService.java
 * @author: pengysh
 * @date 2016年8月9日 下午2:38:44
 * @Description:用户数据
 */
@Service
@Transactional
public class UserDataService {

	private static Logger logger = LogManager.getLogger(UserDataService.class.getName());

	@Autowired
	private UserMapper userMapper;

	@Autowired
	private DepartDataService departDataService;

	@Autowired
	private UserService userService;

	@Reference
	private IUserFriendsDataServiceProvider userFriendsDataService;

	@Autowired
	private UserAuthDataServiceConsumer userAuthDataServiceConsumer;

	@Autowired
	private UserInfoJedisRepository userInfoJedisRepository;

	/**
	 * @Title: addUserData
	 * @author: pengysh
	 * @date 2016年8月9日 下午2:45:13
	 * @Description:创建用户
	 * @param name
	 * @param email
	 * @param password
	 * @throws MessageException
	 */
	public void addUserData(String name, String email, String password) throws MessageException {
		logger.debug("创建用户：" + name + "\t" + email);
		boolean checkResult = userService.emailCheck(email);
		if (checkResult) {
			// 创建用户数据
			User user = new User();
			user.setName(name);
			user.setEmail(email);
			user.setState(Constants.State_Active);
			user.setHeadImage("1.png");
			user.setCompanyId(1l);
			userMapper.insert(user);
			logger.debug(user.getId());

			// 创建用户认证数据
			userAuthDataServiceConsumer.addUserAuthData(user.getId(), email, password);

			// 初始化好友数据
			userFriendsDataService.addUserFriends(user.getId());

			// 暂时写死部门
			departDataService.addUserInDept(5l, user.getId());

			// 插入缓存
			UserCacheInfo userCacheInfo = UserInfoParseUtil.parse(user);
			userInfoJedisRepository.saveUserInfo(userCacheInfo);
		} else {
			throw new MessageException("邮箱已注册");
		}
	}
}
