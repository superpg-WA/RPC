package org.gezuao.test;


import org.gezuao.rpc.HelloObject;
import org.gezuao.rpc.HelloService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HelloServiceImpl implements HelloService {

    // 使用Logger记录服务器接受的消息
    private static final Logger logger = LoggerFactory.getLogger(HelloServiceImpl.class);

    @Override
    public String hello(HelloObject object) {
        logger.info("接收到：{}", object.getMessage());
        return "这是调用返回值，id=" + object.getId();
    }
}
