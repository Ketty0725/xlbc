package com.ketty.api_mapper;

import com.ketty.api_entity.Product;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author ketty
 * @since 2023-02-14
 */
@Mapper
public interface ProductMapper extends BaseMapper<Product> {

}
