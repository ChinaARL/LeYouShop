package com.itheima;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;

/**
 * @author 高策
 * @version V1.0
 * @Package com.itheima
 * @date 2019/12/6 19:50
 * @Copyright © 2018-2019 黑马程序员（顺义）校区
 */
@SpringBootApplication
@EnableDiscoveryClient
@EnableZuulProxy
public class LyGatewayApplication {
    public static void main(String[] args) {
        SpringApplication.run(LyGatewayApplication.class, args);
    }
}
