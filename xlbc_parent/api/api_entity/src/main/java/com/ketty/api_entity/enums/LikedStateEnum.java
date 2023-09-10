package com.ketty.api_entity.enums;

/**
 * @Auther: Ketty Allen
 * @Date:2023/2/2 - 23:58
 * @Description:com.ketty.api_entity.enums
 * @version: 1.0
 */

import lombok.Getter;

/**
 * 用户点赞的状态
 */
@Getter
public enum LikedStateEnum {
    LIKE(1, "点赞"),
    UNLIKE(0, "取消点赞/未点赞"),
    ;

    private Integer code;

    private String msg;

    LikedStateEnum(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
