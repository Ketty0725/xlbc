package com.ketty.api_entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * <p>
 * 
 * </p>
 *
 * @author ketty
 * @since 2023-01-15
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("user")
@ApiModel(value = "User对象", description = "")
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("用户id")
    @JsonProperty("uId")
    @TableId(value = "u_id", type = IdType.INPUT)
    private Long uId;

    @ApiModelProperty("手机号")
    @JsonProperty("uPhone")
    @TableField("u_phone")
    private String uPhone;

    @ApiModelProperty("用户名")
    @JsonProperty("uName")
    @TableField("u_name")
    private String uName;

    @ApiModelProperty("性别")
    @JsonProperty("uSex")
    @TableField("u_sex")
    private String uSex;

    @ApiModelProperty("密码")
    @JsonProperty("uPassword")
    @TableField("u_password")
    private String uPassword;

    @ApiModelProperty("头像")
    @JsonProperty("uHeadicon")
    @TableField("u_headicon")
    private String uHeadicon;

    @ApiModelProperty("生日")
    @JsonProperty("uBirthday")
    @TableField("u_birthday")
    private String uBirthday;

    @ApiModelProperty("地址")
    @JsonProperty("uAddress")
    @TableField("u_address")
    private String uAddress;

    @ApiModelProperty("简介")
    @JsonProperty("uAbout")
    @TableField("u_about")
    private String uAbout;

    @ApiModelProperty("QQid")
    @JsonProperty("uQqId")
    @TableField("u_qq_id")
    private String uQqId;

    @ApiModelProperty("QQ昵称")
    @JsonProperty("uQqName")
    @TableField("u_qq_name")
    private String uQqName;

    @ApiModelProperty("背景图")
    @JsonProperty("backgroundImage")
    @TableField("background_image")
    private String backgroundImage;

    @ApiModelProperty("ip归属地")
    @JsonProperty("ipAddress")
    @TableField("ip_address")
    private String ipAddress;


}
