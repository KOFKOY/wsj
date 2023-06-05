package com.wsj.notice.fsnotice.entity;
import com.wsj.notice.fsnotice.aspect.MessageAspect;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@ConfigurationProperties(prefix = "feishu",ignoreInvalidFields = true,ignoreUnknownFields = true)
@Configuration
public class FsConfig {
    private String appId;
    private String appSecret;
    private String verificationToken;
    private String encryptKey;
    private String openIds;

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getAppSecret() {
        return appSecret;
    }

    public void setAppSecret(String appSecret) {
        this.appSecret = appSecret;
    }

    public String getVerificationToken() {
        return verificationToken;
    }

    public void setVerificationToken(String verificationToken) {
        this.verificationToken = verificationToken;
    }

    public String getEncryptKey() {
        return encryptKey;
    }

    public void setEncryptKey(String encryptKey) {
        this.encryptKey = encryptKey;
    }

    public String getOpenIds() {
        return openIds;
    }

    public void setOpenIds(String openIds) {
        this.openIds = openIds;
    }
}
