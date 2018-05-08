package com.tank.netty.util;

import io.netty.channel.Channel;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.ChannelMatchers;

/**
 * @author duanzhiwei
 *创建channel组，这样就可以以组为单位操作channel了
 */
public class ChannelPoolOper {
	/**
	 * 加入组
	 * @param channel
	 * @return
	 */
	public static boolean add(ChannelGroup channelGroup, Channel channel) {
		return channelGroup.add(channel);
	}
	
	/**
	 * 从组中移除
	 * @param channel
	 * @return
	 */
	public static boolean remove(ChannelGroup channelGroup, Channel channel) {
		return channelGroup.remove(channel);
	}
	
	/**
	 * 给指定channel之外的channel发消息
	 * @param channel
	 * @param message
	 */
	public static void sendWithout(ChannelGroup channelGroup, Channel channel, String message) {
		channelGroup.writeAndFlush(message, ChannelMatchers.isNot(channel), false);
	}
}
