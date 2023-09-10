package com.ketty.service;

import com.ketty.api_entity.CollectCountDTO;
import com.ketty.api_entity.Collectstatistic;
import com.ketty.api_entity.Product;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author ketty
 * @since 2023-02-14
 */
public interface ProductService extends IService<Product> {

    Product getById(Long id);

    int getInventory(Long id);

    List<Product> getItemList(Long categoryId);

    List<Product> getByIds(List<Long> ids);

    List<Product> selectLikeName(String name);

    void updateCollectState(Collectstatistic entity);

    void updateCollectCount(CollectCountDTO dto);

    int getIsCollectByUserId(Long userId, Long id);

    List<Product> getBeCollectList(Long userId);

    void updateInventory(Long id, Integer number);
}
