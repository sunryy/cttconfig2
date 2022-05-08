package com.ctg.mes.config.mgr.monitor.exporter.collector;

import com.ctg.mes.config.common.metrics.ExporterConstants;
import com.ctg.mes.config.mgr.monitor.bean.MonitorDataVO;
import com.ctg.mes.config.mgr.monitor.bean.ZkMonitorDataVO;
import com.ctg.mes.config.mgr.monitor.config.MonitorConstants;
import com.ctg.mes.config.mgr.monitor.ringbuffer.MemRingBuffer;
import io.prometheus.client.Collector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

public class MonitorRingBufferCollector extends Collector {
    public static final Logger logger = LoggerFactory.getLogger(MonitorRingBufferCollector.class);

    private final MemRingBuffer<MonitorDataVO> memRingBuffer;

    public MonitorRingBufferCollector(MemRingBuffer<MonitorDataVO> memRingBuffer){
        this.memRingBuffer = memRingBuffer;
    }
    private AtomicLong logCount = new AtomicLong(0L);

    @Override
    public List<MetricFamilySamples> collect() {
        List<MetricFamilySamples> mfs = new ArrayList<>();

        MetricGaugeInstance.clear();   //清理samples数组
        if (logCount.incrementAndGet() % 100 == 0) {
            logger.info("exec ring buffer collect");
            logCount.set(0L);
        }
        try {
            //1.获取一次监控数据
            List<MonitorDataVO> buffer = memRingBuffer.getBuffer();
            if(buffer.isEmpty()){
                logger.info("buffer empty, return;");
                return mfs;
            }

            //2.获取记录，按类型分类
            for (MonitorDataVO dataVO : buffer) {
                procBufferData(dataVO);
            }

            //3. mfs添加sample返回
            buildMetricGroup(MetricGaugeInstance.zkServerStatus, mfs);
            buildMetricGroup(MetricGaugeInstance.zkAvgLatency, mfs);
            buildMetricGroup(MetricGaugeInstance.zkMaxLatency, mfs);
            buildMetricGroup(MetricGaugeInstance.zkMinLatency, mfs);
            buildMetricGroup(MetricGaugeInstance.zkPacketsReceived, mfs);
            buildMetricGroup(MetricGaugeInstance.zkPacketsSend, mfs);
            buildMetricGroup(MetricGaugeInstance.zkNumAliveConnections, mfs);
            buildMetricGroup(MetricGaugeInstance.zkOutstandingRequests, mfs);
            buildMetricGroup(MetricGaugeInstance.zkZnodeCount, mfs);
            buildMetricGroup(MetricGaugeInstance.zkWatchCount, mfs);
            buildMetricGroup(MetricGaugeInstance.zkEphemeralsCount, mfs);
            buildMetricGroup(MetricGaugeInstance.zkApproximateDataSize, mfs);
            buildMetricGroup(MetricGaugeInstance.zkOpenFileDescriptorCount, mfs);
            buildMetricGroup(MetricGaugeInstance.zkMaxFileDescriptorCount, mfs);
            buildMetricGroup(MetricGaugeInstance.zkFollowers, mfs);
            buildMetricGroup(MetricGaugeInstance.zkSyncedFollowers, mfs);
            buildMetricGroup(MetricGaugeInstance.zkPendingSyncs, mfs);

        }catch (Exception e){
            e.printStackTrace();
            logger.error("collect exception.",e);
        }
        return mfs;
    }


    private void procBufferData(MonitorDataVO monitorDataVO) {
        try {
            parseMonitorDataAndBuildSample(monitorDataVO);
        }catch (Exception e){
            e.printStackTrace();
            logger.error("parseMonitorData exception.",e);
        }
    }

    private void parseMonitorDataAndBuildSample(MonitorDataVO monitorDataVO) {
        //组件指标监控数据
        String monitorType = monitorDataVO.getMonitorMetricDimensionType();
        MonitorDataToSample(monitorDataVO,  monitorType);
    }

