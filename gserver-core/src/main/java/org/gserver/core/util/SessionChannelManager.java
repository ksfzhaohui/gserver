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

	private Map<Integer, Channel> sessionChannelMap = new ConcurrentHashMap<Integer, Channel>();
	private Map<Channel, Integer> channelSessionMap = new ConcurrentHashMap<Channel, Integer>();

	public static SessionChannelManager getInstance() {
		return instance;
	}

	public void addChannle(Integer sessionId, Channel channel) {
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
	public Channel getChannel(Integer sessionId) {
		return sessionChannelMap.get(sessionId);
	}

	/**
	 * 获取指定channel的sessionId
	 * 
	 * @param channel
	 * @return
	 */
	public Integer getSessionId(Channel channel) {
		return channelSessionMap.get(channel);
	}

	/**
	 * 移除指定session和channel
	 * 
	 * @param sessionId
	 */
	public void removeChannel(int sessionId) {
		synchronized (this) {
			Channel channel = sessionChannelMap.get(sessionId);
			if (channel != null) {
				channelSessionMap.remove(channel);
			}
			sessionChannelMap.remove(sessionId);
		}
	}

	public void removeChannel(Channel channel) {
		synchronized (this) {
			Integer sessionId = channelSessionMap.get(channel);
			if (sessionId != null) {
				sessionChannelMap.remove(sessionId);
			}
			channelSessionMap.remove(channel);
		}
	}
}
