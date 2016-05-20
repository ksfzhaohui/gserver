package org.gserver.gate;

import org.apache.log4j.Logger;
import org.gserver.core.util.SessionChannelManager;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelFuture;
import org.jboss.netty.channel.ChannelFutureListener;

/**
 * 监听channel的关闭
 * 
 * @author zhaohui
 * 
 */
public class ChannelCloseListener implements ChannelFutureListener {

	private final static Logger logger = Logger
			.getLogger(ChannelCloseListener.class);

	@Override
	public void operationComplete(ChannelFuture future) throws Exception {
		Channel channel = future.getChannel();
		logger.info("close channle:" + channel);
		Integer sessionId = SessionChannelManager.getInstance().getSessionId(
				channel);
		SessionChannelManager.getInstance().removeChannel(sessionId);
	}
}
