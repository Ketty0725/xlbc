package com.ketty.service;

import com.ketty.api_entity.Chineseherb;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ketty.api_entity.ChineseherbAndImages;
import com.ketty.api_entity.FlowCategory;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author ketty
 * @since 2023-01-15
 */
public interface ChineseherbService extends IService<Chineseherb> {

    List<FlowCategory> getAll();

    ChineseherbAndImages getByName(String name);

    List<ChineseherbAndImages> selectLikeName(String name);
}
