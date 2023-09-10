package com.ketty.api_entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @Auther: Ketty Allen
 * @Date:2023/3/4 - 20:56
 * @Description:com.ketty.api_entity
 * @version: 1.0
 */
@Getter
@Setter
@Accessors(chain = true)
public class PageEnable implements Serializable {

    private static final long serialVersionUID = 1L;

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
