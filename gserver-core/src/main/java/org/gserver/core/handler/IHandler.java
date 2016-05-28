package org.gserver.core.handler;

import org.gserver.core.exception.GsException;
import org.gserver.core.net.Message;

import com.google.protobuf.InvalidProtocolBufferException;

public interface IHandler {
	/**
	 * 业务处理方法
	 * 
	 * @param request
	 *            客户端请求
	 */
	abstract public void execute(Message request) throws GsException,
			InvalidProtocolBufferException;
}
