package org.gserver.gate;

import org.gserver.core.net.codec.HeaderDecoder;
import org.gserver.core.net.codec.HeaderEncoder;
import org.gserver.core.net.codec.decoder.ProtobufDecoder;
import org.gserver.core.net.codec.encoder.ProtobufEncoder;
import org.gserver.protocol.Protocol;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.Channels;

import com.google.protobuf.ExtensionRegistry;

public class GatePipelineFactory implements ChannelPipelineFactory {

	@Override
	public ChannelPipeline getPipeline() throws Exception {
		final GateHandler handler = new GateHandler();
		final ExtensionRegistry registry = ExtensionRegistry.newInstance();
		Protocol.registerAllExtensions(registry);

		ChannelPipeline pipeline = Channels.pipeline();
		pipeline.addLast("decoder", new HeaderDecoder());
		pipeline.addLast("pDecoder",
				new ProtobufDecoder(Protocol.Request.getDefaultInstance(),
						registry));

		pipeline.addLast("encoder", new HeaderEncoder());
		pipeline.addLast("pEncoder", new ProtobufEncoder());
		pipeline.addLast("handler", handler);
		return pipeline;

	}

}
