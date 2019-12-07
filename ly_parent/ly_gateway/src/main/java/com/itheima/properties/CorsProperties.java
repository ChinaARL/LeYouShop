package com.itheima.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 配置类 读取ly.cors 下所有信息
 *
 * @author 高策
 * @version V1.0
 * @Package com.itheima.properties
 * @date 2019/12/7 20:20
 * @Copyright © 2018-2019 黑马程序员（顺义）校区
 */

@Data
@Component
@Configuration
@ConfigurationProperties("ly.cors")
public class CorsProperties {
    private List<String> allowedOrigins;
    private List<String> allowedHeaders;
    private List<String> allowedMethods;
    private String filterPath;
    private Long maxAge;
    private Boolean allowedCredentials;

}
