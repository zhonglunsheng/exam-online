package com.exam.online.access;

import com.exam.online.entity.User;

/**
 * @author zhonglunsheng
 * @Description 获取本地用户
 * @create 2019-02-18 16:43
 */
public class UserContext {

	private static ThreadLocal<User> userHolder = new ThreadLocal<User>();

	public static void setUser(User user) {
		userHolder.set(user);
	}

	public static User getUser() {
		return userHolder.get();
	}

	public static void removeUser() {
		userHolder.remove();
	}

}
