package com.wsj.notice.message;

import cn.hutool.json.JSONUtil;
import com.lark.oapi.Client;
import com.lark.oapi.core.Config;
import com.lark.oapi.core.response.RawResponse;
import com.lark.oapi.core.token.AccessTokenType;
import com.lark.oapi.core.utils.Jsons;
import com.lark.oapi.service.im.v1.ImService;
import com.lark.oapi.service.im.v1.model.CreateMessageReq;
import com.lark.oapi.service.im.v1.model.CreateMessageReqBody;
import com.lark.oapi.service.im.v1.model.CreateMessageResp;
import com.wsj.notice.entity.FsConfig;
import com.wsj.notice.entity.PatchMessageBody;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MessageSend {

    @Resource
    private Config config;

    public void patchMessage(String msg, List<String> openIdList) throws Exception {
        //批量发送
//        Client build = Client.newBuilder(config.getAppId(), config.getAppSecret()).build();
//        PatchMessageBody body = new PatchMessageBody();
//        body.setMsg_type("text");
//        body.setContent(new PatchMessageBody.ContentEntity().setText(msg));
        //ou_6df1ecb8dd56e8dee488c02cb45b57fd
//        body.setOpen_ids(openIdList);
//        RawResponse post = build.post("https://open.feishu.cn/open-apis/message/v4/batch_send/", body, AccessTokenType.Tenant);
        Map<String, String> contentMap = new HashMap<>();
        contentMap.put("text", msg);
        for (String openId : openIdList) {
            ImService.Message message = new ImService.Message(config);
            CreateMessageResp createMessageResp = message.create(CreateMessageReq.newBuilder()
                    .receiveIdType("open_id")
                    .createMessageReqBody(CreateMessageReqBody.newBuilder()
                            .msgType("text")
                            .content(Jsons.DEFAULT.toJson(contentMap))
                            .receiveId(openId)
                            .build())
                    .build());
        }
    }

}
