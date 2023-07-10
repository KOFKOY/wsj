package com.wsj.server.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeanConfig {
    @Bean
    public SocketHandler init(){
        return new SocketHandler();
    }
}
