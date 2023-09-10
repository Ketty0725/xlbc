package com.ketty.api_entity;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 
 * </p>
 *
 * @author ketty
 * @since 2023-01-05
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="Chineseherb对象", description="")
public class ChineseherbAndImages implements Serializable {

    private static final long serialVersionUID=1L;

    private Chineseherb chineseherb;

    private List<String> images;

}
