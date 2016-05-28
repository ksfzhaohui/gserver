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
	private static final String session_player_relation = "session.player.relation";

	private IRedisTemplateMethod redisTemplateMethod;

	/**
	 * 获取指定玩家的sessionId
	 * 
	 * @param roleId
	 * @return
	 */
	public Integer getSessionId(long roleId) {
		String sessionId = redisTemplateMethod.hget(player_session_relation,
				String.valueOf(roleId));
		if (sessionId == null) {
			return null;
		}
		return Integer.valueOf(sessionId);
	}

	/**
	 * 通过session获取指定玩家
	 * 
	 * @param sessionId
	 * @return
	 */
	public Long getRoleId(int sessionId) {
		String roleId = redisTemplateMethod.hget(session_player_relation,
				String.valueOf(sessionId));
		if (roleId == null) {
			return null;
		}
		return Long.valueOf(roleId);
	}

	/**
	 * 从redis中移除session
	 * 
	 * @param sessionId
	 */
	public void removeSession(int sessionId) {
		Long roleId = getRoleId(sessionId);
		if (roleId != null) {
			redisTemplateMethod.hdel(player_session_relation,
					String.valueOf(roleId));
		}
		redisTemplateMethod.hdel(session_player_relation,
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
				String.valueOf(roleId), String.valueOf(sessionId));
		redisTemplateMethod.hset(session_player_relation,
				String.valueOf(sessionId), String.valueOf(roleId));
	}

	public void setRedisTemplateMethod(IRedisTemplateMethod redisTemplateMethod) {
		this.redisTemplateMethod = redisTemplateMethod;
	}

}
