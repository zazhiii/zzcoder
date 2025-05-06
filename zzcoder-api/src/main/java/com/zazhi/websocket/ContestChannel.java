package com.zazhi.websocket;

import java.io.IOException;

import com.zazhi.service.ContestService;
import com.zazhi.service.impl.ContestServiceImpl;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;

import jakarta.websocket.CloseReason;
import jakarta.websocket.EndpointConfig;
import jakarta.websocket.OnClose;
import jakarta.websocket.OnError;
import jakarta.websocket.OnMessage;
import jakarta.websocket.OnOpen;
import jakarta.websocket.Session;
import jakarta.websocket.server.ServerEndpoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

@Slf4j
// 使用 @ServerEndpoint 注解表示此类是一个 WebSocket 端点
// 通过 value 注解，指定 websocket 的路径
@ServerEndpoint(value = "/contest/{contestId}")
@Component
public class ContestChannel {

    @Autowired
    private ContestService contestService;

    // 收到消息
    @OnMessage
    public void onMessage(String message) throws IOException{

    }

    // 连接打开
    @OnOpen
    public void onOpen(Session session, EndpointConfig endpointConfig) throws IOException {
        log.info("连接打开：{}", session);


    }

    // 连接关闭
    @OnClose
    public void onClose(CloseReason closeReason){

    }

    // 连接异常
    @OnError
    public void onError(Throwable throwable) throws IOException {

    }

    public void broadcast(Integer contestId, String message) {

    }
}