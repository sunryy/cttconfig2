package com.ctg.mes.config.mgr.monitor.bean;

/**
 * 监控指标数据
 */
public class MonitorDataVO implements Comparable<MonitorDataVO> {

	private String instId;      //实例ID
	private String nodeId;		// 节点ID
	private long time;			// 监控时间时间戳形式

	public String getMonitorMetricDimensionType() {
		return "";
	}

	@Override
	public int compareTo(MonitorDataVO o) {
		return (int) (this.time - o.getTime());
	}

	public long getTime() {
		return time;
	}

	public void setTime(long time) {
		this.time = time;
	}

	public String getNodeId() {
		return nodeId;
	}

	public void setNodeId(String nodeId) {
		this.nodeId = nodeId;
	}

	public String getInstId() {
		return instId;
	}

	public void setInstId(String instId) {
		this.instId = instId;
	}
}
