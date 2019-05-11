package com.wujiuye.event;

import com.wujiuye.shiro.SessionValidationJob;
import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

/**
 * 监听spring上下文初始化完成，给quartz添加定时任务
 * 推荐关于spring-boot 2.x整合quartz的教程：
 *      1、SpringBoot2.0集成Quartz【https://www.jianshu.com/p/dc814e8014b0】
 *      2、第四十七章：SpringBoot2.0新特性 - Quartz自动化配置集成【https://www.jianshu.com/p/056281e057b3】
 * 建表的sql：
 *      推荐直接去这篇博客拷贝就行了:
 *      quartz 建表语句!(2.3.0版本/oracle/mysql)【https://blog.csdn.net/liukangjie520/article/details/81169269】
 * @author wjy
 */
@Component
@Slf4j
public class ShiroSpringApplicationEvent implements ApplicationListener<ApplicationReadyEvent> {


    @Autowired
    private Scheduler scheduler;

    /**
     * 目前spring boot中支持的事件类型如下
     *     ApplicationFailedEvent：该事件为spring boot启动失败时的操作
     *     ApplicationPreparedEvent：上下文context准备时触发
     *     ApplicationReadyEvent：上下文已经准备完毕的时候触发
     *     ApplicationStartedEvent：spring boot 启动监听类
     *     SpringApplicationEvent：获取SpringApplication
     *     ApplicationEnvironmentPreparedEvent：环境事先准备
     * @param springApplicationEvent
     */
    @Override
    public void onApplicationEvent(ApplicationReadyEvent springApplicationEvent) {
        log.info("=========监听spring上下文初始化完成，给quartz添加定时任务=========");

        JobDetail jobDetail = JobBuilder.newJob(SessionValidationJob.class)
                .withIdentity("SessionValidationScheduler-"+1, "SessionValidationScheduler")
                .withDescription("定时验证session过期的任务调度器")
                .build();

        //cron表达式，在线生成：http://cron.qqe2.com/
        CronScheduleBuilder scheduleBuilder = CronScheduleBuilder
                //多少分钟之后开始执行，每隔多少分钟执行一次
                //这里测试我设置1分钟
                .cronSchedule(String.format("%d 0/%d * * * ? ",30,30))
                .withMisfireHandlingInstructionDoNothing();

        CronTrigger cronTrigger = TriggerBuilder.newTrigger()
                .withIdentity("SessionValidationScheduler-"+1, "SessionValidationScheduler")
                .withDescription("定时验证session过期的任务调度器")
                .withSchedule(scheduleBuilder)
                .startNow()
                .build();

        try {
            scheduler.scheduleJob(jobDetail,cronTrigger);
        } catch (SchedulerException e) {
            e.printStackTrace();
            log.error("[ShiroSpringApplicationEvent]定时任务添加失败："+e.getMessage());
        }
    }
}
