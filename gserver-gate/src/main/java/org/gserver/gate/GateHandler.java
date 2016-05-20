package org.gserver.gate;

import org.apache.log4j.Logger;
import org.gserver.core.net.Message;
import org.gserver.core.threadPool.AbstractWork;
import org.gserver.core.threadPool.executor.OrderedQueuePoolExecutor;
import org.gserver.core.util.SessionChannelManager;
import org.gserver.gate.clientServer.ConnectAppServer;
import org.gserver.services.util.CommandEnumUtil;
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
public class GateHandler extends SimpleChannelHandler {

	private final static Logger logger = Logger.getLogger(GateHandler.class);
	private OrderedQueuePoolExecutor recvExcutor = new OrderedQueuePoolExecutor(
			"消息接收队列", 100, 10000);

	@Override
	public void channelConnected(ChannelHandlerContext ctx, ChannelStateEvent e)
			throws Exception {
		Channel channel = e.getChannel();
		SessionChannelManager.getInstance()
				.addChannle(channel.getId(), channel);
		channel.getCloseFuture().addListener(new ChannelCloseListener());
	}

	@Override
	public void messageReceived(ChannelHandlerContext ctx, MessageEvent e)
			throws Exception {
		Message request = (Message) e.getMessage();
		if (!CommandEnumUtil.isValid(request.getCommand())) {
			return;
		}
		int sessionId = SessionChannelManager.getInstance().getSessionId(
				e.getChannel());
		request.getHeader().setSessionId(sessionId);
		recvExcutor.addTask(sessionId, new MWork(request));
	}

	class MWork extends AbstractWork {
		/** 消息 **/
		private Message request;

		public MWork(Message request) {
			this.request = request;
		}

		@Override
		public void run() {
			try {
				ConnectAppServer.getInstance().write(request);
			} catch (Exception ex) {
				logger.error("gate forward error", ex);
			}
		}
	}

	@Override
	public void channelClosed(ChannelHandlerContext ctx, ChannelStateEvent e)
			throws Exception {
		logger.info("channel is closed" + e.getChannel());
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e) {
	}
}
