package org.gserver.logic;

import org.gserver.core.server.impl.Server;
import org.gserver.util.ServerType;
import org.gserver.util.SpringContainer;
import org.jboss.netty.channel.ChannelPipelineFactory;

public class LogicServer extends Server {

	private static final String DEFUALT_SERVER_CONFIG = "logic-config/server-config.xml";

	public LogicServer() {
		this(DEFUALT_SERVER_CONFIG);
	}

	public LogicServer(String serverConfig) {
		super(serverConfig);
	}

	@Override
	protected void init() {
		super.init();
		SpringContainer.getInstance().loadSpring(ServerType.LOGIC);
	}

	@Override
	protected ChannelPipelineFactory createPipelineFactory() {
		return new LogicPipelineFactory();
	}

	@Override
	protected void stop() {

	}
}
