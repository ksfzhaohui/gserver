package org.gserver.logic;

import org.gserver.core.net.codec.HeaderDecoder;
import org.gserver.core.net.codec.HeaderEncoder;
import org.gserver.core.net.codec.encoder.ProtobufEncoder;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.Channels;

public class LogicPipelineFactory implements ChannelPipelineFactory {

	@Override
	public ChannelPipeline getPipeline() throws Exception {
		final LogicHandler handler = new LogicHandler();

		ChannelPipeline pipeline = Channels.pipeline();
		pipeline.addLast("decoder", new HeaderDecoder());

		pipeline.addLast("encoder", new HeaderEncoder());
		pipeline.addLast("pEncoder", new ProtobufEncoder());
		pipeline.addLast("handler", handler);
		return pipeline;
	}

}
