## feishu实现功能
通过@FsNotice 注解发送飞书通知。优先发送@FsNotice设置的msg 和 接收人

如果没有设置，则读取配置文件中的接收人，发送的消息则为被注解方法的返回值
```yaml
feishu:
  app-id: cli_a1xxxx
  app-secret: c8sT2troxxxxx
  open-ids: 青山   #飞书中显示的名字，可以是多个，用英文逗号隔开
```

## server功能
目前只有一个定时任务
每天每4个小时爬取一次节点数据，写入到clash.yaml中
通过 http://ip:port/clash.yaml 即可更新订阅



## 遇见的问题
### server依赖feishu模块，feishu模块没有注入到容器
注意server模块 application启动类所在路径，默认扫描当前路径及子路径，feishu模块路径也要相对应，处于server启动类的子级或同级
被依赖的模块的pom文件要做相关配置
```xml
    <build>
        <plugins>
            <plugin>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-maven-plugin</artifactId>
                <configuration>
                    <classifier>exec</classifier>
                </configuration>
            </plugin>
        </plugins>
    </build>
```

父模块的pom配置
```xml
<build>
    <plugins>
        <!-- 资源文件拷贝插件 -->
        <plugin>
            <groupId>org.apache.maven.plugins</groupId>
            <artifactId>maven-resources-plugin</artifactId>
            <configuration>
                <encoding>UTF-8</encoding>
            </configuration>
        </plugin>
        <!-- java编译插件 -->
        <plugin>
            <groupId>org.apache.maven.plugins</groupId>
            <artifactId>maven-compiler-plugin</artifactId>
            <configuration>
                <source>1.8</source>
                <target>1.8</target>
                <encoding>UTF-8</encoding>
            </configuration>
        </plugin>
        <plugin>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-maven-plugin</artifactId>
        </plugin>
    </plugins>
    </build>
```
### 在linux中打包
下载配置maven，注意版本问题，不确定具体版本就和开发环境的版本一样
执行mvn命令是只需在父模块执行即可
### github设置action的变量
在actions中引用 {{ secrets.SSH_HOST }},设置变量时只需要设置SSH_HOST
