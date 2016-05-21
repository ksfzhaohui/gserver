package org.gserver.test;

import org.gserver.core.net.Header;
import org.gserver.core.net.Message;
import org.gserver.util.CommandEnum;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelStateEvent;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelHandler;

import protocol.BaseType.PBLogin;
import protocol.ClientServerProtocol.C2SLogin;

public class ClientHandler extends SimpleChannelHandler {

	@Override
	public void channelConnected(ChannelHandlerContext ctx, ChannelStateEvent e)
			throws Exception {
		PBLogin.Builder login = PBLogin.newBuilder();
		login.setUser("zhaohui");
		login.setPswd("11111111");

		C2SLogin.Builder c2sLogin = C2SLogin.newBuilder().setLogin(login);

		Header header = new Header();
		header.setSessionId(1);
		header.setCommandId(CommandEnum.C2S_LOGIN.getId());
		Message message = new Message(header, c2sLogin.build().toByteArray());
		e.getChannel().write(message);
	}

	@Override
	public void messageReceived(ChannelHandlerContext ctx, MessageEvent e)
			throws Exception {
		Message message = (Message) e.getMessage();
		System.out.println("cmd:" + message.getHeader().getCommandId());
		System.out.println(message.getData());
	}
}
