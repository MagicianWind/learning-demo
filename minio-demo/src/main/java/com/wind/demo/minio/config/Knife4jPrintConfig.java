package com.wind.demo.minio.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.context.WebServerInitializedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.net.Inet4Address;
import java.net.UnknownHostException;

/**
 * 控制台打印API文档地址
 *
 * @author wind
 */
@Slf4j
@Component
public class Knife4jPrintConfig implements ApplicationListener<WebServerInitializedEvent> {
    @Override
    public void onApplicationEvent(WebServerInitializedEvent event) {
        try {
            // 获取IP
            String host = Inet4Address.getLocalHost().getHostAddress();
            // 获取端口号
            int port = event.getWebServer().getPort();
            // 获取应用名
            String applicationName = event.getApplicationContext().getApplicationName();

            log.info("API文档地址: http://" + host + ":" + port + applicationName + "/doc.html");
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }
}
