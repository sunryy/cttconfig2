package com.ctg.mes.config.mgr.controller;

import com.ctg.mes.config.common.dto.response.Response;
import com.ctg.mes.config.mgr.service.IZkManagerService;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/zk/manager")
public class ZkManagerController {
    @Autowired
    IZkManagerService managerService;

    @ApiOperation(value = "获取参数配置")
    @GetMapping(value = "/getParams")
    public Response getParams() {
        Map<String,String> params = managerService.getParams();
        return Response.success(params);
    }

    @ApiOperation(value = "更新参数配置")
    @PostMapping(value = "/updateParams")
    public Response updateParams(@RequestBody HashMap<String,String> params) {
        managerService.updateParams(params);
        return Response.success();
    }

    @ApiOperation(value = "启动实例")
    @GetMapping(value = "/startNode")
    public Response startNode() {
        managerService.startNode();
        return Response.success();
    }

    @ApiOperation(value = "重启实例")
    @GetMapping(value = "/restartNode")
    public Response restartNode() {
        managerService.restartNode();
        return Response.success();
    }

    @ApiOperation(value = "停止实例")
    @GetMapping(value = "/stopNode")
    public Response stopNode() {
        managerService.stopNode();
        return Response.success();
    }

    @ApiOperation(value = "获取实例状态")
    @GetMapping(value = "/getNodeStatus")
    public Response getNodeStatus() {
        Pair<List<String>, List<String>> pair = managerService.getNodeStatus();
        if (pair == null) {
            return Response.fail("can not get node status");
        }
        return Response.success(pair.getLeft());
    }
}