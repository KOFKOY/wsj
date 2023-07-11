package com.wsj.server.controller;

import com.dtflys.forest.annotation.Get;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wsj.server.config.SocketHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.socket.WebSocketSession;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/websocket")
public class WebSocketController {

    @Resource
    private SocketHandler socketHandler;
    @Resource
    ObjectMapper mapper;

    @GetMapping("/list")
    public Map<String, List<String>> list() throws JsonProcessingException {
        return socketHandler.getClientSessions();
    }

    @GetMapping("/sendMsg/{id}/{msg}")
    public String sendMsg(@PathVariable("id") String id, @PathVariable("msg") String msg) throws IOException {
        return socketHandler.sendMessage(msg, id) ? "发送消息成功：" + msg : "发送失败，可能没有此ID";
    }

    @GetMapping("/batchSendMsg/{msg}")
    public String batchSendMsg(@PathVariable("msg") String msg) throws IOException {
        socketHandler.sendMessageToAllClients(msg);
        return "群发送消息成功：" + msg;
    }
}
