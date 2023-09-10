package com.ketty.chinesemedicine.entity.enums;

/**
 * @Auther: Ketty Allen
 * @Date:2023/2/6 - 20:24
 * @Description:com.ketty.api_entity.enums
 * @version: 1.0
 */
public enum ConcernEnum {
    NONE(0, "未关注"),
    OPPOSITE(1, "对方关注了我"),
    ALIKE(2, "我关注了对方"),
    ALL(3, "互相关注"),
    ;

    private Integer code;

    private String msg;

    ConcernEnum(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public Integer getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}
