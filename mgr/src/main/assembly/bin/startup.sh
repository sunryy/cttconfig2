#!/bin/sh


#set HOME
CURR_DIR=`pwd`
cd `dirname "$0"`/..
ctgMesConfigMgr_HOME=`pwd`
cd $CURR_DIR

usage() {
	echo "Usage:"
	echo "    startup.sh [-c config_folder] [-l log_folder] [-d debug mode] [-h]"
	echo "Description:"
	echo "    config_folder - config folder path, not must,if empty,default classpath"
	echo "    log_folder - hsb server's logs base folder, /home/work  log path: /home/work/logs"
	echo "    debug_mode - 1|0  1 means debug port is open, 0 close ,default 0"
	echo "    -h - show this help"
	exit -1
}
LOG_BASE_DIR=$ctgMesConfigMgr_HOME
CONFIG_DIR=""
DEBUG_MODE="0";

while getopts "h:l:c:d:" arg
do
	case $arg in
	    l) LOG_BASE_DIR=$OPTARG;;
		c) CONFIG_DIR=$OPTARG;;
		d) DEBUG_MODE=$OPTARG;;
		h) usage;;
		?) usage;;
	esac
done

# echo Baidu.com,Inc.
# echo 'Copyright (c) 2000-2013 All Rights Reserved.'
# echo Distributed
# echo https://github.com/brucexx/heisenberg
# echo brucest0078@gmail.com

#check JAVA_HOME & java
noJavaHome=false
if [ -z "$JAVA_HOME" ] ; then
    noJavaHome=true
fi
if [ ! -e "$JAVA_HOME/bin/java" ] ; then
    noJavaHome=true
fi
if $noJavaHome ; then
    echo
    echo "Error: JAVA_HOME environment variable is not set."
    echo
    exit 1
fi
#==============================================================================
#set JAVA_OPTS
JAVA_OPTS="-server -Xmx2G -XX:MetaspaceSize=256M -XX:+AggressiveOpts -XX:MaxDirectMemorySize=2G"
#performance Options
#JAVA_OPTS="$JAVA_OPTS -Xss256k"
#JAVA_OPTS="$JAVA_OPTS -XX:+AggressiveOpts"
#JAVA_OPTS="$JAVA_OPTS -XX:+UseBiasedLocking"
#JAVA_OPTS="$JAVA_OPTS -XX:+UseFastAccessorMethods"
#JAVA_OPTS="$JAVA_OPTS -XX:+DisableExplicitGC"
#JAVA_OPTS="$JAVA_OPTS -XX:+UseParNewGC"
#JAVA_OPTS="$JAVA_OPTS -XX:+UseConcMarkSweepGC"
#JAVA_OPTS="$JAVA_OPTS -XX:+CMSParallelRemarkEnabled"
#JAVA_OPTS="$JAVA_OPTS -XX:+UseCMSCompactAtFullCollection"
#JAVA_OPTS="$JAVA_OPTS -XX:+UseCMSInitiatingOccupancyOnly"
#JAVA_OPTS="$JAVA_OPTS -XX:CMSInitiatingOccupancyFraction=75"
#JAVA_OPTS="$JAVA_OPTS -XX:CMSInitiatingOccupancyFraction=75"

#GC Log Options
JAVA_OPTS="$JAVA_OPTS -XX:+PrintGCApplicationStoppedTime"
JAVA_OPTS="$JAVA_OPTS -XX:+PrintGCDateStamps"
JAVA_OPTS="$JAVA_OPTS -XX:+PrintGCDetails"
JAVA_OPTS="$JAVA_OPTS -Xloggc:${LOG_BASE_DIR}/logs/gc.log"
#OOM dump
JAVA_OPTS="$JAVA_OPTS -XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=${LOG_BASE_DIR}/"
#debug Options
#JAVA_OPTS="$JAVA_OPTS -agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=7280"

# ZK
#JAVA_OPTS="$JAVA_OPTS -Djava.security.auth.login.config=/etc/security/apps_client_jaas.conf"
#==============================================================================

if [ -z "$ctgMesConfigMgr_HOME" ] ; then
    echo
    echo "Error: ctgMesConfigMgr_HOME environment variable is not defined correctly."
    echo
    exit 1
fi
#==============================================================================

#set CLASSPATH
ctgMesConfigMgr_CLASSPATH="$ctgMesConfigMgr_HOME/conf:$ctgMesConfigMgr_HOME/lib/*"
#==============================================================================

#startup Server
RUN_CMD="\"$JAVA_HOME/bin/java\""
RUN_CMD="$RUN_CMD -DappName=pgbackup -DctgMesConfigMgr_HOME=\"$ctgMesConfigMgr_HOME\""
RUN_CMD="$RUN_CMD -DLOG_BASE_DIR=\"$LOG_BASE_DIR\""
RUN_CMD="$RUN_CMD -Dlogging.path=\"$LOG_BASE_DIR/logs\""
RUN_CMD="$RUN_CMD -classpath \"$ctgMesConfigMgr_CLASSPATH\""
RUN_CMD="$RUN_CMD $JAVA_OPTS"
RUN_CMD="$RUN_CMD com.ctg.mes.config.mgr.Application $@"
RUN_CMD="$RUN_CMD >> \"$LOG_BASE_DIR/logs/console.log\" 2>&1 &"
echo $RUN_CMD
eval $RUN_CMD
echo $! > $ctgMesConfigMgr_HOME/bin/ctgMesConfigMgr.pid
#==============================================================================
