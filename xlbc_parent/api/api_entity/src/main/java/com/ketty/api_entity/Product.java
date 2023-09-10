package com.ketty.api_entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * <p>
 * 
 * </p>
 *
 * @author ketty
 * @since 2023-02-14
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("product")
@ApiModel(value = "Product对象", description = "")
public class Product implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("商品id")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty("商品名称")
    @TableField("name")
    private String name;

    @ApiModelProperty("商品价格")
    @TableField("price")
    private BigDecimal price;

    @ApiModelProperty("商品简介")
    @TableField("info")
    private String info;

    @ApiModelProperty("商品主图")
    @TableField("image")
    private String image;

    @ApiModelProperty("商品库存")
    @TableField("inventory")
    private Integer inventory;

    @ApiModelProperty("商品分类id")
    @TableField("category_id")
    private Long categoryId;

    @ApiModelProperty("收藏数量")
    @TableField("collect_count")
    private Long collectCount;

}
