package com.wsj.server.util;

import com.wsj.notice.message.FsNotice;
import org.springframework.context.annotation.Configuration;

@Configuration
public class NoticeUtil {

    @FsNotice
    public String send(String msg) {
        return msg;
    }
}
