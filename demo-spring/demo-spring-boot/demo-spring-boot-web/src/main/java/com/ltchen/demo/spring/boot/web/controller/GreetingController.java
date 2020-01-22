package com.ltchen.demo.spring.boot.web.controller;

import com.ltchen.demo.spring.boot.web.model.Greeting;
import com.ltchen.demo.spring.boot.web.service.GreetingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author: 01139983
 */
@Controller
public class GreetingController {

    @Autowired
    private GreetingService service;

    @ResponseBody
    @RequestMapping("rest/greeting")
    public Greeting restGreeting(@RequestParam(value="name", defaultValue="World") String name) {
        return service.greeting(name);
    }

    @GetMapping("greeting")
    public String mvcGreeting(@RequestParam(value="name", defaultValue="World") String name, Model model) {
        // TODO 参数与 application 中的配置同名, 优先获取了 application 中的值 ???
        model.addAttribute("username", name);
        return "greeting";
    }
}