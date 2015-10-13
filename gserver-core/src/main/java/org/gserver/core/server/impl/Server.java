package org.gserver.core.server.impl;

import java.net.InetSocketAddress;

import org.gserver.core.server.AbstractServer;
import org.gserver.core.server.config.ServerConfig;
import org.gserver.core.server.loader.ServerConfigXmlLoader;
import org.jboss.netty.bootstrap.ServerBootstrap;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.socket.nio.NioServerSocketChannelFactory;

/**
 * 
 * @author zhaohui
 *
 */
public abstract class Server extends AbstractServer {

	protected ServerBootstrap bootstrap;
	private int port;

	protected Server(String serverConfig) {
		super(new ServerConfigXmlLoader().load(serverConfig));
		port = ((ServerConfig) config).getPort();
	}

	@Override
	public void run() {
		super.run();
		bootstrap = new ServerBootstrap(new NioServerSocketChannelFactory());
		bootstrap.setPipelineFactory(createPipelineFactory());
		bootstrap.bind(new InetSocketAddress(port));
	}

	protected abstract ChannelPipelineFactory createPipelineFactory();

}
