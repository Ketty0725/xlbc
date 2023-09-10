package com.ketty.chinesemedicine.main.community.comment.bean;

import java.util.List;

public class CommentEntity {

    public static final int TYPE_COMMENT_PARENT = 1;
    public static final int TYPE_COMMENT_CHILD = 2;
    public static final int TYPE_COMMENT_MORE = 3;
    public static final int TYPE_COMMENT_EMPTY = 4;
    public static final int TYPE_COMMENT_OTHER = 5;

    private List<Comment> firstLevelBeans;
    private long totalCount;

    public List<Comment> getFirstLevelBeans() {
        return firstLevelBeans;
    }

    public void setFirstLevelBeans(List<Comment> firstLevelBeans) {
        this.firstLevelBeans = firstLevelBeans;
    }

    public long getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(long totalCount) {
        this.totalCount = totalCount;
    }

    @Override
    public String toString() {
        return "CommentEntity{" +
                "firstLevelBeans=" + firstLevelBeans +
                ", totalCount=" + totalCount +
                '}';
    }
}
