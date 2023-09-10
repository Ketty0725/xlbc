package com.ketty.api_web.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.ketty.api_entity.*;
import com.ketty.common_utils.R;
import com.ketty.service.BrowsinghistoryService;
import com.ketty.service.CommunityService;
import com.ketty.service.CommunityimageService;
import com.ketty.service.UserService;
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
 * @since 2023-03-04
 */
@Api(tags = "浏览记录")
@RestController
@RequestMapping("/browsinghistory")
public class BrowsinghistoryController {
    @Autowired
    BrowsinghistoryService service;
    @Autowired
    CommunityService communityService;
    @Autowired
    CommunityimageService communityimageService;
    @Autowired
    UserService userService;

    @PostMapping("/addOrUpdateToRedis")
    public R addOrUpdateToRedis(Long userId, Long beBrowseId) {
        service.addOrUpdateToRedis(userId, beBrowseId);
        return R.ok();
    }

    @PostMapping("/deleteAll")
    public R deleteAll(Long userId) {
        service.deleteAllByUser(userId);
        return R.ok();
    }

    @PostMapping("/getByUserId")
    public R getByUserId(Long userId, Long currentPage) {
        if (userId != null) {
            IPage<Browsinghistory> iPage = service.getByUserId(userId, currentPage);
            List<Community> communities = new ArrayList<>();
            List<Communityimage> images = new ArrayList<>();
            List<User> users = new ArrayList<>();
            for (Browsinghistory history : iPage.getRecords()) {
                Community community = communityService.getByIdIsPart(history.getBeBrowseId());
                if (community != null) {
                    communities.add(community);
                    images.add(communityimageService.getFirstImage(community.getId()));
                    users.add(userService.getByIdIsPart(community.getUserId()));
                }
            }
            PageEnable page = new PageEnable();
            page.setCurrentPage(iPage.getCurrent());
            page.setPageSize(iPage.getSize());
            page.setTotalPages(iPage.getPages());
            page.setTotalDataSize(iPage.getTotal());
            return R.ok()
                    .data("communities",communities)
                    .data("images",images)
                    .data("users",users)
                    .data("page",page);
        }
        return R.error();
    }

}
