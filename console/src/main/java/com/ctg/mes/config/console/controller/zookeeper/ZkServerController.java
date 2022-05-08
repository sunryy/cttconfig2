package com.ctg.mes.config.console.controller.zookeeper;

import com.ctg.mes.config.common.dto.response.Response;
import com.ctg.mes.config.common.dto.zookeeper.ZkCluster;
import com.ctg.mes.config.common.dto.zookeeper.ZkNode;
import com.ctg.mes.config.console.service.zookeeper.IZkServerService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Api(tags = "ZkServerController", description = "服务集群管理")
@RestController
@RequestMapping("/zk/server")
public class ZkServerController {
    private static final Logger LOGGER = LoggerFactory.getLogger(ZkServerController.class);

    @Autowired
    IZkServerService monitorService;

    @ApiOperation(value = "获取实例集群信息")
    @GetMapping(value = "/cluster/{instId}")
    public Response getCluster(@ApiParam(name = "instId", value = "实例Id", required = true) @PathVariable String instId) {
        ZkCluster zkCluster = monitorService.getServerCluster(instId);
        return Response.success(zkCluster);
    }

    @ApiOperation(value = "获取实例节点信息")
    @GetMapping(value = "/node/{instId}/{nodeId}")
    public Response getServerNode(@ApiParam(name = "instId", value = "实例Id", required = true) @PathVariable String instId,@ApiParam(name = "nodeId", value = "节点ID", required = true) @PathVariable String nodeId) {
        ZkNode zkNode = monitorService.getServerNode(instId, nodeId);
        return Response.success(zkNode);
    }

    @ApiOperation(value = "启动实例节点进程")
    @GetMapping(value = "/startNode/{instId}/{nodeId}")
    public Response startNode(@ApiParam(name = "instId", value = "实例Id", required = true) @PathVariable String instId,@ApiParam(name = "nodeId", value = "节点ID", required = true) @PathVariable String nodeId) {
        monitorService.startNode(instId, nodeId);
        return Response.success();
    }

    @ApiOperation(value = "重启实例节点进程")
    @GetMapping(value = "/restartNode/{instId}/{nodeId}")
    public Response restartNode(@ApiParam(name = "instId", value = "实例Id", required = true) @PathVariable String instId,@ApiParam(name = "nodeId", value = "节点ID", required = true) @PathVariable String nodeId) {
        monitorService.restartNode(instId, nodeId);
        return Response.success();
    }

    @ApiOperation(value = "停止实例节点进程")
    @GetMapping(value = "/stopNode/{instId}/{nodeId}")
    public Response stopNode(@ApiParam(name = "instId", value = "实例Id", required = true) @PathVariable String instId,@ApiParam(name = "nodeId", value = "节点ID", required = true) @PathVariable String nodeId) {
        monitorService.stopNode(instId, nodeId);
        return Response.success();
    }

    @ApiOperation(value = "获取实例节点进程")
    @GetMapping(value = "/getNodeStatus/{instId}/{nodeId}")
    public Response getNodeStatus(@ApiParam(name = "instId", value = "实例Id", required = true) @PathVariable String instId,@ApiParam(name = "nodeId", value = "节点ID", required = true) @PathVariable String nodeId) {
        List<String> status = monitorService.getNodeStatus(instId, nodeId);
        return Response.success(status);
    }

}
