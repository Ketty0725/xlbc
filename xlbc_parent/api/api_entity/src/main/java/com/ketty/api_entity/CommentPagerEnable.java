package com.ketty.api_entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * @Auther: Ketty Allen
 * @Date:2022/11/26 - 9:23
 * @version: 1.0
 */
@Getter
@Setter
@Accessors(chain = true)
@ApiModel(value="CommentPagerEnable", description="")
public class CommentPagerEnable {

    private List<CommentResult> comments;

    @ApiModelProperty(value = "评论+回复总数")
    @Builder.Default
    private Long totalAllSize = 0L;

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

    public Long getTotalAllSize() {
        return totalAllSize + totalDataSize;
    }

    public void setTotalAllSize(Long totalAllSize) {
        this.totalAllSize = totalAllSize;
    }

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
