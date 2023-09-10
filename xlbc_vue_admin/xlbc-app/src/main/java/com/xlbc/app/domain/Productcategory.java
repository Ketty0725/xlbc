package com.xlbc.app.domain;

import com.baomidou.mybatisplus.annotation.*;
import com.xlbc.common.core.domain.TreeEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;


/**
 * 药品分类对象 productcategory
 *
 * @author ketty
 * @date 2023-03-26
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("productcategory")
public class Productcategory extends TreeEntity<Productcategory> {

    private static final long serialVersionUID=1L;

    /**
     * 药品分类id
     */
    @TableId(value = "category_id", type = IdType.AUTO)
    private Long categoryId;
    /**
     * 祖级列表
     */
    private String ancestors;
    /**
     * 药品分类名称
     */
    private String categoryName;
    /**
     * 分类图标
     */
    private String categoryIcon;

    @TableField (exist = false)
    private String image;
    /**
     * 显示顺序
     */
    private Integer orderNum;

}
