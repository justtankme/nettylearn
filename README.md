# nettylearn
学习bio nio aio netty的使用
## netty入门
服务端见`NettyServerTest`，客户端见`NettyClientTest`。客户端也可以使用形如`telnet localhost 7397`的shell命令进行测试。
## 关于半包及粘包
参考如下链接  
>http://zhaohuiopensource.iteye.com/blog/1541270

## netty websocket
服务端见`nettyWebsocketTest`。  
如果使用`telnet localhost 7397`连接并发送消息，会收到`HTTP/1.1 400 Bad Request`。
### 单服务路径
客户端使用`http://www.blue-zero.com/WebSocket/`连接`ws://localhost:7397/websocket`即可。  
### 多服务路径
通过握手阶段设置属性值，在消息处理阶段根据属性值switch判断handler的方式实现。
1. 客户端使用`http://www.blue-zero.com/WebSocket/`连接`ws://localhost:7397/websocket`，这个路径会正常打印。
2. 客户端使用`http://www.blue-zero.com/WebSocket/`连接`ws://localhost:7397/test`，这个路径会打印`我这里什么都不做`。