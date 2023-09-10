package com.ketty.api_web.controller;

import com.ketty.api_entity.Concern;
import com.ketty.api_entity.User;
import com.ketty.common_utils.R;
import com.ketty.service.ConcernService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author ketty
 * @since 2023-03-06
 */
@Api(tags = "关注列表")
@RestController
@RequestMapping("/concern")
public class ConcernController {
    @Autowired
    ConcernService service;

    @PostMapping("/addOrDelete")
    public R addOrDelete(Concern concern) {
        if (concern != null) {
            service.addOrDelete(concern);
            return R.ok();
        }
        return R.error();
    }

    @PostMapping("/selectIsConcerned")
    public R selectIsConcerned(Concern concern) {
        if (concern != null) {
            int state = service.selectIsConcerned(concern);
            return R.ok().data("result",state);
        }
        return R.error();
    }

    @PostMapping("/selectConcernList")
    public R selectConcernList(Long userId, Long mineId) {
        if (userId != null) {
            List<User> users = service.selectConcernList(userId);
            List<Integer> states = new ArrayList<>();
            for (User user: users) {
                Concern concern = new Concern(mineId, user.getUId());
                states.add(service.selectIsConcerned(concern));
            }
            return R.ok().data("users",users).data("states",states);
        }
        return R.error();
    }

    @PostMapping("/selectBeConcernedList")
    public R selectBeConcernedList(Long userId, Long mineId) {
        if (userId != null) {
            List<User> users = service.selectBeConcernedList(userId);
            List<Integer> states = new ArrayList<>();
            for (User user: users) {
                Concern concern = new Concern(mineId, user.getUId());
                states.add(service.selectIsConcerned(concern));
            }
            return R.ok().data("users",users).data("states",states);
        }
        return R.error();
    }

    @PostMapping("/getConcernCount")
    public R getConcernCount(Long userId) {
        if (userId != null) {
            long concern = service.getConcernCount(userId);
            long beConcerned = service.getBeConcernedCount(userId);
            return R.ok().data("concern",concern).data("beConcerned",beConcerned);
        }
        return R.error();
    }

}
