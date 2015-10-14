package org.gserver.core.server.impl;

import java.net.InetSocketAddress;

import org.apache.log4j.Logger;
import org.gserver.core.server.AbstractServer;
import org.gserver.core.server.config.ServerConfig;
import org.gserver.core.server.loader.ServerConfigXmlLoader;
import org.jboss.netty.bootstrap.ServerBootstrap;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.socket.nio.NioServerSocketChannelFactory;

/**
 * 服务器
 * 
 * @author zhaohui
 * 
 */
public abstract class Server extends AbstractServer {
	private Logger log = Logger.getLogger(Server.class);

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
		log.info("Server " + getServerName() + " Start At Port " + port);
	}

	protected abstract ChannelPipelineFactory createPipelineFactory();

}
