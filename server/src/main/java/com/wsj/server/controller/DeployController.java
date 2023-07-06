package com.wsj.server.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;

@RestController
@RequestMapping("/deploy")
public class DeployController {

    @PostMapping
    public String deploy() throws Exception {
        // 切换到目标目录
        ProcessBuilder cdProcessBuilder = new ProcessBuilder("sh", "-c", "cd /home/wsj/wsj");
        cdProcessBuilder.redirectErrorStream(true);
        Process cdProcess = cdProcessBuilder.start();
        cdProcess.waitFor();

        // 执行git pull
        ProcessBuilder gitProcessBuilder = new ProcessBuilder("git", "pull");
        gitProcessBuilder.redirectErrorStream(true);
        Process gitProcess = gitProcessBuilder.start();
        gitProcess.waitFor();

        // 执行public.sh
        ProcessBuilder shProcessBuilder = new ProcessBuilder("sh", "public.sh");
        shProcessBuilder.directory(new File("/home/wsj/wsj"));
        shProcessBuilder.redirectErrorStream(true);
        Process shProcess = shProcessBuilder.start();
        shProcess.waitFor();

        // 读取输出信息
        BufferedReader reader = new BufferedReader(new InputStreamReader(shProcess.getInputStream()));
        String line;
        while ((line = reader.readLine()) != null) {
            System.out.println(line);
        }
        return "ok";
    }

}

