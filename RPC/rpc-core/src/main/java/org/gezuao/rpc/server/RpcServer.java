package org.gezuao.rpc.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.*;

/*
远程方法调用的服务端
 */
public class RpcServer {
    private final ExecutorService threadPool;
    private static final Logger logger = LoggerFactory.getLogger(RpcServer.class);

    // 初始化自定义线程池
    public RpcServer() {
        int corePoolSize = 5;
        int maximumPoolSIze = 50;
        long keepAliveTime = 60;
        // 阻塞队列
        BlockingQueue<Runnable> workingQueue = new ArrayBlockingQueue<>(100);
        ThreadFactory threadFactory = Executors.defaultThreadFactory();
        threadPool = new ThreadPoolExecutor(corePoolSize, maximumPoolSIze, keepAliveTime, TimeUnit.SECONDS, workingQueue, threadFactory);
    }

    public void register(Object service, int port){
        try(ServerSocket serverSocket = new ServerSocket(port)){
            logger.info("服务器正在启动...");
            Socket socket;
            while((socket = serverSocket.accept()) != null){
                logger.info("客户端连接！Ip为：" + socket.getInetAddress() + ":" + socket.getPort());
                threadPool.execute(new RequestHandler(socket, service));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
