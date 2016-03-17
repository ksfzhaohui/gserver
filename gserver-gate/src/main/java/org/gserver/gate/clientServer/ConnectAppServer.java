package org.gserver.gate.clientServer;

import org.gserver.core.server.impl.ClientServer;
import org.jboss.netty.channel.ChannelPipelineFactory;

/**
 * 连接app服务器
 * 
 * @author zhaohui
 * 
 */
public class ConnectAppServer extends ClientServer {

	private static final String DEFUALT_SERVER_CONFIG = "gate-config/connect-app-config.xml";

	private static ConnectAppServer connectAppServer = new ConnectAppServer();

	public static ConnectAppServer getInstance() {
		return connectAppServer;
	}

	private ConnectAppServer() {
		super(DEFUALT_SERVER_CONFIG);
	}

	@Override
	protected ChannelPipelineFactory createPipelineFactory() {
		return new ConnectPipelineFactory();
	}

	@Override
	protected void stop() {

	}

}
