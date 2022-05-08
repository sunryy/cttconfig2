package com.ctg.mes.config.mgr.service.impl;

import com.ctg.mes.config.common.util.InjectUtils;
import com.ctg.mes.config.common.util.NamedThreadFactory;
import com.ctg.mes.config.mgr.monitor.bean.MonitorDataVO;
import com.ctg.mes.config.mgr.monitor.bean.ZkMonitorDataVO;
import com.ctg.mes.config.mgr.monitor.exporter.ExporterConfig;
import com.ctg.mes.config.mgr.monitor.exporter.ExporterServer;
import com.ctg.mes.config.mgr.monitor.ringbuffer.RingBufferFactory;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.PostConstruct;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.util.List;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class ZkMonitorServiceImpl {
    private static final Logger LOGGER = LoggerFactory.getLogger(ZkMonitorServiceImpl.class);

    private final int MONITOR_CHECK_INTERNAL = 3000;
    protected final ScheduledExecutorService monotorExecutor = new ScheduledThreadPoolExecutor(1,
            new NamedThreadFactory("zkMonitorChecker", 1, true));

    @Value("${zookeeper.instance-id:}")
    private String instId;

    @Value("${zookeeper.node-id:}")
    private String nodeId;

    @Value("${zookeeper.install-ip:127.0.0.1}")
    private String zkIp;

    @Value("${zookeeper.install-port:2181}")
    private int zkPort;

    @Value("${zookeeper.exporter-port:}")
    private int exporterPort;

    @PostConstruct
    public void init() {
        initSchedule();
        startZkExporterServer();
    }

    public void startZkExporterServer() {
        try {
            if (exporterPort != 0) {
                ExporterConfig config = new ExporterConfig(exporterPort);
                ExporterServer.exporterHttpServer(config);
            }
            LOGGER.info("Start zk exporter http server, exportPort = {}", exporterPort);
        } catch (Exception e) {
            LOGGER.error("can not start zk exporter http server, exportPort = {}", exporterPort, e);
        }
    }

    public void initSchedule() {
        final AtomicLong logCount = new AtomicLong(0);
        monotorExecutor.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                try {
                    getMonitorData();
                    logCount.incrementAndGet();
                    //每1W条打印一次
                    if (logCount.get() % 1000 == 0) {
                        LOGGER.info("Thread[fetchZkStatus] is working.");
                    }
                    if (logCount.get() > 100_0000) {
                        logCount.set(0L);
                    }
                }catch (Throwable e) {
                    LOGGER.warn("Read zk server status error. errMsg = {} ", e.getMessage(), e);
                }
            }
        }, MONITOR_CHECK_INTERNAL, MONITOR_CHECK_INTERNAL, TimeUnit.MILLISECONDS);
    }

    private void getMonitorData() {
        MonitorDataVO monitorDataVO = fetchZkStatus();
        RingBufferFactory.getMonitorRingBuffer().add(monitorDataVO);
    }

    private ZkMonitorDataVO fetchZkStatus() {
        ZkMonitorDataVO zkStatus = null;
        try {
            ZkMonitorDataVO tmpStatus = new ZkMonitorDataVO();
            tmpStatus.setInstId(instId);
            tmpStatus.setNodeId(nodeId);
            tmpStatus.setZkNode(String.format("%s:%d", zkIp, zkPort));
            tmpStatus.setTime(System.currentTimeMillis());
            List<String> result = getZk4LetterCommandResult("ruok");
            if (!CollectionUtils.isEmpty(result)) {
                tmpStatus.setRuok(result.get(0));
                InjectUtils.injectField(tmpStatus, getZk4LetterCommandResult("mntr"), "\t");
            }
            zkStatus = tmpStatus;
        } catch (Throwable e) {
            LOGGER.error("", e);
        }
        return zkStatus;
    }

    private List<String> getZk4LetterCommandResult(String cmd) throws IOException {
        try (Socket socket = new Socket(InetAddress.getByName("127.0.0.1"), zkPort);
             BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             OutputStreamWriter outputStreamWriter = new OutputStreamWriter(socket.getOutputStream())) {
            outputStreamWriter.write(cmd);
            outputStreamWriter.flush();
            return IOUtils.readLines(bufferedReader);
        }
    }

}
