package com.ketty.api_entity.enums;

import lombok.Getter;

/**
 * @Auther: Ketty Allen
 * @Date:2023/2/6 - 20:24
 * @Description:com.ketty.api_entity.enums
 * @version: 1.0
 */
@Getter
public enum OrderFormEnum {
    ALL(-1, "全部"),
    SHIPMENTS(0, "待发货"),
    RECEIPT(1, "待收货"),
    FINISH(2, "已完成"),
    ;

    private Integer code;

    private String msg;

    OrderFormEnum(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

}
