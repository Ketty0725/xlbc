package com.ketty.serviceimpl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.ketty.api_entity.Typhoidtheoryprescription;
import com.ketty.api_entity.Typhoidtheoryprescriptionworkcited;
import com.ketty.api_entity.Typhoidtheorytype;
import com.ketty.api_mapper.TyphoidtheoryprescriptionMapper;
import com.ketty.api_mapper.TyphoidtheoryprescriptionworkcitedMapper;
import com.ketty.api_mapper.TyphoidtheorytypeMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ketty.service.TyphoidtheoryprescriptionworkcitedService;
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
 * @since 2023-01-15
 */
@Service
public class TyphoidtheoryprescriptionworkcitedServiceImpl extends ServiceImpl<TyphoidtheoryprescriptionworkcitedMapper, Typhoidtheoryprescriptionworkcited> implements TyphoidtheoryprescriptionworkcitedService {
    @Autowired
    TyphoidtheoryprescriptionworkcitedMapper mapper;
    @Autowired
    TyphoidtheoryprescriptionMapper typhoidtheoryprescriptionMapper;
    @Autowired
    TyphoidtheorytypeMapper typeMapper;

    @Override
    public List<Typhoidtheoryprescriptionworkcited> getByNameAndType(String name, String typeName) {
        Typhoidtheoryprescription prescription = typhoidtheoryprescriptionMapper.selectOne(
                new LambdaQueryWrapper<Typhoidtheoryprescription>()
                        .select(Typhoidtheoryprescription::getId)
                        .eq(Typhoidtheoryprescription::getName,name));
        Typhoidtheorytype type = typeMapper.selectOne(
                new LambdaQueryWrapper<Typhoidtheorytype>()
                        .select(Typhoidtheorytype::getTypeId)
                        .eq(Typhoidtheorytype::getTypeName,typeName));
        if (prescription != null && type != null) {
            List<Typhoidtheoryprescriptionworkcited> list = mapper.selectList(
                    new LambdaQueryWrapper<Typhoidtheoryprescriptionworkcited>()
                            .eq(Typhoidtheoryprescriptionworkcited::getPreparationId,prescription.getId())
                            .eq(Typhoidtheoryprescriptionworkcited::getType,type.getTypeId()));
            if (list.size() > 0) {
                return list;
            }
        }
        return null;
    }

    @Override
    public Long countByName(String name) {
        Typhoidtheoryprescription prescription = typhoidtheoryprescriptionMapper.selectOne(
                new LambdaQueryWrapper<Typhoidtheoryprescription>()
                        .select(Typhoidtheoryprescription::getId)
                        .eq(Typhoidtheoryprescription::getName,name));
        if (prescription != null) {
            Long count = mapper.selectCount(new LambdaQueryWrapper<Typhoidtheoryprescriptionworkcited>()
                    .eq(Typhoidtheoryprescriptionworkcited::getPreparationId,prescription.getId()));
            if (count > 0) {
                return count;
            }
        }
        return null;
    }

}
