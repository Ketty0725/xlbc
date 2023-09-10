package com.ketty.serviceimpl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.ketty.api_entity.Typhoidtheoryprescription;
import com.ketty.api_entity.Typhoidtheorytype;
import com.ketty.api_mapper.TyphoidtheoryprescriptionMapper;
import com.ketty.api_mapper.TyphoidtheorytypeMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ketty.service.TyphoidtheoryprescriptionService;
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
public class TyphoidtheoryprescriptionServiceImpl extends ServiceImpl<TyphoidtheoryprescriptionMapper, Typhoidtheoryprescription> implements TyphoidtheoryprescriptionService {
    @Autowired
    TyphoidtheoryprescriptionMapper mapper;
    @Autowired
    TyphoidtheorytypeMapper typeMapper;

    @Override
    public List<String> selectNameList(String typeName) {
        Typhoidtheorytype type = typeMapper.selectOne(
                new LambdaQueryWrapper<Typhoidtheorytype>()
                        .eq(Typhoidtheorytype::getTypeName,typeName));
        if (type != null) {
            List<Typhoidtheoryprescription> list = mapper.selectList(
                    new LambdaQueryWrapper<Typhoidtheoryprescription>()
                            .select(Typhoidtheoryprescription::getName)
                            .eq(Typhoidtheoryprescription::getType,type.getTypeId())
                            .orderByAsc(Typhoidtheoryprescription::getId));
            if (list.size() > 0) {
                List<String> names = new ArrayList<>();
                for (Typhoidtheoryprescription entity : list) {
                    names.add(entity.getName());
                }
                return names;
            }
        }
        return null;
    }

    @Override
    public Typhoidtheoryprescription getByName(String name) {
        Typhoidtheoryprescription entity = mapper.selectOne(
                new LambdaQueryWrapper<Typhoidtheoryprescription>()
                        .eq(Typhoidtheoryprescription::getName,name));
        if (entity != null) {
            return entity;
        }
        return null;
    }
}
