package org.gserver.db.accessor;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Set;

import org.apache.commons.dbutils.QueryRunner;

public class MysqlAccessor extends AbstractMysqlAccessor implements
		IMysqlAccessor {

	private QueryRunner queryRunner;

	public MysqlAccessor() {
		queryRunner = new QueryRunner();
	}

	@Override
	public <T> T queryBean(Class<T> type, long id) {
		return null;
	}

	@Override
	public <T> List<T> batchQueryBean(Class<T> type, Set<Long> ids) {
		return null;
	}

	@Override
	public int insert(String sql, Object... args) {
		return executeUpdate(sql, args);
	}

	@Override
	public int update(String sql, Object... args) {
		return executeUpdate(sql, args);
	}

	@Override
	public void delete(String sql, Object... args) {
		executeUpdate(sql, args);
	}

	@Override
	public int executeUpdate(final String sql, final Object... args) {
		return execute(new SimpleJdbcCallback<Integer>() {
			@Override
			public Integer doIt(Connection conn) throws SQLException {
				return queryRunner.update(conn, sql, args);
			}
		});
	}

}
