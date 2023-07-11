package com.wsj.server.config;

import cn.hutool.core.lang.hash.Hash;
import com.wsj.server.util.NoticeUtil;
import org.springframework.web.socket.*;
import javax.annotation.Resource;
import javax.swing.table.TableRowSorter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SocketHandler implements WebSocketHandler {

    private Map<String, WebSocketSession> sessionMap = new HashMap<>();

    private Map<String, List<String>> msgList = new HashMap<>();

    @Resource
    NoticeUtil noticeUtil;

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        sessionMap.put(session.getId(), session);
        List<String> list = new ArrayList<>();
        msgList.put(session.getId(),list);
        noticeUtil.send("有新客户端连接进来，ID:" + session.getId());
    }

    @Override
    public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {
        // 处理收到的消息
        String receivedMessage = (String) message.getPayload();
        // 在此处添加自定义的消息处理逻辑
        //noticeUtil.send("收到的消息：" + receivedMessage);
        if (msgList.containsKey(session.getId())) {
            msgList.get(session.getId()).add(receivedMessage);
        }else {
            List<String> list = new ArrayList<>();
            list.add(receivedMessage);
            msgList.put(session.getId(),list);
        }
    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
        // 处理传输错误
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus) throws Exception {
        sessionMap.remove(session.getId());
        //noticeUtil.send("客户端断开连接：" + session.getId());
    }

    @Override
    public boolean supportsPartialMessages() {
        return false;
    }

    public void sendMessageToAllClients(String message){
        sessionMap.values().forEach(f->{
            try {
                f.sendMessage(new TextMessage(message));
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }
    public Map<String, List<String>> getClientSessions() {
        return msgList;
    }

    public boolean sendMessage(String message, String id) throws IOException {
        if (sessionMap.containsKey(id)) {
            sessionMap.get(id).sendMessage(new TextMessage(message));
            return true;
        }
        return false;
    }
}

