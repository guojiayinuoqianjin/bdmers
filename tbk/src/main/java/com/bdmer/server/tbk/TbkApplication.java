package com.bdmer.server.tbk;

import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.annotation.Order;
import org.springframework.scheduling.annotation.EnableAsync;

@Slf4j
@EnableDiscoveryClient
@EnableEurekaClient
@EnableFeignClients
@Order(2)
@EnableAsync
@ComponentScan({"com.bdmer.framework.base", "com.bdmer.server.tbk"})
@SpringBootApplication
public class TbkApplication {

    public static void main(String[] args) {
        SpringApplication.run(TbkApplication.class, args);
        log.info("淘宝客服务启动成功");
    }
}
