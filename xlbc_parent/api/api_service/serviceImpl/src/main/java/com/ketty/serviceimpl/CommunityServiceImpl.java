package com.ketty.serviceimpl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ketty.api_entity.*;
import com.ketty.api_entity.enums.CollectTypeEnum;
import com.ketty.api_entity.enums.LikedTypeEnum;
import com.ketty.api_mapper.CommunityMapper;
import com.ketty.common_base.MinioUtil;
import com.ketty.common_utils.IDKeyUtil;
import com.ketty.common_utils.SnowFlakeUtil;
import com.ketty.service.*;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.checkerframework.checker.units.qual.C;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author ketty
 * @since 2023-03-09
 */
@Service
public class CommunityServiceImpl extends ServiceImpl<CommunityMapper, Community> implements CommunityService {
    @Autowired
    CommunityMapper communityMapper;
    @Autowired
    CommunityimageService communityimageService;
    @Autowired
    LikestatisticService likestatisticService;
    @Autowired
    CollectstatisticService collectstatisticService;
    @Autowired
    RedisService redisService;
    @Autowired
    MinioUtil minioUtil;
    @Autowired
    ConcernService concernService;
    @Autowired
    BrowsinghistoryService browsinghistoryService;
    @Autowired
    CommentService commentService;
    @Autowired
    UserService userService;

    @Override
    public void add(List<MultipartFile> imgList, Community community) {
        community.setId(SnowFlakeUtil.getId());
        communityMapper.insert(community);

        for (MultipartFile image : imgList) {
            String imgUrl = minioUtil.upload(image, "community");
            Communityimage communityimage = new Communityimage();
            communityimage.setNoteId(community.getId());
            communityimage.setImageUrl(imgUrl);
            communityimageService.add(communityimage);
        }
    }

    @Override
    public Community getById(Long id) {
        Community community = communityMapper.selectOne(
                new LambdaQueryWrapper<Community>()
                        .eq(Community::getId,id));
        String likeCount = redisService.getLikedCountFromRedis(community.getId(), LikedTypeEnum.NOTE.getCode());
        if (likeCount != null) {
            community.setLikeCount(community.getLikeCount() + Long.parseLong(likeCount));
        }
        String collectCount = redisService.getCollectCountFromRedis(community.getId(), CollectTypeEnum.NOTE.getCode());
        if (collectCount != null) {
            community.setCollectCount(community.getCollectCount() + Long.parseLong(collectCount));
        }
        return community;
    }

    @Override
    public Community getByIdIsPart(Long id) {
        Community community = communityMapper.selectOne(
                new LambdaQueryWrapper<Community>()
                        .select(Community::getId,Community::getTitle,Community::getUserId)
                        .eq(Community::getId,id)
                        .eq(Community::getDeleted,0));
        return community;
    }

    @Override
    public IPage<Community> selectAllPage(Long current) {
        Page<Community> page = new Page<>(current, 10);
        LambdaQueryWrapper<Community> wrapper = new LambdaQueryWrapper<Community>()
                .select(Community::getId,Community::getTitle,Community::getUserId)
                .eq(Community::getDeleted,0)
                .orderByDesc(Community::getCreateTime);
        IPage<Community> iPage = communityMapper.selectPage(page, wrapper);
        return iPage;
    }

    @Override
    public IPage<Community> selectPageByUser(Long current, Long userId) {
        Page<Community> page = new Page<>(current, 10);
        LambdaQueryWrapper<Community> wrapper = new LambdaQueryWrapper<Community>()
                .select(Community::getId,Community::getTitle,Community::getUserId)
                .eq(Community::getUserId,userId)
                .eq(Community::getDeleted,0)
                .orderByDesc(Community::getCreateTime);
        IPage<Community> iPage = communityMapper.selectPage(page, wrapper);
        return iPage;
    }

    @Override
    public IPage<Community> selectPageByConcern(Long current, Long userId) {
        List<User> users = concernService.selectConcernList(userId);
        if (users.size() > 0) {
            Page<Community> page = new Page<>(current, 10);
            LambdaQueryWrapper<Community> wrapper = new LambdaQueryWrapper<Community>();
            wrapper.select(Community::getId,Community::getTitle,Community::getUserId);
            wrapper.eq(Community::getDeleted,0);
            for (User user: users) {
                wrapper.eq(Community::getUserId,user.getUId());
                wrapper.or();
            }
            wrapper.orderByDesc(Community::getCreateTime);
            IPage<Community> iPage = communityMapper.selectPage(page, wrapper);
            return iPage;
        }
        return null;
    }

    @Override
    public IPage<Community> selectPageByIp(Long current, Long userId) {
        String ip = userService.getUserIP(userId);
        if (ip != null) {
            Page<Community> page = new Page<>(current, 10);
            LambdaQueryWrapper<Community> wrapper = new LambdaQueryWrapper<Community>();
            wrapper.select(Community::getId,Community::getTitle,Community::getUserId);
            wrapper.eq(Community::getDeleted,0);
            wrapper.eq(Community::getIpAddress,ip);
            wrapper.orderByDesc(Community::getCreateTime);
            IPage<Community> iPage = communityMapper.selectPage(page, wrapper);
            return iPage;
        }
        return null;
    }

