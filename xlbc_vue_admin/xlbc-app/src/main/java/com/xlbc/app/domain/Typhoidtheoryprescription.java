package com.xlbc.app.domain;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.List;


/**
 * 经方对象 typhoidtheoryprescription
 *
 * @author ketty
 * @date 2023-04-02
 */
@Data
@TableName("typhoidtheoryprescription")
public class Typhoidtheoryprescription implements Serializable {

    private static final long serialVersionUID=1L;

    @TableField (exist = false)
    private List<Typhoidtheoryprescriptionworkcited> workCites;

    /**
     * 经方id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    /**
     * 经方名
     */
    private String name;
    /**
     * 类型id
     */
    private Long type;
    /**
     * 经方组成
     */
    private String compose;
    /**
     * 炮制方法
     */
    private String preparationMethod;

}
