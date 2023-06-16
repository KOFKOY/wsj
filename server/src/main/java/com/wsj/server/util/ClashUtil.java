package com.wsj.server.util;

import cn.hutool.extra.spring.SpringUtil;
import com.wsj.server.api.ClashApi;
import org.springframework.boot.system.ApplicationHome;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ClashUtil {

    public static String updateClashBode() throws Exception {
        String url = "https://clashnode.com";
        ClashApi clash = SpringUtil.getBean(ClashApi.class);
        String test = clash.getRequest("https://clashnode.com");
        Pattern pattern = Pattern.compile("(?<=href=\")https://\\S+\\.html");
        Matcher matcher = pattern.matcher(test);
        if (!matcher.find(0)) {
            return url + "正则匹配最新节点html地址失败" ;
        }
        //获取最新的节点地址
        String nodeUrl = matcher.group();
        String node = clash.getRequest(nodeUrl);
        Pattern compile = Pattern.compile("https://\\S+\\.ya?ml");
        Matcher matcher1 = compile.matcher(node);
        if (!matcher1.find(0)) {
            return url + "正则匹配yaml地址失败" ;
        }
        String resultUrl = matcher1.group();
        String content = clash.getRequest(resultUrl);
        //可能需要调整
        Yaml yaml = new Yaml();
        Map hashMap = yaml.loadAs(content, Map.class);
        if (hashMap.containsKey("proxies")) {
            filter(hashMap,true);
            DumperOptions dumperOptions = new DumperOptions();
            dumperOptions.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
            Yaml wyaml = new Yaml(dumperOptions);
            String savePath = getSavePath();
            File dumpfile = new File(savePath + "clash.yaml"); //保存yaml
            try(FileWriter writer = new FileWriter(dumpfile)) {
                wyaml.dump(hashMap, writer);
            } catch (IOException e) {
                e.printStackTrace();
                return url +  "生成yaml失败" ;
            }
            System.out.println("解析节点成功  sucess !!!");
            return null;
        }
        return url + "解析节点失败" ;
    }

    public static String updateClashFree() throws Exception {
        String url = "https://clashfree.eu.org/";
        ClashApi clash = SpringUtil.getBean(ClashApi.class);
        String test = clash.getRequest(url);
        Pattern pattern = Pattern.compile("(?<=href=\")https://\\S+\\.html");
        Matcher matcher = pattern.matcher(test);
        if (!matcher.find(0)) {
            return url + "正则匹配最新节点html地址失败";
        }
        //获取最新的节点地址
        String nodeUrl = matcher.group();
        String node = clash.getRequest(nodeUrl);
        Pattern compile = Pattern.compile("https://\\S+\\.ya?ml");
        Matcher matcher1 = compile.matcher(node);
        if (!matcher1.find(0)) {
            return url + "正则匹配yaml地址失败";
        }
        String resultUrl = matcher1.group();
        String content = clash.getRequest(resultUrl);
        //可能需要调整
        Yaml yaml = new Yaml();
        Map hashMap = yaml.loadAs(content, Map.class);
        if (hashMap.containsKey("proxies")) {
            filter(hashMap,false);
            DumperOptions dumperOptions = new DumperOptions();
            dumperOptions.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
            Yaml wyaml = new Yaml(dumperOptions);
            String savePath = getSavePath();
            File dumpfile = new File(savePath + "clash3.yaml"); //保存yaml
            try(FileWriter writer = new FileWriter(dumpfile)) {
                wyaml.dump(hashMap, writer);
            } catch (IOException e) {
                e.printStackTrace();
                return url + "生成yaml失败";
            }
            System.out.println("解析节点成功  sucess !!!");
            return null;
        }
        return url + "解析节点失败";
    }


    public static String updateNodeFree() throws Exception {
        String url = "https://nodefree.org/t/clash";
        ClashApi clash = SpringUtil.getBean(ClashApi.class);
        String test = clash.getRequest(url);
        Pattern pattern = Pattern.compile("(?<=href=\")https://\\S+\\.html");
        Matcher matcher = pattern.matcher(test);
        if (!matcher.find(0)) {
            return url + "正则匹配最新节点html地址失败" ;
        }
        //获取最新的节点地址
        String nodeUrl = matcher.group();
        String node = clash.getRequest(nodeUrl);
        Pattern compile = Pattern.compile("https://\\S+\\.ya?ml");
        Matcher matcher1 = compile.matcher(node);
        if (!matcher1.find(0)) {
            return url + "正则匹配yaml地址失败" ;
        }
        String resultUrl = matcher1.group();
        String content = clash.getRequest(resultUrl);
        //可能需要调整
        Yaml yaml = new Yaml();
        Map hashMap = yaml.loadAs(content, Map.class);
        if (hashMap.containsKey("proxies")) {
            filter(hashMap,true);
            DumperOptions dumperOptions = new DumperOptions();
            dumperOptions.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
            Yaml wyaml = new Yaml(dumperOptions);
            String savePath = getSavePath();
            File dumpfile = new File(savePath + "clash2.yaml"); //保存yaml
            try(FileWriter writer = new FileWriter(dumpfile)) {
                wyaml.dump(hashMap, writer);
            } catch (IOException e) {
                e.printStackTrace();
                return url + "生成yaml失败" ;
            }
            System.out.println("解析节点成功  sucess !!!");
            return null;
        }
        return url + "解析节点失败" ;
    }

    private static void filter(Map hashMap,boolean autoSelect) {
        List array =(List) hashMap.get("proxies");
        List<String> nameList = new ArrayList<>();
        Iterator<Object> iterator = array.iterator();
        while (iterator.hasNext()) {
            Object obj = iterator.next();
            Map map = (Map) obj;
            String type = (String) map.getOrDefault("type",null);
            if ("vless".equals(type) || map.containsKey("plugin")) {
                nameList.add((String) map.get("name"));
                iterator.remove();
            }
        }
        if(hashMap.containsKey("proxy-groups")){
            List arr = (List) hashMap.get("proxy-groups");
            for (int i = 0; i < arr.size(); i++) {
                Map obj = (Map)arr.get(i);
                if (i == 0 && autoSelect && obj.containsKey("type")) {
                    obj.put("type", "url-test");
                    obj.put("url", "http://www.gstatic.com/generate_204");
                    obj.put("interval",300);
                }
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
    }


    public static String getSavePath() throws IOException {
        ApplicationHome h = new ApplicationHome(ClashUtil.class);
        File jarF = h.getSource();
        String staticPath = jarF.getParentFile().toString()+File.separator+"files"+File.separator;
        File newFile = new File(staticPath);
        if (!newFile.exists()) {
            newFile.mkdir();
        }
        return staticPath;
    }
}
