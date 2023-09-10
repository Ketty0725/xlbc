package com.xlbc.app.domain;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;


/**
 * 药品说明对象 productbasicinfo
 *
 * @author ketty
 * @date 2023-03-31
 */
@Data
@TableName("productbasicinfo")
public class Productbasicinfo implements Serializable {

    private static final long serialVersionUID=1L;

    @TableField (exist = false)
    Productimage images;

    /**
     * 商品基本信息id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    /**
     * 商品id
     */
    private Long productId;
    /**
     * 通用名
     */
    private String commonName;
    /**
     * 品牌
     */
    private String brand;
    /**
     * 生产企业
     */
    private String company;
    /**
     * 批准文号
     */
    private String approvalNumber;
    /**
     * 包装规格
     */
    private String specification;
    /**
     * 产品剂型
     */
    private String dosageForm;
    /**
     * 用法
     */
    private String useMethod;
    /**
     * 使用剂量
     */
    private String useDose;
    /**
     * 有效期
     */
    private String expirationDate;
    /**
     * 适用人群
     */
    private String targetUser;
    /**
     * 详细说明书
     */
    private String instructionBook;

}
