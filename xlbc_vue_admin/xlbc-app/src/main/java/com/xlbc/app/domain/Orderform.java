package com.xlbc.app.domain;

import com.baomidou.mybatisplus.annotation.*;
import com.xlbc.system.domain.vo.SysOssVo;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * 订单管理对象 orderform
 *
 * @author ketty
 * @date 2023-04-05
 */
@Data
@TableName("orderform")
public class Orderform implements Serializable {

    private static final long serialVersionUID=1L;

    @TableField (exist = false)
    private Product product;

    @TableField (exist = false)
    private User user;

    @TableField (exist = false)
    private Shoppingaddress address;

    /**
     * 订单编号
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;
    /**
     * 用户id
     */
    private Long userId;
    /**
     * 商品id
     */
    private Long productId;
    /**
     * 地址id
     */
    private Long addressId;
    /**
     * 数量
     */
    private Long number;
    /**
     * 总金额
     */
    private BigDecimal price;
    /**
     * 状态
     */
    private Long state;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 修改时间
     */
    private Date updateTime;
    /**
     * 发货时间
     */
    private Date shipmentTime;
    /**
     * 成交时间
     */
    private Date finishTime;

}
