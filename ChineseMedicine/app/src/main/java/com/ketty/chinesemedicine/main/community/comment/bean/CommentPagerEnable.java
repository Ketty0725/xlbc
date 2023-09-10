package com.ketty.chinesemedicine.main.community.comment.bean;

import java.io.Serializable;
import java.util.List;

public class CommentPagerEnable implements Serializable {
    private List<Comment> comments;

    private long totalAllSize;

    private long currentPage;

    private long totalPages;

    private long totalDataSize;

    private long pageSize;

    private long nextPage;

    private long prefPage;

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    public long getTotalAllSize() {
        return totalAllSize;
    }

    public void setTotalAllSize(long totalAllSize) {
        this.totalAllSize = totalAllSize;
    }

    public long getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(long currentPage) {
        this.currentPage = currentPage;
    }

    public long getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(long totalPages) {
        this.totalPages = totalPages;
    }

    public long getTotalDataSize() {
        return totalDataSize;
    }

    public void setTotalDataSize(long totalDataSize) {
        this.totalDataSize = totalDataSize;
    }

    public long getPageSize() {
        return pageSize;
    }

    public void setPageSize(long pageSize) {
        this.pageSize = pageSize;
    }

    public long getNextPage() {
        return nextPage;
    }

    public void setNextPage(long nextPage) {
        this.nextPage = nextPage;
    }

    public long getPrefPage() {
        return prefPage;
    }

    public void setPrefPage(long prefPage) {
        this.prefPage = prefPage;
    }
}
