package org.gserver.services;

import org.gserver.redis.IRedisTemplateMethod;

/**
 * session管理
 * 
 * @author zhaohui
 * 
 */
public class SessionService {

	private static final String player_session_relation = "player.session.relation";

	private IRedisTemplateMethod redisTemplateMethod;

	/**
	 * 从redis中移除session
	 * 
	 * @param sessionId
	 */
	public void removeSession(int sessionId) {
		redisTemplateMethod.hdel(player_session_relation,
				String.valueOf(sessionId));
	}

	/**
	 * 向redis中添加session
	 * 
	 * @param sessionId
	 * @param roleId
	 */
	public void addSession(int sessionId, long roleId) {
		redisTemplateMethod.hset(player_session_relation,
				String.valueOf(sessionId), String.valueOf(roleId));
	}

	public void setRedisTemplateMethod(IRedisTemplateMethod redisTemplateMethod) {
		this.redisTemplateMethod = redisTemplateMethod;
	}

}
