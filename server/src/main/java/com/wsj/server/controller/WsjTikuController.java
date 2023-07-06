package com.wsj.server.controller;

import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wsj.server.api.BaseApi;
import com.wsj.server.entity.WsjTiku;
import com.wsj.server.service.IWsjTikuService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author wsj
 * @since 2023-07-06
 */
@RestController
@RequestMapping("/xxqg")
@Slf4j
public class WsjTikuController {
    @Resource
    private IWsjTikuService wsjTikuService;

    @Resource
    BaseApi api;

    @GetMapping("/search/{question}")
    public List<String> search(@PathVariable("question")String question,HttpServletRequest request) {
        String ipAddress = request.getHeader("X-Forwarded-For");
        if (ipAddress == null || ipAddress.isEmpty() || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getHeader("Proxy-Client-IP");
        }
        if (ipAddress == null || ipAddress.isEmpty() || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ipAddress == null || ipAddress.isEmpty() || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getRemoteAddr();
        }
        log.info("访问者ip:{}", ipAddress);
        if (CharSequenceUtil.isBlank(question)) {
            return new ArrayList<>();
        }
        return wsjTikuService.lambdaQuery().like(WsjTiku::getQuestion, question).list()
                .stream().map(x->x.getAnswer()).distinct().collect(Collectors.toList());
    }

    @GetMapping
    public void test() throws JsonProcessingException {
        String request = api.getRequest("https://raw.kgithub.com/OuO-dodo/tiku/master/dati_tiku_20230121.txt");
        ObjectMapper mapper = new ObjectMapper();
        List<List<String>> list2 = mapper.readValue(request, new TypeReference<List<List<String>>>() {});
        List<WsjTiku> insert = new ArrayList<>();
        List<WsjTiku> list = wsjTikuService.list();
        Map<String, WsjTiku> collect1 = list.stream().collect(Collectors.toMap(x -> x.getQuestion(), Function.identity(), (x1, x2) -> x2));
        Map<String, WsjTiku> dbMap = list.stream().collect(Collectors.toMap(x -> x.getAnswer(), Function.identity(), (x1, x2) -> x2));
        for (List<String> ssss : list2) {
            WsjTiku tiku = new WsjTiku();
            List<String> collect = ssss.stream().filter(x -> StrUtil.isNotBlank(x)).collect(Collectors.toList());
            if (collect.size()>1&&!collect1.containsKey(collect.get(0))) {
                if (!dbMap.containsKey(collect.get(1))) {
                    tiku.setQuestion(collect.get(0));
                    tiku.setAnswer(collect.get(1));
                    insert.add(tiku);
                }

            }
        }
        wsjTikuService.saveBatch(insert);
        log.info("ok");
    }

}
