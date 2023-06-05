package com.wsj.notice.fsnotice.config;

import com.lark.oapi.core.Config;
import com.wsj.notice.fsnotice.entity.FsConfig;
import com.wsj.notice.fsnotice.message.MessageSend;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Configuration
public class BaseConfig {
    @Resource
    FsConfig fsConfig;

    @Bean
    @ConditionalOnClass(value = {FsConfig.class})
    public Config initFsConfig(){
        Config config = new Config();
        config.setAppId(fsConfig.getAppId());
        config.setAppSecret(fsConfig.getAppSecret());
        return config;
    }

    @Bean
    @ConditionalOnMissingBean(MessageSend.class)
    public MessageSend initMessageSend(){
        return new MessageSend();
    }

}
