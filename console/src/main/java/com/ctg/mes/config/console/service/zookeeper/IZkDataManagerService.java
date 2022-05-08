package com.ctg.mes.config.console.service.zookeeper;

import com.ctg.mes.config.common.dto.zookeeper.ZkData;

public interface IZkDataManagerService {

    /**
     *  获取节点内容
     */
   ZkData getData(String instId, String path, int maxDepth);

    /**
     *  更新或创建节点内容
     */
    void createOrUpdateData(String instId, String path, String content);

    /**
     *  尝试删除指定节点以及子节点
     */
    void removeNode(String instId, String path);
}
