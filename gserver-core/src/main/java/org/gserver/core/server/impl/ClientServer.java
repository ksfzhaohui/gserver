package org.gserver.core.server.impl;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.gserver.core.server.AbstractServer;
import org.gserver.core.server.ServerType;
import org.gserver.core.server.config.ClientServerConfig;
import org.gserver.core.server.config.ServerInfo;
import org.gserver.core.server.loader.ClientServerConfigXmlLoader;
import org.jboss.netty.bootstrap.ClientBootstrap;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelFuture;
import org.jboss.netty.channel.ChannelPipelineFactory;

/**
 * 
 * @author zhaohui
 * 
 */
public abstract class ClientServer extends AbstractServer {

	private Logger log = Logger.getLogger(ClientServer.class);
	/** 网关服session数量 **/
	public static int gateSessionNumber = 1;
	/** 世界服session数量 **/
	public static int worldSessionNumber = 1;
	/** 公共服session数量 **/
	public static int publicSessionNumber = 1;

	protected ClientBootstrap bootstrap = null;

	protected Map<Integer, List<Channel>> gateSessions = new HashMap<Integer, List<Channel>>();

	protected List<Channel> worldSessions = new ArrayList<Channel>();

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
		createBootStrap();
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

	private void createBootStrap() {
		bootstrap = new ClientBootstrap();
		bootstrap.setPipelineFactory(createPipelineFactory());
	}

	protected abstract ChannelPipelineFactory createPipelineFactory();

	/**
	 * 初始化clientServer和其他服务器建立连接
	 * 
	 * @param serverInfo
	 * @param sessionNumber
	 * @param serverId
	 * @param serverType
	 */
	private void initConnect(ServerInfo serverInfo, int sessionNumber,
			int serverId, ServerType serverType) {
		int connected = 0;
		while (connected < sessionNumber) {
			ChannelFuture fapp = bootstrap.connect(new InetSocketAddress(
					serverInfo.getIp(), serverInfo.getPort()));
			Channel channel = fapp.getChannel();
			if (!fapp.getChannel().isConnected()) {
				try {
					Thread.sleep(5000L);
				} catch (Exception e) {
					this.log.error(e, e);
				}
			} else {
				add(channel, serverId, serverType);
				connected++;
			}
		}
	}

	/**
	 * 添加channel
	 * 
	 * @param channel
	 * @param id
	 * @param type
	 */
	public void add(Channel channel, int id, ServerType type) {
		if (type == ServerType.GATE_SERVER) {
			synchronized (this.gateSessions) {
				List<Channel> sessions = this.gateSessions.get(Integer
						.valueOf(id));
				if (sessions == null) {
					sessions = new ArrayList<Channel>();
					this.gateSessions.put(Integer.valueOf(id), sessions);
				}
				sessions.add(channel);
			}
		}
		if (type == ServerType.WORLD_SERVER) {
			synchronized (this.worldSessions) {
				this.worldSessions.add(channel);
			}
		}
	}

	public List<Channel> getWorldSessions() {
		return this.worldSessions;
	}

	public Map<Integer, List<Channel>> getGateSessions() {
		return this.gateSessions;
	}

}
