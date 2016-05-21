package org.gserver.gate;

import org.gserver.core.server.impl.Server;
import org.gserver.util.ServerType;
import org.gserver.util.SpringContainer;
import org.jboss.netty.channel.ChannelPipelineFactory;

/**
 * 网关服务器
 * 
 * @author zhaohui
 * 
 */
public class GateServer extends Server {

	private static final String DEFUALT_SERVER_CONFIG = "gate-config/server-config.xml";

	private static GateServer gateServer = new GateServer();

	public static GateServer getInstance() {
		return gateServer;
	}

	private GateServer() {
		this(DEFUALT_SERVER_CONFIG);
	}

	public GateServer(String serverConfig) {
		super(serverConfig);
	}

	@Override
	protected void init() {
		super.init();
		SpringContainer.getInstance().loadSpring(ServerType.GATE);
	}

	@Override
	protected ChannelPipelineFactory createPipelineFactory() {
		return new GatePipelineFactory();
	}

	@Override
	protected void stop() {

	}
}
