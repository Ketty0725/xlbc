package com.ketty.chinesemedicine.main.community.comment.bean;

import static com.ketty.chinesemedicine.main.community.comment.bean.CommentEntity.TYPE_COMMENT_CHILD;

import com.chad.library.adapter.base.entity.MultiItemEntity;

public class Reply implements MultiItemEntity {

    //二级评论id
    private long id;
    //二级评论头像
    private String headImg;
    //二级评论的用户名
    private String userName;
    //二级评论的用户id
    private long userId;
    //回复评论人的用户名
    private String replyUserName;
    //回复评论人的用户id
    private long replyUserId;
    //评论内容（回复内容）
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
    //当前二级评论所在的位置(下标)
    private int positionCount;
    //当前二级评论所在一级评论条数的位置（下标）
    private int childPosition;

    private long fatherId;

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public long getFatherId() {
        return fatherId;
    }

    public void setFatherId(long fatherId) {
        this.fatherId = fatherId;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public long getReplyUserId() {
        return replyUserId;
    }

    public void setReplyUserId(long replyUserId) {
        this.replyUserId = replyUserId;
    }

    public String getReplyUserName() {
        return replyUserName;
    }

    public void setReplyUserName(String replyUserName) {
        this.replyUserName = replyUserName;
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

    public int getChildPosition() {
        return childPosition;
    }

    public void setChildPosition(int childPosition) {
        this.childPosition = childPosition;
    }

    @Override
    public String toString() {
        return "SecondLevelBean{" +
                "id=" + id +
                ", headImg='" + headImg + '\'' +
                ", userName='" + userName + '\'' +
                ", userId=" + userId +
                ", replyUserName='" + replyUserName + '\'' +
                ", replyUserId=" + replyUserId +
                ", content='" + content + '\'' +
                ", createTime=" + createTime +
                ", likeCount=" + likeCount +
                ", isLike=" + isLike +
                ", position=" + position +
                ", positionCount=" + positionCount +
                ", childPosition=" + childPosition +
                ", fatherId=" + fatherId +
                '}';
    }

    @Override
    public boolean equals( Object obj) {
        if (obj == null) return false;
        if (!(obj instanceof Reply)) return false;
        Reply bean = (Reply) obj;
        return bean.getId() == id;
    }

    @Override
    public int getItemType() {
        return TYPE_COMMENT_CHILD;
    }
}
