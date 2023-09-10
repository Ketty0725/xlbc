package com.xlbc.app.domain;

import com.baomidou.mybatisplus.annotation.*;
import com.xlbc.common.core.domain.TreeEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;


/**
 * 中药分类对象 chineseherbcategorylevel
 *
 * @author ketty
 * @date 2023-03-19
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("chineseherbcategory")
public class Chineseherbcategory extends TreeEntity<Chineseherbcategory> {

    private static final long serialVersionUID=1L;

    /**
     * 中药分类id
     */
    @TableId(value = "category_id", type = IdType.AUTO)
    private Long categoryId;
    /**
     * 祖级列表
     */
    private String ancestors;
    /**
     * 中药分类名称
     */
    private String categoryName;
    /**
     * 显示顺序
     */
    private Integer orderNum;

}
