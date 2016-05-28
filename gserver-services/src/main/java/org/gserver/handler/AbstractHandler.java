package org.gserver.handler;

import org.gserver.core.exception.GsException;
import org.gserver.core.handler.IHandler;
import org.gserver.core.net.Message;
import org.gserver.services.SendMessageService;
import org.gserver.services.SessionService;
import org.gserver.util.CommandEnum;

import com.google.protobuf.InvalidProtocolBufferException;

public abstract class AbstractHandler implements IHandler {

	private SendMessageService sendMessageService;
	private SessionService sessionService;

	public void sendMessage(long roleId, CommandEnum command, byte data[]) {
		sendMessageService.sendMessage(roleId, command, data);
	}

	public void sendMessage(int sessionId, CommandEnum command, byte data[]) {
		sendMessageService.pushMessage(sessionId, command, data);
	}

	@Override
	public void execute(Message request) throws GsException,
			InvalidProtocolBufferException {
		Long roleId = sessionService.getRoleId(request.getHeader()
				.getSessionId());
		execute(roleId, request);
	}

	protected abstract void execute(Long roleId, Message request)
			throws GsException, InvalidProtocolBufferException;

	public void setSendMessageService(SendMessageService sendMessageService) {
		this.sendMessageService = sendMessageService;
	}

	public void setSessionService(SessionService sessionService) {
		this.sessionService = sessionService;
	}
}
