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
@TableName("prescriptioncategoryintroduce")
@ApiModel(value = "Prescriptioncategoryintroduce对象", description = "")
public class Prescriptioncategoryintroduce implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("方剂分类详情id")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty("方剂分类id")
    @TableField("category_id")
    private Integer categoryId;

    @ApiModelProperty("释义")
    @TableField("paraphrase")
    private String paraphrase;

    @ApiModelProperty("适应症")
    @TableField("indications")
    private String indications;

    @ApiModelProperty("注意事项")
    @TableField("notes")
    private String notes;


}
