#! /bin/bash

print_usage()
{
  echo "Usage: COMMAND"
  echo "COMMAND is one of:"
  echo "  start       to start ctg-mes-config-console"
  echo "  stop        to stop ctg-mes-config-console"
  echo "  status      ctg-mes-config-console is running or not"
}
if [ $# = 0 ]; then
  print_usage
  exit 1
fi

# get arguments
ACTION=$1
shift
COMMAND=analytic-console

pid=`ps ax | grep -i ctg-mes-config |grep java | grep -v grep | awk '{print $1}'`

case $ACTION in
    stop)

    if [ -z "$pid" ] ; then
            echo "No ${COMMAND} is running. please start first !"
            exit -1;
    fi

    kill -9 ${pid}
    echo "The ${COMMAND}(${pid}) is stopped !"
    ;;
    start)

    if [ -n "$pid" ] ; then
            echo "The ${COMMAND}(${pid}) is running, please stop it first!"
            exit -1;
    fi

    nohup java -jar -server -Dspring.profiles.active=prod -Dserver.contextPath=/ctg-mes-config -Ddebug=false   ctg-ctg-mes-config-1.0.0.jar >/dev/null 2>&1 &
    echo "${COMMAND} started !"
    ;;
	status)

    if [ -z "$pid" ] ; then
            echo "No ${COMMAND} is running now !"
            exit -1;
    fi

    echo "The ${COMMAND}(${pid}) is running !"
    ;;
    *)
    echo "Useage: start | stop"
esac