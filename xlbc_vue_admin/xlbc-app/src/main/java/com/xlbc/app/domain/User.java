package com.xlbc.app.domain;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.xlbc.common.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;


/**
 * 用户对象 user
 *
 * @author ketty
 * @date 2023-03-18
 */
@Data
@TableName("user")
public class User implements Serializable {

    private static final long serialVersionUID=1L;

    /**
     * 用户id
     */
    @TableId(value = "u_id")
    @JsonProperty("uId")
    private Long uId;
    /**
     * 手机号
     */
    @JsonProperty("uPhone")
    private String uPhone;
    /**
     * 用户名
     */
    @JsonProperty("uName")
    private String uName;
    /**
     * 性别
     */
    @JsonProperty("uSex")
    private String uSex;
    /**
     * 密码
     */
    @JsonProperty("uPassword")
    private String uPassword;
    /**
     * 头像
     */
    @JsonProperty("uHeadicon")
    private String uHeadicon;
    /**
     * 生日
     */
    @JsonProperty("uBirthday")
    private String uBirthday;
    /**
     * 地址
     */
    @JsonProperty("uAddress")
    private String uAddress;
    /**
     * 简介
     */
    @JsonProperty("uAbout")
    private String uAbout;
    /**
     * QQid
     */
    @JsonProperty("uQqId")
    private String uQqId;
    /**
     * QQ昵称
     */
    @JsonProperty("uQqName")
    private String uQqName;
    /**
     * 背景图
     */
    @JsonProperty("backgroundImage")
    private String backgroundImage;
    /**
     * ip归属地
     */
    @JsonProperty("ipAddress")
    private String ipAddress;

}
