package com.ketty.serviceimpl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.ketty.api_entity.Shoppingaddress;
import com.ketty.api_mapper.ShoppingaddressMapper;
import com.ketty.service.ShoppingaddressService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author ketty
 * @since 2023-02-25
 */
@Service
public class ShoppingaddressServiceImpl extends ServiceImpl<ShoppingaddressMapper, Shoppingaddress> implements ShoppingaddressService {
    @Autowired
    ShoppingaddressMapper mapper;

    @Override
    public void add(Shoppingaddress bean) {
        boolean isExists = mapper.exists(
                new LambdaQueryWrapper<Shoppingaddress>()
                        .eq(Shoppingaddress::getUserId,bean.getUserId()));
        if (isExists) {
            if (bean.getIsDefault() == 1) {
                Shoppingaddress value = mapper.selectOne(
                        new LambdaQueryWrapper<Shoppingaddress>()
                                .select(Shoppingaddress::getId)
                                .eq(Shoppingaddress::getUserId,bean.getUserId())
                                .eq(Shoppingaddress::getIsDefault,1));
                updateIsDefault(value.getId(),0);
            }
        } else {
            bean.setIsDefault(1);
        }
        mapper.insert(bean);
    }

    @Override
    public List<Shoppingaddress> getByUserId(Long userId) {
        List<Shoppingaddress> list = mapper.selectList(
                new LambdaQueryWrapper<Shoppingaddress>()
                        .eq(Shoppingaddress::getUserId,userId)
                        .last("order by case when is_default = '1' then 0 else 1 end, update_time asc"));
        return list;
    }

    @Override
    public Shoppingaddress getDefaultByUserId(Long userId) {
        Shoppingaddress bean = mapper.selectOne(
                new LambdaQueryWrapper<Shoppingaddress>()
                        .eq(Shoppingaddress::getUserId,userId)
                        .eq(Shoppingaddress::getIsDefault,1));
        return bean;
    }

    @Override
    public void updateIsDefault(Integer id, Integer idDefault) {
        mapper.update(null,new LambdaUpdateWrapper<Shoppingaddress>()
                        .set(Shoppingaddress::getIsDefault,idDefault)
                .eq(Shoppingaddress::getId,id));
    }

    @Override
    public void updateData(Shoppingaddress bean) {
        Shoppingaddress value = mapper.selectOne(
                new LambdaQueryWrapper<Shoppingaddress>()
                        .select(Shoppingaddress::getId)
                        .eq(Shoppingaddress::getUserId,bean.getUserId())
                        .eq(Shoppingaddress::getIsDefault,1));
        if (bean.getIsDefault() == 1) {
            if (!Objects.equals(bean.getId(), value.getId())) {
                updateIsDefault(value.getId(),0);
            }
        } else {
            if (Objects.equals(bean.getId(), value.getId())) {
                bean.setIsDefault(1);
            }
        }
        mapper.updateById(bean);
    }

    @Override
    public void deleteByUser(Long userId) {
        mapper.delete(new LambdaUpdateWrapper<Shoppingaddress>()
                .eq(Shoppingaddress::getUserId,userId));
    }
}
