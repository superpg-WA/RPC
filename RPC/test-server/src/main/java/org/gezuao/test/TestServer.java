package org.gezuao.test;

import org.gezuao.rpc.HelloService;
import org.gezuao.rpc.server.RpcServer;

public class TestServer {
    public static void main(String[] args) {
        // 在服务端创建服务
        HelloService helloService = new HelloServiceImpl();
        // 创建服务器类
        RpcServer rpcServer = new RpcServer();
        // 注册服务到9000端口
        rpcServer.register(helloService, 9000);
    }
}
