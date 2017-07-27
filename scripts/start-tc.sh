#- start-tc.sh: lancia tutto il necessario per utilizzare il TC service, ovvero:
#	- lancia hdfs
#	- lancia spark
#	- lancia Apache
#	- carica la web GUI su Apache
#	- lancia il tc-service

export HADOOP_HOME=$HOME/hadoop-2.6.0
export SPARK_HOME=$HOME/spark-1.4.1-bin-hadoop2.6
export HOSTNAME=`hostname`

$HADOOP_HOME/bin/hdfs namenode -format
$HADOOP_HOME/sbin/start-dfs.sh
$HADOOP_HOME/bin/hdfs dfs -mkdir /sparklogs
$HADOOP_HOME/bin/hdfs dfs -mkdir /user/
$HADOOP_HOME/bin/hdfs dfs -mkdir /user/$USER

$SPARK_HOME/sbin/start-master.sh

$SPARK_HOME/sbin/start-slave.sh spark://$HOSTNAME:7077

sudo service apache2 start

sudo cp -r $HOME/trace-checking-service/web /var/www/html/

export TC_PROPERTIES_FILE=$HOME/trace-checking-service/conf/config.properties
cd $HOME/trace-checking-service/tc-service/target
java -jar tc-service-0.0.1-SNAPSHOT.jar server ../../tc-service/config.yaml > tc-service.log 2>&1 & echo $! > PID.pid

