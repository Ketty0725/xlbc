package com.xlbc.app.domain;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;


/**
 * 分类介绍对象 prescriptioncategoryintroduce
 *
 * @author ketty
 * @date 2023-04-01
 */
@Data
@TableName("prescriptioncategoryintroduce")
public class Prescriptioncategoryintroduce implements Serializable {

    private static final long serialVersionUID=1L;

    /**
     * 方剂分类详情id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    /**
     * 方剂分类id
     */
    private Long categoryId;
    /**
     * 释义
     */
    private String paraphrase;
    /**
     * 适应症
     */
    private String indications;
    /**
     * 注意事项
     */
    private String notes;

}
