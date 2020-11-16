package com.xxxx.server.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 测试
 */

@RestController
public class HelloController {

    @PostMapping("/hello")
    public String hello(){
        return "hello";
    }

    @GetMapping("/employee/base/hello")
    public String hello2(){
        return "/employee/base/hello";
    }

    @GetMapping("/employee/advanced/hello")
    public String hello3(){
        return "/employee/advanced/hello";
    }
}
