package org.gserver.redis.util;

/**
 * 生成key工具类
 * 
 * @author zhaohui
 * 
 */
public class KeyUtil {

	public static final String Suffix = ":";

	public static String getRedisBeanKey(Class<?> _class, long beanId) {
		return _class.getName() + Suffix + beanId;
	}

}
