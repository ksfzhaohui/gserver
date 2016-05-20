package org.gserver.core.util;

import org.apache.log4j.Logger;
import org.gserver.core.net.Header;
import org.gserver.core.net.Message;
import org.jboss.netty.channel.Channel;

/**
 * 推送消息
 * 
 * @author zhaohui
 * 
 */
public class PushMessageManager {

	private final static Logger logger = Logger
			.getLogger(PushMessageManager.class);

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
	public static void pushMessage(Integer sessionId, int cmdId, byte[] data) {
		if (sessionId != null) {
			Header header = new Header(sessionId, cmdId);
			Message message = new Message(header, data);

			writeMessage(message);
		}
	}

	public static void pushMessage(long roleId, int cmdId, byte[] data) {
		Integer sessionId = SessionPlayerManager.getInstance()
				.getSessionId(roleId);
		pushMessage(sessionId, cmdId, data);
	}

	private static void writeMessage(Message message) {
		try {
			Channel channel = ChannelPoolManager.getInstance().getChannel();
			channel.write(message);
		} catch (Exception e) {
			logger.error("push message error", e);
		}
	}

}
