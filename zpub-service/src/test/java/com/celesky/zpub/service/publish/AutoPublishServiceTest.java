package com.celesky.zpub.service.publish;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class AutoPublishServiceTest {

    @Autowired
    AutoPublishService autoPublishService;

    @Test
    public void createPublishTask() {
        autoPublishService.createPublishTask("panqiong","sentry-service","harbor-release.zuzuche.net/zzc/sentry-service:master_20200120_221654",false);

        try {
            Thread.sleep(500000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void savePubLog() {
    }

    @Test
    public void updateLogResult() {
    }
}