    private void MonitorDataToSample(MonitorDataVO dataVO, String monitorType ){
        switch(monitorType){
            case MonitorConstants.MONITOR_METRIC_DIMENSION_ZOOKEEPER:
                ZkMonitorDataVO monitorDataVO = (ZkMonitorDataVO) dataVO;
                buildMetricGaugeSample(monitorDataVO, MetricGaugeInstance.zkServerStatus);
                buildMetricGaugeSample(monitorDataVO, MetricGaugeInstance.zkAvgLatency);
                buildMetricGaugeSample(monitorDataVO, MetricGaugeInstance.zkMaxLatency);
                buildMetricGaugeSample(monitorDataVO, MetricGaugeInstance.zkMinLatency);
                buildMetricGaugeSample(monitorDataVO, MetricGaugeInstance.zkPacketsReceived);
                buildMetricGaugeSample(monitorDataVO, MetricGaugeInstance.zkNumAliveConnections);
                buildMetricGaugeSample(monitorDataVO, MetricGaugeInstance.zkOutstandingRequests);
                buildMetricGaugeSample(monitorDataVO, MetricGaugeInstance.zkZnodeCount);
                buildMetricGaugeSample(monitorDataVO, MetricGaugeInstance.zkWatchCount);
                buildMetricGaugeSample(monitorDataVO, MetricGaugeInstance.zkEphemeralsCount);
                buildMetricGaugeSample(monitorDataVO, MetricGaugeInstance.zkApproximateDataSize);
                buildMetricGaugeSample(monitorDataVO, MetricGaugeInstance.zkOpenFileDescriptorCount);
                buildMetricGaugeSample(monitorDataVO, MetricGaugeInstance.zkMaxFileDescriptorCount);
                buildMetricGaugeSample(monitorDataVO, MetricGaugeInstance.zkFollowers);
                buildMetricGaugeSample(monitorDataVO, MetricGaugeInstance.zkSyncedFollowers);
                buildMetricGaugeSample(monitorDataVO, MetricGaugeInstance.zkPendingSyncs);
                break;
            default:
                break;
        }
    }


    public void buildMetricGaugeSample(ZkMonitorDataVO monitorDataVO, IMetricGauge metricGauge){
        String metricName = metricGauge.metricName;
        List<String> labelNames = metricGauge.labelNames;
        String sampleValueKey = metricGauge.sampleValueKey;

        List<String> labelValues = new ArrayList<>();
        for(String labelName : labelNames){
            String labelValue = getLabelValueByMonitorDataVO(labelName, monitorDataVO);
            labelValues.add(labelValue);
        }

        double sampleValue = getSampleValueByMonitorDataVO(sampleValueKey, monitorDataVO);
        long timestampMs = monitorDataVO.getTime();

        MetricFamilySamples.Sample sample = new MetricFamilySamples.Sample(metricName,
                labelNames,  labelValues, sampleValue,  timestampMs);
        metricGauge.add(sample);
    }

    private void buildMetricGroup(IMetricGauge metricGauge, List<MetricFamilySamples> mfs) {
        String metricName = metricGauge.metricName;
        String helpContent = metricGauge.helpContent;
        List<MetricFamilySamples.Sample> samples = metricGauge.samples;

        if(samples.isEmpty()){
            logger.debug("metricName:{} sample is empty. discard it.", metricName);
            return;
        }
        MetricFamilySamples gaugeSample = new MetricFamilySamples(metricName, Type.GAUGE, helpContent, samples);
        mfs.add(gaugeSample);
    }

    private String getLabelValueByMonitorDataVO(String labelName, ZkMonitorDataVO monitorDataVO){
        String labelValue = "";
        switch (labelName){
            case ExporterConstants.LABEL_INST_ID:
                labelValue = monitorDataVO.getInstId();
                break;
            case ExporterConstants.LABEL_NODE_ID:
                labelValue = monitorDataVO.getNodeId();
                break;
            case ExporterConstants.LABEL_ZK_NODE:
                labelValue = monitorDataVO.getZkNode();
                break;
            default:
                logger.error("unknown labelName:{}", labelName);
                break;
        }
        return labelValue;
    }


