package com.ketty.api_entity.enums;

import lombok.Getter;

/**
 * @Auther: Ketty Allen
 * @Date:2023/2/6 - 20:24
 * @Description:com.ketty.api_entity.enums
 * @version: 1.0
 */
@Getter
public enum LikedTypeEnum {
    NOTE(0, "笔记"),
    COMMENT(1, "评论"),
    REPLY(2, "回复"),
    ;

    private Integer code;

    private String msg;

    LikedTypeEnum(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public static LikedTypeEnum getByValue(Integer value){
        for(LikedTypeEnum likedTypeEnum : values()){
            if (likedTypeEnum.getCode().equals(value)) {
                return likedTypeEnum;
            }
        }
        return null;
    }
}
