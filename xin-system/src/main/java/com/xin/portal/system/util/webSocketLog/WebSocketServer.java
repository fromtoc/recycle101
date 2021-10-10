package com.xin.portal.system.util.webSocketLog;


import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * Created by cww on 2019/8/16.
 */
@ServerEndpoint(value = "/websocketNews/{userId}")
@Component
public class WebSocketServer {
    private static final Logger log = LogManager.getLogger(WebPageWebSocket.class);

    //concurrent包的线程安全Set，用来存放每个客户端对应的MyWebSocket对象。
    private static CopyOnWriteArraySet<WebSocketServer> webSocketSet = new CopyOnWriteArraySet<WebSocketServer>();

    //与某个客户端的连接会话，需要通过它来给客户端发送数据
    private Session session;

    /**
     * 连接建立成功调用的方法
     */
    @OnOpen
    public void onOpen(@PathParam("userId") String userId,Session session) {
        this.session = session;
        session.getUserProperties().put("userId", userId);//存入用户id  由于有多租户，在不同租户中存在相同用户名的用户。如果只是通过用户名去发送信息存在信息混乱的问题
        webSocketSet.add(this);     //加入set中

    }

    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void onClose(@PathParam("userId") String userId) {
        webSocketSet.remove(this);  //从set中删除

    }
    
    /**
     * 收到客户端消息后调用的方法
     *
     * @param message 客户端发送过来的消息
     */

  /*  @OnMessage
    public void onMessage(String count) {
        for (WebSocketServer item : webSocketSet) {
            try {

                String userPrincipal = String.valueOf(item.session.getUserPrincipal());
                if(count.equals(userPrincipal)){
                    item.sendMessage("3");
                }else {
                    item.sendMessage(count);
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }*/
    /**
     * 给所有在线用户发信息
     * @param count
     */
    @OnMessage
    public void onMessage(@PathParam("userId") String userId,String count) {
        for (WebSocketServer item : webSocketSet) {
            try {
            	item.sendMessage(count);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    
    /**
     * @param session
     * @param error
     */
    @OnError
    public void onError(@PathParam("userId") String userId,Session session, Throwable error) {
        log.error("发生错误");
        error.printStackTrace();
    }

    public void setonMessage(String count,String userId) {
        for (WebSocketServer item : webSocketSet) {
            try {
            	String sessionUserId = String.valueOf(item.session.getUserProperties().get("userId"));
                if(count=="3"){
                    if(sessionUserId!=null && userId.equals(sessionUserId)){
                        item.sendMessage(count);
                    }
                }
                if(count=="4"){
                    if(sessionUserId!=null && userId.equals(sessionUserId)){
                        item.sendMessage(count);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    /**
     * 实现服务器主动推送
     */
    public void  sendMessage(String count) throws IOException {
        this.session.getBasicRemote().sendText(count);
    }

}