    private double getSampleValueByMonitorDataVO(String sampleValueKey, ZkMonitorDataVO monitorDataVO) {
        double sampleValue = 0;
        switch (sampleValueKey) {
            case ExporterConstants.LABEL_ZK_SERVER_STATE:
                sampleValue = "Leader".equalsIgnoreCase(monitorDataVO.getZk_server_state()) ? 1 : 0;
                break;
            case ExporterConstants.LABEL_ZK_AVG_LATENCY:
                sampleValue = monitorDataVO.getZk_avg_latency();
                break;
            case ExporterConstants.LABEL_ZK_MAX_LATENCY:
                sampleValue = monitorDataVO.getZk_max_latency();
                break;
            case ExporterConstants.LABEL_ZK_MIN_LATENCY:
                sampleValue = monitorDataVO.getZk_min_latency();
                break;
            case ExporterConstants.LABEL_ZK_PACKETS_RECEIVED:
                sampleValue = monitorDataVO.getZk_packets_received();
                break;
            case ExporterConstants.LABEL_ZK_PACKETS_SEND:
                sampleValue = monitorDataVO.getZk_packets_sent();
                break;
            case ExporterConstants.LABEL_ZK_NUM_ALIVE_CONNECTIONS:
                sampleValue = monitorDataVO.getZk_num_alive_connections();
                break;
            case ExporterConstants.LABEL_ZK_OUTSTANDING_REQUESTS:
                sampleValue = monitorDataVO.getZk_outstanding_requests();
                break;
            case ExporterConstants.LABEL_ZK_ZNODE_COUNT:
                sampleValue = monitorDataVO.getZk_znode_count();
                break;
            case ExporterConstants.LABEL_ZK_WATCH_COUNT:
                sampleValue = monitorDataVO.getZk_watch_count();
                break;
            case ExporterConstants.LABEL_ZK_EPHEMERALS_COUNT:
                sampleValue = monitorDataVO.getZk_ephemerals_count();
                break;
            case ExporterConstants.LABEL_ZK_APPROXIMATE_SIZE:
                sampleValue = monitorDataVO.getZk_approximate_data_size();
                break;
            case ExporterConstants.LABEL_ZK_OPEN_FILE_DESCRIPTOR_COUNT:
                sampleValue = monitorDataVO.getZk_open_file_descriptor_count();
                break;
            case ExporterConstants.LABEL_ZK_MAX_FILE_DESCRIPTOR_COUNT:
                sampleValue = monitorDataVO.getZk_max_file_descriptor_count();
                break;
            case ExporterConstants.LABEL_ZK_FOLLOWERS:
                sampleValue = monitorDataVO.getZk_followers();
                break;
            case ExporterConstants.LABEL_ZK_SYNCED_FOLLOWERS:
                sampleValue = monitorDataVO.getZk_synced_followers();
                break;
            case ExporterConstants.LABEL_ZK_PENDING_SYNCS:
                sampleValue = monitorDataVO.getZk_pending_syncs();
                break;
            default:
                logger.error("unknown sampleValueKey:{}", sampleValueKey);
                break;
        }
        return sampleValue;
    }


    private static class MetricGaugeInstance{

        public static List<String> zkMetricLabelNames = Arrays.asList(
                ExporterConstants.LABEL_INST_ID,
                ExporterConstants.LABEL_NODE_ID,
                ExporterConstants.LABEL_ZK_NODE
        );

        public static IMetricGauge zkServerStatus = new IMetricGauge(
                ExporterConstants.METRIC_NAME_ZK_SERVER_STATE,
                "zk服务状态",
                zkMetricLabelNames,
                ExporterConstants.LABEL_ZK_SERVER_STATE
        );

        public static IMetricGauge zkAvgLatency = new IMetricGauge(
                ExporterConstants.METRIC_NAME_ZK_AVG_LATENCY,
                "响应客户端的平均请求的时间",
                zkMetricLabelNames,
                ExporterConstants.LABEL_ZK_AVG_LATENCY
        );

        public static IMetricGauge zkMaxLatency = new IMetricGauge(
                ExporterConstants.METRIC_NAME_ZK_MAX_LATENCY,
                "响应客户端的最大请求的时间",
                zkMetricLabelNames,
                ExporterConstants.LABEL_ZK_MAX_LATENCY
        );

        public static IMetricGauge zkMinLatency = new IMetricGauge(
                ExporterConstants.METRIC_NAME_ZK_MIN_LATENCY,
                "响应客户端的最小请求的时间",
                zkMetricLabelNames,
                ExporterConstants.LABEL_ZK_MIN_LATENCY
        );

        public static IMetricGauge zkPacketsReceived = new IMetricGauge(
                ExporterConstants.METRIC_NAME_ZK_PACKETS_RECEIVED,
                "接收到客户端请求的包数量",
                zkMetricLabelNames,
                ExporterConstants.LABEL_ZK_PACKETS_RECEIVED
        );

        public static IMetricGauge zkPacketsSend = new IMetricGauge(
                ExporterConstants.METRIC_NAME_ZK_PACKETS_SEND,
                "发送给客户端的包数量",
                zkMetricLabelNames,
                ExporterConstants.LABEL_ZK_PACKETS_SEND
        );

        public static IMetricGauge zkNumAliveConnections = new IMetricGauge(
                ExporterConstants.METRIC_NAME_ZK_NUM_ALIVE_CONNECTIONS,
                "最大存活连接数",
                zkMetricLabelNames,
                ExporterConstants.LABEL_ZK_NUM_ALIVE_CONNECTIONS
        );

