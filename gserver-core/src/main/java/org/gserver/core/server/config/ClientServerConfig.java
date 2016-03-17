package org.gserver.core.server.config;

import java.util.ArrayList;
import java.util.List;

/**
 * 客戶端server
 * 
 * 逻辑服务器经常去连的服务器:gateServer,worldServer,publicServer 此时逻辑服务器被称为clientServer
 * 
 * @author zhaohui
 * 
 */
public class ClientServerConfig extends Config {

	private List<ServerInfo> connectServers = new ArrayList<ServerInfo>();

	public List<ServerInfo> getConnectServers() {
		return connectServers;
	}

	public void setConnectServers(List<ServerInfo> connectServers) {
		this.connectServers = connectServers;
	}

}
