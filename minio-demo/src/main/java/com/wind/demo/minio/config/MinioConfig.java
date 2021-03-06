package com.wind.demo.minio.config;

import io.minio.MinioClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * minio配置
 *
 * @author wind
 */
@Configuration
public class MinioConfig {

    @Bean
    public MinioClient minioClient() {
        return MinioClient.builder().endpoint("http://127.0.0.1:9000")
                .credentials("minioadmin", "minioadmin").build();
    }
}
