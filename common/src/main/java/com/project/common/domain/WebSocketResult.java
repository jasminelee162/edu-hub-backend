package com.project.common.domain;

import com.project.common.enums.ResultCode;
import lombok.Data;

@Data
public class WebSocketResult {

    private int number;//1是成员加入信息 2是普通信息
    private Object data;

}
