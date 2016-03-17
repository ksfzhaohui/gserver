package org.gserver.core.server.impl;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.gserver.core.net.Message;
import org.gserver.core.server.AbstractServer;
import org.gserver.core.server.config.ClientServerConfig;
import org.gserver.core.server.config.ServerInfo;
import org.gserver.core.server.loader.ClientServerConfigXmlLoader;
import org.jboss.netty.bootstrap.ClientBootstrap;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelFuture;
import org.jboss.netty.channel.ChannelFutureListener;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.socket.nio.NioClientSocketChannelFactory;

/**
 * 客户端服务器经常去连其他服务器，比如逻辑服务器连接网关服务器
 * 
 * @author zhaohui
 * 
 */
public abstract class ClientServer extends AbstractServer {

	private Logger log = Logger.getLogger(ClientServer.class);

	protected ClientBootstrap bootstrap = null;
	protected Map<Integer, List<Channel>> connectSessions = new HashMap<Integer, List<Channel>>();
	protected List<Channel> channelList = new ArrayList<Channel>();

	protected ClientServer(String serverConfig) {
		super(new ClientServerConfigXmlLoader().load(serverConfig));
	}

	@Override
	public void run() {
		try {
			super.run();
			createBootStrap();
			ClientServerConfig config = (ClientServerConfig) this.config;
			if (config != null) {
				List<ServerInfo> connectServers = config.getConnectServers();
				if (connectServers != null) {
					for (ServerInfo serverInfo : connectServers) {
						initConnect(serverInfo);
					}
				}

				Set<Integer> keySet = connectSessions.keySet();
				for (int key : keySet) {
					channelList.addAll(connectSessions.get(key));
				}
			}
		} catch (Exception e) {
			log.error(e);
		}

	}

	private void createBootStrap() {
		bootstrap = new ClientBootstrap(new NioClientSocketChannelFactory(
				Executors.newCachedThreadPool(),
				Executors.newCachedThreadPool()));
		bootstrap.setPipelineFactory(createPipelineFactory());
	}

	protected abstract ChannelPipelineFactory createPipelineFactory();

	/**
	 * 初始化clientServer和其他服务器建立连接
	 * 
	 * @param serverInfo
	 * @throws InterruptedException
	 */
	private void initConnect(ServerInfo serverInfo) throws InterruptedException {
		int connected = 0;
		while (connected < serverInfo.getConnectNum()) {
			ChannelFuture fapp = bootstrap.connect(new InetSocketAddress(
					serverInfo.getIp(), serverInfo.getPort()));
			Channel channel = fapp.getChannel();
			CountDownLatch countLatch = new CountDownLatch(1);
			fapp.addListener(new ChannelListener(countLatch));
			if (!channel.isConnected()) {
				countLatch.await(2000, TimeUnit.MILLISECONDS);
			}

			if (!channel.isConnected()) {
				log.info("not connect ip:" + serverInfo.getIp() + ",port:"
						+ serverInfo.getPort());
				try {
					fapp.cancel();
					Thread.sleep(5000L);
				} catch (Exception e) {
					this.log.error(e, e);
				}
			} else {
				log.info("connect ip:" + serverInfo.getIp() + ",port:"
						+ serverInfo.getPort());
				add(channel, serverInfo.getId());
				connected++;
			}
		}
	}

	/**
	 * 添加channel
	 * 
	 * @param channel
	 * @param id
	 */
	public void add(Channel channel, int id) {
		synchronized (this.connectSessions) {
			List<Channel> sessions = this.connectSessions.get(Integer
					.valueOf(id));
			if (sessions == null) {
				sessions = new ArrayList<Channel>();
				this.connectSessions.put(Integer.valueOf(id), sessions);
			}
			sessions.add(channel);
		}
	}

	public List<Channel> getChannelList(int serverId) {
		return connectSessions.get(serverId);
	}

	private Channel getChannel() {
		return channelList.get(new Random().nextInt(channelList.size() - 1));
	}

	public void write(Message message) {
		Channel channel = getChannel();
		channel.write(message);
	}

}

class ChannelListener implements ChannelFutureListener {

	private CountDownLatch countlatch;

	public ChannelListener(CountDownLatch countlatch) {
		this.countlatch = countlatch;
	}

	@Override
	public void operationComplete(ChannelFuture future) throws Exception {
		if (future.getChannel().isConnected()) {
			countlatch.countDown();
		}
	}

}
