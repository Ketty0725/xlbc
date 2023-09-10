package com.ketty.api_web.controller;

import com.ketty.api_entity.*;
import com.ketty.common_utils.R;
import com.ketty.service.ReplyService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author ketty
 * @since 2023-01-31
 */
@Api(tags = "子回复评论")
@RestController
@RequestMapping("/reply")
public class ReplyController {
    @Autowired
    ReplyService service;

    @PostMapping("/add")
    public R add(Reply reply) {
        String result = service.add(reply);
        return R.ok().data("result", result);
    }

    @PostMapping("/get")
    public R get(Long fatherId, Long currentPage) {
        CommentResult result = service.get(fatherId, currentPage);
        return R.ok().data("result",result);
    }

    @PostMapping("/deleteById")
    public R deleteById(Long id, Long userId) {
        if (id != null) {
            service.deleteById(id, userId);
            return R.ok();
        }
        return R.error();
    }

    @PostMapping("/updateLikedState")
    public R updateLikedState(Likestatistic entity) {
        service.updateLikedState(entity);
        return R.ok();
    }

}
