package org.gserver.dao;

import org.gserver.redis.IRedisTemplateMethod;

public abstract class BaseDao {

	protected IRedisTemplateMethod redisTemplateMethod;

	public void setRedisTemplateMethod(IRedisTemplateMethod redisTemplateMethod) {
		this.redisTemplateMethod = redisTemplateMethod;
	}

}
