package com.ketty.api_entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * OSS对象存储视图对象 sys_oss
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("sys_oss")
@ApiModel(value = "sys_oss对象", description = "")
public class SysOss implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("对象存储主键")
    @TableId(value = "oss_id", type = IdType.ASSIGN_ID)
    private Long ossId;

    @ApiModelProperty("文件名")
    @TableField("file_name")
    private String fileName;

    @ApiModelProperty("原名")
    @TableField("original_name")
    private String originalName;

    @ApiModelProperty("文件后缀名")
    @TableField("file_suffix")
    private String fileSuffix;

    @ApiModelProperty("URL地址")
    @TableField("url")
    private String url;

    @ApiModelProperty("创建时间")
    @TableField("create_time")
    private Date createTime;

    @ApiModelProperty("上传人")
    @TableField("create_by")
    private String createBy;

    @ApiModelProperty("更新时间")
    @TableField("update_time")
    private Date updateTime;

    @ApiModelProperty("更新人")
    @TableField("update_by")
    private String updateBy;

    @ApiModelProperty("服务商")
    @TableField("service")
    private String service;


}
