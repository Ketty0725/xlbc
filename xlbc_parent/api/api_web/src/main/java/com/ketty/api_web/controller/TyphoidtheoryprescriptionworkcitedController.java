package com.ketty.api_web.controller;

import com.ketty.api_entity.Typhoidtheoryprescriptionworkcited;
import com.ketty.common_utils.R;
import com.ketty.service.TyphoidtheoryprescriptionworkcitedService;
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
 * @since 2023-01-15
 */
@Api(tags = "伤寒论经方相关条文")
@RestController
@RequestMapping("/typhoidtheoryprescriptionworkcited")
public class TyphoidtheoryprescriptionworkcitedController {
    @Autowired
    TyphoidtheoryprescriptionworkcitedService service;

    @PostMapping("/getByNameAndType")
    public R getByNameAndType(String name, String typeName) {
        List<Typhoidtheoryprescriptionworkcited> list = service.getByNameAndType(name, typeName);
        return R.ok().data("result",list);
    }

    @PostMapping("/countByName")
    public R countByName(String name) {
        Long count = service.countByName(name);
        return R.ok().data("result",count);
    }

}
