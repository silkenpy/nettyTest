package ir.rkr.nettyTest


import io.netty.bootstrap.ServerBootstrap
import io.netty.channel.socket.SocketChannel
import java.net.InetSocketAddress
import io.netty.buffer.Unpooled
import io.netty.util.CharsetUtil
import io.netty.buffer.ByteBuf
import io.netty.channel.*
import io.netty.channel.epoll.EpollEventLoopGroup
import io.netty.channel.epoll.EpollServerSocketChannel


class HelloServerHandler : ChannelInboundHandlerAdapter() {

    @Throws(Exception::class)
    override fun channelRead(ctx: ChannelHandlerContext, msg: Any) {
        val inBuffer = msg as ByteBuf

        val received = inBuffer.toString(CharsetUtil.US_ASCII)
        println("Server received: $received")

//        ctx.write(Unpooled.copiedBuffer("Hello $received", CharsetUtil.UTF_8))
        ctx.write(Unpooled.copiedBuffer(":29\r\n", CharsetUtil.US_ASCII))
    }

    @Throws(Exception::class)
    override fun channelReadComplete(ctx: ChannelHandlerContext) {
        ctx.writeAndFlush(Unpooled.EMPTY_BUFFER)
                .addListener(ChannelFutureListener.CLOSE)
    }

    @Throws(Exception::class)
    override fun exceptionCaught(ctx: ChannelHandlerContext, cause: Throwable) {
        cause.printStackTrace()
        ctx.close()
    }
}
const val version = 0.1

/**
 * CacheService main entry point.
 */
fun main(args: Array<String>) {

//
//
//    val parent = NioEventLoopGroup(16)
//    val child = NioEventLoopGroup(16)
//    val serverBootstrap = ServerBootstrap()
//    serverBootstrap.group(parent,child)
//    serverBootstrap.channel(NioServerSocketChannel::class.java)
//    serverBootstrap.localAddress(InetSocketAddress("localhost", 6379))
//    serverBootstrap.option(ChannelOption.SO_BACKLOG, 128)
//    serverBootstrap.childOption(ChannelOption.SO_KEEPALIVE, true)
//
//
//
//    serverBootstrap.childHandler(object : ChannelInitializer<SocketChannel>() {
//        @Throws(Exception::class)
//        override fun initChannel(socketChannel: SocketChannel) {
//            socketChannel.pipeline().addLast(HelloServerHandler())
//        }
//    })
//    val channelFuture = serverBootstrap.bind().sync()
//    channelFuture.channel()

//
//
    val parent = EpollEventLoopGroup(16)
    val child = EpollEventLoopGroup(16)
    val serverBootstrap = ServerBootstrap()
    serverBootstrap.group(parent,child)
    serverBootstrap.channel(EpollServerSocketChannel::class.java)
    serverBootstrap.localAddress(InetSocketAddress("localhost", 6379))
    serverBootstrap.option(ChannelOption.SO_BACKLOG, 128)
    serverBootstrap.childOption(ChannelOption.SO_KEEPALIVE, true)



    serverBootstrap.childHandler(object : ChannelInitializer<SocketChannel>() {
        @Throws(Exception::class)
        override fun initChannel(socketChannel: SocketChannel) {
            socketChannel.pipeline().addLast(HelloServerHandler())
        }
    })
    val channelFuture = serverBootstrap.bind().sync()
    channelFuture.channel()
}
