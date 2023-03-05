package org.gezuao.rpc.client;

/*
远程方法调用的消费者（客户端）
使用socket建立和服务端的连接，并发送request请求
并通过socket接受返回结果
 */

import org.gezuao.rpc.Exception.RpcException;
import org.gezuao.rpc.entity.RpcRequest;
import org.gezuao.rpc.entity.RpcResponse;
import org.gezuao.rpc.enumeration.ResponseCode;
import org.gezuao.rpc.enumeration.RpcError;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

public class RpcClient {
    private static final Logger logger = LoggerFactory.getLogger(RpcClient.class);

    public Object sendRequest(RpcRequest rpcRequest, String host, int port){
        try(Socket socket = new Socket(host, port)){
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());
            objectOutputStream.writeObject(rpcRequest);
            objectOutputStream.flush();
            //System.out.println("here");
            //Object res = objectInputStream.readObject();
            //System.out.println(res);

            /*
            处理调用错误异常
             */
            RpcResponse rpcResponse = (RpcResponse) objectInputStream.readObject();
            if(rpcResponse == null){
                // 如果返回response为null，说明服务端调用这个服务出错or没有调用这个服务，抛出异常。
                logger.error("服务调用失败，service：{}", rpcRequest.getInterfaceName());
                throw new RpcException(RpcError.SERVICE_INCOCATION_FAILURE, " service:" + rpcRequest.getInterfaceName());
            }
            if(rpcResponse.getStatusCode() == null || rpcResponse.getStatusCode() != ResponseCode.SUCCESS.getCode()){
                logger.error("调用服务失败, service: {}, response{}", rpcRequest.getInterfaceName(), rpcResponse);
                throw new RpcException(RpcError.SERVICE_INCOCATION_FAILURE, " service:" + rpcRequest.getInterfaceName());
            }
            return rpcResponse.getData();
        } catch (UnknownHostException e) {
            logger.error("调用时有错误发生：", e);
            e.printStackTrace();
            throw new RpcException("服务调用失败：", e);

        } catch (IOException e) {
            logger.error("调用时有错误发生：", e);
            e.printStackTrace();
            throw new RpcException("服务调用失败：", e);
        } catch (ClassNotFoundException e) {
            logger.error("调用时有错误发生：", e);
            e.printStackTrace();
            throw new RpcException("服务调用失败：", e);
        }
    }
}
