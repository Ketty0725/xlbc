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
 * @since 2023-04-04
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("chineseherbcategoryintroduce")
@ApiModel(value = "Chineseherbcategoryintroduce对象", description = "")
public class Chineseherbcategoryintroduce implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("中药分类介绍id")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty("中药分类id")
    @TableField("category_id")
    private Integer categoryId;

    @ApiModelProperty("释义")
    @TableField("paraphrase")
    private String paraphrase;

    @ApiModelProperty("分类")
    @TableField("classify")
    private String classify;

    @ApiModelProperty("功用")
    @TableField("efficacy")
    private String efficacy;

    @ApiModelProperty("配伍特点")
    @TableField("matching_features")
    private String matchingFeatures;

    @ApiModelProperty("用药注意")
    @TableField("medication_attention")
    private String medicationAttention;

    @ApiModelProperty("现代研究")
    @TableField("modern_research")
    private String modernResearch;


}
