package com.ketty.api_web.controller;

import com.ketty.api_entity.Typhoidtheorychineseherbcontent;
import com.ketty.common_utils.R;
import com.ketty.service.TyphoidtheorychineseherbcontentService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author ketty
 * @since 2023-01-16
 */
@Api(tags = "伤寒论中药详情")
@RestController
@RequestMapping("/typhoidtheorychineseherbcontent")
public class TyphoidtheorychineseherbcontentController {
    @Autowired
    TyphoidtheorychineseherbcontentService service;

    @PostMapping("/getByNameAndType")
    public R getByNameAndType(String name, String typeName) {
        List<Typhoidtheorychineseherbcontent> list = service.getByNameAndType(name, typeName);
        return R.ok().data("result",list);
    }

}
