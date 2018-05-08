package com.tank.netty.nettyclient;

import java.nio.charset.Charset;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.LineBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.util.concurrent.GlobalEventExecutor;

/**
 * @author duanzhiwei
 * 当channel注册到eventloop线程池后初始化channel
 */
public class ChildChannelHandler extends ChannelInitializer<SocketChannel>{
	private ChannelGroup channelGroup = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

	@Override
	protected void initChannel(SocketChannel e) throws Exception {
		System.out.println("客户端链接到服务端");
		// 基于换行符号
		e.pipeline().addLast(new LineBasedFrameDecoder(1024));
		//添加解码器，将ByteBuf转为String
		e.pipeline().addLast(new StringDecoder(Charset.forName("utf-8")));
		e.pipeline().addLast(new StringEncoder(Charset.forName("utf-8")));
		//在管道中添加我们自己的接收数据实现方法
		e.pipeline().addLast(new MyClientHanlder(channelGroup));
	}

}
