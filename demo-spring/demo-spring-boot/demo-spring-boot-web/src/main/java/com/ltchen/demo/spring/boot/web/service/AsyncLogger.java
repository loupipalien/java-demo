package com.ltchen.demo.spring.boot.web.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

/**
 * @author: 01139983
 * 异步调用
 * https://segmentfault.com/a/1190000013974727
 */
@Slf4j
@Service
public class AsyncLogger {

    @Async
    public void logging(String message) {
        log.info("Async logging: {}", message);
    }
}
