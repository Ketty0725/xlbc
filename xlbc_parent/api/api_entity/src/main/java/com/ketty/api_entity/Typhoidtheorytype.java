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
@TableName("typhoidtheorytype")
@ApiModel(value = "Typhoidtheorytype对象", description = "")
public class Typhoidtheorytype implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("类别id")
    @TableId(value = "type_id", type = IdType.AUTO)
    private Integer typeId;

    @ApiModelProperty("类别名称")
    @TableField("type_name")
    private String typeName;


}
