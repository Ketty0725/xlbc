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
@TableName("typhoidtheoryprescriptionworkcited")
@ApiModel(value = "Typhoidtheoryprescriptionworkcited对象", description = "")
public class Typhoidtheoryprescriptionworkcited implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("相关条文id")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty("相关条文内容")
    @TableField("content")
    private String content;

    @ApiModelProperty("相关条文出处")
    @TableField("provenance")
    private String provenance;

    @ApiModelProperty("类型id")
    @TableField("type")
    private Integer type;

    @ApiModelProperty("经方id")
    @TableField("preparation_id")
    private Integer preparationId;


}
