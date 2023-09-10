package com.ketty.serviceimpl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ketty.api_entity.*;
import com.ketty.api_entity.enums.LikedTypeEnum;
import com.ketty.api_mapper.ReplyMapper;
import com.ketty.api_mapper.UserMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ketty.common_utils.IDKeyUtil;
import com.ketty.common_utils.SnowFlakeUtil;
import com.ketty.service.LikestatisticService;
import com.ketty.service.RedisService;
import com.ketty.service.ReplyService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
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
public class ReplyServiceImpl extends ServiceImpl<ReplyMapper, Reply> implements ReplyService {
    @Autowired
    ReplyMapper replyMapper;
    @Autowired
    UserMapper userMapper;
    @Autowired
    LikestatisticService likestatisticService;
    @Autowired
    RedisService redisService;

    @Override
    public String add(Reply reply) {
        if (reply != null) {
            reply.setId(SnowFlakeUtil.getId());
            replyMapper.insert(reply);
            return String.valueOf(reply.getId());
        }
        return null;
    }

    @Override
    public CommentResult get(Long fatherId, Long currentPage) {
        Page<Reply> replyPage = new Page<>(currentPage, 2);
        LambdaQueryWrapper<Reply> wrapper = new LambdaQueryWrapper<Reply>()
                .eq(Reply::getFatherId,fatherId);
        IPage<Reply> replyIPage = replyMapper.selectPage(replyPage, wrapper);
        List<ReplyResult> replies = new ArrayList<>();
        for (Reply reply : replyIPage.getRecords()) {
            ReplyResult replyResult = new ReplyResult();
            String value = redisService.getLikedCountFromRedis(reply.getId(),LikedTypeEnum.REPLY.getCode());
            if (value != null) {
                reply.setLikeCount(reply.getLikeCount() + Long.parseLong(value));
            }
            BeanUtils.copyProperties(reply,replyResult);
            User user = userMapper.selectById(reply.getUserId());
            replyResult.setUserName(user.getUName());
            replyResult.setHeadImg(user.getUHeadicon());
            if (reply.getReplyUserId() != 0) {
                user = userMapper.selectById(reply.getReplyUserId());
                replyResult.setReplyUserName(user.getUName());
            }
            int likeState = likestatisticService.getState(reply.getUserId(),reply.getId(),LikedTypeEnum.REPLY.getCode());
            replyResult.setIsLike(likeState);
            replies.add(replyResult);
        }
        CommentResult commentResult = new CommentResult();
        commentResult.setReplies(replies);
        commentResult.setCurrentPage(replyIPage.getCurrent());
        commentResult.setPageSize(replyIPage.getSize());
        commentResult.setTotalPages(replyIPage.getPages());
        commentResult.setTotalDataSize(replyIPage.getTotal());
        return commentResult;
    }

    @Override
    public void updateLikedState(Likestatistic entity) {
        likestatisticService.saveToRedis(entity.getUserId(),entity.getBeLikedId(),LikedTypeEnum.REPLY.getCode());
    }

    @Override
    public void updateLikedCount(LikedCountDTO dto) {
        replyMapper.update(null, new LambdaUpdateWrapper<Reply>()
                .eq(Reply::getId,dto.getBeLikedId())
                .setSql("like_count = like_count + " + dto.getCount()));
    }

    @Override
    public void deleteById(Long id, Long userId) {
        replyMapper.deleteById(id);
        likestatisticService.delete(userId, id, LikedTypeEnum.REPLY.getCode());
    }

    @Override
    public void deleteByFatherId(Long fatherId) {
        List<Reply> list = replyMapper.selectList(
                new LambdaQueryWrapper<Reply>()
                        .select(Reply::getId)
                        .eq(Reply::getFatherId,fatherId));
        if (list.size() > 0) {
            for (Reply reply : list) {
                replyMapper.deleteById(reply.getId());
                likestatisticService.deleteBatch(reply.getId(), LikedTypeEnum.REPLY.getCode());
            }
        }
    }

    @Override
    public void deleteByFatherId(Long fatherId, Long userId) {
        List<Reply> list = replyMapper.selectList(
                new LambdaQueryWrapper<Reply>()
                        .select(Reply::getId)
                        .eq(Reply::getFatherId,fatherId));
        if (list.size() > 0) {
            for (Reply reply : list) {
                replyMapper.deleteById(reply.getId());
                likestatisticService.delete(userId, reply.getId(), LikedTypeEnum.REPLY.getCode());
            }
        }
    }

    @Override
    public boolean isExists(Long fatherId) {
        boolean isExists = replyMapper.exists(new LambdaQueryWrapper<Reply>()
                .eq(Reply::getFatherId, fatherId));
        return isExists;
    }

}
