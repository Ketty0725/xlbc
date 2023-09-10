package com.xlbc.app.domain;

import com.baomidou.mybatisplus.annotation.*;
import com.xlbc.common.core.domain.TreeEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;


/**
 * 方剂分类对象 prescriptioncategory
 *
 * @author ketty
 * @date 2023-04-01
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("prescriptioncategory")
public class Prescriptioncategory extends TreeEntity<Prescriptioncategory> {

    private static final long serialVersionUID=1L;

    /**
     * 方剂分类id
     */
    @TableId(value = "category_id", type = IdType.AUTO)
    private Long categoryId;
    /**
     * 祖级列表
     */
    private String ancestors;
    /**
     * 方剂分类名称
     */
    private String categoryName;
    /**
     * 显示顺序
     */
    private Integer orderNum;

}
