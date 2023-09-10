package com.ketty.api_mapper;

import com.ketty.api_entity.Comment;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author ketty
 * @since 2023-01-31
 */
@Mapper
public interface CommentMapper extends BaseMapper<Comment> {

}
