package com.xlbc.app.domain;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;


/**
 * 相关条文对象 typhoidtheoryprescriptionworkcited
 *
 * @author ketty
 * @date 2023-04-02
 */
@Data
@TableName("typhoidtheoryprescriptionworkcited")
public class Typhoidtheoryprescriptionworkcited implements Serializable {

    private static final long serialVersionUID=1L;

    /**
     * 相关条文id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    /**
     * 相关条文内容
     */
    private String content;
    /**
     * 相关条文出处
     */
    private String provenance;
    /**
     * 类型id
     */
    private Long type;
    /**
     * 经方id
     */
    private Long preparationId;

}
