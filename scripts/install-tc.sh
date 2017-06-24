#- install-tc.sh: installa tutto il necessario per far girare il TC sulla macchina, quindi questo dovrebbe fare orientativamente (potrei mancare qualcosa) i seguenti passaggi:
#	- scarica la versione di hdfs e ne fa il setup
#	- scarica il server Apache e ne fa il setup
#	- scarica spark e ne fa il setup
#	- scarica maven e git
#	- fa clone del repo del tc
#	- fa mvn clean package del TC

#pass macchina virtuale: ok

sudo apt-get update
sudo apt-get install -y openjdk-8-jre openjdk-8-jdk apache2 maven git curl ssh

wget https://archive.apache.org/dist/hadoop/common/hadoop-2.6.0/hadoop-2.6.0.tar.gz
tar -zxvf hadoop-2.6.0.tar.gz
#sudo mv hadoop-2.6.0 /usr/local/hadoop
sed -i '/export JAVA_HOME=${JAVA_HOME}/c\export JAVA_HOME=$(readlink -f /usr/bin/java | sed "s:bin/java::")' $HOME/hadoop-2.6.0/etc/hadoop/hadoop-env.sh

curl https://d3kbcqa49mib13.cloudfront.net/spark-1.4.1-bin-hadoop2.6.tgz | tar xvz

cd $HOME

git clone https://github.com/MicheleGuerriero/trace-checking-service.git

cd trace-checking-service/
mvn clean package

sudo iptables -A INPUT -p tcp --dport ssh -j ACCEPT
java -jar tc-service/target/tc-service-0.0.1-SNAPSHOT.jar server
ssh-keygen -t rsa
cat $HOME/.ssh/id_rsa.pub >> $HOME/.ssh/authorized_keys
chmod og-wx $HOME/.ssh/authorized_keys

sed -i '$ d' $HOME/hadoop-2.6.0/etc/hadoop/core-site.xml
sed -i '$ d' $HOME/hadoop-2.6.0/etc/hadoop/core-site.xml

cat <<EOT >> $HOME/hadoop-2.6.0/etc/hadoop/core-site.xml
<configuration>
    <property>
        <name>fs.defaultFS</name>
        <value>hdfs://localhost:9000</value>
    </property>
</configuration>
EOT

sh -c "echo 'localhost' >> $HOME/spark-1.4.1-bin-hadoop2.6/conf/slaves"

sh -c "cat <<EOT >> $HOME/spark-1.4.1-bin-hadoop2.6/conf/spark-defaults.conf
 spark.master                     spark://localhost:7077
 spark.eventLog.enabled           true
 spark.eventLog.dir               hdfs://localhost:9000/sparklogs
 spark.executor.memory            2g
 spark.history.fs.logDirectory	  hdfs://localhost:9000/sparklogs
 spark.serializer                 org.apache.spark.serializer.KryoSerializer
EOT"


 
