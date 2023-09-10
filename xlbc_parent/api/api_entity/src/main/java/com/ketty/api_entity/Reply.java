package com.ketty.api_entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
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
 * @since 2023-01-31
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("reply")
@ApiModel(value = "Reply对象", description = "")
public class Reply implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("回复评论id")
    @TableId(value = "id", type = IdType.INPUT)
    private Long id;

    @ApiModelProperty("用户id")
    @TableField("user_id")
    private Long userId;

    @ApiModelProperty("回复用户id")
    @TableField("reply_user_id")
    private Long replyUserId;

    @ApiModelProperty("回复内容")
    @TableField("content")
    private String content;

    @ApiModelProperty("回复时间")
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private Long createTime;

    @ApiModelProperty("ip归属地")
    @TableField("ip_address")
    private String ipAddress;

    @ApiModelProperty("点赞数量")
    @TableField("like_count")
    private Long likeCount;

    @ApiModelProperty("父评论id")
    @TableField("father_id")
    private Long fatherId;


}
