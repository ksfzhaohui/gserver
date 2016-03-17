package org.gserver.redis.util;

import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.gserver.redis.persistence.AbstractRedisBean;
import org.gserver.redis.persistence.RedisBeanField;

/**
 * Bean/Map 转换工具
 * 
 * @author zhaohui
 * 
 */
public class BeanUtil {
	private static Logger logger = Logger.getLogger(BeanUtil.class);
	private static final String CLASS = "class";

	/**
	 * 将指定的对象数据封装成map
	 * 
	 * @param bean
	 *            对象数据
	 * @return
	 */
	@SuppressWarnings("all")
	public static Map<String, String> warp(AbstractRedisBean bean) {
		Map<String, String> propertyMap = new HashMap<String, String>();
		try {
			PropertyDescriptor[] ps = Introspector.getBeanInfo(bean.getClass())
					.getPropertyDescriptors();
			for (PropertyDescriptor propertyDescriptor : ps) {
				String propertyName = propertyDescriptor.getName();
				if (propertyName != null && !propertyName.equals(CLASS)) {
					Method getter = propertyDescriptor.getReadMethod();
					if (getter != null) {
						RedisBeanField mannota = getter
								.getAnnotation(RedisBeanField.class);
						if (mannota != null && mannota.serialize() == false) {
							continue;
						}
						propertyMap.put(propertyName,
								String.valueOf(getter.invoke(bean, null)));
					}
				}
			}
		} catch (Exception e) {
			logger.error(e);
		}
		return propertyMap;
	}

	/**
	 * 将map转成指定的对象
	 * 
	 * @param beanMap
	 *            map数据
	 * @param clazz
	 *            指定的类对象
	 * @return
	 */
	public static <T extends AbstractRedisBean> T reverse(
			Map<String, String> beanMap, Class<T> clazz) {
		T bean = getRedisBean(clazz);
		try {
			PropertyDescriptor[] ps = Introspector.getBeanInfo(clazz)
					.getPropertyDescriptors();
			for (PropertyDescriptor propertyDescriptor : ps) {
				String propertyName = propertyDescriptor.getName();
				if (propertyName != null && !propertyName.equals(CLASS)) {
					Method setter = propertyDescriptor.getWriteMethod();
					String value = beanMap.get(propertyName);
					String type = propertyDescriptor.getPropertyType()
							.getName();
					if (setter != null && value != null
							&& !value.equalsIgnoreCase("null")) {
						Object obj = value(value, type);
						if (obj != null) {
							setter.invoke(bean, obj);
						}
					}
				}
			}
		} catch (Exception e) {
			logger.error(e);
			e.printStackTrace();
		}
		bean.clearChangeMap();
		return bean;
	}

	/**
	 * 将String类型数值转换成指定的类型
	 * 
	 * @param value
	 *            数值
	 * @param type
	 *            指定的类型
	 * @return
	 */
	private static Object value(String value, String type) {
		if (type.equals("boolean")) {
			return Boolean.valueOf(value);
		} else if (type.equals("byte")) {
			return Byte.valueOf(value);
		} else if (type.equals("short")) {
			return Short.valueOf(value);
		} else if (type.equals("float")) {
			return Float.valueOf(value);
		} else if (type.equals("int")) {
			return Integer.valueOf(value);
		} else if (type.equals("java.lang.String")) {
			return value;
		} else if (type.equals("long")) {
			return Long.valueOf(value);
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	private static <T extends AbstractRedisBean> T getRedisBean(Class<T> _class) {
		try {
			return (T) Class.forName(_class.getName()).newInstance();
		} catch (Exception e) {
			logger.error(e);
		}
		return null;
	}
}
