package com.a.eye.bot.chat.ui.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.a.eye.bot.chat.share.dubbo.provider.IUserGroupServiceProvider;
import com.a.eye.bot.common.ui.base.ControllerBase;
import com.alibaba.dubbo.config.annotation.Reference;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@Controller
@RequestMapping(value = "/topic", produces = { "application/json;charset=UTF-8" })
@Api(value = "/topic", description = "用户话题服务")
public class UserGroupController extends ControllerBase {

	private static Logger logger = LogManager.getLogger(UserGroupController.class.getName());

	@Reference
	private IUserGroupServiceProvider userGroupServiceProvider;

	@RequestMapping(value = "getUserTopic", method = RequestMethod.POST)
	@ApiOperation(notes = "getUserTopic", httpMethod = "POST", value = "用户话题服务")
	@ResponseBody
	public void getUserTopic(@ApiParam(required = true, value = "userFriends data") @CookieValue("userId") String userId, HttpServletResponse response) throws IOException {
		logger.debug("获取用户群组列表：" + userId);
		String groups = userGroupServiceProvider.getUserGroup(Long.valueOf(userId));
		logger.debug("获取到的用户群组列表：" + groups);
		reply(groups, response);
	}
}
