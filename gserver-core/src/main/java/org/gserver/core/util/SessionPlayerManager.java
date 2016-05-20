package org.gserver.core.util;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * session和玩家id管理
 * 
 * @author zhaohui
 * 
 */
public class SessionPlayerManager {

	private static SessionPlayerManager instance = new SessionPlayerManager();

	private Map<Integer, Long> sessionRoleIdMap = new ConcurrentHashMap<Integer, Long>();
	private Map<Long, Integer> roleIdSessionMap = new ConcurrentHashMap<Long, Integer>();

	public static SessionPlayerManager getInstance() {
		return instance;
	}

	public void setSessionRoleId(int sessionId, long roleId) {
		synchronized (this) {
			sessionRoleIdMap.put(sessionId, roleId);
			roleIdSessionMap.put(roleId, sessionId);
		}
	}

	public void removeRoleIdSession(long roleId) {
		synchronized (this) {
			Integer sessionId = roleIdSessionMap.get(roleId);
			if (sessionId != null) {
				roleIdSessionMap.remove(roleId);
				sessionRoleIdMap.remove(sessionId);
			}
		}
	}

	public void removeRoleIdSession(int sessionId) {
		synchronized (this) {
			Long roleId = sessionRoleIdMap.get(sessionId);
			if (roleId != null) {
				sessionRoleIdMap.remove(sessionId);
				roleIdSessionMap.remove(roleId);
			}
		}
	}

	public long getRoleId(int sessionId) {
		return sessionRoleIdMap.get(sessionId);
	}

	public int getSessionId(long roleId) {
		return roleIdSessionMap.get(roleId);
	}
}
