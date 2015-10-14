package org.gserver.core.server;

import org.apache.log4j.Logger;
import org.gserver.core.server.config.Config;

public abstract class AbstractServer implements Runnable {
	private int server_id;
	private String server_name;
	protected Config config;

	protected AbstractServer(Config serverConfig) {
		this.config = serverConfig;
		if (this.config != null) {
			init();
		}
	}

	protected void init() {
		this.server_name = this.config.getName();
		this.server_id = this.config.getId();
	}

	@Override
	public void run() {
		Runtime.getRuntime().addShutdownHook(
				new Thread(new CloseByExit(this.server_name)));
	}

	public String getServerName() {
		return server_name;
	}

	public int getServerId() {
		return server_id;
	}

	protected abstract void stop();

	/**
	 * 服务器关闭的钩子
	 * 
	 * @author zhaohui
	 * 
	 */
	private class CloseByExit implements Runnable {
		private Logger log = Logger.getLogger(CloseByExit.class);
		private String server_name;

		public CloseByExit(String server_name) {
			this.server_name = server_name;
		}

		@Override
		public void run() {
			AbstractServer.this.stop();
			log.info(this.server_name + " Stop!");
		}
	}
}
