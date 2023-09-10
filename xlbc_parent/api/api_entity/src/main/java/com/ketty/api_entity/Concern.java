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
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * <p>
 * 
 * </p>
 *
 * @author ketty
 * @since 2023-03-06
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("concern")
@ApiModel(value = "Concern对象", description = "")
public class Concern implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("关注列表id")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty("关注者id")
    @TableField("concern_id")
    private Long concernId;

    @ApiModelProperty("被关注者id")
    @TableField("be_concerned_id")
    private Long beConcernedId;

    @ApiModelProperty("创建时间")
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private Date createTime;

    public Concern() {
    }

    public Concern(Long concernId, Long beConcernedId) {
        this.concernId = concernId;
        this.beConcernedId = beConcernedId;
    }
}
