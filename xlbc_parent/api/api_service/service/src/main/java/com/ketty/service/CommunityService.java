package com.ketty.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.ketty.api_entity.*;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author ketty
 * @since 2023-03-09
 */
public interface CommunityService extends IService<Community> {

    void add(List<MultipartFile> imgList, Community community);

    Community getById(Long id);

    Community getByIdIsPart(Long id);

    IPage<Community> selectAllPage(Long current);

    IPage<Community> selectPageByUser(Long current, Long userId);

    IPage<Community> selectPageByConcern(Long current, Long userId);

    IPage<Community> selectPageByIp(Long current, Long userId);

    IPage<Community> selectPageByTitle(Long current, String title);

    void moveToRecycled(Long id);

    void retrievalFromRecycled(Long id);

    List<Community> getFromRecycled(Long userId);

    void deleteOfCompletely(Long id);

    void clearRecycled(Long userId);

    void updateLikedState(Likestatistic entity);

    void updateLikedCount(LikedCountDTO dto);

    int getIsLikedByUserId(Long userId, Long noteId);

    void updateCollectState(Collectstatistic entity);

    void updateCollectCount(CollectCountDTO dto);

    int getIsCollectByUserId(Long userId, Long noteId);

    List<Community> getBeLikedList(Long userId);

    List<Community> getBeCollectList(Long userId);

    void deleteAllByUser(Long userId);

}
