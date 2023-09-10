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
@TableName("productbasicinfo")
@ApiModel(value = "Productbasicinfo对象", description = "")
public class Productbasicinfo implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("商品基本信息id")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty("商品id")
    @TableField("product_id")
    private Long productId;

    @ApiModelProperty("通用名")
    @TableField("common_name")
    private String commonName;

    @ApiModelProperty("品牌")
    @TableField("brand")
    private String brand;

    @ApiModelProperty("生产企业")
    @TableField("company")
    private String company;

    @ApiModelProperty("批准文号")
    @TableField("approval_number")
    private String approvalNumber;

    @ApiModelProperty("包装规格")
    @TableField("specification")
    private String specification;

    @ApiModelProperty("产品剂型")
    @TableField("dosage_form")
    private String dosageForm;

    @ApiModelProperty("用法")
    @TableField("use_method")
    private String useMethod;

    @ApiModelProperty("使用剂量")
    @TableField("use_dose")
    private String useDose;

    @ApiModelProperty("有效期")
    @TableField("expiration_date")
    private String expirationDate;

    @ApiModelProperty("适用人群")
    @TableField("target_user")
    private String targetUser;

    @ApiModelProperty("适用人群")
    @TableField("instruction_book")
    private String instructionBook;


}
