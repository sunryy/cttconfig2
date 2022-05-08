package com.ctg.mes.config.common.metrics;


public class ExporterConstants {

	//------------------------exporter label ----------------------------------------------------
	public static final String LABEL_INST_ID = "inst_id"; 			//实例ID
	public static final String LABEL_NODE_ID = "node_id"; 			//实例ID
	public static final String LABEL_ZK_NODE = "node";   	    //节点路径ip:port

	public static final String LABEL_ZK_AVG_LATENCY = "zk_avg_latency";
	public static final String LABEL_ZK_MAX_LATENCY = "zk_max_latency";
	public static final String LABEL_ZK_MIN_LATENCY = "zk_min_latency";
	public static final String LABEL_ZK_PACKETS_RECEIVED = "zk_packets_received";
	public static final String LABEL_ZK_PACKETS_SEND = "zk_packets_sent";
	public static final String LABEL_ZK_NUM_ALIVE_CONNECTIONS = "zk_num_alive_connections";
	public static final String LABEL_ZK_OUTSTANDING_REQUESTS = "zk_outstanding_requests";
	public static final String LABEL_ZK_SERVER_STATE = "zk_server_state";  //服务状态
	public static final String LABEL_ZK_ZNODE_COUNT = "zk_znode_count";
	public static final String LABEL_ZK_WATCH_COUNT = "zk_watch_count";
	public static final String LABEL_ZK_EPHEMERALS_COUNT = "zk_ephemerals_count";
	public static final String LABEL_ZK_APPROXIMATE_SIZE = "zk_approximate_data_size";
	public static final String LABEL_ZK_OPEN_FILE_DESCRIPTOR_COUNT = "zk_open_file_descriptor_count";
	public static final String LABEL_ZK_MAX_FILE_DESCRIPTOR_COUNT = "zk_max_file_descriptor_count";
	public static final String LABEL_ZK_FOLLOWERS = "zk_followers";
	public static final String LABEL_ZK_SYNCED_FOLLOWERS = "zk_synced_followers";
	public static final String LABEL_ZK_PENDING_SYNCS = "zk_pending_syncs";


	//------------------------exporter metric-----------------------------------------------------
	public static final String METRIC_NAME_ZK_AVG_LATENCY = "ctg_mes_config_zk_avg_latency";    				//平均延迟 响应一个客户端请求的时间，建议这个时间大于10个Tick就报警
	public static final String METRIC_NAME_ZK_MAX_LATENCY = "ctg_mes_config_zk_max_latency";   				 	//最大延迟
	public static final String METRIC_NAME_ZK_MIN_LATENCY = "ctg_mes_config_zk_min_latency";    				//最小延迟
	public static final String METRIC_NAME_ZK_PACKETS_RECEIVED = "ctg_mes_config_zk_packets_received";    		//接收到客户端请求的包数量
	public static final String METRIC_NAME_ZK_PACKETS_SEND = "ctg_mes_config_zk_packets_sent";   		  		//发送给客户端的包数量，主要是响应和通知
	public static final String METRIC_NAME_ZK_NUM_ALIVE_CONNECTIONS = "ctg_mes_config_zk_num_alive_connections";//最大存活连接数
	public static final String METRIC_NAME_ZK_OUTSTANDING_REQUESTS = "ctg_mes_config_zk_outstanding_requests";  //排队请求的数量，当ZooKeeper超过了它的处理能力时，这个值会增大，建议设置报警阀值为10
	public static final String METRIC_NAME_ZK_SERVER_STATE = "ctg_mes_config_zk_server_state"; 					//服务状态 (leader/follower)
	public static final String METRIC_NAME_ZK_ZNODE_COUNT = "ctg_mes_config_zk_znode_count";    				//znodes的数量（节点数量）
	public static final String METRIC_NAME_ZK_WATCH_COUNT = "ctg_mes_config_zk_watch_count";    				//zwatches的数量（watch数量）
	public static final String METRIC_NAME_ZK_EPHEMERALS_COUNT = "ctg_mes_config_zk_ephemerals_count";    		//临时节点数量
	public static final String METRIC_NAME_ZK_APPROXIMATE_SIZE = "ctg_mes_config_zk_approximate_data_size";    	//快照体积大小，近似数据总和大小
	public static final String METRIC_NAME_ZK_OPEN_FILE_DESCRIPTOR_COUNT = "ctg_mes_config_zk_open_file_descriptor_count";//打开文件描述符总数
	public static final String METRIC_NAME_ZK_MAX_FILE_DESCRIPTOR_COUNT = "ctg_mes_config_zk_max_file_descriptor_count";//最大 文件描述符 数
	public static final String METRIC_NAME_ZK_FOLLOWERS = "ctg_mes_config_zk_followers";						//leader角色才会有这个输出,集合中follower的个数。正常的值应该是集合成员的数量减1
	public static final String METRIC_NAME_ZK_SYNCED_FOLLOWERS = "ctg_mes_config_zk_synced_followers";			//已经同步的FOLLOWERS的个数
	public static final String METRIC_NAME_ZK_PENDING_SYNCS = "ctg_mes_config_zk_pending_syncs";			    //leader角色才会有这个输出，pending syncs的FOLLOWERS数量
 
}
