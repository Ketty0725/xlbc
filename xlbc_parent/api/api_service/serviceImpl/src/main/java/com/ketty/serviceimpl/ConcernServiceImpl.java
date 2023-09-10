package com.ketty.serviceimpl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.ketty.api_entity.Concern;
import com.ketty.api_entity.User;
import com.ketty.api_entity.enums.ConcernEnum;
import com.ketty.api_mapper.ConcernMapper;
import com.ketty.service.ConcernService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ketty.service.UserService;
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
 * @since 2023-03-06
 */
@Service
public class ConcernServiceImpl extends ServiceImpl<ConcernMapper, Concern> implements ConcernService {
    @Autowired
    ConcernMapper mapper;
    @Autowired
    UserService userService;

    @Override
    public void addOrDelete(Concern concern) {
        int exists = selectIsConcerned(concern);
        if (exists == ConcernEnum.ALIKE.getCode() || exists == ConcernEnum.ALL.getCode()) {
            mapper.delete(new LambdaUpdateWrapper<Concern>()
                    .eq(Concern::getConcernId,concern.getConcernId())
                    .eq(Concern::getBeConcernedId,concern.getBeConcernedId()));
        } else {
            mapper.insert(concern);
        }
    }

    @Override
    public int selectIsConcerned(Concern concern) {
        boolean isAlike = mapper.exists(new LambdaQueryWrapper<Concern>()
                .eq(Concern::getConcernId,concern.getConcernId())
                .eq(Concern::getBeConcernedId,concern.getBeConcernedId()));
        boolean isOpposite = mapper.exists(new LambdaQueryWrapper<Concern>()
                .eq(Concern::getConcernId,concern.getBeConcernedId())
                .eq(Concern::getBeConcernedId,concern.getConcernId()));
        if (isAlike && isOpposite) {
            return ConcernEnum.ALL.getCode();
        } else if (isAlike) {
            return ConcernEnum.ALIKE.getCode();
        } else if (isOpposite) {
            return ConcernEnum.OPPOSITE.getCode();
        }
        return ConcernEnum.NONE.getCode();
    }

    @Override
    public List<User> selectConcernList(Long userId) {
        List<Concern> concerns = mapper.selectList(new LambdaQueryWrapper<Concern>()
                        .select(Concern::getBeConcernedId)
                .eq(Concern::getConcernId,userId)
                .orderByDesc(Concern::getCreateTime));
        List<User> list = new ArrayList<>();
        for (Concern concern: concerns) {
            list.add(userService.getById(concern.getBeConcernedId()));
        }
        return list;
    }

    @Override
    public List<User> selectBeConcernedList(Long userId) {
        List<Concern> concerns = mapper.selectList(new LambdaQueryWrapper<Concern>()
                .select(Concern::getConcernId)
                .eq(Concern::getBeConcernedId,userId)
                .orderByDesc(Concern::getCreateTime));
        List<User> list = new ArrayList<>();
        for (Concern concern: concerns) {
            list.add(userService.getById(concern.getConcernId()));
        }
        return list;
    }

    @Override
    public Long getConcernCount(Long userId) {
        long count = mapper.selectCount(new LambdaQueryWrapper<Concern>()
                .eq(Concern::getConcernId,userId));
        return count;
    }

    @Override
    public Long getBeConcernedCount(Long userId) {
        long count = mapper.selectCount(new LambdaQueryWrapper<Concern>()
                .eq(Concern::getBeConcernedId,userId));
        return count;
    }

    @Override
    public void deleteByUser(Long userId) {
        mapper.delete(new LambdaUpdateWrapper<Concern>()
                .eq(Concern::getConcernId,userId)
                .or()
                .eq(Concern::getBeConcernedId,userId));
    }
}
