package org.gserver.redis;

import org.gserver.redis.persistence.AbstractRedisBean;

/**
 * redis操作模板方法接口
 * 
 * @author zhaohui
 * 
 */
public interface IRedisTemplateMethod {

	/**
	 * 创建新对象
	 * 
	 * @param bean
	 *            实例对象
	 * @return
	 */
	public <T extends AbstractRedisBean> T create(T bean);

	public <T extends AbstractRedisBean> T create(String beanKey, T bean);

	/**
	 * 查询对象
	 * 
	 * @param key
	 *            对象key
	 * @param type
	 *            对象类型
	 * @return
	 */
	public <T extends AbstractRedisBean> T query(String key, Class<T> type);

	public <T extends AbstractRedisBean> T query(long beanId, Class<T> type);

	/**
	 * 更新对象
	 * 
	 * @param id
	 * @param bean
	 */
	public void update(long id, AbstractRedisBean bean);

	public void update(String key, AbstractRedisBean bean);

	/**
	 * 删除对象
	 * 
	 * @param beanId
	 * @param type
	 */
	public <T extends AbstractRedisBean> void delete(long beanId, Class<T> type);

	public <T extends AbstractRedisBean> void delete(String key);
	
	public Long hset(String key, String field, String value);

	public String hget(String key, String field);

	public Long hdel(final String key, final String field);
}
