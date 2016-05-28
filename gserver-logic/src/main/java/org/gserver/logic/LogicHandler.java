package org.gserver.logic;

import org.apache.log4j.Logger;
import org.gserver.core.exception.GsException;
import org.gserver.core.handler.IHandler;
import org.gserver.core.net.Message;
import org.gserver.core.threadPool.AbstractWork;
import org.gserver.core.threadPool.executor.OrderedQueuePoolExecutor;
import org.gserver.core.util.ChannelPoolManager;
import org.gserver.util.CommandEnum;
import org.gserver.util.ErrorCode;
import org.gserver.util.SpringContainer;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelStateEvent;
import org.jboss.netty.channel.ExceptionEvent;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelHandler;

import protocol.ServerClientProtocol.S2CErrorCode;

/**
 * 协议处理器 1.创建sessionID 2.接受客户端的消息进行转发
 * 
 * @author zhaohui
 * 
 */
public class LogicHandler extends SimpleChannelHandler {

	private final static Logger logger = Logger.getLogger(LogicHandler.class);
	private OrderedQueuePoolExecutor recvExcutor = new OrderedQueuePoolExecutor(
			"消息接收队列", 100, 10000);

	@Override
	public void channelConnected(ChannelHandlerContext ctx, ChannelStateEvent e)
			throws Exception {
		ChannelPoolManager.getInstance().addChannel(e.getChannel());
		logger.info("connected:" + e.getChannel().toString());
	}

	@Override
	public void messageReceived(ChannelHandlerContext ctx, MessageEvent e)
			throws Exception {
		Message request = (Message) e.getMessage();
		int sessionId = request.getHeader().getSessionId();
		recvExcutor.addTask(sessionId, new MWork(request, e.getChannel()));
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
			channel.write(processRequest(request));
		}
	}

	/**
	 * 处理请求
	 * 
	 * @param request
	 *            请求消息
	 * @return
	 */
	private Message processRequest(Message request) {
		int cmdId = getCommandId(request);
		Message response = new Message(request.getHeader().clone());
		try {
			IHandler handler = (IHandler) SpringContainer.getInstance()
					.getBeanById("handler" + cmdId);
			if (handler == null) {
				setErrorMsg(ErrorCode.PACKAGE_TAG_ERROR, response);
				return response;
			}
			handler.execute(request);
		} catch (GsException e) {
			setErrorMsg(e.getErrorCode(), response);
		} catch (Exception ex) {
			setErrorMsg(ErrorCode.SERVER_ERROR, response);
			logger.error("processRequest error", ex);
		}
		return response;
	}

	/**
	 * 获取协议号
	 * 
	 * @param message
	 *            消息
	 * @return
	 */
	private int getCommandId(Message message) {
		return message.getHeader().getCommandId();
	}

	private void setErrorMsg(int errcode, Message response) {
		S2CErrorCode.Builder builder = S2CErrorCode.newBuilder();
		builder.setErrorCode(errcode);
		response.setContent(CommandEnum.S2C_ERROR_CODE.getId(), builder.build()
				.toByteArray());
	}

	@Override
	public void channelClosed(ChannelHandlerContext ctx, ChannelStateEvent e)
			throws Exception {
		ChannelPoolManager.getInstance().removeChannel(e.getChannel());
		logger.info("channelClosed:" + e.getChannel().toString());
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e) {
	}
}
