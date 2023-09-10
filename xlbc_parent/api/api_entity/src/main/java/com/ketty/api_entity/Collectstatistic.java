package com.ketty.api_entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;

import com.ketty.api_entity.enums.CollectStateEnum;
import com.ketty.api_entity.enums.LikedStateEnum;
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
 * @since 2023-02-27
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("collectstatistic")
@ApiModel(value = "Collectstatistic对象", description = "")
public class Collectstatistic implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("收藏id")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty("用户id")
    @TableField("user_id")
    private Long userId;

    @ApiModelProperty("被收藏对象id")
    @TableField("be_collect_id")
    private Long beCollectId;

    @ApiModelProperty("类型")
    @TableField("type")
    private Integer type;

    @ApiModelProperty("收藏状态，0取消，1收藏")
    @TableField("state")
    private Integer state = CollectStateEnum.COLLECT.getCode();

    public Collectstatistic() {
    }

    public Collectstatistic(Long userId, Long beCollectId, Integer type, Integer state) {
        this.userId = userId;
        this.beCollectId = beCollectId;
        this.type = type;
        this.state = state;
    }
}
