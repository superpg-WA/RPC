package org.gezuao.rpc.enumeration;

/*
方法调用的响应状态码
 */

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ResponseCode {
    SUCCESS(200, "调用成功"),
    FAIL(500, "调用失败"),
    NOT_FOUND_METHOD(500, "未找到指定方法"),
    NOT_FOUND_CLASS(500, "未找到指定类");

    private final int code;
    private final String message;
}
