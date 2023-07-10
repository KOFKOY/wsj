package com.wsj.server.config;

import java.util.ArrayList;
import java.util.List;

import com.wsj.server.util.NoticeUtil;
import org.aspectj.weaver.ast.Not;
import org.springframework.context.annotation.Configuration;
import org.springframework.retry.annotation.Recover;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;

import javax.annotation.Resource;

public class SocketHandler implements WebSocketHandler {

    private List<WebSocketSession> sessions = new ArrayList<>();

    @Resource
    NoticeUtil noticeUtil;

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        sessions.add(session);
        System.out.println(session.getId());
        System.out.println(session.toString());
        noticeUtil.send("有新客户端连接进来，ID:" + session.getId());
    }

    @Override
    public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {
        // 处理收到的消息
        String receivedMessage = (String) message.getPayload();
        // 在此处添加自定义的消息处理逻辑
        System.out.println(receivedMessage);
        noticeUtil.send("收到的消息" + receivedMessage);
    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
        // 处理传输错误
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus) throws Exception {
        sessions.remove(session);
    }

    @Override
    public boolean supportsPartialMessages() {
        return false;
    }

    public void sendMessageToAllClients(String message) throws Exception {
        for (WebSocketSession session : sessions) {
            session.sendMessage(new TextMessage(message));
        }
    }

    public List<WebSocketSession> getClientSessions() {
        return sessions;
    }
}

