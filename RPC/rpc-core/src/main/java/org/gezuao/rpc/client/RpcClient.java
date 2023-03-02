package org.gezuao.rpc.client;

/*
远程方法调用的消费者（客户端）
使用socket建立和服务端的连接，并发送request请求
并通过socket接受返回结果
 */

import org.gezuao.rpc.entity.RpcRequest;
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
            Object res = objectInputStream.readObject();
            //System.out.println(res);
            return res;
        } catch (UnknownHostException e) {
            logger.error("调用时有错误发生：", e);
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            logger.error("调用时有错误发生：", e);
            e.printStackTrace();
            return null;
        } catch (ClassNotFoundException e) {
            logger.error("调用时有错误发生：", e);
            e.printStackTrace();
            return null;
        }
    }
}
