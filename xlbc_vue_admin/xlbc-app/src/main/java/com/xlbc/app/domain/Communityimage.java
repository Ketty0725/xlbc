package com.xlbc.app.domain;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;


/**
 * 笔记图片对象 communityimage
 *
 * @author ketty
 * @date 2023-04-09
 */
@Data
@TableName("communityimage")
public class Communityimage implements Serializable {

    private static final long serialVersionUID=1L;

    /**
     * 图片id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    /**
     * 笔记id
     */
    private Long noteId;
    /**
     * 图片地址
     */
    private String imageUrl;

}
