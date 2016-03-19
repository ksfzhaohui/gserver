package org.gserver.dao;


public interface IBaseDao<T> extends IRedisKey {

	/**
	 * 获取指定ID对象
	 * 
	 * @param id
	 *            唯一ID
	 * @return
	 */
	public T get(long id);

	/**
	 * 添加对象
	 * 
	 * @param bean
	 */
	public long add(T bean);

	/**
	 * 更新对象
	 * 
	 * @param bean
	 */
	public void update(T bean);

	/**
	 * 删除对象
	 * 
	 * @param id
	 */
	public void delete(long id);

}
