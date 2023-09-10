package com.ketty.serviceimpl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ketty.api_entity.SysOss;
import com.ketty.api_mapper.SysOssMapper;
import com.ketty.service.SysOssService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 文件上传 服务层实现
 *
 */
@Service
@DS("slave")
public class SysOssServiceImpl extends ServiceImpl<SysOssMapper, SysOss> implements SysOssService {
    @Autowired
    SysOssMapper sysOssMapper;

    @Override
    public SysOss getById(Long ossId) {
        return sysOssMapper.selectById(ossId);
    }

}
