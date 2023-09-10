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
@TableName("medicateddiet")
@ApiModel(value = "Medicateddiet对象", description = "")
public class Medicateddiet implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("药膳id")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty("药膳名称")
    @TableField("medicated_diet_name")
    private String medicatedDietName;

    @ApiModelProperty("出处")
    @TableField("derivation")
    private String derivation;

    @ApiModelProperty("食材")
    @TableField("food_material")
    private String foodMaterial;

    @ApiModelProperty("制法")
    @TableField("preparation_method")
    private String preparationMethod;

    @ApiModelProperty("功效")
    @TableField("efficacy")
    private String efficacy;


}
