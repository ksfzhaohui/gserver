package org.gserver.handler;

import org.gserver.core.exception.GsException;
import org.gserver.core.net.Message;
import org.gserver.util.CommandEnum;

import protocol.ClientServerProtocol.C2SLogin;
import protocol.ServerClientProtocol.S2CLogin;

import com.google.protobuf.InvalidProtocolBufferException;

public class Handler100001 extends AbstractHandler {

	@Override
	public void execute(Long roleId, Message request) throws GsException,
			InvalidProtocolBufferException {
		C2SLogin login = C2SLogin.parseFrom((byte[]) request.getData());
		System.out.println(login.toString());

		S2CLogin.Builder builder = S2CLogin.newBuilder();
		builder.setResult(1);

		sendMessage(request.getSessionId(), CommandEnum.S2C_LOGIN, builder
				.build().toByteArray());
	}

}
