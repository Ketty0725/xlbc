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
@TableName("typhoidtheorychineseherbcontent")
@ApiModel(value = "Typhoidtheorychineseherbcontent对象", description = "")
public class Typhoidtheorychineseherbcontent implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("伤寒论-中药详情id")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty("中药id")
    @TableField("cb_id")
    private Integer cbId;

    @ApiModelProperty("类型id")
    @TableField("type_id")
    private Integer typeId;

    @ApiModelProperty("内容")
    @TableField("content")
    private String content;


}
