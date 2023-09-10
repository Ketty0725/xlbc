package com.xlbc.app.domain;

import com.baomidou.mybatisplus.annotation.*;
import com.xlbc.common.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.List;


/**
 * 中药数据对象 chineseherb
 *
 * @author ketty
 * @date 2023-03-20
 */
@Data
@TableName("chineseherb")
public class Chineseherb implements Serializable {

    private static final long serialVersionUID=1L;

    @TableField (exist = false)
    Chineseherbimage images;

    /**
     * 中药id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    /**
     * 中药分类id
     */
    private Long categoryId;
    /**
     * 药材名称
     */
    private String medicinalName;
    /**
     * 拼音
     */
    private String pinYin;
    /**
     * 始载于
     */
    private String startedWith;
    /**
     * 所属分类
     */
    private String category;
    /**
     * 别名
     */
    private String aliases;
    /**
     * 性味归经
     */
    private String sexualTaste;
    /**
     * 功效
     */
    private String efficacy;
    /**
     * 主治
     */
    private String attending;
    /**
     * 药材简介
     */
    private String briefIntroduction;
    /**
     * 用法用量
     */
    private String usageAndDosage;
    /**
     * 注意事项
     */
    private String notes;
    /**
     * 歌诀
     */
    private String songTips;
    /**
     * 临床应用
     */
    private String clinicalApplication;
    /**
     * 相关配伍
     */
    private String relatedMatching;
    /**
     * 现代炮制
     */
    private String modernConcocted;
    /**
     * 古法炮制
     */
    private String ancientConcocted;
    /**
     * 性状
     */
    private String characters;
    /**
     * 鉴别
     */
    private String differential;
    /**
     * 各家论述
     */
    private String treatise;
    /**
     * 药籍摘要
     */
    private String summary;
    /**
     * 分布区域
     */
    private String distributionArea;
    /**
     * 道地产区
     */
    private String daoRealEstateDistrict;
    /**
     * 生长环境
     */
    private String growingEnvironment;
    /**
     * 化学成分
     */
    private String chemicalComposition;
    /**
     * 药理作用
     */
    private String pharmacologicalAction;

}
