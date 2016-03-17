package org.gserver.core.server.config;

public class ServerInfo {
	/** 唯一编号 **/
	private int id;
	/** ip **/
	private String ip;
	/** 端口 **/
	private int port;
	/** 建立连接的数量 **/
	private int connectNum;

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getIp() {
		return this.ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public int getPort() {
		return this.port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public int getConnectNum() {
		return connectNum;
	}

	public void setConnectNum(int connectNum) {
		this.connectNum = connectNum;
	}

}
