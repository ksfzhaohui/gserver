package org.gserver.db.accessor;

import java.util.List;
import java.util.Set;

public interface IMysqlAccessor {
	
	public <T> T queryBean(Class<T> type, long id);

	public <T> List<T> batchQueryBean(Class<T> type, Set<Long> ids);

	public int insert(String sql, Object... args);

	public int update(String sql, Object... args);

	public void delete(String sql, Object... args);
	
	public int executeUpdate(String sql, Object... args);

}
