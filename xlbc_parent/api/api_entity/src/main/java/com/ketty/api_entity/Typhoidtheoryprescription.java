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
@TableName("typhoidtheoryprescription")
@ApiModel(value = "Typhoidtheoryprescription对象", description = "")
public class Typhoidtheoryprescription implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("经方id")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty("经方名")
    @TableField("name")
    private String name;

    @ApiModelProperty("类型id")
    @TableField("type")
    private Integer type;

    @ApiModelProperty("经方组成")
    @TableField("compose")
    private String compose;

    @ApiModelProperty("炮制方法")
    @TableField("preparation_method")
    private String preparationMethod;


}
