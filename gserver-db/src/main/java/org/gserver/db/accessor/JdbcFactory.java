package org.gserver.db.accessor;

import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.apache.log4j.Logger;

public class JdbcFactory {

	private static final Logger logger = Logger.getLogger(JdbcFactory.class);

	protected DataSource datasource;

	public void setDatasource(DataSource datasource) {
		this.datasource = datasource;
	}

	public Connection getConnection() {
		try {
			return datasource.getConnection();
		} catch (SQLException e) {
			logger.error("getConnection error", e);
		}
		return null;
	}

}
