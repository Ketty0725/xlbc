package com.ketty.serviceimpl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ketty.api_entity.*;
import com.ketty.api_entity.enums.LikedTypeEnum;
import com.ketty.api_mapper.CommentMapper;
import com.ketty.api_mapper.UserMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ketty.common_utils.IDKeyUtil;
import com.ketty.common_utils.SnowFlakeUtil;
import com.ketty.service.CommentService;
import com.ketty.service.LikestatisticService;
import com.ketty.service.RedisService;
import com.ketty.service.ReplyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author ketty
 * @since 2023-01-31
 */
@Service
public class CommentServiceImpl extends ServiceImpl<CommentMapper, Comment> implements CommentService {
    @Autowired
    CommentMapper commentMapper;
    @Autowired
    ReplyService replyService;
    @Autowired
    UserMapper userMapper;
    @Autowired
    LikestatisticService likestatisticService;
    @Autowired
    RedisService redisService;

    @Override
    public String add(Comment comment) {
        if (comment != null) {
            comment.setId(SnowFlakeUtil.getId());
            commentMapper.insert(comment);
            return String.valueOf(comment.getId());
        }
        return null;
    }

    @Override
    public CommentPagerEnable get(Long noteId, Long currentPage) {
        Page<Comment> commentPage = new Page<>(currentPage, 10);
        IPage<Comment> commentIPage = commentMapper.selectPage(commentPage,
                new LambdaQueryWrapper<Comment>()
                        .eq(Comment::getNoteId,noteId)
                        .orderByDesc(Comment::getCreateTime));

        Long num = 0L;
        List<CommentResult> comments = new ArrayList<>();
        for (Comment c : commentIPage.getRecords()) {
            String value = redisService.getLikedCountFromRedis(c.getId(),LikedTypeEnum.COMMENT.getCode());
            if (value != null) {
                c.setLikeCount(c.getLikeCount() + Long.parseLong(value));
            }
            CommentResult commentResult = replyService.get(c.getId(), 1L);
            commentResult.setId(c.getId());
            commentResult.setUserId(c.getUserId());
            commentResult.setNoteId(c.getNoteId());
            commentResult.setContent(c.getContent());
            commentResult.setCreateTime(c.getCreateTime());
            commentResult.setIpAddress(c.getIpAddress());
            commentResult.setLikeCount(c.getLikeCount());

            User user = userMapper.selectById(c.getUserId());
            commentResult.setUserName(user.getUName());
            commentResult.setHeadImg(user.getUHeadicon());
            int likeState = likestatisticService.getState(c.getUserId(),c.getId(),LikedTypeEnum.COMMENT.getCode());
            commentResult.setIsLike(likeState);
            num += commentResult.getTotalDataSize();
            comments.add(commentResult);
        }

        CommentPagerEnable pagerEnable = new CommentPagerEnable();
        pagerEnable.setCurrentPage(commentIPage.getCurrent());
        pagerEnable.setPageSize(commentIPage.getSize());
        pagerEnable.setTotalPages(commentIPage.getPages());
        pagerEnable.setTotalDataSize(commentIPage.getTotal());
        pagerEnable.setComments(comments);
        pagerEnable.setTotalAllSize(num);
        return pagerEnable;
    }

    @Override
    public void updateLikedState(Likestatistic entity) {
        likestatisticService.saveToRedis(entity.getUserId(),entity.getBeLikedId(),LikedTypeEnum.COMMENT.getCode());
    }

    @Override
    public void updateLikedCount(LikedCountDTO dto) {
        commentMapper.update(null, new LambdaUpdateWrapper<Comment>()
                .eq(Comment::getId,dto.getBeLikedId())
                .setSql("like_count = like_count + " + dto.getCount()));
    }

    @Override
    public Long getTotalCount(Long noteId) {
        List<Comment> comments = commentMapper.selectList(new LambdaQueryWrapper<Comment>()
                        .select(Comment::getId)
                .eq(Comment::getNoteId,noteId));
        long totalCount = (long) comments.size();
        for (Comment comment : comments) {
            totalCount += replyService.count(new LambdaQueryWrapper<Reply>()
                    .eq(Reply::getFatherId,comment.getId()));
        }
        return totalCount;
    }

    @Override
    public void deleteById(Long id, Long userId) {
        commentMapper.deleteById(id);
        replyService.deleteByFatherId(id, userId);
        likestatisticService.delete(userId, id, LikedTypeEnum.COMMENT.getCode());
    }

    @Override
    public void deleteByNote(Long noteId) {
        List<Comment> list = commentMapper.selectList(new LambdaQueryWrapper<Comment>()
                        .select(Comment::getId)
                .eq(Comment::getNoteId,noteId));
        for (Comment comment: list) {
            commentMapper.deleteById(comment.getId());
            replyService.deleteByFatherId(comment.getId());
            likestatisticService.deleteBatch(comment.getId(), LikedTypeEnum.COMMENT.getCode());
        }
    }

}
