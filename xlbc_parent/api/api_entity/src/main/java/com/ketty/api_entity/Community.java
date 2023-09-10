package com.ketty.api_entity;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.util.Date;

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
 * @since 2023-03-09
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("community")
@ApiModel(value = "Community对象", description = "")
public class Community implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("社区笔记id")
    @TableId(value = "id", type = IdType.INPUT)
    private Long id;

    @ApiModelProperty("标题")
    @TableField("title")
    private String title;

    @ApiModelProperty("内容")
    @TableField("content")
    private String content;

    @ApiModelProperty("发布时间")
    @TableField("time")
    private Long time;

    @ApiModelProperty("ip归属地")
    @TableField("ip_address")
    private String ipAddress;

    @ApiModelProperty("点赞数量")
    @TableField("like_count")
    private Long likeCount;

    @ApiModelProperty("收藏数量")
    @TableField("collect_count")
    private Long collectCount;

    @ApiModelProperty("用户id")
    @TableField("user_id")
    private Long userId;

    @ApiModelProperty("逻辑删除")
    @TableField("deleted")
    private Integer deleted;

    @ApiModelProperty("创建时间")
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private Date createTime;

    @ApiModelProperty("修改时间")
    @TableField("update_time")
    private Date updateTime;


}
