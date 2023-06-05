mvn clean & mvn compile & mvn package -Dmaven.test.skip=true
echo "打包成功"
cd server
cd target
# 获取与 java -jar 相关的进程ID列表
pids=$(ps -aux | grep "java -jar" | grep -v grep | awk '{print $2}')
# 关闭进程
for pid in $pids; do
    kill -9 $pid
done
nohup java -jar server-1.0.jar > log.txt &
echo "发布成功"