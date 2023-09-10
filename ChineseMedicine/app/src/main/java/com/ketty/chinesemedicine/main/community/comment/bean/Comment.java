package com.ketty.chinesemedicine.main.community.comment.bean;

import static com.ketty.chinesemedicine.main.community.comment.bean.CommentEntity.TYPE_COMMENT_PARENT;

import com.chad.library.adapter.base.entity.MultiItemEntity;

import java.util.List;

public class Comment implements MultiItemEntity {

    //一级评论id
    private long id;
    private long userId;
    //一级评论头像
    private String headImg;
    //一级评论的用户名
    private String userName;
    //一级评论的用户id
    private long noteId;
    //评论内容
    private String content;
    //创建时间
    private long createTime;
    private String ipAddress;
    //点赞数量
    private long likeCount;
    //是否点赞了  0没有 1点赞
    private int isLike;
    //当前一级评论的位置（下标）
    private int position;
    //当前一级评论所在的位置(下标)
    private int positionCount;

    private List<Reply> replies;

    private long currentPage;

    private long totalPages;

    private long totalDataSize;

    private long pageSize;

    private long nextPage;

    private long prefPage;

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
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

    public long getNoteId() {
        return noteId;
    }

    public void setNoteId(long noteId) {
        this.noteId = noteId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getHeadImg() {
        return headImg;
    }

    public void setHeadImg(String headImg) {
        this.headImg = headImg;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public long getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(long likeCount) {
        this.likeCount = likeCount;
    }

    public int getIsLike() {
        return isLike;
    }

    public void setIsLike(int isLike) {
        this.isLike = isLike;
    }

    public List<Reply> getReplies() {
        return replies;
    }

    public void setReplies(List<Reply> replies) {
        this.replies = replies;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public int getPositionCount() {
        return positionCount;
    }

    public void setPositionCount(int positionCount) {
        this.positionCount = positionCount;
    }

    @Override
    public String toString() {
        return "Comment{" +
                "id=" + id +
                ", userId=" + userId +
                ", headImg='" + headImg + '\'' +
                ", userName='" + userName + '\'' +
                ", noteId='" + noteId + '\'' +
                ", content='" + content + '\'' +
                ", createTime=" + createTime +
                ", likeCount=" + likeCount +
                ", isLike=" + isLike +
                ", position=" + position +
                ", positionCount=" + positionCount +
                ", replies=" + replies +
                ", currentPage=" + currentPage +
                ", totalPages=" + totalPages +
                ", totalDataSize=" + totalDataSize +
                ", pageSize=" + pageSize +
                ", nextPage=" + nextPage +
                ", prefPage=" + prefPage +
                '}';
    }

    @Override
    public int getItemType() {
        return TYPE_COMMENT_PARENT;
    }
}
