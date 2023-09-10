package com.ketty.api_entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
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
 * @since 2023-01-15
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("chineseherb")
@ApiModel(value = "Chineseherb对象", description = "")
public class Chineseherb implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("中药id")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty("中药分类id")
    @TableField("category_id")
    private Integer categoryId;

    @ApiModelProperty("药材名称")
    @TableField("medicinal_name")
    private String medicinalName;

    @ApiModelProperty("拼音")
    @TableField("pin_yin")
    private String pinYin;

    @ApiModelProperty("始载于")
    @TableField("started_with")
    private String startedWith;

    @ApiModelProperty("所属分类")
    @TableField("category")
    private String category;

    @ApiModelProperty("别名")
    @TableField("aliases")
    private String aliases;

    @ApiModelProperty("性味归经")
    @TableField("sexual_taste")
    private String sexualTaste;

    @ApiModelProperty("功效")
    @TableField("efficacy")
    private String efficacy;

    @ApiModelProperty("主治")
    @TableField("attending")
    private String attending;

    @ApiModelProperty("药材简介")
    @TableField("brief_introduction")
    private String briefIntroduction;

    @ApiModelProperty("用法用量")
    @TableField("usage_and_dosage")
    private String usageAndDosage;

    @ApiModelProperty("注意事项")
    @TableField("notes")
    private String notes;

    @ApiModelProperty("歌诀")
    @TableField("song_tips")
    private String songTips;

    @ApiModelProperty("临床应用")
    @TableField("clinical_application")
    private String clinicalApplication;

    @ApiModelProperty("相关配伍")
    @TableField("related_matching")
    private String relatedMatching;

    @ApiModelProperty("现代炮制")
    @TableField("modern_concocted")
    private String modernConcocted;

    @ApiModelProperty("古法炮制")
    @TableField("ancient_concocted")
    private String ancientConcocted;

    @ApiModelProperty("性状")
    @TableField("characters")
    private String characters;

    @ApiModelProperty("鉴别")
    @TableField("differential")
    private String differential;

    @ApiModelProperty("各家论述")
    @TableField("treatise")
    private String treatise;

    @ApiModelProperty("药籍摘要")
    @TableField("summary")
    private String summary;

    @ApiModelProperty("分布区域")
    @TableField("distribution_area")
    private String distributionArea;

    @ApiModelProperty("道地产区")
    @TableField("dao_real_estate_district")
    private String daoRealEstateDistrict;

    @ApiModelProperty("生长环境")
    @TableField("growing_environment")
    private String growingEnvironment;

    @ApiModelProperty("化学成分")
    @TableField("chemical_composition")
    private String chemicalComposition;

    @ApiModelProperty("药理作用")
    @TableField("pharmacological_action")
    private String pharmacologicalAction;


}
