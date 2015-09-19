package org.gserver.core.server.config;

import java.util.ArrayList;
import java.util.List;

/**
 * 客戶端server
 * 
 * 逻辑服务器经常去连的服务器:gateServer,worldServer,publicServer 
 * 此时逻辑服务器被称为clientServer
 * 
 * @author zhaohui
 * 
 */
public class ClientServerConfig extends Config {

	private List<ServerInfo> gateServers = new ArrayList<ServerInfo>();
	private ServerInfo worldServer;
	private ServerInfo publicServer;

	public List<ServerInfo> getGateServers() {
		return this.gateServers;
	}

	public void setGateServers(List<ServerInfo> gateServers) {
		this.gateServers = gateServers;
	}

	public ServerInfo getWorldServer() {
		return this.worldServer;
	}

	public void setWorldServer(ServerInfo worldServer) {
		this.worldServer = worldServer;
	}

	public ServerInfo getPublicServer() {
		return this.publicServer;
	}

	public void setPublicServers(ServerInfo publicServer) {
		this.publicServer = publicServer;
	}
}
