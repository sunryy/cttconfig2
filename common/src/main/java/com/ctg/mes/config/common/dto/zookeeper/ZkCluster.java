package com.ctg.mes.config.common.dto.zookeeper;

import lombok.Data;
import org.springframework.util.CollectionUtils;

import java.util.List;

@Data
public class ZkCluster {

    private String instId;                  //实例Id
    private List<ZkNode> nodeList;          //节点列表

    /**
     *  获取zk的客户端地址
     */
    public String getConnectionString() {
        List<ZkNode> zkNodes = nodeList;
        if (CollectionUtils.isEmpty(zkNodes)) {
            throw null;
        }

        StringBuilder sb = new StringBuilder();
        for (ZkNode zkNode : zkNodes) {
            sb.append(zkNode.getConnectionString()).append(",");
        }
        sb.deleteCharAt(sb.length()-1);
        return sb.toString();
    }

    /**
     * 获取集群的leader节点
     * @return
     */
    public ZkNode getLeaderNode() {
        for (ZkNode zkNode : nodeList) {
            if (zkNode.isLeader()) {
                return zkNode;
            }
        }
        return null;
    }

    @Override
    public String toString() {
        return "ZkCluster{" +
                "instId='" + instId + '\'' +
                ", nodeList=" + nodeList +
                '}';
    }

}
