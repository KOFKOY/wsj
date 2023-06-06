package com.wsj.server.controller;

import com.wsj.notice.entity.WsjTest;
import com.wsj.notice.message.FsNotice;
import com.wsj.server.api.ClashApi;
import com.wsj.server.util.ClashUtil;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import javax.annotation.Resource;

@RestController
@RequestMapping("/api")
public class ClashController {
    @Resource
    ClashApi clashApi;


    //测试2

    @GetMapping("/clash")
    @FsNotice
    public String test() throws Exception {
        ClashUtil.updateNode();
        return "更新文件成功~~~";
    }
}
