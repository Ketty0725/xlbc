package com.ketty.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ketty.api_entity.SysOss;

/**
 * 文件上传 服务层
 *
 */
public interface SysOssService extends IService<SysOss> {

    SysOss getById(Long ossId);

}
