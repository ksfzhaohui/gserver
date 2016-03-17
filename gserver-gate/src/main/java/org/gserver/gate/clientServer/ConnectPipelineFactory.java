package org.gserver.gate.clientServer;

import org.gserver.core.net.codec.HeaderDecoder;
import org.gserver.core.net.codec.HeaderEncoder;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.Channels;

public class ConnectPipelineFactory implements ChannelPipelineFactory {

	@Override
	public ChannelPipeline getPipeline() throws Exception {
		final ConnectHandler handler = new ConnectHandler();
		ChannelPipeline pipeline = Channels.pipeline();
		pipeline.addLast("decoder", new HeaderDecoder());
		pipeline.addLast("encoder", new HeaderEncoder());
		pipeline.addLast("handler", handler);
		return pipeline;
	}

}
