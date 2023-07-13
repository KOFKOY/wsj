package com.wsj.server.controller;

import com.wsj.server.api.BaseApi;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/test")
public class TestController {

    @Resource
    BaseApi clashApi;

    @GetMapping
    public String test(){
        String request = clashApi.getRequest("https://svip.bljiex.cc/?v=https://m.iqiyi.com/v_111a08k45as.html");
        System.out.println(request);
        return "ok";
    }
}
