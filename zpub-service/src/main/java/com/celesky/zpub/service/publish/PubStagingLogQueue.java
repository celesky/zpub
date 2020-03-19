package com.celesky.zpub.service.publish;

import com.celesky.zpub.entity.PublishStagingLog;
import com.celesky.zpub.mapper.PublishStagingLogMapper;
import com.zuzuche.msa.threadpool.ThreadPoolExecutorFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.*;

/**
 * @desc: 发布日志收集队列,并持久化到数据库
 * @author: panqiong
 * @date: 2019-12-30
 */
@Slf4j
@Component
public class PubStagingLogQueue implements InitializingBean {

    private static volatile boolean running = true;

    private static ExecutorService executor = ThreadPoolExecutorFactory.create(
            ThreadPoolExecutorFactory.Config.builder()
                    .corePoolSize(1)
                    .maximumPoolSize(1)
                    .keepAliveTime(5)
                    .workQueue(new SynchronousQueue())
                    .unit(TimeUnit.MINUTES)
                    .handler(new ThreadPoolExecutor.DiscardPolicy())
                    .threadPoolName("persistengPublishStatingLogPool")
                    .build());

    /**
     * 发布日志队列
     */
    private final static ArrayBlockingQueue<PublishStagingLog> queue = new ArrayBlockingQueue(500);


    @Autowired
    PublishStagingLogMapper stagingLogMapper;

    public static void push(PublishStagingLog stagingLog){
        try {
            boolean result = queue.offer(stagingLog);
            if(!result){
                log.error("[日志入队失败]:result:"+result+" "+stagingLog.getPubLogId());
            }
        } catch (Exception e) {
            log.error("日志入队异常",e.getCause(),e);
        }
    }


    private void persistent(){
        executor.submit(()->{
            while (running){
                try {
                    PublishStagingLog stagingLog =  queue.take();
                    stagingLogMapper.insert(stagingLog);

                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    log.error("日志持久化线程中断",e.getCause(),e);
                }

                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    log.error("线程中断",e.getCause(),e);
                }
            }
        });

    }

    public static void close(){
        if(queue.size()>0){
            log.info("目前发布日志队列还有"+queue.size()+"条没有入库,等待10秒...");
            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        running = false;
    }


    @Override
    public void afterPropertiesSet() throws Exception {
        persistent();
    }
}
