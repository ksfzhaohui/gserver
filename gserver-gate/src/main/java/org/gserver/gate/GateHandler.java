package org.gserver.gate;

import org.apache.log4j.Logger;
import org.gserver.core.command.IHandler;
import org.gserver.core.exception.GsException;
import org.gserver.core.net.Header;
import org.gserver.core.net.Message;
import org.gserver.core.threadPool.AbstractWork;
import org.gserver.core.threadPool.executor.OrderedQueuePoolExecutor;
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
		e.getChannel().write("连接成功");
	}

	@Override
	public void messageReceived(ChannelHandlerContext ctx, MessageEvent e)
			throws Exception {
		Message request = (Message) e.getMessage();

		recvExcutor.addTask(request.getHeader().getSessionid(), new MWork(
				request, e.getChannel()));
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
		Message response = new Message(getResponseHeader(request, cmdId));
		try {
			IHandler handler = (IHandler) SpringContainer.getInstance()
					.getBeanById("handler" + cmdId);
			if (handler == null) {
				setErrorMsg(-1, ErrorCode.PACKAGE_TAG_ERROR, response);
				return response;
			}
			handler.execute(request, response);
		} catch (GsException e) {
			setErrorMsg(-1, e.getErrorCode(), response);
		} catch (Exception ex) {
			setErrorMsg(-1, ErrorCode.SERVER_ERROR, response);
			logger.error("processRequest异常", ex);
		}
		return response;
	}

	/**
	 * 获取请求头
	 * 
	 * @param request
	 *            请求
	 * @param cmdId
	 *            协议号
	 * @return
	 */
	private Header getResponseHeader(Message request, int cmdId) {
		Header header = request.getHeader().clone();
		header.setCommandId(cmdId + 1);
		return header;
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

	/**
	 * 设置错误消息体
	 * 
	 * @param state
	 *            响应状态
	 * @param errcode
	 *            错误号
	 * @param response
	 *            返回的消息
	 */
	private void setErrorMsg(int state, ErrorCode errcode, Message response) {

	}

	private void setErrorMsg(int state, int errcode, Message response) {

	}

	@Override
	public void channelClosed(ChannelHandlerContext ctx, ChannelStateEvent e)
			throws Exception {
		logger.info("连接已关闭" + e.getChannel());
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e) {
	}
}
