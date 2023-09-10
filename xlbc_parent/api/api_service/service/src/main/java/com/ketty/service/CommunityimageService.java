package com.ketty.service;

import com.ketty.api_entity.Communityimage;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author ketty
 * @since 2023-01-15
 */
public interface CommunityimageService extends IService<Communityimage> {
    void add(Communityimage communityimage);

    Communityimage getFirstImage(Long noteId);

    List<Communityimage> selectListByNoteId(Long noteId);

    void delete(Long noteId);
}
