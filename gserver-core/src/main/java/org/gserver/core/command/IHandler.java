package org.gserver.core.command;

import org.gserver.core.exception.GsException;
import org.gserver.core.net.Message;

public interface IHandler {
	/**
	 * 业务处理方法
	 * 
	 * @param request
	 *        客户端请求
	 * @param response
	 *        服务器响应
	 */
	abstract public void execute(Message request, Message response)
			throws GsException;
}
