package com.ketty.serviceimpl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.ketty.api_entity.*;
import com.ketty.api_mapper.TyphoidtheorychineseherbMapper;
import com.ketty.api_mapper.TyphoidtheorychineseherbcontentMapper;
import com.ketty.api_mapper.TyphoidtheorytypeMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ketty.service.TyphoidtheorychineseherbcontentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author ketty
 * @since 2023-01-16
 */
@Service
public class TyphoidtheorychineseherbcontentServiceImpl extends ServiceImpl<TyphoidtheorychineseherbcontentMapper, Typhoidtheorychineseherbcontent> implements TyphoidtheorychineseherbcontentService {
    @Autowired
    TyphoidtheorychineseherbcontentMapper mapper;
    @Autowired
    TyphoidtheorychineseherbMapper typhoidtheorychineseherbMapper;
    @Autowired
    TyphoidtheorytypeMapper typeMapper;

    @Override
    public List<Typhoidtheorychineseherbcontent> getByNameAndType(String name, String typeName) {
        Typhoidtheorychineseherb typhoidtheorychineseherb = typhoidtheorychineseherbMapper.selectOne(
                new LambdaQueryWrapper<Typhoidtheorychineseherb>()
                        .select(Typhoidtheorychineseherb::getId)
                        .eq(Typhoidtheorychineseherb::getName,name));
        Typhoidtheorytype type = typeMapper.selectOne(
                new LambdaQueryWrapper<Typhoidtheorytype>()
                        .select(Typhoidtheorytype::getTypeId)
                        .eq(Typhoidtheorytype::getTypeName,typeName));
        if (typhoidtheorychineseherb != null && type != null) {
            List<Typhoidtheorychineseherbcontent> list = mapper.selectList(
                    new LambdaQueryWrapper<Typhoidtheorychineseherbcontent>()
                            .eq(Typhoidtheorychineseherbcontent::getCbId,typhoidtheorychineseherb.getId())
                            .eq(Typhoidtheorychineseherbcontent::getTypeId,type.getTypeId()));
            if (list.size() > 0) {
                return list;
            }
        }
        return null;
    }
}
