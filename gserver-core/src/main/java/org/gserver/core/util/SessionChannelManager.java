package org.gserver.core.util;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.jboss.netty.channel.Channel;

/**
 * session和channel关系管理器
 * 
 * @author zhaohui
 * 
 */
public class SessionChannelManager {

	public static SessionChannelManager instance = new SessionChannelManager();

	private Map<String, Channel> sessionChannelMap = new ConcurrentHashMap<String, Channel>();
	private Map<Channel, String> channelSessionMap = new ConcurrentHashMap<Channel, String>();

	public static SessionChannelManager getInstance() {
		return instance;
	}

	public void addChannle(String sessionId, Channel channel) {
		synchronized (this) {
			sessionChannelMap.put(sessionId, channel);
			channelSessionMap.put(channel, sessionId);
		}
	}

	/**
	 * 获取指定session的channel
	 * 
	 * @param sessionId
	 * @return
	 */
	public Channel getChannel(String sessionId) {
		return sessionChannelMap.get(sessionId);
	}

	/**
	 * 获取指定channel的sessionId
	 * 
	 * @param channel
	 * @return
	 */
	public String getSessionId(Channel channel) {
		return channelSessionMap.get(channel);
	}

	/**
	 * 移除指定session和channel
	 * 
	 * @param sessionId
	 */
	public void removeChannel(String sessionId) {
		synchronized (this) {
			Channel channel = sessionChannelMap.get(sessionId);
			if (channel != null) {
				channelSessionMap.remove(channel);
			}
			sessionChannelMap.remove(sessionId);
		}
	}

}
