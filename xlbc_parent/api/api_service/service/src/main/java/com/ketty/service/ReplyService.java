package com.ketty.service;

import com.ketty.api_entity.CommentResult;
import com.ketty.api_entity.LikedCountDTO;
import com.ketty.api_entity.Likestatistic;
import com.ketty.api_entity.Reply;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author ketty
 * @since 2023-01-31
 */
public interface ReplyService extends IService<Reply> {

    String add(Reply reply);

    CommentResult get(Long fatherId, Long currentPage);

    void updateLikedState(Likestatistic entity);

    void updateLikedCount(LikedCountDTO dto);

    void deleteById(Long id, Long userId);

    void deleteByFatherId(Long fatherId);

    void deleteByFatherId(Long fatherId, Long userId);

    boolean isExists(Long fatherId);
}
