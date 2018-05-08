package com.tank.netty.websocket;

import java.util.Date;

import com.tank.constant.MyConstants;
import com.tank.netty.util.ChannelPoolOper;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.handler.codec.http.websocketx.CloseWebSocketFrame;
import io.netty.handler.codec.http.websocketx.PingWebSocketFrame;
import io.netty.handler.codec.http.websocketx.PongWebSocketFrame;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketServerHandshaker;
import io.netty.handler.codec.http.websocketx.WebSocketServerHandshakerFactory;
import io.netty.util.AttributeKey;
import io.netty.util.CharsetUtil;

public class MyWebSocketServerHandler extends SimpleChannelInboundHandler<Object> {
	private ChannelGroup channelGroup;

	public ChannelGroup getChannelGroup() {
		return channelGroup;
	}

	public MyWebSocketServerHandler setChannelGroup(ChannelGroup channelGroup) {
		this.channelGroup = channelGroup;
		return this;
	}

	public MyWebSocketServerHandler() {

	}

	private WebSocketServerHandshaker handshaker;

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		// 添加
		ChannelPoolOper.add(channelGroup, ctx.channel());
		System.out.println("客户端与服务端连接开启");
	}

	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		// 移除
		ChannelPoolOper.remove(channelGroup, ctx.channel());
		System.out.println("客户端与服务端连接关闭");
	}

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
		//这里处理消息
		if (msg instanceof FullHttpRequest) {
			System.out.println("收到http请求");
			//http消息只会在握手阶段有用
			handleHttpRequest(ctx, ((FullHttpRequest) msg));
		} else if (msg instanceof WebSocketFrame) {
			System.out.println("收到WebSocketFrame");
			//根据握手阶段设置的attribute判断调用哪个处理方法
			if ("anzhuo".equals(ctx.channel().attr(AttributeKey.valueOf("type")).get())) {
				System.out.println(1111);
				handlerWebSocketFrameDoNoThing(ctx, (WebSocketFrame) msg);
			} else {
				System.out.println(2323);
				handlerWebSocketFrame(ctx, (WebSocketFrame) msg);
			}
		}
	}

	@Override
	public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
		ctx.flush();
	}

	private void handlerWebSocketFrameDoNoThing(ChannelHandlerContext ctx, WebSocketFrame frame) {
		System.out.println("我这里什么都不做");
	}
	
	private void handlerWebSocketFrame(ChannelHandlerContext ctx, WebSocketFrame frame) {
		// 判断是否关闭链路的指令
		if (frame instanceof CloseWebSocketFrame) {
			System.out.println("收到关闭链路的指令");
			handshaker.close(ctx.channel(), (CloseWebSocketFrame) frame.retain());
		}
		// 判断是否ping消息
		if (frame instanceof PingWebSocketFrame) {
			System.out.println("收到ping消息");
			ctx.channel().write(new PongWebSocketFrame(frame.content().retain()));
			return;
		}
		// 本例程仅支持文本消息，不支持二进制消息
		if (!(frame instanceof TextWebSocketFrame)) {
			System.out.println("本例程仅支持文本消息，不支持二进制消息");
			throw new UnsupportedOperationException(
					String.format("%s frame types not supported", frame.getClass().getName()));
		}
		System.out.println("收到文本消息");
		// 返回应答消息
		String request = ((TextWebSocketFrame) frame).text();
		System.out.println("服务端收到：" + request);
		System.out.println(String.format("%s received %s", ctx.channel(), request));
		TextWebSocketFrame tws = new TextWebSocketFrame(new Date().toString() + ctx.channel().id() + "：" + request);
		// 群发
		ChannelPoolOper.sendWithout(channelGroup, ctx.channel(), tws);
	}

	private void handleHttpRequest(ChannelHandlerContext ctx, FullHttpRequest req) {
		System.out.println("处理http请求");
		if (!req.decoderResult().isSuccess() || (!"websocket".equals(req.headers().get("Upgrade")))) {
			System.out.println("不是websocket请求，拒绝");
			sendHttpResponse(ctx, req,
					new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.BAD_REQUEST));
			return;
		}
		//TODO 待优化
		// 这里处理不同的uri
		if (req.method() == HttpMethod.GET && MyConstants.WSURL_TEST.equals(req.uri())) {
			// 重点在这里，对于URL的不同，给ChannelHandlerContext设置一个Attribut
			ctx.channel().attr(AttributeKey.valueOf("type")).set("anzhuo");
		} else if (req.method() == HttpMethod.GET && MyConstants.WSURL_WEBSOCKET.equals(req.uri())) {
			// ...处理
			ctx.channel().attr(AttributeKey.valueOf("type")).set("live");
		}
		System.out.println("将http请求升级为websocket");
		// 注意，这条地址别被误导了，其实这里填写什么都无所谓，WS协议消息的接收不受这里控制
		WebSocketServerHandshakerFactory wsFactory = new WebSocketServerHandshakerFactory(
				"ws://localhost:7397/websocket", null, false);
		System.out.println("开始握手");
		handshaker = wsFactory.newHandshaker(req);
		if (handshaker == null) {
			System.out.println("握手失败");
			WebSocketServerHandshakerFactory.sendUnsupportedVersionResponse(ctx.channel());
		} else {
			handshaker.handshake(ctx.channel(), req);
			System.out.println("握手成功");
		}
	}

	private static void sendHttpResponse(ChannelHandlerContext ctx, FullHttpRequest req, DefaultFullHttpResponse res) {
		// 返回应答给客户端
		if (res.status().code() != 200) {
			ByteBuf buf = Unpooled.copiedBuffer(res.status().toString(), CharsetUtil.UTF_8);
			res.content().writeBytes(buf);
			buf.release();
		}
		// 如果是非Keep-Alive，关闭连接
		ChannelFuture f = ctx.channel().writeAndFlush(res);
		if (!isKeepAlive(req) || res.status().code() != 200) {
			f.addListener(ChannelFutureListener.CLOSE);
		}
	}

	private static boolean isKeepAlive(FullHttpRequest req) {
		return false;
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		cause.printStackTrace();
		ctx.close();
	}
}
