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
@TableName("communityimage")
@ApiModel(value = "Communityimage对象", description = "")
public class Communityimage implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("图片id")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty("笔记id")
    @TableField("note_id")
    private Long noteId;

    @ApiModelProperty("图片地址")
    @TableField("image_url")
    private String imageUrl;


}
