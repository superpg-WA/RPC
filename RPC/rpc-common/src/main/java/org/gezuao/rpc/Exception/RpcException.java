package org.gezuao.rpc.Exception;

import org.gezuao.rpc.enumeration.RpcError;

/*
自定义异常，为了在Rpc调用出错时能够返回提示信息。
 */

public class RpcException extends RuntimeException{
    public RpcException(RpcError error, String detail){
        super(error.getMessage() + ": " + detail);
    }

    public RpcException(String message, Throwable cause){
        super(message, cause);
    }

    public RpcException(RpcError error){
        super(error.getMessage());
    }
}
