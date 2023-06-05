package com.wsj.notice.message;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class TestCon {

    @GetMapping("/test")
    @FsNotice
    public String test(){
        return "hello";
    }
}
