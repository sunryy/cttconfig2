package com.ctg.mes.config.console.service.zookeeper.impl;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.RetryUntilElapsed;
import org.apache.curator.utils.DebugUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 实例只使用同一个CuratorFramework
 */
@Service
public class ZkScheduleImpl {
    private final static Logger log = LoggerFactory.getLogger(ZkScheduleImpl.class);
    //todo 网络太慢，设置较长的超时时间做测试验证
    private static final int zkConnectTimeoutMs = 4000*10;
    private static final int zkSessionTimeoutMs = 8000*10;

    private static final Map<String, CuratorFramework> clientMap = new ConcurrentHashMap<>();
    private static Object lock = new Object();

    public synchronized CuratorFramework get(String serverKey) {
        CuratorFramework zkClient = clientMap.get(serverKey);
        if (null == zkClient) {
            setZkLogSystemProperty();
            try {
                init(serverKey);
                zkClient = clientMap.get(serverKey);
            } catch (Exception e) {
                log.error("error init zkClient instance", e);
            }
        }
        return zkClient;
    }

    /**
     * 只在系统初始化的时候调用一次
     */
    private void init(String serverKey) {
        CuratorFramework zkClient = clientMap.get(serverKey);
        if (zkClient == null) {
            synchronized (lock) {
                zkClient = clientMap.get(serverKey);
                if (zkClient == null) {
                    CuratorFrameworkFactory.Builder builder = CuratorFrameworkFactory.builder()
                            .connectString(serverKey)
                            .connectionTimeoutMs(zkConnectTimeoutMs)
                            .sessionTimeoutMs(zkSessionTimeoutMs)
                            .retryPolicy(new RetryUntilElapsed(2000, 250));
                    //builder.authorization("digest", digest.getBytes(StandardCharsets.UTF_8));
                    zkClient = builder.build();
                    zkClient.start();
                    clientMap.put(serverKey, zkClient);
                }
            }
        }
    }

    public synchronized void close(String serverKey) {
        CuratorFramework zkClient = clientMap.get(serverKey);
        if (null != zkClient) {
            try {
                zkClient.close();
            } catch (Exception e) {
                log.error("error close zkClient instance", e);
            }
            zkClient = null;
        }
    }

    /**
     * 设置org.apache.curator库的日志信息的系统变量,打印更多的连接信息
     */
    private void setZkLogSystemProperty() {
        //如果没有设置系统变量,再自动设置
        {
            String p = System.getProperty(DebugUtils.PROPERTY_DONT_LOG_CONNECTION_ISSUES);
            if (null == p || p.isEmpty()) {
                System.setProperty(DebugUtils.PROPERTY_DONT_LOG_CONNECTION_ISSUES, "false");
            }
        }
        {
            String p = System.getProperty(DebugUtils.PROPERTY_LOG_ONLY_FIRST_CONNECTION_ISSUE_AS_ERROR_LEVEL);
            if (null == p || p.isEmpty()) {
                System.setProperty(DebugUtils.PROPERTY_LOG_ONLY_FIRST_CONNECTION_ISSUE_AS_ERROR_LEVEL, "false");
            }
        }
    }

}
