package com.ctg.mes.config.console.service.zookeeper.impl;

import com.ctg.mes.config.common.dto.zookeeper.ZkData;
import com.ctg.mes.config.common.exception.BusinessException;
import com.ctg.mes.config.console.service.zookeeper.IZkDataManagerService;
import com.ctg.mes.config.console.service.zookeeper.IZkServerService;
import com.ctg.mes.config.console.util.ZookeeperUtils;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.utils.ZKPaths;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@Service
public class ZkDataManagerServiceImpl implements IZkDataManagerService {
    private static final Logger LOGGER = LoggerFactory.getLogger(ZkDataManagerServiceImpl.class);

    @Autowired
    private IZkServerService serverService;

    @Autowired
    private ZkScheduleImpl zookeeperSchedule;

    @Override
    public ZkData getData(String instId, String path, int maxDepth) {
        ZkData zkData = getNodeData(instId, path);
        if (zkData != null && maxDepth > 0 ) {
            zkData = getChildrenData(zkData, instId, path, maxDepth - 1);
        }
        return zkData;
    }

    @Override
    public void createOrUpdateData(String instId, String path, String content) {
        CuratorFramework zkClient = getZkClient(instId);
        try {
           ZookeeperUtils.saveData(zkClient, path, content.getBytes(StandardCharsets.UTF_8));
        } catch (Exception e) {
            LOGGER.info(e.getMessage());
        }
    }

    @Override
    public void removeNode(String instId, String path) {
        CuratorFramework zkClient = getZkClient(instId);
        try {
            ZookeeperUtils.tryDeleteNode(zkClient, path);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
        }
    }

    /**
     * 获取当前节点的内容
     * @param instId
     * @param path
     * @return
     */
    public ZkData getNodeData(String instId, String path) {
        CuratorFramework zkClient = getZkClient(instId);
        String content = "";
        try {
            content = ZookeeperUtils.getDataAsString(zkClient, path);
            return new ZkData(path, content, 0);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
        }
        return null;
    }

    /**
     * 获取节点下（包括子节点）的内容
     * @param parentZkData
     * @param instId
     * @param path
     * @param maxDepth
     * @return
     */
    public ZkData getChildrenData(ZkData parentZkData, String instId, String path, int maxDepth) {
        try {
            if (maxDepth == 0) {
                return parentZkData;
            }
            CuratorFramework zkClient = getZkClient(instId);
            List<String> childrenPathList = zkClient.getChildren().forPath(path);
            if (CollectionUtils.isEmpty(childrenPathList)) {
                return parentZkData;
            }

            List<ZkData> childrenData = new ArrayList<>();
            for (String childrenPath : childrenPathList) {
                String childPath = ZKPaths.makePath(path, childrenPath);
                String childData = ZookeeperUtils.getDataAsString(zkClient, childPath);
                ZkData child = new ZkData(childPath, childData, maxDepth - 1);
                getChildrenData(child, instId, childPath, maxDepth -1); //填充子节点内容
                childrenData.add(child);
            }
            parentZkData.setChildren(childrenData);
        } catch (Exception e){
            LOGGER.error(e.getMessage());
        }
        return parentZkData;
    }

    /**
     * 根据实例获取zk的客户端实例
     * @param instId
     * @return
     */
    private CuratorFramework getZkClient(String instId) {
        String serverKey = getClusterConnectionString(instId);
        CuratorFramework zkClient = zookeeperSchedule.get(serverKey);
        if (zkClient == null) {
            throw new BusinessException("can not find zk client by instId : " + instId);
        }
        return zkClient;
    }

    /**
     * 获取集群的连接串
     * @param instId
     * @return
     */
    private String getClusterConnectionString(String instId) {
        return serverService.getServerConnectString(instId);
    }
}
