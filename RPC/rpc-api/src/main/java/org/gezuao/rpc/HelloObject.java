package org.gezuao.rpc;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

// 实现序列化接口
@Data
@AllArgsConstructor
public class HelloObject implements Serializable {
    private Integer id;
    private String message;
}
