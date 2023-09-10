package com.xlbc.app.domain;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;


/**
 * 收货地址对象 shoppingaddress
 *
 * @author ketty
 * @date 2023-04-08
 */
@Data
@TableName("shoppingaddress")
public class Shoppingaddress implements Serializable {

    private static final long serialVersionUID=1L;

    /**
     * 收货地址id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    /**
     * 用户id
     */
    private Long userId;
    /**
     * 收货人
     */
    private String name;
    /**
     * 联系电话
     */
    private String phone;
    /**
     * 所在地区
     */
    private String area;
    /**
     * 详细地址
     */
    private String address;
    /**
     * 是否为默认地址
     */
    private Long isDefault;

}
