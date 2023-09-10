package com.ketty.api_entity.enums;

/**
 * @Auther: Ketty Allen
 * @Date:2023/2/2 - 23:58
 * @Description:com.ketty.api_entity.enums
 * @version: 1.0
 */

import lombok.Getter;

/**
 * 用户收藏的状态
 */
@Getter
public enum CollectStateEnum {
    COLLECT(1, "收藏"),
    UNCOLLECT(0, "取消收藏/未收藏"),
    ;

    private Integer code;

    private String msg;

    CollectStateEnum(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
