package com.wsj.server.controller;
import com.wsj.notice.fsnotice.entity.FsConfig;
import com.wsj.notice.fsnotice.entity.WsjTest;
import com.wsj.notice.fsnotice.message.FsNotice;
import com.wsj.server.api.ClashApi;
import com.wsj.server.util.ClashUtil;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RestController
@RequestMapping("/api")
public class ClashController {
    @Resource
    ClashApi clashApi;

    @GetMapping("/clash")
//    @FsNotice
    public String test(){
        ClashUtil.updateNode();
        return "ok";
    }
}
