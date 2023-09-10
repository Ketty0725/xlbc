package com.ketty.service;

import com.ketty.api_entity.*;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author ketty
 * @since 2023-01-31
 */
public interface CommentService extends IService<Comment> {

    String add(Comment comment);

    CommentPagerEnable get(Long noteId, Long currentPage);

    void updateLikedState(Likestatistic entity);

    void updateLikedCount(LikedCountDTO dto);

    Long getTotalCount(Long noteId);

    void deleteById(Long id, Long userId);

    void deleteByNote(Long noteId);
}
