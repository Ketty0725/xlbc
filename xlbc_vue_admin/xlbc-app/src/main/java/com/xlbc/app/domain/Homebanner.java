package com.xlbc.app.domain;

import com.baomidou.mybatisplus.annotation.*;
import com.xlbc.common.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;


/**
 * 轮播图对象 homebanner
 *
 * @author ketty
 * @date 2023-04-04
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("homebanner")
public class Homebanner extends BaseEntity {

    private static final long serialVersionUID=1L;

    /**
     * 首页轮播图id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    /**
     * oss图片id
     */
    private String ossId;
    /**
     * 显示顺序
     */
    private Integer orderNum;

    @TableField (exist = false)
    private String image;

}
