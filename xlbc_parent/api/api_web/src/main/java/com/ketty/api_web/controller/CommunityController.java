package com.ketty.api_web.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.ketty.api_entity.*;
import com.ketty.common_utils.R;
import com.ketty.service.CommunityService;
import com.ketty.service.CommunityimageService;
import com.ketty.service.UserService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author ketty
 * @since 2023-03-09
 */
@Api(tags = "社区笔记")
@RestController
@RequestMapping("/community")
public class CommunityController {
    @Autowired
    CommunityService service;
    @Autowired
    CommunityimageService communityimageService;
    @Autowired
    UserService userService;

    @PostMapping("/add")
    public R add(@RequestParam(value = "upload") List<MultipartFile> imgList, Community community) {
        if (imgList.size() > 0 && community != null) {
            service.add(imgList, community);
            return R.ok();
        }
        return R.error();
    }

    @PostMapping("/getById")
    public R getById(Long id) {
        if (id != null) {
            Community community = service.getById(id);
            List<Communityimage> images = new ArrayList<>();
            User user = new User();
            if (community != null) {
                images = communityimageService.selectListByNoteId(id);
                user = userService.getByIdIsPart(community.getUserId());
            }
            return R.ok()
                    .data("data",community)
                    .data("images",images)
                    .data("user",user);
        }
        return R.error();
    }

    @PostMapping("/moveToRecycled")
    public R moveToRecycled(Long id) {
        if (id != null) {
            service.moveToRecycled(id);
            return R.ok();
        }
        return R.error();
    }

    @PostMapping("/retrievalFromRecycled")
    public R retrievalFromRecycled(Long id) {
        if (id != null) {
            service.retrievalFromRecycled(id);
            return R.ok();
        }
        return R.error();
    }

    @PostMapping("/getFromRecycled")
    public R getFromRecycled(Long userId) {
        if (userId != null) {
            List<Community> communities = service.getFromRecycled(userId);
            List<Communityimage> images = new ArrayList<>();
            List<User> users = new ArrayList<>();
            if (communities.size() > 0) {
                for (Community community: communities) {
                    images.add(communityimageService.getFirstImage(community.getId()));
                    users.add(userService.getByIdIsPart(community.getUserId()));
                }
            }
            return R.ok()
                    .data("communities",communities)
                    .data("images",images)
                    .data("users",users);
        }
        return R.error();
    }

    @PostMapping("/deleteOfCompletely")
    public R deleteOfCompletely(Long id) {
        if (id != null) {
            service.deleteOfCompletely(id);
            return R.ok();
        }
        return R.error();
    }

    @PostMapping("/clearRecycled")
    public R clearRecycled(Long userId) {
        if (userId != null) {
            service.clearRecycled(userId);
            return R.ok();
        }
        return R.error();
    }

    @PostMapping("/selectAllPage")
    public R selectAllPage(Long current) {
        if (current != null) {
            IPage<Community> iPage = service.selectAllPage(current);
            List<Communityimage> images = new ArrayList<>();
            List<User> users = new ArrayList<>();
            if (iPage.getRecords() != null) {
                for (Community community : iPage.getRecords()) {
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
                    .data("data",iPage.getRecords())
                    .data("images",images)
                    .data("users",users)
                    .data("page",page);
        }
        return R.error();
    }

    @PostMapping("/selectPageByUser")
    public R selectPageByUser(Long current, Long userId) {
        if (current != null && userId != null) {
            IPage<Community> iPage = service.selectPageByUser(current, userId);
            List<Communityimage> images = new ArrayList<>();
            List<User> users = new ArrayList<>();
            if (iPage.getRecords() != null) {
                for (Community community : iPage.getRecords()) {
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
                    .data("data",iPage.getRecords())
                    .data("images",images)
                    .data("users",users)
                    .data("page",page);
        }
        return R.error();
    }

    @PostMapping("/selectPageByConcern")
    public R selectPageByConcern(Long current, Long userId) {
        if (current != null && userId != null) {
            IPage<Community> iPage = service.selectPageByConcern(current, userId);
            if (iPage != null) {
                List<Communityimage> images = new ArrayList<>();
                List<User> users = new ArrayList<>();
                if (iPage.getRecords() != null) {
                    for (Community community : iPage.getRecords()) {
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
                        .data("data",iPage.getRecords())
                        .data("images",images)
                        .data("users",users)
                        .data("page",page);
            }
        }
        return R.error();
    }

    @PostMapping("/selectPageByIp")
    public R selectPageByIp(Long current, Long userId) {
        if (current != null && userId != null) {
            IPage<Community> iPage = service.selectPageByIp(current, userId);
            if (iPage != null) {
                List<Communityimage> images = new ArrayList<>();
                List<User> users = new ArrayList<>();
                if (iPage.getRecords() != null) {
                    for (Community community : iPage.getRecords()) {
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
                        .data("data",iPage.getRecords())
                        .data("images",images)
                        .data("users",users)
                        .data("page",page);
            }
        }
        return R.error();
    }

    @PostMapping("/selectPageByTitle")
    public R selectPageByTitle(Long current, String title) {
        if (current != null && title != null) {
            IPage<Community> iPage = service.selectPageByTitle(current, title);
            List<Communityimage> images = new ArrayList<>();
            List<User> users = new ArrayList<>();
            if (iPage.getRecords() != null) {
                for (Community community : iPage.getRecords()) {
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
                    .data("data",iPage.getRecords())
                    .data("images",images)
                    .data("users",users)
                    .data("page",page);
        }
        return R.error();
    }

    @PostMapping("/getBeLikedList")
    public R getBeLikedList(Long userId) {
        if (userId != null) {
            List<Community> list = service.getBeLikedList(userId);
            List<Communityimage> images = new ArrayList<>();
            List<User> users = new ArrayList<>();
            if (list.size() > 0) {
                for (Community community: list) {
                    images.add(communityimageService.getFirstImage(community.getId()));
                    users.add(userService.getByIdIsPart(community.getUserId()));
                }
            }
            return R.ok()
                    .data("data",list)
                    .data("images",images)
                    .data("users",users);
        }
        return R.error();
    }

    @PostMapping("/getBeCollectList")
    public R getBeCollectList(Long userId) {
        if (userId != null) {
            List<Community> list = service.getBeCollectList(userId);
            List<Communityimage> images = new ArrayList<>();
            List<User> users = new ArrayList<>();
            if (list.size() > 0) {
                for (Community community: list) {
                    images.add(communityimageService.getFirstImage(community.getId()));
                    users.add(userService.getByIdIsPart(community.getUserId()));
                }
            }
            return R.ok()
                    .data("data",list)
                    .data("images",images)
                    .data("users",users);
        }
        return R.error();
    }

    @PostMapping("/updateLikedState")
    public R updateLikedState(Likestatistic entity) {
        service.updateLikedState(entity);
        return R.ok();
    }

    @PostMapping("/updateCollectState")
    public R updateCollectState(Collectstatistic entity) {
        service.updateCollectState(entity);
        return R.ok();
    }

    @PostMapping("/getIsLikedAndCollectByUserId")
    public R getIsLikedAndCollectByUserId(Long userId, Long noteId) {
        int isLiked = service.getIsLikedByUserId(userId, noteId);
        int isCollect = service.getIsCollectByUserId(userId, noteId);
        return R.ok()
                .data("Liked", isLiked)
                .data("Collect", isCollect);
    }

}
