package com.xin.portal.system.config;

/*import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;*/
import org.springframework.context.annotation.Bean;
/*import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.junit4.SpringRunner;*/
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

/*@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)*/
public class WebSocketConfig {
    @Bean
    public ServerEndpointExporter serverEndpointExporter() {
        return new ServerEndpointExporter();
    }

}
