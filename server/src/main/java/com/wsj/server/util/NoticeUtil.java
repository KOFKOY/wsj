package com.wsj.server.util;

import com.wsj.notice.message.FsNotice;
import com.wsj.server.Constant;
import org.apache.tomcat.util.bcel.Const;
import org.springframework.context.annotation.Configuration;

@Configuration
public class NoticeUtil {

    @FsNotice
    public String send(String msg) {
        if (!Constant.SHOWLOG) {
            return null;
        }
        return msg;
    }
}
