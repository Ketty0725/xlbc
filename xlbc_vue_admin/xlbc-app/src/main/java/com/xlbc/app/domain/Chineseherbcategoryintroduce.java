package com.xlbc.app.domain;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;


/**
 * 分类介绍对象 chineseherbcategoryintroduce
 *
 * @author ketty
 * @date 2023-04-01
 */
@Data
@TableName("chineseherbcategoryintroduce")
public class Chineseherbcategoryintroduce implements Serializable {

    private static final long serialVersionUID=1L;

    /**
     * 中药分类介绍id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    /**
     * 中药分类id
     */
    private Long categoryId;
    /**
     * 释义
     */
    private String paraphrase;
    /**
     * 分类
     */
    private String classify;
    /**
     * 功用
     */
    private String efficacy;
    /**
     * 配伍特点
     */
    private String matchingFeatures;
    /**
     * 用药注意
     */
    private String medicationAttention;
    /**
     * 现代研究
     */
    private String modernResearch;

}
