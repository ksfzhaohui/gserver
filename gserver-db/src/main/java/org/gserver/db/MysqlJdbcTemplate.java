package org.gserver.db;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.gserver.db.accessor.IMysqlAccessor;
import org.gserver.db.accessor.SQLUtil;

public class MysqlJdbcTemplate implements IMysqlJdbcTemplate {

	private IMysqlAccessor accessor;

	@Override
	public <T> T queryBean(Class<T> type, long id) {
		return null;
	}

	@Override
	public <T> List<T> batchQueryBean(Class<T> type, Set<Long> ids) {
		return null;
	}

	@Override
	public <T> int insert(Class<T> type, Map<String, String> beanMap) {
		String insertSql = SQLUtil.insert(beanMap, type.getSimpleName());
		return accessor.insert(insertSql);
	}

	@Override
	public <T> int update(Class<T> type, long id, Map<String, String> updateMap) {
		return 0;
	}

	@Override
	public <T> void delete(Class<T> type, long id) {

	}

	public void setAccessor(IMysqlAccessor accessor) {
		this.accessor = accessor;
	}

}
