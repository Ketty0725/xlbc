package com.ketty.api_web.controller;

import com.ketty.api_entity.Chineseherb;
import com.ketty.api_entity.ChineseherbAndImages;
import com.ketty.api_entity.FlowCategory;
import com.ketty.common_utils.R;
import com.ketty.service.ChineseherbService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author ketty
 * @since 2023-01-15
 */
@Api(tags = "中药名称")
@RestController
@RequestMapping("/chineseherb")
public class ChineseherbController {
    @Autowired
    ChineseherbService service;

    @GetMapping("/getAll")
    public R getAll() {
        List<FlowCategory> list = service.getAll();
        return R.ok().data("result",list);
    }

    @PostMapping("/getByName")
    public R getByName(String name) {
        try {
            ChineseherbAndImages chineseherb = service.getByName(name);
            return R.ok().data("result",chineseherb);
        } catch (Exception e) {
            return R.error().message(e.getMessage());
        }
    }

    @PostMapping("/selectLikeName")
    public R selectLikeName(String name) {
        try {
            List<ChineseherbAndImages> list = service.selectLikeName(name);
            return R.ok().data("result",list);
        } catch (Exception e) {
            return R.error().message(e.getMessage());
        }
    }

}
