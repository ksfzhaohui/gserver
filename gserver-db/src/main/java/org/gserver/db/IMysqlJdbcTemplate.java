package org.gserver.db;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface IMysqlJdbcTemplate {

	public <T> T queryBean(Class<T> type, long id);

	public <T> List<T> batchQueryBean(Class<T> type, Set<Long> ids);

	public <T> int insert(Class<T> type, Map<String, String> beanMap);

	public <T> int update(Class<T> type, long id, Map<String, String> updateMap);

	public <T> void delete(Class<T> type, long id);

}
