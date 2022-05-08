package com.ctg.mes.config.console.service.zookeeper.impl;

import com.ctg.mes.config.common.dto.response.Response;
import com.ctg.mes.config.common.dto.zookeeper.ZkCluster;
import com.ctg.mes.config.common.dto.zookeeper.ZkNode;
import com.ctg.mes.config.common.util.HttpClient;
import com.ctg.mes.config.common.util.JsonUtil;
import com.ctg.mes.config.console.service.zookeeper.IZkServerService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class ZkServerServiceImpl implements IZkServerService {

    private static final String MGR_API_PREFIX = "MesConfMgr/zk/manager"; //mgr的api前缀地址

    @Override
    public ZkCluster getServerCluster(String instId) {
        //mock zk node list
        List<ZkNode> zkNodes = new ArrayList<>();
        ZkNode zkNode = ZkNode.builder().instId(instId).nodeId("1").ipv4("172.16.200.125")
                .clientPort(13181).mgrPort(8099).installPath("/data/apps/pg-zookeeper-3.5.9/").build();
        zkNodes.add(zkNode);

        ZkCluster cluster = new ZkCluster();
        cluster.setNodeList(zkNodes);
        return cluster;
    }

    @Override
    public ZkNode getServerNode(String instId, String nodeId) {
        ZkCluster zkCluster = getServerCluster(instId);
        List<ZkNode> zkNodes = zkCluster.getNodeList();
        for (ZkNode zkNode : zkNodes) {
            if (zkNode.getNodeId().equals(nodeId)) {
                return zkNode;
            }
        }
        return null;
    }

    @Override
    public String getServerConnectString(String instId) {
        ZkCluster zkCluster = getServerCluster(instId);
        if (zkCluster != null) {
           return zkCluster.getConnectionString();
        }
        return null;
    }

    @Override
    public void startNode(String instId, String nodeId) {
        String result = callMgrGetRestful(instId, nodeId, "startNode");
        Response<List<String>> response = JsonUtil.fromJson(result, Response.class);
        if (200 != response.getCode()) {
            throw new RuntimeException(response.getMessage());
        }
    }

    @Override
    public void stopNode(String instId, String nodeId) {
        String result = callMgrGetRestful(instId, nodeId, "stopNode");
        Response<List<String>> response = JsonUtil.fromJson(result, Response.class);
        if (200 != response.getCode()) {
            throw new RuntimeException(response.getMessage());
        }
    }

    @Override
    public void restartNode(String instId, String nodeId) {
        String result = callMgrGetRestful(instId, nodeId, "restartNode");
        Response<List<String>> response = JsonUtil.fromJson(result, Response.class);
        if (200 != response.getCode()) {
            throw new RuntimeException(response.getMessage());
        }
    }

    @Override
    public List<String> getNodeStatus(String instId, String nodeId) {
        String reponse = callMgrGetRestful(instId, nodeId, "getNodeStatus");
        Response<List<String>> response = JsonUtil.fromJson(reponse, Response.class);
        if (200 != response.getCode()) {
            throw new RuntimeException(response.getMessage());
        }
        List<String> result = response.getData();
        return result;
    }

    /**
     *  调用mgr的restful服务,目前都是Get方法
     */
    @Override
    public String callMgrGetRestful(String instId, String nodeId, String api) {
        ZkNode zkNode = getServerNode(instId, nodeId);
        String url = String.format("%s/%s/%s", zkNode.getMrgAddr(), MGR_API_PREFIX, api);
        String response = HttpClient.get(url);
        return response;
    }

    @Override
    public String callMgrPostRestful(String instId, String nodeId, String api, Map params) {
        ZkNode zkNode = getServerNode(instId, nodeId);
        String url = String.format("%s/%s/%s", zkNode.getMrgAddr(), MGR_API_PREFIX, api);
        String response = HttpClient.post(url, params);
        return response;
    }
}
