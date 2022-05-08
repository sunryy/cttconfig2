package com.ctg.mes.config.console.service.zookeeper.impl;

import com.ctg.mes.config.common.dto.response.Response;
import com.ctg.mes.config.common.dto.zookeeper.ZkCluster;
import com.ctg.mes.config.common.dto.zookeeper.ZkNode;
import com.ctg.mes.config.common.exception.BusinessException;
import com.ctg.mes.config.common.util.JsonUtil;
import com.ctg.mes.config.console.service.zookeeper.IZkParamService;
import com.ctg.mes.config.console.service.zookeeper.IZkServerService;
import com.google.gson.internal.LinkedTreeMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ZkParamServiceImpl implements IZkParamService {
    private static final Logger LOGGER = LoggerFactory.getLogger(ZkParamServiceImpl.class);

    @Autowired
    private IZkServerService serverService;

    @Override
    public LinkedTreeMap getParams(String instId) {
        ZkCluster zkCluster = serverService.getServerCluster(instId);
        ZkNode zkNode = zkCluster.getLeaderNode();
        String result = serverService.callMgrGetRestful(instId, zkNode.getNodeId(), "getParams");
        Response<LinkedTreeMap> response = JsonUtil.fromJson(result, new Response<LinkedTreeMap>().getClass());
        if (200 != response.getCode()) {
            throw new BusinessException(response.getMessage());
        }
        return response.getData();
    }

    @Override
    public boolean updateParams(String instId, HashMap<String,String> params) {
        ZkCluster zkCluster = serverService.getServerCluster(instId);
        List<ZkNode> zkNodeList = zkCluster.getNodeList();
        for (ZkNode zkNode : zkNodeList) {
            try {
              serverService.callMgrPostRestful(instId, zkNode.getNodeId(), "updateParams", params);
            }catch (Exception e) {
                LOGGER.warn("can not update params. zknode = {}, params = {}, errMsg = {}", zkNode, JsonUtil.toJson(params), e.getMessage());
                throw new BusinessException(e.getMessage());
            }
        }
        return true;
    }

}