        public static IMetricGauge zkOutstandingRequests = new IMetricGauge(
                ExporterConstants.METRIC_NAME_ZK_OUTSTANDING_REQUESTS,
                "排队请求的数量",
                zkMetricLabelNames,
                ExporterConstants.LABEL_ZK_OUTSTANDING_REQUESTS
        );

        public static IMetricGauge zkZnodeCount = new IMetricGauge(
                ExporterConstants.METRIC_NAME_ZK_ZNODE_COUNT,
                "节点的总数量",
                zkMetricLabelNames,
                ExporterConstants.LABEL_ZK_ZNODE_COUNT
        );

        public static IMetricGauge zkWatchCount = new IMetricGauge(
                ExporterConstants.METRIC_NAME_ZK_WATCH_COUNT,
                "watch的总数量",
                zkMetricLabelNames,
                ExporterConstants.LABEL_ZK_WATCH_COUNT
        );

        public static IMetricGauge zkEphemeralsCount = new IMetricGauge(
                ExporterConstants.METRIC_NAME_ZK_EPHEMERALS_COUNT,
                "临时节点数量",
                zkMetricLabelNames,
                ExporterConstants.LABEL_ZK_EPHEMERALS_COUNT
        );

        public static IMetricGauge zkApproximateDataSize = new IMetricGauge(
                ExporterConstants.METRIC_NAME_ZK_APPROXIMATE_SIZE,
                "大致的总数据量",
                zkMetricLabelNames,
                ExporterConstants.LABEL_ZK_APPROXIMATE_SIZE
        );

        public static IMetricGauge zkOpenFileDescriptorCount = new IMetricGauge(
                ExporterConstants.METRIC_NAME_ZK_OPEN_FILE_DESCRIPTOR_COUNT,
                "打开文件描述符总数",
                zkMetricLabelNames,
                ExporterConstants.LABEL_ZK_OPEN_FILE_DESCRIPTOR_COUNT
        );

        public static IMetricGauge zkMaxFileDescriptorCount = new IMetricGauge(
                ExporterConstants.METRIC_NAME_ZK_MAX_FILE_DESCRIPTOR_COUNT,
                "最大的文件描述符总数",
                zkMetricLabelNames,
                ExporterConstants.LABEL_ZK_MAX_FILE_DESCRIPTOR_COUNT
        );

        public static IMetricGauge zkFollowers = new IMetricGauge(
                ExporterConstants.METRIC_NAME_ZK_FOLLOWERS,
                "集群中FOLLOWERS的个数",
                zkMetricLabelNames,
                ExporterConstants.LABEL_ZK_FOLLOWERS
        );

        public static IMetricGauge zkSyncedFollowers = new IMetricGauge(
                ExporterConstants.METRIC_NAME_ZK_SYNCED_FOLLOWERS,
                "已同步的FOLLOWERS的个数",
                zkMetricLabelNames,
                ExporterConstants.LABEL_ZK_SYNCED_FOLLOWERS
        );

        public static IMetricGauge zkPendingSyncs = new IMetricGauge(
                ExporterConstants.METRIC_NAME_ZK_PENDING_SYNCS,
                "未同步的FOLLOWERS的个数",
                zkMetricLabelNames,
                ExporterConstants.LABEL_ZK_PENDING_SYNCS
        );

        public static void clear(){
            zkServerStatus.clear();
            zkAvgLatency.clear();
            zkMaxLatency.clear();
            zkMinLatency.clear();
            zkPacketsReceived.clear();
            zkPacketsSend.clear();
            zkNumAliveConnections.clear();
            zkOutstandingRequests.clear();
            zkZnodeCount.clear();
            zkWatchCount.clear();
            zkEphemeralsCount.clear();
            zkApproximateDataSize.clear();
            zkOpenFileDescriptorCount.clear();
            zkMaxFileDescriptorCount.clear();
            zkFollowers.clear();
            zkSyncedFollowers.clear();
            zkPendingSyncs.clear();
        }
    }


    private static class IMetricGauge{
        public  String metricName ;          //指标名
        public  String helpContent ;         //help说明
        public  List<String> labelNames;     //标签keys
        public  String sampleValueKey ;      //指标值key
        public  List<MetricFamilySamples.Sample> samples;   //样本列表

        public IMetricGauge(String metricName, String helpContent, List<String> labelNames, String sampleValueKey){
            this.metricName = metricName;
            this.helpContent = helpContent;
            this.labelNames = labelNames;
            this.sampleValueKey = sampleValueKey;
            this.samples = new ArrayList<>();
        }
        public void add(MetricFamilySamples.Sample sample){
            samples.add(sample);
        }
        public void clear(){
            samples.clear();
        }
    }

}
