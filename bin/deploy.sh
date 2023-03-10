#!/bin/bash --login

### BEGIN INIT INFO
# Provides:          YOUR_APP_NAME
# Required-Start:    $local_fs $remote_fs $network
# Required-Stop:     $local_fs $remote_fs $network
# Should-Start:      $named
# Should-Stop:       $named
# Default-Start:     2 3 4 5
# Default-Stop:      0 1 6
# Short-Description: Start Tomcat YOUR_APP_NAME.
# Description:       Start the Tomcat YOUR_APP_NAME servlet engine.
### END INIT INFO

SERVICE_NAME=wechatgpt
PATH_TO_JAR=../target/Wechatgpt.jar
ENV=dev
OPTS="-server -Xms256m -Xmx256m -Xmn256m
      -XX:+UseG1GC
      -XX:+PrintGCDateStamps
      -XX:+PrintGCApplicationStoppedTime
      -XX:+PrintAdaptiveSizePolicy
      -XX:+UseGCLogFileRotation
      -XX:NumberOfGCLogFiles=5
      -XX:GCLogFileSize=60m
      -XX:ErrorFile=/var/log/${SERVICE_NAME}/hs_err_data_nexus.log
      -XX:+TraceClassUnloading
      -XX:+TraceClassLoading
      -verbose:class
      -verbose:gc
      -Xloggc:/var/log/${SERVICE_NAME}/c_%p.log
      -XX:+HeapDumpOnOutOfMemoryError
      -XX:HeapDumpPath=/var/log/${SERVICE_NAME}/
      -XX:+PrintGCDetails"
JAVA_OPTIONS="$OPTS -Denv=$ENV -Dspring.profiles.active=$ENV"
PID_PATH_NAME=/var/log/${SERVICE_NAME}/PID


case "$1" in
    start)
        echo "Starting $SERVICE_NAME ..."
        if [ ! -f $PID_PATH_NAME ]; then
            nohup java -jar $JAVA_OPTIONS $PATH_TO_JAR /tmp >> /tmp/console.log &
                        echo $! > $PID_PATH_NAME
            echo "$SERVICE_NAME started ..."
        else
            echo "$SERVICE_NAME is already running ..."
        fi
    ;;
    stop)
        if [ -f $PID_PATH_NAME ]; then
            PID=$(cat $PID_PATH_NAME);
            echo "$SERVICE_NAME stoping ..."
            kill -9 $PID
            sleep 5s
            echo "$SERVICE_NAME stopped ..."
            sleep 5s
            rm -f $PID_PATH_NAME
        else
            echo "$SERVICE_NAME is not running ..."
        fi
    ;;
    restart)
        if [ -f $PID_PATH_NAME ]; then
            PID=$(cat $PID_PATH_NAME);
            kill -9 $PID
            sleep 5s
            rm -f $PID_PATH_NAME
            nohup java -jar $JAVA_OPTIONS $PATH_TO_JAR /tmp >> /var/log/${SERVICE_NAME}/console.log &
                        echo $! > $PID_PATH_NAME
        fi
    ;;
  *)
echo "Usage: $0 {start|stop|restart}"
exit 1
esac
exit 0
