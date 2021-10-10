package com.xin.portal.system.util.webSocketLog;

import java.io.IOException;
import java.util.concurrent.CopyOnWriteArraySet;

import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

@Component
@ServerEndpoint("/websocketServerEndPoint")
public class WebPageWebSocket {

    private static final Logger log = LogManager.getLogger(WebPageWebSocket.class);

    static CopyOnWriteArraySet<WebPageWebSocket> allSocket = new CopyOnWriteArraySet<WebPageWebSocket>();
    private Session session;
    private static int onlineCount = 0;

    @OnOpen
    public void onOpen(Session session) {
        this.session = session;
        allSocket.add(this);
        addOnlineCount();
        if (log.isInfoEnabled()) {
            log.info("The current number of log readers is:" + onlineCount);
        }
    }

    @OnClose
    public void onClose() {
        allSocket.remove(this);
        subOnlineCount();
        if (log.isInfoEnabled()) {
            log.info("The current number of log readers is:" + onlineCount);
        }
    }

    @OnMessage
    public void onMessage(String message, Session session) {
        if (log.isInfoEnabled()) {
            log.info("Messages from the client:" + message);
        }

        for (WebPageWebSocket item : allSocket) {
            item.sendMessage(message);
        }
    }

    @OnError
    public void onError(Session session, Throwable error) {
        if (log.isInfoEnabled()) {
            log.info("Websocket connection error");
        }
        error.printStackTrace();
    }

    public void sendMessage(String message) {
        try {
            this.session.getBasicRemote().sendText(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static synchronized int getOnlineCount() {
        return onlineCount;
    }

    public static synchronized void addOnlineCount() {
        WebPageWebSocket.onlineCount++;
    }

    public static synchronized void subOnlineCount() {
        WebPageWebSocket.onlineCount--;
    }

}
