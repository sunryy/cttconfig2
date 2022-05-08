package com.ctg.mes.config.console.service.zookeeper.impl;

import com.ctg.mes.config.common.metrics.ExporterConstants;
import com.ctg.mes.config.console.VO.metrics.MetricsReqVO;
import com.ctg.mes.config.console.VO.metrics.MetricsVO;
import com.ctg.mes.config.console.metrics.InstantFloatMonitorMetrics;
import com.ctg.mes.config.console.metrics.AbstractMonitorMetrics;
import com.ctg.mes.config.console.metrics.promethus.PrometheusApiClient;
import com.ctg.mes.config.console.service.zookeeper.IMonitorService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Service
public class PromethusMonitorServiceImpl implements IMonitorService {

    private static final Logger LOGGER = LoggerFactory.getLogger(PromethusMonitorServiceImpl.class);

    private final static Map<String, AbstractMonitorMetrics> metricsMap = new HashMap<>();
    static {
        metricsMap.put(ExporterConstants.METRIC_NAME_ZK_AVG_LATENCY, new InstantFloatMonitorMetrics());
    }

    private PrometheusApiClient prometheusApiClient;

    @Value("${monitor.prometheus.addr:}")
    private String prometheusAddr;

    @PostConstruct
    public void init() {
        prometheusApiClient = new PrometheusApiClient(prometheusAddr);
    }

    @Override
    public MetricsVO queryMetrics(MetricsReqVO reqVO) throws Exception {
        String type = reqVO.getType();
        AbstractMonitorMetrics metrics = metricsMap.get(type);
        if (metrics == null) {
            metrics = new InstantFloatMonitorMetrics(); //默认处理方式
        }
        String nodeId = reqVO.getNodeId();
        if (StringUtils.isEmpty(nodeId)) {
            throw new RuntimeException("Node id is empty");
        }
        return metrics.getMetrics(prometheusApiClient, getTimeUnitString(reqVO), reqVO);
    }


    /**
     * 计算监控的时间点数量,尽量让每个时间点之间比较契合监控的采集频率。
     * 因而最低粒度是分钟，
     * 12小时内都是1分钟
     * 24小时内都是2分钟
     */
    private static long getTimeUnit(MetricsReqVO reqVO) {
        long timeRange = reqVO.getEndTime().getTime() - reqVO.getStartTime().getTime();
        if (timeRange <= TimeUnit.HOURS.toMillis(12)) {
            return TimeUnit.MINUTES.toMillis(1);
        }
        if (timeRange <= TimeUnit.HOURS.toMillis(24)) {
            return TimeUnit.MINUTES.toMillis(2);
        }
        if (timeRange <= TimeUnit.DAYS.toMillis(7)) {
            return TimeUnit.MINUTES.toMillis(5);
        }
        if (timeRange <= TimeUnit.DAYS.toMillis(10)) {
            return TimeUnit.MINUTES.toMillis(10);
        }
        if (timeRange <= TimeUnit.DAYS.toMillis(14)) {
            return TimeUnit.MINUTES.toMillis(20);
        }
        return TimeUnit.MINUTES.toMillis(60);
    }

    private static String getTimeUnitString(MetricsReqVO reqVO) {
        long unit = getTimeUnit(reqVO);
        long minutes = unit / 1000 / 60;
        return minutes + "m";
    }
}
