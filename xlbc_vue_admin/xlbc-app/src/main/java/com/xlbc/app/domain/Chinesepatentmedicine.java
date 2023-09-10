package com.xlbc.app.domain;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;


/**
 * 中成药数据对象 chinesepatentmedicine
 *
 * @author ketty
 * @date 2023-04-02
 */
@Data
@TableName("chinesepatentmedicine")
public class Chinesepatentmedicine implements Serializable {

    private static final long serialVersionUID=1L;

    /**
     * 中成药id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    /**
     * 药名
     */
    private String drugName;
    /**
     * 处方
     */
    private String prescription;
    /**
     * 制法
     */
    private String preparationMethod;
    /**
     * 主治
     */
    private String attending;
    /**
     * 性状
     */
    private String characters;
    /**
     * 用法与用量
     */
    private String usageAndDosage;
    /**
     * 注意事项
     */
    private String notes;
    /**
     * 规格
     */
    private String specification;
    /**
     * 贮藏
     */
    private String storeUp;

}
