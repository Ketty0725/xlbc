package com.ketty.serviceimpl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.ketty.api_entity.NameAndType;
import com.ketty.api_entity.Typhoidtheorychineseherb;
import com.ketty.api_entity.Typhoidtheoryprescription;
import com.ketty.api_entity.Typhoidtheorytype;
import com.ketty.api_mapper.TyphoidtheorychineseherbMapper;
import com.ketty.api_mapper.TyphoidtheoryprescriptionMapper;
import com.ketty.api_mapper.TyphoidtheorytypeMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ketty.service.TyphoidtheorytypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.Collator;
import java.util.*;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author ketty
 * @since 2023-01-15
 */
@Service
public class TyphoidtheorytypeServiceImpl extends ServiceImpl<TyphoidtheorytypeMapper, Typhoidtheorytype> implements TyphoidtheorytypeService {
    @Autowired
    TyphoidtheoryprescriptionMapper typhoidtheoryprescriptionMapper;
    @Autowired
    TyphoidtheorychineseherbMapper typhoidtheorychineseherbMapper;

    @Override
    public List<NameAndType> selectLikeName(String name) {
        List<Typhoidtheoryprescription> prescriptions = typhoidtheoryprescriptionMapper.selectList(
                new LambdaQueryWrapper<Typhoidtheoryprescription>()
                        .select(Typhoidtheoryprescription::getName)
                        .like(name != null,Typhoidtheoryprescription::getName,name));
        List<Typhoidtheorychineseherb> chineseherbs = typhoidtheorychineseherbMapper.selectList(
                new LambdaQueryWrapper<Typhoidtheorychineseherb>()
                        .select(Typhoidtheorychineseherb::getName)
                        .like(name != null,Typhoidtheorychineseherb::getName,name));

        List<NameAndType> list = new ArrayList<>();
        NameAndType nameAndType;
        for (Typhoidtheoryprescription entity : prescriptions) {
            nameAndType = new NameAndType();
            nameAndType.setName(entity.getName());
            nameAndType.setType(0);
            list.add(nameAndType);
        }
        for (Typhoidtheorychineseherb entity : chineseherbs) {
            nameAndType = new NameAndType();
            nameAndType.setName(entity.getName());
            nameAndType.setType(1);
            list.add(nameAndType);
        }

        Collections.sort(list, (l1, l2) -> {
            Collator instance = Collator.getInstance(Locale.CHINA);
            return instance.compare(l1.getName(), l2.getName());
        });

        return list;
    }

}
