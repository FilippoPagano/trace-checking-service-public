export HADOOP_HOME=$HOME/hadoop-2.6.0
export SPARK_HOME=$HOME/spark-1.4.1-bin-hadoop2.6

$HADOOP_HOME/sbin/stop-dfs.sh

$SPARK_HOME/sbin/stop-master.sh

$SPARK_HOME/sbin/stop-slave.sh fil-VirtualBox:7077

service apache2 stop

cd $HOME/trace-checking-service/tc-service/target
kill -9 `cat PID.pid`
