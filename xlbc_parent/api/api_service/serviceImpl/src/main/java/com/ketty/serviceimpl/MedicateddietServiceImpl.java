package com.ketty.serviceimpl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.ketty.api_entity.Medicateddiet;
import com.ketty.api_mapper.MedicateddietMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ketty.service.MedicateddietService;
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
public class MedicateddietServiceImpl extends ServiceImpl<MedicateddietMapper, Medicateddiet> implements MedicateddietService {
    @Autowired
    MedicateddietMapper mapper;

    @Override
    public List<Medicateddiet> getNameList() {
        List<Medicateddiet> selectList =  mapper.selectList(new LambdaQueryWrapper<Medicateddiet>()
                .select(Medicateddiet::getMedicatedDietName,Medicateddiet::getDerivation)
                .orderByAsc(Medicateddiet::getId));
        if (selectList != null && selectList.size() > 0) {
            return selectList;
        }
        return null;
    }

    @Override
    public Medicateddiet getByName(String name) {
        Medicateddiet medicateddiet =  mapper.selectOne(new LambdaQueryWrapper<Medicateddiet>()
                .eq(Medicateddiet::getMedicatedDietName,name));
        if (medicateddiet != null) {
            return medicateddiet;
        }
        return null;
    }

    @Override
    public List<Medicateddiet> selectLikeName(String name) {
        List<Medicateddiet> selectList = mapper.selectList(
                new LambdaQueryWrapper<Medicateddiet>()
                        .select(Medicateddiet::getMedicatedDietName,Medicateddiet::getDerivation)
                        .like(name != null,Medicateddiet::getMedicatedDietName,name)
                        .orderByAsc(Medicateddiet::getId));
        if (selectList != null && selectList.size() > 0) {
            return selectList;
        } else {
            throw new RuntimeException("无数据");
        }
    }
}
