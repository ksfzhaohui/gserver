package org.gserver.redis;

import org.apache.log4j.Logger;
import redis.clients.jedis.Jedis;

public abstract class AbstractRedisAccessor {

	private static final Logger logger = Logger
			.getLogger(AbstractRedisAccessor.class);

	private RedisFactory redisFactory;

	protected <T> T execute(RedisCallback<T> action) {
		T result = null;
		Jedis jedis = null;
		try {
			jedis = redisFactory.getJedis();
			result = action.doIt(jedis);
		} catch (Exception e) {
			logger.error("redis doIt error", e);
		} finally {
			if (jedis != null) {
				redisFactory.close(jedis);
			}
		}
		return result;
	}

	public RedisFactory getRedisFactory() {
		return redisFactory;
	}

	public void setRedisFactory(RedisFactory redisFactory) {
		this.redisFactory = redisFactory;
	}

}

interface RedisCallback<T> {
	T doIt(Jedis jedis);
}
