package com.example.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

/**
 *  websocket 配置类
 * @author Taopeng
 */
@Configuration
public class WebSocketConfig {
    /**
     * 注入一个ServerEndpointExporter,
     * 该Bean会自动注册使用@ServerEndpoint注解申明的websocket endpoint
     */
    /**该@Bean会导致@Test功能失效**/
    @Bean  //给容器中添加组件。以方法名作为组件的id。返回类型就是组件类型。返回的值，就是组件在容器中的实例
    public ServerEndpointExporter serverEndpointExporter(){
        return new ServerEndpointExporter();
    }
}
