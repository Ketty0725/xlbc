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
 * @since 2023-02-19
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("productdoctorqa")
@ApiModel(value = "Productdoctorqa对象", description = "")
public class Productdoctorqa implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("医生问答id")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty("商品id")
    @TableField("product_id")
    private Long productId;

    @ApiModelProperty("提问")
    @TableField("ask")
    private String ask;

    @ApiModelProperty("回答")
    @TableField("answer")
    private String answer;


}
