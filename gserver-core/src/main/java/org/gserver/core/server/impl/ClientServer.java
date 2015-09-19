package org.gserver.core.server.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.gserver.core.server.Server;
import org.gserver.core.server.ServerType;
import org.gserver.core.server.config.ClientServerConfig;
import org.gserver.core.server.config.ServerInfo;
import org.gserver.core.server.loader.ClientServerConfigXmlLoader;

/**
 * 
 * @author zhaohui
 * 
 * @param <T>
 *            session
 * @param <V>
 *            socket
 * @param <K>
 *            connect
 */
public abstract class ClientServer<T, V, K> extends Server {

	private Logger log = Logger.getLogger(ClientServer.class);
	/** 网关服session数量 **/
	public static int gateSessionNumber = 1;
	/** 世界服session数量 **/
	public static int worldSessionNumber = 1;
	/** 公共服session数量 **/
	public static int publicSessionNumber = 1;

	protected V socket = null;

	protected Map<Integer, List<T>> gateSessions = new HashMap<Integer, List<T>>();

	protected List<T> worldSessions = new ArrayList<T>();

	protected ClientServer(String serverConfig) {
		this(serverConfig, gateSessionNumber, worldSessionNumber);
	}

	protected ClientServer(String serverConfig, int gSessionNumber,
			int wSessionNumber) {
		super(new ClientServerConfigXmlLoader().load(serverConfig));
	}

	@Override
	public void run() {
		super.run();
		socket = createSocket();
		ClientServerConfig config = (ClientServerConfig) this.config;
		if (config != null) {
			if (config.getWorldServer() != null) {
				ServerInfo serverInfo = config.getWorldServer();
				initConnect(serverInfo, worldSessionNumber, serverInfo.getId(),
						ServerType.WORLD_SERVER);
			}
			for (int i = 0; i < config.getGateServers().size(); i++) {
				ServerInfo serverInfo = (ServerInfo) config.getGateServers()
						.get(i);
				initConnect(serverInfo, gateSessionNumber, serverInfo.getId(),
						ServerType.GATE_SERVER);
			}
			if (config.getPublicServer() != null) {
				ServerInfo serverInfo = config.getPublicServer();
				initConnect(serverInfo, publicSessionNumber,
						serverInfo.getId(), ServerType.PUBLIC_SERVER);
			}
		}
	}

	/**
	 * 初始化clientServer和其他服务器建立连接
	 * @param serverInfo
	 * @param sessionNumber
	 * @param serverId
	 * @param serverType
	 */
	private void initConnect(ServerInfo serverInfo, int sessionNumber,
			int serverId, ServerType serverType) {
		int connected = 0;
		while (connected < sessionNumber) {
			K connect = createConnect(serverInfo);
			if (isConnected(connect)) {
				try {
					Thread.sleep(5000L);
				} catch (Exception e) {
					this.log.error(e, e);
				}
			} else {
				T session = createSession(connect, serverInfo);
				add(session, serverId, serverType);
				notifyServer(session, serverType.getId());
				connected++;
			}
		}
	}

	protected abstract V createSocket();

	protected abstract K createConnect(ServerInfo serverInfo);

	protected abstract boolean isConnected(K connect);

	protected abstract T createSession(K connect, ServerInfo serverInfo);

	protected abstract void notifyServer(T session, int type);

	public List<T> getWorldSessions() {
		return this.worldSessions;
	}

	public Map<Integer, List<T>> getGateSessions() {
		return this.gateSessions;
	}

	public void add(T session, int id, ServerType type) {
		if (type == ServerType.GATE_SERVER) {
			synchronized (this.gateSessions) {
				List<T> sessions = (List<T>) this.gateSessions.get(Integer
						.valueOf(id));
				if (sessions == null) {
					sessions = new ArrayList<T>();
					this.gateSessions.put(Integer.valueOf(id), sessions);
				}
				sessions.add(session);
			}
		}
		if (type == ServerType.WORLD_SERVER) {
			synchronized (this.worldSessions) {
				this.worldSessions.add(session);
			}
		}
	}

}
