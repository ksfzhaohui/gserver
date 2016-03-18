package org.gserver.redis;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.log4j.Logger;
import org.gserver.redis.accessor.IRedisAccessor;
import org.gserver.redis.persistence.AbstractRedisBean;
import org.gserver.redis.util.BeanUtil;
import org.gserver.redis.util.KeyUtil;

public class RedisTemplateMethod implements IRedisTemplateMethod {

	private static final Logger logger = Logger
			.getLogger(RedisTemplateMethod.class);

	private IRedisAccessor redisAccessor;

	@Override
	public <T extends AbstractRedisBean> T create(T bean) {
		long beanIncrId = redisAccessor.getSequence(bean.getClass().getName());
		bean.setId(beanIncrId);
		String beanKey = KeyUtil.getRedisBeanKey(bean.getClass(), beanIncrId);
		return createBean(beanKey, bean);
	}

	@Override
	public <T extends AbstractRedisBean> T create(String beanKey, T bean) {
		Long beanIncrId = redisAccessor.getSequence(bean.getClass().getName());
		bean.setId(beanIncrId);
		return createBean(beanKey, bean);
	}

	private <T extends AbstractRedisBean> T createBean(String beanKey, T bean) {
		Map<String, String> beanMap = bean.transToMap();
		try {
			long startTime = System.currentTimeMillis();
			redisAccessor.setHashMapValue(beanKey, beanMap);
			long endTime = System.currentTimeMillis();
			logger.debug("【createBean】:" + beanKey + ",costTime:"
					+ (endTime - startTime));
		} catch (Exception ex) {
			logger.error("createBean error", ex);
		}
		return bean;
	}

	@Override
	public void update(long id, AbstractRedisBean bean) {
		String key = KeyUtil.getRedisBeanKey(bean.getClass(), id);
		updateBean(key, bean);
	}

	@Override
	public void update(String key, AbstractRedisBean bean) {
		updateBean(key, bean);
	}

	private void updateBean(String key, AbstractRedisBean bean) {
		Map<String, Object> map = bean.getChangedMap();
		if (map.size() <= 0)
			return;

		Map<String, String> updateMap = new HashMap<String, String>();
		for (Entry<String, Object> param : map.entrySet()) {
			if (param.getValue() instanceof String) {
				updateMap.put(param.getKey(), param.getValue().toString());
			} else {
				updateMap.put(param.getKey(), String.valueOf(param.getValue()));
			}
		}

		try {
			redisAccessor.setHashMapValue(key, updateMap);
			bean.clearChangeMap();
		} catch (Exception ex) {
			logger.error("updateBean error", ex);
		}
	}

	@Override
	public <T extends AbstractRedisBean> T query(String key, Class<T> type) {
		return queryBean(key, type);
	}

	@Override
	public <T extends AbstractRedisBean> T query(long beanId, Class<T> type) {
		if (beanId <= 0)
			return null;
		String key = KeyUtil.getRedisBeanKey(type, beanId);
		return queryBean(key, type);
	}

	private <T extends AbstractRedisBean> T queryBean(String key, Class<T> type) {
		Map<String, String> beanMap = null;
		try {
			long startTime = System.currentTimeMillis();
			beanMap = redisAccessor.getHashMapValue(key);
			long endTime = System.currentTimeMillis();
			logger.debug("【queryBean】:" + key + ",costTime:"
					+ (endTime - startTime));
		} catch (Exception ex) {
			logger.error("queryBean error", ex);
		}
		String checkedId = beanMap.get("id");
		if (checkedId != null && !checkedId.equals(""))
			return BeanUtil.reverse(beanMap, type);
		else {
			return null;
		}
	}

	public void setRedisAccessor(IRedisAccessor redisAccessor) {
		this.redisAccessor = redisAccessor;
	}

	@Override
	public <T extends AbstractRedisBean> void delete(long beanId, Class<T> type) {
		String key = KeyUtil.getRedisBeanKey(type, beanId);
		delete(key);
	}

	@Override
	public <T extends AbstractRedisBean> void delete(String key) {
		redisAccessor.deleteKey(key);
	}
}
