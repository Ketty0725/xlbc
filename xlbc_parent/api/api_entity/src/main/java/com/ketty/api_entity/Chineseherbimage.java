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
@TableName("chineseherbimage")
@ApiModel(value = "Chineseherbimage对象", description = "")
public class Chineseherbimage implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("中药图片id")
    @TableId(value = "img_id", type = IdType.AUTO)
    private Integer imgId;

    @ApiModelProperty("oss图片id集合")
    @TableField("oss_id_list")
    private String ossIdList;

    @ApiModelProperty("中药id")
    @TableField("cb_id")
    private Integer cbId;


}
