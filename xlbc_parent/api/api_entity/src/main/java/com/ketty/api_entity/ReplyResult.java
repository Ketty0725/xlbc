package com.ketty.api_entity;

import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.io.Serializable;

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
@ApiModel(value = "ReplyResult对象", description = "")
public class ReplyResult implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("回复评论id")
    private Long id;

    @ApiModelProperty("用户id")
    private Long userId;

    private String userName;

    private String headImg;

    @ApiModelProperty("回复用户id")
    private Long replyUserId;

    private String replyUserName;

    @ApiModelProperty("回复内容")
    private String content;

    @ApiModelProperty("回复时间")
    private Long createTime;

    @ApiModelProperty("ip归属地")
    private String ipAddress;

    @ApiModelProperty("点赞数量")
    private Long likeCount;

    private Integer isLike;

    @ApiModelProperty("父评论id")
    private Long fatherId;


}
