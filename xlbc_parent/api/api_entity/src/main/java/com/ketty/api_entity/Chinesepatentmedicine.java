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
@TableName("chinesepatentmedicine")
@ApiModel(value = "Chinesepatentmedicine对象", description = "")
public class Chinesepatentmedicine implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("中成药id")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty("药名")
    @TableField("drug_name")
    private String drugName;

    @ApiModelProperty("处方")
    @TableField("prescription")
    private String prescription;

    @ApiModelProperty("制法")
    @TableField("preparation_method")
    private String preparationMethod;

    @ApiModelProperty("主治")
    @TableField("attending")
    private String attending;

    @ApiModelProperty("性状")
    @TableField("characters")
    private String characters;

    @ApiModelProperty("用法与用量")
    @TableField("usage_and_dosage")
    private String usageAndDosage;

    @ApiModelProperty("注意事项")
    @TableField("notes")
    private String notes;

    @ApiModelProperty("规格")
    @TableField("specification")
    private String specification;

    @ApiModelProperty("贮藏")
    @TableField("store_up")
    private String storeUp;


}
