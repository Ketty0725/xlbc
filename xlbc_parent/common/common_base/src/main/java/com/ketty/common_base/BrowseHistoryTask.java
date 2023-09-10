package com.ketty.common_base;

import com.ketty.service.BrowsinghistoryService;
import com.ketty.service.CollectstatisticService;
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
public class BrowseHistoryTask extends QuartzJobBean {

    @Autowired
    BrowsinghistoryService service;

    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Override
    protected void executeInternal(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        log.info("BrowseHistoryTask-------- {}", sdf.format(new Date()));

        service.transBrowseHistoryFromRedis2DB();
    }
}
