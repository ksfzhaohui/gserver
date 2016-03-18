package org.gserver.redis.accessor;

import java.util.Map;

import redis.clients.jedis.JedisPubSub;

/**
 * redis连接器
 * 
 * @author zhaohui
 * 
 */
public interface IRedisAccessor {

	public Integer setHashMapValue(String key, Map<String, String> values);

	public Map<String, String> getHashMapValue(String key);

	/**
	 * 获取指定key的序列号
	 * 
	 * @param key
	 * @return
	 */
	public long getSequence(final String key);

	/**
	 * 删除指定的key
	 * 
	 * @param keys
	 */
	public void deleteKey(final String... keys);

	/**
	 * 发布消息
	 * 
	 * @param channel
	 * @param message
	 * @return
	 */
	public Long publishMessage(final String channel, final String message);

	/**
	 * 订阅消息
	 * 
	 * @param listener
	 * @param channels
	 */
	public void subscribe(final JedisPubSub listener, final String... channels);

}
