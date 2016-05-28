package org.gserver.services;

import org.apache.log4j.Logger;
import org.gserver.core.net.Header;
import org.gserver.core.net.Message;
import org.gserver.core.util.ChannelPoolManager;
import org.gserver.util.CommandEnum;
import org.jboss.netty.channel.Channel;

/**
 * 推送消息
 * 
 * @author zhaohui
 * 
 */
public class SendMessageService {

	private final static Logger logger = Logger
			.getLogger(SendMessageService.class);

	private SessionService sessionService;

	/**
	 * 推送消息
	 * 
	 * @param sessionId
	 *            回话id
	 * @param comId
	 *            协议号
	 * @param data
	 *            二进制数据包
	 */
	public void pushMessage(Integer sessionId, CommandEnum command, byte[] data) {
		if (sessionId != null) {
			Header header = new Header(sessionId, command.getId());
			Message message = new Message(header, data);

			writeMessage(message);
		}
	}

	/**
	 * 发送消息
	 * 
	 * @param roleId
	 * @param command
	 * @param data
	 */
	public void sendMessage(long roleId, CommandEnum command, byte[] data) {
		Integer sessionId = sessionService.getSessionId(roleId);
		pushMessage(sessionId, command, data);
	}

	private void writeMessage(Message message) {
		try {
			Channel channel = ChannelPoolManager.getInstance().getChannel();
			channel.write(message);
		} catch (Exception e) {
			logger.error("push message error", e);
		}
	}

	public void setSessionService(SessionService sessionService) {
		this.sessionService = sessionService;
	}
}
