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
@TableName("prescription")
@ApiModel(value = "Prescription对象", description = "")
public class Prescription implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("方剂id")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty("方剂分类id")
    @TableField("category_id")
    private Integer categoryId;

    @ApiModelProperty("方剂名称")
    @TableField("prescription_name")
    private String prescriptionName;

    @ApiModelProperty("出处")
    @TableField("provenance")
    private String provenance;

    @ApiModelProperty("所属分类")
    @TableField("category")
    private String category;

    @ApiModelProperty("功用")
    @TableField("efficacy")
    private String efficacy;

    @ApiModelProperty("组成")
    @TableField("compose")
    private String compose;

    @ApiModelProperty("用法")
    @TableField("usage_method")
    private String usageMethod;

    @ApiModelProperty("主治")
    @TableField("attending")
    private String attending;

    @ApiModelProperty("使用注意")
    @TableField("notes")
    private String notes;

    @ApiModelProperty("歌诀")
    @TableField("song_tips")
    private String songTips;

    @ApiModelProperty("方义")
    @TableField("fang_yi")
    private String fangYi;

    @ApiModelProperty("配伍特点")
    @TableField("matching_features")
    private String matchingFeatures;

    @ApiModelProperty("运用")
    @TableField("wield")
    private String wield;

    @ApiModelProperty("加减化裁")
    @TableField("addition_and_subtraction")
    private String additionAndSubtraction;

    @ApiModelProperty("化裁方之间的鉴别")
    @TableField("tailoring_identification")
    private String tailoringIdentification;

    @ApiModelProperty("文献摘要")
    @TableField("literature_abstracts")
    private String literatureAbstracts;

    @ApiModelProperty("各家论述")
    @TableField("various_discussions")
    private String variousDiscussions;


}
