package org.gserver.gate.clientServer;

import org.apache.log4j.Logger;
import org.gserver.core.net.Message;
import org.gserver.core.threadPool.AbstractWork;
import org.gserver.core.threadPool.executor.OrderedQueuePoolExecutor;
import org.gserver.core.util.SessionChannelManager;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelStateEvent;
import org.jboss.netty.channel.ExceptionEvent;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelHandler;

/**
 * 协议处理器 1.创建sessionID 2.接受客户端的消息进行转发
 * 
 * @author zhaohui
 * 
 */
public class ConnectHandler extends SimpleChannelHandler {

	private final static Logger logger = Logger.getLogger(ConnectHandler.class);
	private OrderedQueuePoolExecutor recvExcutor = new OrderedQueuePoolExecutor(
			"消息接收队列", 100, 10000);

	@Override
	public void channelConnected(ChannelHandlerContext ctx, ChannelStateEvent e)
			throws Exception {
	}

	@Override
	public void messageReceived(ChannelHandlerContext ctx, MessageEvent e)
			throws Exception {
		Message request = (Message) e.getMessage();
		String sessionId = request.getHeader().getSessionId();
		Channel channel = SessionChannelManager.getInstance().getChannel(
				sessionId);
		recvExcutor.addTask(sessionId, new MWork(request, channel));
	}

	class MWork extends AbstractWork {
		/** 消息 **/
		private Message request;
		/** 消息队列 **/
		private Channel channel;

		public MWork(Message request, Channel channel) {
			this.request = request;
			this.channel = channel;
		}

		@Override
		public void run() {
			try {
				channel.write(request);
			} catch (Exception e) {
				logger.error("connect forward error", e);
			}
		}
	}

	@Override
	public void channelClosed(ChannelHandlerContext ctx, ChannelStateEvent e)
			throws Exception {
		logger.info("channelClosed:" + e.getChannel());
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e) {
	}
}
