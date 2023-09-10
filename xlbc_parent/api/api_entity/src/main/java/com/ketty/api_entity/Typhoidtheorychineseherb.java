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
 * @since 2023-01-16
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("typhoidtheorychineseherb")
@ApiModel(value = "Typhoidtheorychineseherb对象", description = "")
public class Typhoidtheorychineseherb implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("中药id")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty("中药名")
    @TableField("name")
    private String name;


}
