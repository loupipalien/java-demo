package com.ltchen.demo.spring.boot.web.service;

import com.ltchen.demo.spring.boot.web.model.Greeting;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.atomic.AtomicLong;

/**
 * @author: 01139983
 */
@Slf4j
@Service
public class GreetingService {

    private static final String template = "Hello, %s!";
    private final AtomicLong counter = new AtomicLong();

    @Autowired
    private AsyncLogger logger;
    public Greeting greeting(String name) {
        // 同步调用
        log.info("Greeting method is invoked");
        // 异步调用 (会使用其他线程)
        logger.logging("Greeting method is invoked");
        return new Greeting(counter.incrementAndGet(), String.format(template, name));
    }
}
