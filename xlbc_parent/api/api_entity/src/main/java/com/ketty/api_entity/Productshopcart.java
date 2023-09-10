package com.ketty.api_entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * <p>
 * 
 * </p>
 *
 * @author ketty
 * @since 2023-02-20
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("productshopcart")
@ApiModel(value = "Productshopcart对象", description = "")
public class Productshopcart implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("购物车id")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty("用户id")
    @TableField("user_id")
    private Long userId;

    @ApiModelProperty("商品id")
    @TableField("product_id")
    private Long productId;

    @ApiModelProperty("数量")
    @TableField("number")
    private Integer number;

    public Productshopcart() {
    }

    public Productshopcart(Long userId, Long productId, Integer number) {
        this.userId = userId;
        this.productId = productId;
        this.number = number;
    }

    @Override
    public String toString() {
        return "Productshopcart{" +
                "id=" + id +
                ", userId=" + userId +
                ", productId=" + productId +
                ", number=" + number +
                '}';
    }
}
