package com.ketty.api_entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;

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
 * @since 2023-02-01
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("likestatistic")
@ApiModel(value = "Likestatistic对象", description = "")
public class Likestatistic implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("点赞id")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty("用户id")
    @TableField("user_id")
    private Long userId;

    @ApiModelProperty("被点赞对象id")
    @TableField("be_liked_id")
    private Long beLikedId;

    @ApiModelProperty("类型")
    @TableField("type")
    private Integer type;

    @ApiModelProperty("点赞状态")
    @TableField("state")
    private Integer state = LikedStateEnum.LIKE.getCode();

    public Likestatistic() {
    }

    public Likestatistic(Long userId, Long beLikedId, Integer type, Integer state) {
        this.userId = userId;
        this.beLikedId = beLikedId;
        this.type = type;
        this.state = state;
    }
}
