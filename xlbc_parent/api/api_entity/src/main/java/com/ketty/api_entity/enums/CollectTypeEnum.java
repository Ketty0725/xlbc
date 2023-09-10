package com.ketty.api_entity.enums;

import lombok.Getter;

/**
 * @Auther: Ketty Allen
 * @Date:2023/2/6 - 20:24
 * @Description:com.ketty.api_entity.enums
 * @version: 1.0
 */
@Getter
public enum CollectTypeEnum {
    NOTE(0, "笔记"),
    PRODUCT(1, "商品"),
    ;

    private Integer code;

    private String msg;

    CollectTypeEnum(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public static CollectTypeEnum getByValue(Integer value){
        for(CollectTypeEnum collectTypeEnum : values()){
            if (collectTypeEnum.getCode().equals(value)) {
                return collectTypeEnum;
            }
        }
        return null;
    }
}
