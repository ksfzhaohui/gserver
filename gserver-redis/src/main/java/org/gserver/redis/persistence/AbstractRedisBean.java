package org.gserver.redis.persistence;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import org.gserver.redis.util.BeanUtil;

public abstract class AbstractRedisBean {

	protected long version;//版本号

	private long id;//编号

	@RedisBeanField(serialize = false)
	protected Map<String, Object> changedMap = new HashMap<String, Object>();

	final public Map<String, String> transToMap() {
		return BeanUtil.warp(this);
	}

	final public AbstractRedisBean transToBean(Map<String, String> beanMap) {
		return BeanUtil.reverse(beanMap, this.getClass());
	}

	public String toString() {
		return viewBeans();
	}

	public String viewBeans() {
		Map<String, String> beanFields = transToMap();
		Iterator<String> iter = beanFields.keySet().iterator();
		StringBuffer sbuffer = new StringBuffer();
		sbuffer.append("\n{ \n");
		while (iter.hasNext()) {
			String tmpKey = iter.next();
			String tmpVal = beanFields.get(tmpKey);
			sbuffer.append("  ").append(tmpKey).append(":").append(tmpVal)
					.append("\n");
		}
		sbuffer.append("}");
		return sbuffer.toString();
	}

	public void clearChangeMap() {
		changedMap.clear();
	}

	@RedisBeanField(serialize = false)
	final public Map<String, Object> getChangedMap() {
		return changedMap;
	}

	final public long getVersion() {
		return version;
	}

	final public void setVersion(long version) {
		this.version = version;
	}

	final public long getId() {
		return id;
	}

	final public void setId(long id) {
		this.id = id;
	}

}
