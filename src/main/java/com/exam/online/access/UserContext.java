package com.exam.online.access;

import com.exam.online.entity.User;

/**
 * @author zhonglunsheng
 * @Description 获取本地用户
 * @create 2019-02-18 16:43
 */
public class UserContext {

	private static ThreadLocal<Object> userHolder = new ThreadLocal<Object>();

	public static void setUser(Object user) {
		userHolder.set(user);
	}

	public static Object getUser() {
		return userHolder.get();
	}

	public static void removeUser() {
		userHolder.remove();
	}

}
