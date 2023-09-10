package com.xlbc.app.domain;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;


/**
 * 医生问答对象 productdoctorqa
 *
 * @author ketty
 * @date 2023-04-01
 */
@Data
@TableName("productdoctorqa")
public class Productdoctorqa implements Serializable {

    private static final long serialVersionUID=1L;

    /**
     * 医生问答id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    /**
     * 商品id
     */
    private Long productId;
    /**
     * 提问
     */
    private String ask;
    /**
     * 回答
     */
    private String answer;

}
