package com.itheima;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import tk.mybatis.spring.annotation.MapperScan;

/**
 * @author 高策
 * @version V1.0
 * @Package com.itheima
 * @date 2019/12/6 20:12
 * @Copyright © 2018-2019 黑马程序员（顺义）校区
 */
@SpringBootApplication
@EnableDiscoveryClient
@MapperScan("com.itheima.item.dao")
public class LyItemApplication {
    public static void main(String[] args) {
        SpringApplication.run(LyItemApplication.class, args);
    }
}
