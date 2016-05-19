package org.gserver.core.util;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * session和玩家id管理
 * 
 * @author zhaohui
 * 
 */
public class SessionPidManager {

	private static SessionPidManager instance = new SessionPidManager();

	private Map<Integer, Long> sessionPidMap = new ConcurrentHashMap<Integer, Long>();
	private Map<Long, Integer> pidSessionMap = new ConcurrentHashMap<Long, Integer>();

	public static SessionPidManager getInstance() {
		return instance;
	}

	public void setSessionPid(int sessionId, long pid) {
		synchronized (this) {
			sessionPidMap.put(sessionId, pid);
			pidSessionMap.put(pid, sessionId);
		}
	}

	public void removePidSession(long pid) {
		synchronized (this) {
			Integer sessionId = pidSessionMap.get(pid);
			if (sessionId != null) {
				pidSessionMap.remove(pid);
				sessionPidMap.remove(sessionId);
			}
		}
	}

	public long getPid(String sessionId) {
		return sessionPidMap.get(sessionId);
	}

	public int getSessionId(long pid) {
		return pidSessionMap.get(pid);
	}
}
