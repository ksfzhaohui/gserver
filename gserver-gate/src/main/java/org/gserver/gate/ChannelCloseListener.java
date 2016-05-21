package org.gserver.gate;

import org.apache.log4j.Logger;
import org.gserver.core.util.SessionChannelManager;
import org.gserver.services.SessionService;
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

	private SessionService sessionService;

	/**
	 * channel关闭处理： 1.移除sessionId和channel的关系 2.移除公共缓存中sessionId和roleId的关系
	 */
	@Override
	public void operationComplete(ChannelFuture future) throws Exception {
		Channel channel = future.getChannel();
		logger.info("close channle:" + channel);
		Integer sessionId = SessionChannelManager.getInstance().getSessionId(
				channel);
		SessionChannelManager.getInstance().removeChannel(sessionId);
		sessionService.removeSession(sessionId);
	}

	public void setSessionService(SessionService sessionService) {
		this.sessionService = sessionService;
	}

}
