package org.gezuao.test;

import org.gezuao.rpc.HelloObject;
import org.gezuao.rpc.HelloService;
import org.gezuao.rpc.client.RpcClient;
import org.gezuao.rpc.client.RpcClientProxy;

public class TestClient {
    public static void main(String[] args) {
        // 创建需要调用的服务 helloservice 的代理类
        RpcClientProxy proxy = new RpcClientProxy("127.0.0.1", 9000);
        HelloService helloService = proxy.getProxy(HelloService.class);
        // 通过代理类执行函数
        HelloObject object = new HelloObject(12, "This is a message");
        String res = helloService.hello(object);
        System.out.println(res);
    }
}
