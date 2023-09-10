package com.ketty.api_web.controller;

import com.ketty.api_entity.Comment;
import com.ketty.api_entity.CommentPagerEnable;
import com.ketty.api_entity.Likestatistic;
import com.ketty.common_utils.R;
import com.ketty.service.CommentService;
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
@Api(tags = "父评论")
@RestController
@RequestMapping("/comment")
public class CommentController {
    @Autowired
    CommentService service;

    @PostMapping("/add")
    public R add(Comment comment) {
        String result = service.add(comment);
        return R.ok().data("result", result);
    }

    @PostMapping("/get")
    public R get(Long noteId, Long currentPage) {
        CommentPagerEnable page = service.get(noteId, currentPage);
        return R.ok().data("result", page);
    }

    @PostMapping("/deleteById")
    public R deleteById(Long id, Long userId) {
        if (id != null) {
            service.deleteById(id, userId);
            return R.ok();
        }
        return R.error();
    }

    @PostMapping("/getTotalCount")
    public R getTotalCount(Long noteId) {
        Long totalCount = service.getTotalCount(noteId);
        return R.ok().data("result", totalCount);
    }

        @PostMapping("/updateLikedState")
    public R updateLikedState(Likestatistic entity) {
        service.updateLikedState(entity);
        return R.ok();
    }

}
