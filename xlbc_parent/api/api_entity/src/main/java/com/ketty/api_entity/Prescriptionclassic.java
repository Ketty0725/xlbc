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
 * @since 2023-01-19
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("prescriptionclassic")
@ApiModel(value = "Prescriptionclassic对象", description = "")
public class Prescriptionclassic implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("方剂经方id")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty("经方出处")
    @TableField("name")
    private String name;


}
