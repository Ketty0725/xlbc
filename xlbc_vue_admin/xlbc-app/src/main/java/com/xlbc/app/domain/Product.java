package com.xlbc.app.domain;

import com.baomidou.mybatisplus.annotation.*;
import com.xlbc.system.domain.vo.SysOssVo;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 药品信息对象 product
 *
 * @author ketty
 * @date 2023-03-26
 */
@Data
@TableName("product")
public class Product implements Serializable {

    private static final long serialVersionUID=1L;

    @TableField (exist = false)
    private SysOssVo oss;

    /**
     * 商品id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    /**
     * 商品名称
     */
    private String name;
    /**
     * 商品价格
     */
    private BigDecimal price;
    /**
     * 商品简介
     */
    private String info;
    /**
     * 商品主图
     */
    private String image;
    /**
     * 商品库存
     */
    private Long inventory;
    /**
     * 商品分类二级id
     */
    private Long categoryId;
    /**
     * 收藏数量
     */
    private Long collectCount;

}
