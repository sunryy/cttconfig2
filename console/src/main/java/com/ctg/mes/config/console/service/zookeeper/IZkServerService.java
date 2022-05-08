package com.ctg.mes.config.console.service.zookeeper;

import com.ctg.mes.config.common.dto.zookeeper.ZkCluster;
import com.ctg.mes.config.common.dto.zookeeper.ZkNode;

import java.util.List;
import java.util.Map;

public interface IZkServerService {

    /**
     * 根据实例获取实例节点列表
     * @param instId
     * @return
     */
    ZkCluster getServerCluster(String instId);

    /**
     * 根据实例ID与节点ID获取服务节点信息
     * @param instId
     * @param nodeId
     * @return
     */
    ZkNode getServerNode(String instId, String nodeId);

    /**
     * 根据实例ID获取服务的连接串
     */
    String getServerConnectString(String instId);

    /**
     * 启动节点进程
     * @param instId
     * @param nodeId
     */
    void startNode(String instId, String nodeId);

    /**
     * 停止节点进程
     * @param instId
     * @param nodeId
     */
    void stopNode(String instId, String nodeId);

    /**
     * 重启节点进程
     * @param instId
     * @param nodeId
     */
    void restartNode(String instId, String nodeId);

    /**
     * 获取节点状态
     * @param instId
     * @param nodeId
     */
    List<String> getNodeStatus(String instId, String nodeId);

    /**
     * 调用mgr的restful服务（GET方法）
     * @param instId
     * @param nodeId
     * @param api
     * @return
     */
    String callMgrGetRestful(String instId, String nodeId, String api);

    /**
     * 调用mgr的restful服务（POST方法）
     * @param instId
     * @param nodeId
     * @param api
     * @param params
     * @return
     */
    String callMgrPostRestful(String instId, String nodeId, String api, Map params);
}
