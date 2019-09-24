package com.ltchen.demo.spring.boot.web;

import com.ltchen.demo.spring.boot.web.upload.StorageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.ApplicationContext;
import org.springframework.web.client.RestTemplate;

import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author: 01139983
 */
@Slf4j
@SpringBootApplication
public class Application implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Autowired
    ApplicationContext context;
    @Autowired
    private RestTemplateBuilder builder;
    @Autowired
    private StorageService storageService;
    @Override
    public void run(String... strings) throws Exception {
        // 测试时注释使用字段域和方法内容
        RestTemplate template = builder.build();
        try {
            Greeting greeting = template.getForObject("http://localhost:" + context.getEnvironment().getProperty("server.port") + "/rest/greeting", Greeting.class);
            log.info("Rest greeting: {}", greeting);
        } catch (Exception e) {
            // swallow exception message
        }

        // upload file
        storageService.deleteAll();
        storageService.init();
    }
}
