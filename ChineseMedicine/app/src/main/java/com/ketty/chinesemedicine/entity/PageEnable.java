package com.ketty.chinesemedicine.entity;

import java.io.Serializable;

/**
 * @Auther: Ketty Allen
 * @Date:2023/3/4 - 20:56
 * @Description:com.ketty.api_entity
 * @version: 1.0
 */
public class PageEnable implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long currentPage = 1L;

    private Long totalPages = 0L;

    private Long totalDataSize = 0L;

    private Long pageSize = 0L;

    private Long nextPage;

    private Long prefPage;

    public Long getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(Long currentPage) {
        this.currentPage = currentPage;
    }

    public Long getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(Long totalPages) {
        this.totalPages = totalPages;
    }

    public Long getTotalDataSize() {
        return totalDataSize;
    }

    public void setTotalDataSize(Long totalDataSize) {
        this.totalDataSize = totalDataSize;
    }

    public Long getPageSize() {
        return pageSize;
    }

    public void setPageSize(Long pageSize) {
        this.pageSize = pageSize;
    }

    public Long getNextPage() {
        return nextPage;
    }

    public void setNextPage(Long nextPage) {
        this.nextPage = nextPage;
    }

    public Long getPrefPage() {
        return prefPage;
    }

    public void setPrefPage(Long prefPage) {
        this.prefPage = prefPage;
    }
}
