package com.celesky;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.scheduling.annotation.EnableScheduling;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@EnableDiscoveryClient
@EnableScheduling
@EnableSwagger2
public class ZpubServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(ZpubServiceApplication.class, args);
    }
}
