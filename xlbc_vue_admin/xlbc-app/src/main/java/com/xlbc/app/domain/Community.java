package com.xlbc.app.domain;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;
import java.util.List;


/**
 * 笔记对象 community
 *
 * @author ketty
 * @date 2023-03-26
 */
@Data
@TableName("community")
public class Community implements Serializable {

    private static final long serialVersionUID=1L;

    @TableField (exist = false)
    private User user;

    @TableField (exist = false)
    private List<Communityimage> images;

    /**
     * 社区笔记id
     */
    @TableId(value = "id")
    private Long id;
    /**
     * 标题
     */
    private String title;
    /**
     * 内容
     */
    private String content;
    /**
     * 发布时间
     */
    private Long time;
    /**
     * ip归属地
     */
    private String ipAddress;
    /**
     * 点赞数量
     */
    private Long likeCount;
    /**
     * 收藏数量
     */
    private Long collectCount;
    /**
     * 用户id
     */
    private Long userId;
    /**
     * 逻辑删除
     */
    private Long deleted;

    private Date createTime;

}
