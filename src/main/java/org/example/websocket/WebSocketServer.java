package org.example.websocket;

import com.alibaba.fastjson2.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.example.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.util.Date;
import java.util.concurrent.CopyOnWriteArraySet;


/**
 * @Version v1.0
 * @Description:
 * @Author Lee
 * @Date 2023/7/18 13:38
 * @Copyright 李子木
 */
@Slf4j
@Component
@ServerEndpoint(value = "/api/websocket/{from}/{type}")
public class WebSocketServer {

    @Autowired
    private MessageService messageService;

    public static WebSocketServer webSocketServer;

    // 所有的连接会话
    private static CopyOnWriteArraySet<SessionWrap> sessionList = new CopyOnWriteArraySet<>();

    private String from;
    private String type;

    @PostConstruct
    public void init() {
        webSocketServer = this;
        webSocketServer.messageService = this.messageService;
    }

    /**
     * @author Lee
     * @date 2023/7/18 13:57
     * @description 创建连接
     */
    @OnOpen
    public void onOpen(Session session, @PathParam(value = "from") String from, @PathParam(value = "type") String type) {
        this.from = from;
        this.type = type;
        try {
            // 遍历list，如果有会话，更新，如果没有，创建一个新的
            for (SessionWrap item : sessionList) {
                if (item.getFrom().equals(from) && item.getType().equals(type)) {
                    item.setSession(session);
                    item.setLastTime(new Date());
                    log.info("【websocket消息】更新连接，总数为:" + sessionList.size());
                    return;
                }
            }
            SessionWrap sessionWrap = new SessionWrap();
            sessionWrap.setFrom(from);
            sessionWrap.setType(type);
            sessionWrap.setSession(session);
            sessionWrap.setLastTime(new Date());
            sessionList.add(sessionWrap);
            log.info("【websocket消息】有新的连接，总数为:" + sessionList.size());
        } catch (Exception e) {
            log.info("【websocket消息】连接失败！错误信息：" + e.getMessage());
        }
    }

    /**
     * @author Lee
     * @date 2023/7/18 13:57
     * @description 关闭连接
     */
    @OnClose
    public void onClose() {
        try {
            sessionList.removeIf(item -> item.getFrom().equals(from) && item.getType().equals(type));
            log.info("【websocket消息】连接断开，总数为:" + sessionList.size());
        } catch (Exception e) {
            log.info("【websocket消息】连接断开失败！错误信息：" + e.getMessage());
        }
    }

    /**
     * @author Lee
     * @date 2023/7/18 14:04
     * @description 发送消息
     */
    @OnMessage
    public void onMessage(String message, Session session) {
        try {
            JSONObject r = webSocketServer.messageService.insertMessage(message);
            String userId = r.getString("userId");
            for (SessionWrap item : sessionList) {
                if (item.getFrom().equals(userId) && item.getType().equals("test")) {
                    item.getSession().getBasicRemote().sendText(r.toJSONString());
                    log.info("【websocket消息】发送消息成功:" + r.toJSONString());
                }
            }
        } catch (Exception e) {
            log.info("【websocket消息】发送消息失败！错误信息：" + e.getMessage());
        }
    }

    @OnError
    public void onError(Session session, Throwable error) {

        log.error("用户错误,原因:"+error.getMessage());
        error.printStackTrace();
    }

}
