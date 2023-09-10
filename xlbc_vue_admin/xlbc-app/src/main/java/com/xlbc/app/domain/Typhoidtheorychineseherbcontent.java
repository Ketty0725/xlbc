package com.xlbc.app.domain;

import com.baomidou.mybatisplus.annotation.*;
import com.xlbc.common.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.List;


/**
 * 中药内容对象 typhoidtheorychineseherbcontent
 *
 * @author ketty
 * @date 2023-04-02
 */
@Data
@TableName("typhoidtheorychineseherbcontent")
public class Typhoidtheorychineseherbcontent implements Serializable {

    private static final long serialVersionUID=1L;

    /**
     * 伤寒论-中药详情id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    /**
     * 中药id
     */
    private Long cbId;
    /**
     * 类型id
     */
    private Long typeId;
    /**
     * 内容
     */
    private String content;

}
