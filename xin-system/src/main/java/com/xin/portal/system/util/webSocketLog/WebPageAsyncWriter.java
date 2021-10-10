package com.xin.portal.system.util.webSocketLog;

import java.io.IOException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * 消息读取器
 * 
 */
public class WebPageAsyncWriter {

    private static final Logger log = LogManager.getLogger(WebPageAsyncWriter.class);

    static {
        powerBoosterOn();
    }

    /**
     * log消息队列。此队列为先进先出的阻塞队列
     * 阻塞的意思是：取的时候若队列空则一直等到队列有元素并拿到为止，不然势不罢休；
     *           放的时候若队列没有位置，则一直等到有空位且放进去为止，不然势不罢休
     */
    private static final BlockingQueue<String> msgQueue = new LinkedBlockingQueue<String>();

    /**
     * 启动强力推进装置
     */
    public static void powerBoosterOn() {
        Thread notifier = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        String message = msgQueue.take();//一直拿消息，不拿到此线程就阻塞
                        for (WebPageWebSocket w : WebPageWebSocket.allSocket) {
                            w.sendMessage(message);//给所有会话发送消息
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                        if (log.isErrorEnabled()) {
                            log.error("WebPageAsyncWriter InterruptedException 异常！msgQueue.take()");
                        }
                    }
                }
            }
        });
        notifier.setName("日志监控线程");
        notifier.start();
    }

    /**
     * appender通过此方法将消息放进来
     * 
     * @param message
     * @throws IOException
     */
    public static void write(String message) throws IOException {
        try {
            msgQueue.put(message);
        } catch (Exception ex) {
            IOException t = new IOException();
            t.initCause(ex);
            throw t;
        }
    }

}
