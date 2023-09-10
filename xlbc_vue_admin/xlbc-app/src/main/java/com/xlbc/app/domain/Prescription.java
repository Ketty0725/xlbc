package com.xlbc.app.domain;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;


/**
 * 方剂数据对象 prescription
 *
 * @author ketty
 * @date 2023-04-01
 */
@Data
@TableName("prescription")
public class Prescription implements Serializable {

    private static final long serialVersionUID=1L;

    /**
     * 方剂id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    /**
     * 方剂分类id
     */
    private Long categoryId;
    /**
     * 方剂名称
     */
    private String prescriptionName;
    /**
     * 出处
     */
    private String provenance;
    /**
     * 所属分类
     */
    private String category;
    /**
     * 功用
     */
    private String efficacy;
    /**
     * 组成
     */
    private String compose;
    /**
     * 用法
     */
    private String usageMethod;
    /**
     * 主治
     */
    private String attending;
    /**
     * 使用注意
     */
    private String notes;
    /**
     * 歌诀
     */
    private String songTips;
    /**
     * 方义
     */
    private String fangYi;
    /**
     * 配伍特点
     */
    private String matchingFeatures;
    /**
     * 运用
     */
    private String wield;
    /**
     * 加减化裁
     */
    private String additionAndSubtraction;
    /**
     * 化裁方之间的鉴别
     */
    private String tailoringIdentification;
    /**
     * 文献摘要
     */
    private String literatureAbstracts;
    /**
     * 各家论述
     */
    private String variousDiscussions;

}
