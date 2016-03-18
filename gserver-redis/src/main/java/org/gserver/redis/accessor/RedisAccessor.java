package org.gserver.redis.accessor;

import java.util.Map;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPubSub;

public class RedisAccessor extends AbstractRedisAccessor implements
		IRedisAccessor {

	@Override
	public Integer setHashMapValue(final String key,
			final Map<String, String> values) {
		return execute(new RedisCallback<Integer>() {
			@Override
			public Integer doIt(Jedis jedis) {
				jedis.hmset(key, values);
				return 1;
			}
		});
	}

	@Override
	public Map<String, String> getHashMapValue(final String key) {
		return execute(new RedisCallback<Map<String, String>>() {
			@Override
			public Map<String, String> doIt(Jedis jedis) {
				return jedis.hgetAll(key);
			}
		});
	}

	@Override
	public Long publishMessage(final String channel, final String message) {
		return execute(new RedisCallback<Long>() {
			@Override
			public Long doIt(Jedis jedis) {
				return jedis.publish(channel, message);
			}
		});
	}

	@Override
	public void subscribe(final JedisPubSub listener, final String... channels) {
		execute(new RedisCallback<Integer>() {
			@Override
			public Integer doIt(Jedis jedis) {
				jedis.subscribe(listener, channels);
				return 0;
			}
		});
	}

	@Override
	public long getSequence(final String key) {
		return execute(new RedisCallback<Long>() {
			@Override
			public Long doIt(Jedis jedis) {
				return jedis.incr(key);
			}
		});
	}

	public void deleteKey(final String... keys) {
		execute(new RedisCallback<Object>() {
			@Override
			public Object doIt(Jedis jedis) {
				jedis.del(keys);
				return null;
			}
		});
	}
}
