package com.ketty.api_web.controller;

import com.ketty.api_entity.Browsinghistory;
import com.ketty.api_entity.Likestatistic;
import com.ketty.api_entity.enums.LikedStateEnum;
import com.ketty.api_entity.enums.LikedTypeEnum;
import com.ketty.common_utils.R;
import com.ketty.common_utils.RedisKeyUtils;
import com.ketty.common_utils.RedisUtils;
import com.ketty.service.LikestatisticService;
import com.ketty.service.RedisService;
import lombok.SneakyThrows;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * @Auther: Ketty Allen
 * @Date:2023/2/3 - 23:25
 * @Description:com.ketty.api_web.controller
 * @version: 1.0
 */
@RestController
@RequestMapping("/redis")
public class RedisController {
    @Autowired
    RedisService service;

    @PostMapping("/put")
    public R put(Long userId, Long beBrowseId) {
        String time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        service.saveBrowseHistoryToRedis(userId,beBrowseId,time);
        return R.ok();
    }

    @PostMapping("/get")
    public R get(Long userId) {
        List<Browsinghistory> list = service.getBrowseHistoryDataFromRedis(userId);
        return R.ok().data("result",list);
    }

    @PostMapping("/delete")
    public R delete(Long userId) {
        service.deleteLikedFromRedis(userId, LikedTypeEnum.NOTE.getCode());
        return R.ok();
    }

}
