package com.ketty.serviceimpl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.ketty.api_entity.Orderform;
import com.ketty.api_entity.enums.OrderFormEnum;
import com.ketty.api_mapper.OrderformMapper;
import com.ketty.common_utils.IDKeyUtil;
import com.ketty.common_utils.SnowFlakeUtil;
import com.ketty.service.OrderformService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ketty.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author ketty
 * @since 2023-02-23
 */
@Service
public class OrderformServiceImpl extends ServiceImpl<OrderformMapper, Orderform> implements OrderformService {
    @Autowired
    OrderformMapper orderformMapper;
    @Autowired
    ProductService productService;

    @Override
    public void add(List<Orderform> list) {
        for (Orderform bean : list) {
            bean.setId(SnowFlakeUtil.getId());
            orderformMapper.insert(bean);
            productService.updateInventory(bean.getProductId(),bean.getNumber());
        }
    }

    @Override
    public List<Orderform> getListByState(Long userId, Integer state) {
        List<Orderform> list = new ArrayList<>();
        LambdaQueryWrapper<Orderform> wrapper = new LambdaQueryWrapper<>();
        if (!state.equals(OrderFormEnum.ALL.getCode())) {
            wrapper.eq(Orderform::getState,state);
        }
        wrapper.eq(Orderform::getUserId,userId);
        wrapper.orderByDesc(Orderform::getId);
        list = orderformMapper.selectList(wrapper);
        return list;
    }

    @Override
    public void alterState(Long id, Integer state) {
        orderformMapper.update(null,new LambdaUpdateWrapper<Orderform>()
                .eq(Orderform::getId,id)
                .set(Orderform::getState,state));
        Orderform bean = orderformMapper.selectById(id);
        if (state.equals(OrderFormEnum.RECEIPT.getCode())) {
            orderformMapper.update(null,new LambdaUpdateWrapper<Orderform>()
                    .eq(Orderform::getId,id)
                    .set(Orderform::getShipmentTime,bean.getUpdateTime()));
        } else if (state.equals(OrderFormEnum.FINISH.getCode())) {
            orderformMapper.update(null,new LambdaUpdateWrapper<Orderform>()
                    .eq(Orderform::getId,id)
                    .set(Orderform::getFinishTime,bean.getUpdateTime()));
        } else if (state.equals(OrderFormEnum.SHIPMENTS.getCode())) {
            productService.updateInventory(bean.getProductId(),-bean.getNumber());
            delete(id);
        }
    }

    @Override
    public void delete(Long id) {
        orderformMapper.deleteById(id);
    }

    @Override
    public void deleteByUser(Long userId) {
        orderformMapper.delete(new LambdaUpdateWrapper<Orderform>()
                .eq(Orderform::getUserId,userId));
    }

}
