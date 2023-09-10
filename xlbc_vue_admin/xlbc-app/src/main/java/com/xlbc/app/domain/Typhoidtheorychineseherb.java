package com.xlbc.app.domain;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.List;


/**
 * 中药对象 typhoidtheorychineseherb
 *
 * @author ketty
 * @date 2023-04-02
 */
@Data
@TableName("typhoidtheorychineseherb")
public class Typhoidtheorychineseherb implements Serializable {

    private static final long serialVersionUID=1L;

    /**
     * 原文摘要
     */
    @TableField (exist = false)
    private List<Typhoidtheorychineseherbcontent> abstracts;

    /**
     * 引用
     */
    @TableField (exist = false)
    private List<Typhoidtheorychineseherbcontent>  cites;

    /**
     * 中药id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    /**
     * 中药名
     */
    private String name;

}
