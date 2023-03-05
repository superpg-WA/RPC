package org.gezuao.rpc.server;

/*
服务端线程池实际调用的线程
 */

import org.gezuao.rpc.entity.RpcRequest;
import org.gezuao.rpc.entity.RpcResponse;
import org.gezuao.rpc.enumeration.ResponseCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.Socket;

public class RequestHandler implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(RequestHandler.class);

    private Socket socket;
    private Object service;

    public RequestHandler(Socket socket, Object service){
        this.socket = socket;
        this.service = service;
    }

    @Override
    public void run() {
        try (ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());
             ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream())) {
            RpcRequest rpcRequest = (RpcRequest) objectInputStream.readObject();
//            Method method = service.getClass().getMethod(rpcRequest.getMethodName(), rpcRequest.getParamTypes());
//            Object returnObject = method.invoke(service, rpcRequest.getParameters());
            Object returnObject = invokeMethod(rpcRequest);
            objectOutputStream.writeObject(RpcResponse.success(returnObject));

            objectOutputStream.flush();
        } catch (IOException | ClassNotFoundException | IllegalAccessException | InvocationTargetException e) {
            logger.error("调用或发送时有错误发生：", e);
        }
    }

    /*
    函数封装：
    传入rpcRequest，检测想要调用的方法是否可以调用。
    并invoke调用方法并返回Object类型结果。
     */
    private Object invokeMethod(RpcRequest rpcRequest) throws ClassNotFoundException, InvocationTargetException, IllegalAccessException {
        // 通过反射获取调用接口名
        Class<?> clazz = Class.forName(rpcRequest.getInterfaceName());
        // 确定需要调用的方法是不是已经注册的服务的子类
        if(!clazz.isAssignableFrom(service.getClass())){
            return RpcResponse.fail(ResponseCode.NOT_FOUND_CLASS);
        }
        Method method;
        try{
            method = service.getClass().getMethod(rpcRequest.getMethodName(),
                    rpcRequest.getParamTypes());
        } catch (NoSuchMethodException e) {
//            e.printStackTrace();
            return RpcResponse.fail(ResponseCode.NOT_FOUND_METHOD);
        }
        return method.invoke(service, rpcRequest.getParameters());
    }
}
