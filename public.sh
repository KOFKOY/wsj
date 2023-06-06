mvn clean & mvn compile & mvn package -Dmaven.test.skip=true
echo "打包成功"
# 获取与 java -jar 相关的进程ID列表
pids=$(ps -aux | grep "java -jar" | grep -v grep | awk '{print $2}')
# 关闭进程
echo "正在运行的进程ID"
for pid in $pids; do
    echo $pid
    kill -9 $pid
done
echo "关闭进程"
cd server
cd target
nohup java -jar server-1.0.jar > /dev/null 2>&1 &
echo "发布成功"