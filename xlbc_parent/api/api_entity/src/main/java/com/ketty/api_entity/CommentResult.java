package com.ketty.api_entity;

import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;

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
@ApiModel(value = "CommentResult对象", description = "")
public class CommentResult implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("父评论id")
    private Long id;

    @ApiModelProperty("用户id")
    private Long userId;

    private String userName;

    private String headImg;

    @ApiModelProperty("笔记id")
    private Long noteId;

    @ApiModelProperty("评论内容")
    private String content;

    @ApiModelProperty("评论时间")
    private Long createTime;

    @ApiModelProperty("ip归属地")
    private String ipAddress;

    @ApiModelProperty("点赞数量")
    private Long likeCount;

    private Integer isLike;


    private List<ReplyResult> replies;

    @ApiModelProperty(value = "当前页码")
    @Builder.Default
    private Long currentPage = 1L;

    @ApiModelProperty(value = "总页数")
    @Builder.Default
    private Long totalPages = 0L;

    @ApiModelProperty(value = "数据总数")
    @Builder.Default
    private Long totalDataSize = 0L;

    @ApiModelProperty(value = "每一页数据的大小")
    @Builder.Default
    private Long pageSize = 0L;

    @ApiModelProperty(value = "下一个页码")
    private Long nextPage;

    @ApiModelProperty(value = "上一个页码")
    private Long prefPage;

    public Long getNextPage() {
        return currentPage < getTotalPages() ? currentPage + 1 : 0;
    }

    public void setNextPage(Long nextPage) {
        this.nextPage = nextPage;
    }

    public Long getPrefPage() {
        return currentPage - 1;
    }

    public void setPrefPage(Long prefPage) {
        this.prefPage = prefPage;
    }

}
