package org.gserver.test;

import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

import org.gserver.core.net.codec.HeaderDecoder;
import org.gserver.core.net.codec.HeaderEncoder;
import org.gserver.core.net.codec.encoder.ProtobufEncoder;
import org.jboss.netty.bootstrap.ClientBootstrap;
import org.jboss.netty.channel.ChannelFuture;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.channel.socket.nio.NioClientSocketChannelFactory;

public class Client {

	public static void main(String[] args) {
		ClientBootstrap cbApp = new ClientBootstrap(
				new NioClientSocketChannelFactory(
						Executors.newCachedThreadPool(),
						Executors.newCachedThreadPool()));
		final ClientHandler handler = new ClientHandler();
		cbApp.setPipelineFactory(new ChannelPipelineFactory() {
			public ChannelPipeline getPipeline() {
				ChannelPipeline pipeline = Channels.pipeline();
				pipeline.addLast("decoder", new HeaderDecoder());
				pipeline.addLast("hEncoder", new HeaderEncoder());
				pipeline.addLast("pEncoder", new ProtobufEncoder());
				pipeline.addLast("handler", handler);
				return pipeline;
			}
		});
		ChannelFuture future = cbApp.connect(new InetSocketAddress("localhost",
				8000));
		future.getChannel().getCloseFuture().awaitUninterruptibly();
		cbApp.releaseExternalResources();
	}
}
