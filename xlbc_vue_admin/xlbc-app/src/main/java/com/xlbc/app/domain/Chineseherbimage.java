package com.xlbc.app.domain;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.List;


/**
 * 中药图片对象 chineseherbimage
 *
 * @author ketty
 * @date 2023-03-20
 */
@Data
@TableName("chineseherbimage")
public class Chineseherbimage implements Serializable {

    private static final long serialVersionUID=1L;

    /**
     * 封面图
     */
    @TableField (exist = false)
    private String image;

    /**
     * 中药图片id
     */
    @TableId(value = "img_id", type = IdType.AUTO)
    private Long imgId;
    /**
     * oss图片id集合
     */
    private String ossIdList;
    /**
     * 中药id
     */
    private Long cbId;

}
