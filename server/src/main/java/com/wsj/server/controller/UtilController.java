package com.wsj.server.controller;

import com.wsj.notice.message.FsNotice;
import com.wsj.server.Constant;
import com.wsj.server.api.BaseApi;
import com.wsj.server.util.ClashUtil;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import javax.annotation.Resource;

@RestController
@RequestMapping("/api")
public class UtilController {
    @Resource
    BaseApi clashApi;

    @GetMapping("/clash")
    @FsNotice
    public String clash() throws Exception {
        ClashUtil.githubV2rayToClash();
//        ClashUtil.updateNodeFree();
//        ClashUtil.updateClashFree();
        return "更新3个订阅文件成功~~~";
    }
    @GetMapping("/log")
    @FsNotice
    public String log(){
        Constant.SHOWLOG = !Constant.SHOWLOG;
        return (Constant.SHOWLOG ? "打开" : "关闭") + "日志";
    }
}
