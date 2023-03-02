package org.gezuao.rpc.client;

import lombok.AllArgsConstructor;
import org.gezuao.rpc.entity.RpcRequest;
import org.gezuao.rpc.entity.RpcResponse;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/*
RPC客户端动态代理
 */
@AllArgsConstructor
public class RpcClientProxy implements InvocationHandler {
    // 指明服务端的位置
    private String host;
    private int port;

    // 生成动态代理对象
    @SuppressWarnings("unchecked")
    public <T> T getProxy(Class<T> clazz){
        return (T) Proxy.newProxyInstance(clazz.getClassLoader(), new Class<?>[]{clazz}, this);
    }

    // 代理对象方法被调用时的动作
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        RpcRequest rpcRequest = RpcRequest.builder()
                .interfaceName(method.getDeclaringClass().getName())
                .methodName(method.getName())
                .parameters(args)
                .paramTypes(method.getParameterTypes())
                .build();
        RpcClient rpcClient = new RpcClient();
        return ((RpcResponse) rpcClient.sendRequest(rpcRequest, host, port)).getData();
    }
}
