package com.ketty.common_base;

import com.ketty.service.CollectstatisticService;
import com.ketty.service.LikestatisticService;
import lombok.extern.slf4j.Slf4j;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 收藏的定时任务
 */
@Slf4j
public class CollectTask extends QuartzJobBean {

    @Autowired
    CollectstatisticService service;

    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Override
    protected void executeInternal(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        log.info("CollectTask-------- {}", sdf.format(new Date()));

        service.transCollectFromRedis2DB();
        service.transCollectCountFromRedis2DB();
    }
}
