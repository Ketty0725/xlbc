package com.ketty.api_entity;

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
 * @since 2023-03-04
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("browsinghistory")
@ApiModel(value = "Browsinghistory对象", description = "")
public class Browsinghistory implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("浏览记录id")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty("用户id")
    @TableField("user_id")
    private Long userId;

    @ApiModelProperty("被浏览对象id")
    @TableField("be_browse_id")
    private Long beBrowseId;

    @ApiModelProperty("浏览时间")
    @TableField("browse_time")
    private Date browseTime;

    public Browsinghistory() {
    }

    public Browsinghistory(Long userId, Long beBrowseId, Date browseTime) {
        this.userId = userId;
        this.beBrowseId = beBrowseId;
        this.browseTime = browseTime;
    }
}
