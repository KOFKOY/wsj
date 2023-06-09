package com.wsj.server.job;

import com.wsj.notice.message.FsNotice;
import com.wsj.server.util.ClashUtil;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@EnableAsync
@Component
public class ClashJob {
//    @Recover
//    public String testRecover(Exception e) {
//        System.out.println("结束");
//        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        return sf.format(new Date()) + " 解析节点失败: " + e.getMessage()+"\n" + "地址:https://clashnode.com/";
//    }

//    @Scheduled(cron = "30 * * * * *")
//    @Retryable(value = Exception.class)
    @Scheduled(cron = "0 1 0/4 * * *")
    @Async
    @FsNotice
    public String clash() throws Exception {
        return ClashUtil.updateClashBode();
    }

    @Scheduled(cron = "0 1 0/4 * * *")
    @Async
    @FsNotice
    public String clash2() throws Exception {
        return ClashUtil.updateNodeFree();
    }

    @Scheduled(cron = "0 1 0/4 * * *")
    @Async
    @FsNotice
    public String clash3() throws Exception {
        return ClashUtil.updateClashFree();
    }

    @Scheduled(cron = "0 1 0/4 * * *")
    @Async
    @FsNotice
    public String clash4() throws Exception {
        return ClashUtil.githubV2rayToClash();
    }




}
