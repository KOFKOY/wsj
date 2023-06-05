package com.wsj.server.util;

import cn.hutool.extra.spring.SpringUtil;
import cn.hutool.json.JSON;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import com.wsj.server.api.ClashApi;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.system.ApplicationHome;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class ClashUtil {

    public static void updateNode(){
        ClashApi clash = SpringUtil.getBean(ClashApi.class);
        String test = clash.nodeList();
        Pattern pattern = Pattern.compile("(?<=href=\")https://\\S+\\.html");
        Matcher matcher = pattern.matcher(test);
        if (!matcher.find(0)) {
            return;
        }
        //获取最新的节点地址
        String nodeUrl = matcher.group();
        String node = clash.node(nodeUrl);
        Pattern compile = Pattern.compile("https://\\S+\\.yaml");
        Matcher matcher1 = compile.matcher(node);
        if (!matcher1.find(0)) {
           return;
        }
        String resultUrl = matcher1.group();
        String test2 = "https://clashnode.com/wp-content/uploads/2023/06/20230604.yaml";
        String content = clash.node(test2);
        //可能需要调整
        Yaml yaml = new Yaml();
        Map hashMap = yaml.loadAs(content, Map.class);
        if (hashMap.containsKey("proxies")) {
            List array =(List) hashMap.get("proxies");
            List<String> nameList = new ArrayList<>();
            Iterator<Object> iterator = array.iterator();
            while (iterator.hasNext()) {
                Object obj = iterator.next();
                Map map = (Map) obj;
                String type = (String) map.getOrDefault("type",null);
                if ("vless".equals(type)) {
                    nameList.add((String) map.get("name"));
                    iterator.remove();
                }
            }
            if(hashMap.containsKey("proxy-groups")){
                List arr = (List)hashMap.get("proxy-groups");
                for (Object o : arr) {
                    Map obj = (Map)o;
                    List proxies = (List)obj.get("proxies");
                    Iterator<Object> iterator1 = proxies.iterator();
                    while (iterator1.hasNext()) {
                        String next = (String)iterator1.next();
                        if (nameList.contains(next)) {
                            iterator1.remove();
                        }
                    }
                }
            }

            DumperOptions dumperOptions = new DumperOptions();
            dumperOptions.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
            Yaml wyaml = new Yaml(dumperOptions);
            String savePath = getSavePath();
            File dumpfile = new File(savePath + "clash.yaml"); //保存yaml
            try(FileWriter writer = new FileWriter(dumpfile)) {
                wyaml.dump(hashMap, writer);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }


    public static String getSavePath() {
        // 这里需要注意的是ApplicationHome是属于SpringBoot的类
        // 获取项目下resources/static/img路径
        ApplicationHome applicationHome = new ApplicationHome(ClashUtil.class);

        // 保存目录位置根据项目需求可随意更改
        return applicationHome.getDir().getParentFile()
                .getParentFile().getAbsolutePath() + "\\src\\main\\resources\\static\\";
    }
}
