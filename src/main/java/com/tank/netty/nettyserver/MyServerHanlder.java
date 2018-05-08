package com.tank.netty.nettyserver;

import java.util.Date;

import com.tank.netty.util.ChannelPoolOper;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.group.ChannelGroup;

/**
 * @author duanzhiwei
 *监听channel状态的变化
 */
public class MyServerHanlder extends ChannelInboundHandlerAdapter {
	private ChannelGroup channelGroup;
	
	public MyServerHanlder() {
		
	}
	
	public MyServerHanlder(ChannelGroup channelGroup) {
		this.channelGroup = channelGroup;
	}

	/*
	 * channelAction
	 * channel 通道 action 活跃的
	 * 当客户端主动链接服务端的链接后，这个通道就是活跃的了。也就是客户端与服务端建立了通信通道并且可以传输数据
	 */
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		ChannelPoolOper.add(channelGroup, ctx.channel());
		System.out.println(ctx.channel().localAddress().toString() + " channelActive");
		//通知您已经链接上客户端
		String str = "您已经开启与服务端链接"+" "+new Date()+" "+ctx.channel().localAddress() + System.lineSeparator();
//		ByteBuf buf = Unpooled.buffer(str.getBytes().length);
//		buf.writeBytes(str.getBytes());
//		ctx.writeAndFlush(buf);
		//加了编码器之后可以直接写
		ctx.writeAndFlush(str);
		ChannelPoolOper.sendWithout(channelGroup, ctx.channel(), "有个傻X上线了" + System.lineSeparator());
	}

	/*
	 * channelInactive
	 * channel 通道 Inactive 不活跃的
	 * 当客户端主动断开服务端的链接后，这个通道就是不活跃的。也就是说客户端与服务端的关闭了通信通道并且不可以传输数据
	 */
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		System.out.println(ctx.channel().localAddress().toString() + " channelInactive");
		ChannelPoolOper.sendWithout(channelGroup, ctx.channel(), "有个傻X下线了" + System.lineSeparator());
	}

	/*
	 * channelRead
	 * channel 通道 Read 读
	 * 简而言之就是从通道中读取数据，也就是服务端接收客户端发来的数据 但是这个数据在不进行解码时它是ByteBuf类型的后面例子我们在介绍
	 */
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
//		ByteBuf buf = (ByteBuf) msg;
//		byte[] msgByte = new byte[buf.readableBytes()];
//		buf.readBytes(msgByte);
//		System.out.println(new Date() + " " + new String(msgByte, "UTF-8"));
		//加了解码器之后就可以直接输出
		System.out.println(new Date() + " " + msg);
		//通知您已经链接上客户端
		String str = "服务端收到："+new Date()+" "+msg + System.lineSeparator();
//		ByteBuf buf = Unpooled.buffer(str.getBytes().length);
//		buf.writeBytes(str.getBytes());
//		ctx.writeAndFlush(buf);
		//加了编码器之后可以直接写
		ctx.writeAndFlush(str);
		ChannelPoolOper.sendWithout(channelGroup, ctx.channel(), "有个傻X说：" + msg + System.lineSeparator());
	}

	/*
	 * channelReadComplete
	 * channel 通道 Read 读取 Complete 完成
	 * 在通道读取完成后会在这个方法里通知，对应可以做刷新操作 ctx.flush()
	 */
	public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
		ctx.flush();
	}

	/*
	 * exceptionCaught
	 * exception 异常 Caught 抓住
	 * 抓住异常，当发生异常的时候，可以做一些相应的处理，比如打印日志、关闭链接
	 */
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		ctx.close();
		System.out.println("异常信息：" + cause.getMessage() + System.lineSeparator());
	}
}