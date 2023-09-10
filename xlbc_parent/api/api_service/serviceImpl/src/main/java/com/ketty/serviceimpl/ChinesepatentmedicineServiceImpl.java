package com.ketty.serviceimpl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.ketty.api_entity.Chinesepatentmedicine;
import com.ketty.api_mapper.ChinesepatentmedicineMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ketty.service.ChinesepatentmedicineService;
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
public class ChinesepatentmedicineServiceImpl extends ServiceImpl<ChinesepatentmedicineMapper, Chinesepatentmedicine> implements ChinesepatentmedicineService {
    @Autowired
    ChinesepatentmedicineMapper mapper;

    @Override
    public List<String> getNameList() {
        List<Chinesepatentmedicine> selectList =  mapper.selectList(new LambdaQueryWrapper<Chinesepatentmedicine>()
                .select(Chinesepatentmedicine::getDrugName)
                .orderByAsc(Chinesepatentmedicine::getId));
        if (selectList != null && selectList.size() > 0) {
            List<String> list = new ArrayList<>();
            for (Chinesepatentmedicine chinesepatentmedicine : selectList) {
                list.add(chinesepatentmedicine.getDrugName());
            }
            return list;
        }
        return null;
    }

    @Override
    public Chinesepatentmedicine getByName(String name) {
        Chinesepatentmedicine chinesepatentmedicine =  mapper.selectOne(new LambdaQueryWrapper<Chinesepatentmedicine>()
                .eq(Chinesepatentmedicine::getDrugName,name));
        if (chinesepatentmedicine != null) {
            return chinesepatentmedicine;
        }
        return null;
    }

    @Override
    public List<String> selectLikeName(String name) {
        List<Chinesepatentmedicine> chinesepatentmedicineList = mapper.selectList(
                new LambdaQueryWrapper<Chinesepatentmedicine>()
                        .select(Chinesepatentmedicine::getDrugName)
                        .like(name != null,Chinesepatentmedicine::getDrugName,name)
                        .orderByAsc(Chinesepatentmedicine::getId));
        if (chinesepatentmedicineList != null && chinesepatentmedicineList.size() > 0) {
            List<String> list = new ArrayList<>();
            for (Chinesepatentmedicine chinesepatentmedicine : chinesepatentmedicineList) {
                list.add(chinesepatentmedicine.getDrugName());
            }
            return list;
        } else {
            throw new RuntimeException("无数据");
        }
    }
}
