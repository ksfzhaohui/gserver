package org.gserver.handler;

import org.gserver.core.exception.GsException;
import org.gserver.core.handler.IHandler;
import org.gserver.core.net.Message;
import org.gserver.services.util.CommandEnum;

import protocol.ClientServerProtocol.C2SLogin;
import protocol.ServerClientProtocol.S2CLogin;

import com.google.protobuf.InvalidProtocolBufferException;

public class Handler100 implements IHandler {

	@Override
	public void execute(Message request, Message response) throws GsException,
			InvalidProtocolBufferException {
		C2SLogin login = C2SLogin.parseFrom((byte[]) request.getData());
		System.out.println(login.toString());

		S2CLogin.Builder builder = S2CLogin.newBuilder();
		builder.setResult(1);

		response.setContent(CommandEnum.S2C_LOGIN.getId(), builder.build()
				.toByteArray());
	}

}
