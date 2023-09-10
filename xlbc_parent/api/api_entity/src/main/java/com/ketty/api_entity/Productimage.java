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
 * @since 2023-02-17
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("productimage")
@ApiModel(value = "Productimage对象", description = "")
public class Productimage implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("商品图片id")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty("商品id")
    @TableField("product_id")
    private Long productId;

    @ApiModelProperty("oss图片id集合")
    @TableField("oss_id_list")
    private String ossIdList;


}
