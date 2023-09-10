package com.ketty.service;

import com.ketty.api_entity.Productbasicinfo;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author ketty
 * @since 2023-02-17
 */
public interface ProductbasicinfoService extends IService<Productbasicinfo> {

    Productbasicinfo getByProductId(Long productId);

    String getInstructionBook(Long productId);
}
