package com.xlbc.app.domain;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;


/**
 * 药品图片对象 productimage
 *
 * @author ketty
 * @date 2023-04-01
 */
@Data
@TableName("productimage")
public class Productimage implements Serializable {

    private static final long serialVersionUID=1L;

    /**
     * 封面图
     */
    @TableField (exist = false)
    private String image;

    /**
     * 商品图片id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    /**
     * 商品id
     */
    private Long productId;
    /**
     * oss图片id集合
     */
    private String ossIdList;

}
