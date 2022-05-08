package com.ctg.mes.config.mgr.monitor.bean;

import lombok.Data;

@Data
public class ZkMonitorDataVO extends MonitorDataVO {
    private static final long serialVersionUID = 1375061402L;
    private String zkNode;   //节点路径ip:port
    //指标
    private String ruok;
    //服务器状态，Leader, Follower, Standalone, Observer, Read-only
    private String zk_server_state; //
    private int zk_znode_count;
    private int zk_max_latency;
    private int zk_avg_latency;
    private int zk_min_latency;
    private int zk_packets_received;
    private int zk_packets_sent;
    private int zk_watch_count;
    private int zk_approximate_data_size;
    private int zk_open_file_descriptor_count;
    private int zk_max_file_descriptor_count;
    private int zk_num_alive_connections;
    private int zk_outstanding_requests;
    private int zk_ephemerals_count;

    //leader-only
    private int zk_followers;
    private int zk_synced_followers;
    private int zk_pending_syncs;

    @Override
    public String getMonitorMetricDimensionType() {
        return "CTG_MES_CONFIG_ZOOKEEPER";
    }
}
