package com.wsj.server.controller;

import cn.hutool.json.JSONUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wsj.server.config.SocketHandler;
import com.wsj.server.entity.RebotEvent;
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
    @Resource
    private ObjectMapper mapper;
    @Resource
    private SocketHandler socketHandler;

    @PostMapping
    public Map feishu(@RequestBody Map map) throws JsonProcessingException {
        noticeUtil.send(JSONUtil.toJsonStr(map));
        Map result = new HashMap();
        if (map.containsKey("challenge")) {
            result.put("challenge", map.get("challenge"));
            return result;
        }
        RebotEvent rebotEvent = mapper.readValue(JSONUtil.toJsonStr(map), RebotEvent.class);
        if("10000".equals(rebotEvent.getEvent().getEvent_key())){
            noticeUtil.forceSend(JSONUtil.toJsonStr(socketHandler.getClientSessions()));
        }
        return result;
    }
}