    @Override
    public IPage<Community> selectPageByTitle(Long current, String title) {
        Page<Community> page = new Page<>(current, 10);
        LambdaQueryWrapper<Community> wrapper = new LambdaQueryWrapper<Community>()
                .select(Community::getId,Community::getTitle,Community::getUserId)
                .like(Community::getTitle,title)
                .eq(Community::getDeleted,0)
                .orderByDesc(Community::getCreateTime);
        IPage<Community> iPage = communityMapper.selectPage(page, wrapper);
        return iPage;
    }

    @Override
    public void moveToRecycled(Long id) {
        communityMapper.update(null,new LambdaUpdateWrapper<Community>()
                        .set(Community::getDeleted, -1)
                .eq(Community::getId,id));
    }

    @Override
    public void retrievalFromRecycled(Long id) {
        communityMapper.update(null,new LambdaUpdateWrapper<Community>()
                .set(Community::getDeleted, 0)
                .eq(Community::getId,id));
    }

    @Override
    public List<Community> getFromRecycled(Long userId) {
        List<Community> list = communityMapper.selectList(
                new LambdaQueryWrapper<Community>()
                        .eq(Community::getDeleted,-1)
                        .eq(Community::getUserId,userId)
                        .orderByDesc(Community::getUpdateTime));
        return list;
    }

    @Override
    public void deleteOfCompletely(Long id) {
        List<Communityimage> images = communityimageService.selectListByNoteId(id);
        if (images.size() > 0) {
            for (Communityimage image : images) {
                minioUtil.remove(image.getImageUrl());
            }
        }
        communityMapper.deleteById(id);
        communityimageService.delete(id);
        likestatisticService.deleteBatch(id, LikedTypeEnum.NOTE.getCode());
        collectstatisticService.deleteBatch(id, CollectTypeEnum.NOTE.getCode());
        browsinghistoryService.deleteAllByObject(id);
        commentService.deleteByNote(id);
    }

    @Override
    public void clearRecycled(Long userId) {
        communityMapper.delete(new LambdaUpdateWrapper<Community>()
                .eq(Community::getUserId,userId)
                .eq(Community::getDeleted, -1));
    }

    @Override
    public void updateLikedState(Likestatistic entity) {
        likestatisticService.saveToRedis(entity.getUserId(),entity.getBeLikedId(), LikedTypeEnum.NOTE.getCode());
    }

    @Override
    public void updateLikedCount(LikedCountDTO dto) {
        communityMapper.update(null, new LambdaUpdateWrapper<Community>()
                .eq(Community::getId,dto.getBeLikedId())
                .setSql("like_count = like_count + " + dto.getCount()));
    }

    @Override
    public int getIsLikedByUserId(Long userId, Long noteId) {
        int likeState = likestatisticService.getState(userId,noteId,CollectTypeEnum.NOTE.getCode());
        return likeState;
    }

    @Override
    public void updateCollectState(Collectstatistic entity) {
        collectstatisticService.saveToRedis(entity.getUserId(),entity.getBeCollectId(),CollectTypeEnum.NOTE.getCode());
    }

    @Override
    public void updateCollectCount(CollectCountDTO dto) {
        communityMapper.update(null, new LambdaUpdateWrapper<Community>()
                .eq(Community::getId,dto.getBeCollectId())
                .setSql("collect_count = collect_count + " + dto.getCount()));
    }

    @Override
    public int getIsCollectByUserId(Long userId, Long noteId) {
        int collectState = collectstatisticService.getState(userId, noteId, CollectTypeEnum.NOTE.getCode());
        return collectState;
    }

    @Override
    public List<Community> getBeLikedList(Long userId) {
        List<Likestatistic> list = likestatisticService.getBeLikedByUser(userId,LikedTypeEnum.NOTE.getCode());
        List<Community> communities = new ArrayList<>();
        for (Likestatistic like : list) {
            Community community = getByIdIsPart(like.getBeLikedId());
            if (community != null) {
                communities.add(community);
            }
        }
        return communities;
    }

    @Override
    public List<Community> getBeCollectList(Long userId) {
        List<Collectstatistic> list = collectstatisticService.getBeCollectByUser(userId,CollectTypeEnum.NOTE.getCode());
        List<Community> communities = new ArrayList<>();
        for (Collectstatistic collect : list) {
            Community community = getByIdIsPart(collect.getBeCollectId());
            if (community != null) {
                communities.add(community);
            }
        }
        return communities;
    }

    @Override
    public void deleteAllByUser(Long userId) {
        List<Community> list = communityMapper.selectList(
                new LambdaQueryWrapper<Community>()
                        .select(Community::getId)
                        .eq(Community::getUserId,userId));
        if (list.size() > 0) {
            for (Community community : list) {
                deleteOfCompletely(community.getId());
            }
        }
    }

}
