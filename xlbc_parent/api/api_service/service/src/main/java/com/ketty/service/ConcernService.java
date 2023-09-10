package com.ketty.service;

import com.ketty.api_entity.Concern;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ketty.api_entity.User;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author ketty
 * @since 2023-03-06
 */
public interface ConcernService extends IService<Concern> {

    void addOrDelete(Concern concern);

    int selectIsConcerned(Concern concern);

    List<User> selectConcernList(Long userId);

    List<User> selectBeConcernedList(Long userId);

    Long getConcernCount(Long userId);

    Long getBeConcernedCount(Long userId);

    void deleteByUser(Long userId);

}
