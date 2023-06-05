package com.wsj.notice.fsnotice.aspect;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.lark.oapi.core.Config;
import com.lark.oapi.service.contact.v3.ContactService;
import com.lark.oapi.service.contact.v3.model.FindByDepartmentUserReq;
import com.lark.oapi.service.contact.v3.model.FindByDepartmentUserResp;
import com.lark.oapi.service.contact.v3.model.User;
import com.wsj.notice.fsnotice.entity.FsConfig;
import com.wsj.notice.fsnotice.message.FsNotice;
import com.wsj.notice.fsnotice.message.MessageSend;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Aspect
@Configuration
public class MessageAspect {
    @Resource
    MessageSend send;
    @Resource
    FsConfig fsConfig;
    @Resource
    Config config;

    @Pointcut(value = "@annotation(com.wsj.notice.fsnotice.message.FsNotice)")
    public void fmsg(){
    }

    @AfterReturning(value = "fmsg()&&@annotation(fsNotice)",returning="returnValue")
    public void sendMessage(JoinPoint jp,Object returnValue, FsNotice fsNotice) throws Exception {
        String[] ids = fsNotice.openIds();
        if (ObjectUtil.isNull(ids) || ids.length == 0 ||"".equals(ids[0])) {
            String openIds = fsConfig.getOpenIds();
            if(openIds!=null&&!openIds.equals("")){
                ids = openIds.split(",");
            }
        }
        if (ObjectUtil.isNull(ids) || ids.length == 0) {
            return;
        }
        Object msg = StrUtil.isBlank(fsNotice.msg()) ? returnValue : fsNotice.msg();
        if (ObjectUtil.isNull(msg) || msg.toString().length()==0) {
            return;
        }
        send.patchMessage(msg.toString(), getOpenIds(ids));
    }

    public List<String> getOpenIds(String[] ids) throws Exception {
        User[] userByDept = getUserByDept();
        Map<String, User> collect = Arrays.stream(userByDept).collect(Collectors.toMap(x -> x.getName(), Function.identity()));
        List<String> result = new ArrayList<>();
        for (String id : ids) {
            User orDefault = collect.getOrDefault(id, null);
            if (orDefault != null) {
                result.add(orDefault.getOpenId());
            }
        }
        return result;
    }
    public User[] getUserByDept() throws Exception {
        ContactService.User user = new ContactService.User(config);
        FindByDepartmentUserResp byDepartment = user.findByDepartment(FindByDepartmentUserReq.newBuilder()
                .departmentId("0")
                .build());
        return byDepartment.getData().getItems();
    }
}
