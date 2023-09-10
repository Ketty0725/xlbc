package com.ketty.serviceimpl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.ketty.api_entity.Communityimage;
import com.ketty.api_mapper.CommunityimageMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ketty.service.CommunityimageService;
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
 * @since 2023-01-15
 */
@Service
public class CommunityimageServiceImpl extends ServiceImpl<CommunityimageMapper, Communityimage> implements CommunityimageService {
    @Autowired
    CommunityimageMapper mapper;

    @Override
    public void add(Communityimage communityimage) {
        mapper.insert(communityimage);
    }

    @Override
    public Communityimage getFirstImage(Long noteId) {
        List<Communityimage> list = mapper.selectList(new LambdaQueryWrapper<Communityimage>()
                .eq(Communityimage::getNoteId,noteId)
                        .last("limit 1"));
        return list.get(0);
    }

    @Override
    public List<Communityimage> selectListByNoteId(Long noteId) {
        List<Communityimage> list = mapper.selectList(new LambdaQueryWrapper<Communityimage>()
                .eq(Communityimage::getNoteId,noteId));
        return list;
    }

    @Override
    public void delete(Long noteId) {
        mapper.delete(new LambdaUpdateWrapper<Communityimage>()
                .eq(Communityimage::getNoteId,noteId));
    }
}
