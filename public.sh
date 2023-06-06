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
nohup java -jar server-1.0.jar > startup.log 2>&1 &
pid=$!  # 获取Java程序的进程ID
echo "正在启动Java程序..."

# 等待Java程序完全启动
sleep 10  # 这里使用10秒的等待时间，您可以根据实际情况调整

# 检查Java程序是否在后台运行
if ps -p $pid > /dev/null; then
    echo "Java程序已成功启动"
    echo "发布成功"
else
    echo "Java程序启动失败"
fi