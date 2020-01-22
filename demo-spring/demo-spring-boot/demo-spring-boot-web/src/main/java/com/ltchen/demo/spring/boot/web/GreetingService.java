package com.ltchen.demo.spring.boot.web;

import org.springframework.stereotype.Service;

import java.util.concurrent.atomic.AtomicLong;

/**
 * @author: 01139983
 */
@Service
public class GreetingService {

    private static final String template = "Hello, %s!";
    private final AtomicLong counter = new AtomicLong();

    public Greeting greeting(String name) {
        return new Greeting(counter.incrementAndGet(), String.format(template, name));
    }
}
