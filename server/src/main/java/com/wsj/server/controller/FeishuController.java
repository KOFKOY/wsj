package com.wsj.server.controller;

import cn.hutool.json.JSONUtil;
import com.wsj.server.util.NoticeUtil;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/fs")
public class FeishuController {
    @Resource
    private NoticeUtil noticeUtil;
    @PostMapping
    public Map feishu(@RequestBody Map map) {
        Map result = new HashMap();
        if (map.containsKey("challenge")) {
            result.put("challenge", map.get("challenge"));
            return result;
        }
        noticeUtil.send(JSONUtil.toJsonStr(map));
        return result;
    }
}
