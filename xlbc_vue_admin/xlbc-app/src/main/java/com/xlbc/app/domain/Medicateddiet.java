package com.xlbc.app.domain;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;


/**
 * 药膳数据对象 medicateddiet
 *
 * @author ketty
 * @date 2023-04-02
 */
@Data
@TableName("medicateddiet")
public class Medicateddiet implements Serializable {

    private static final long serialVersionUID=1L;

    /**
     * 药膳id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    /**
     * 药膳名称
     */
    private String medicatedDietName;
    /**
     * 出处
     */
    private String derivation;
    /**
     * 食材
     */
    private String foodMaterial;
    /**
     * 制法
     */
    private String preparationMethod;
    /**
     * 功效
     */
    private String efficacy;

}
