package com.ketty.common_base;

import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Configuration
public class QuartzConfig {

    private static final String LIKE_TASK_IDENTITY = "LikeTaskQuartz";
    private static final String COLLECT_TASK_IDENTITY = "CollectTaskQuartz";
    private static final String SHOPCART_TASK_IDENTITY = "ShopCartTaskQuartz";
    private static final String BROWSEHISTORY_TASK_IDENTITY = "BrowseHistoryTaskQuartz";

    @Bean
    public JobDetail likeQuartzDetail(){
        return JobBuilder.newJob(LikeTask.class).withIdentity(LIKE_TASK_IDENTITY).storeDurably().build();
    }

    @Bean
    public Trigger likeQuartzTrigger(){
        SimpleScheduleBuilder scheduleBuilder = SimpleScheduleBuilder.simpleSchedule()
//                .withIntervalInSeconds(30)  //设置时间周期单位秒
                .withIntervalInHours(2)  //两个小时执行一次
                .repeatForever();
        return TriggerBuilder.newTrigger().forJob(likeQuartzDetail())
                .withIdentity(LIKE_TASK_IDENTITY)
                .withSchedule(scheduleBuilder)
                .build();
    }

    @Bean
    public JobDetail collectQuartzDetail(){
        return JobBuilder.newJob(CollectTask.class).withIdentity(COLLECT_TASK_IDENTITY).storeDurably().build();
    }

    @Bean
    public Trigger collectQuartzTrigger(){
        SimpleScheduleBuilder scheduleBuilder = SimpleScheduleBuilder.simpleSchedule()
//                .withIntervalInSeconds(30)  //设置时间周期单位秒
                .withIntervalInHours(2)  //两个小时执行一次
                .repeatForever();
        return TriggerBuilder.newTrigger().forJob(collectQuartzDetail())
                .withIdentity(COLLECT_TASK_IDENTITY)
                .withSchedule(scheduleBuilder)
                .build();
    }

    @Bean
    public JobDetail shopCartQuartzDetail(){
        return JobBuilder.newJob(ShopCartTask.class).withIdentity(SHOPCART_TASK_IDENTITY).storeDurably().build();
    }

    @Bean
    public Trigger shopCartQuartzTrigger(){
        SimpleScheduleBuilder scheduleBuilder = SimpleScheduleBuilder.simpleSchedule()
//                .withIntervalInSeconds(30)  //设置时间周期单位秒
                .withIntervalInHours(2)  //两个小时执行一次
                .repeatForever();
        return TriggerBuilder.newTrigger().forJob(shopCartQuartzDetail())
                .withIdentity(SHOPCART_TASK_IDENTITY)
                .withSchedule(scheduleBuilder)
                .build();
    }

    @Bean
    public JobDetail browseHistoryQuartzDetail(){
        return JobBuilder.newJob(BrowseHistoryTask.class).withIdentity(BROWSEHISTORY_TASK_IDENTITY).storeDurably().build();
    }

    @Bean
    public Trigger browseHistoryQuartzTrigger(){
        SimpleScheduleBuilder scheduleBuilder = SimpleScheduleBuilder.simpleSchedule()
//                .withIntervalInSeconds(30)  //设置时间周期单位秒
                .withIntervalInHours(2)  //两个小时执行一次
                .repeatForever();
        return TriggerBuilder.newTrigger().forJob(browseHistoryQuartzDetail())
                .withIdentity(BROWSEHISTORY_TASK_IDENTITY)
                .withSchedule(scheduleBuilder)
                .build();
    }

}
