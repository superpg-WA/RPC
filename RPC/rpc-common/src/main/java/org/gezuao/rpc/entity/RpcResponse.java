package org.gezuao.rpc.entity;

import lombok.Data;
import org.gezuao.rpc.enumeration.ResponseCode;

import java.io.Serializable;

@Data
public class RpcResponse<T> implements Serializable {
    /*
    响应状态码
     */
    private Integer statusCode;
    /*
    相应状态补充信息
     */
    private String message;
    /*
    响应数据
     */
    private T data;

    public static <T> RpcResponse<T> success(T data){
        RpcResponse<T> response = new RpcResponse<>();
        response.setStatusCode(ResponseCode.SUCCESS.getCode());
        response.setData(data);
        //System.out.println(response.message);
        return response;
    }

    public static <T> RpcResponse<T> fail(ResponseCode code){
        RpcResponse<T> response = new RpcResponse<>();
        response.setStatusCode(code.getCode());
        response.setMessage(code.getMessage());
        return response;
    }
}
