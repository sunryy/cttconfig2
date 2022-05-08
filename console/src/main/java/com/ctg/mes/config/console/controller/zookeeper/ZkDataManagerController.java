package com.ctg.mes.config.console.controller.zookeeper;

import com.ctg.mes.config.common.dto.response.Response;
import com.ctg.mes.config.common.dto.zookeeper.ZkData;
import com.ctg.mes.config.console.service.zookeeper.IZkDataManagerService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Api(tags = "ZkDataManagerController", description = "数据管理")
@RestController
@RequestMapping("/zk/dataManager")
public class ZkDataManagerController {

    @Autowired
    IZkDataManagerService dataManagerService;

    @GetMapping(value = "/get/{instId}")
    @ApiOperation(value = "根据配置路径获取数据")
    public Response getData(@ApiParam(name = "instId", value = "实例Id", required = true) @PathVariable String instId, String path, Integer maxDepth) {
        //默认只获取当前节点的内容
        if (maxDepth == null || maxDepth == 0) {
            maxDepth = 1;
        }
        ZkData content = dataManagerService.getData(instId, path, maxDepth);
        return Response.success(content);
    }

    @PostMapping(value = "/update/{instId}")
    @ApiOperation(value = "根据配置路径设置数据")
    public Response setData(@ApiParam(name = "instId", value = "实例Id", required = true) @PathVariable String instId, String path, String content) {
        dataManagerService.createOrUpdateData(instId, path, content);
        return Response.success();
    }

    @PostMapping(value = "/create/{instId}")
    @ApiOperation(value = "创建节点")
    public Response createData(@ApiParam(name = "instId", value = "实例Id", required = true) @PathVariable String instId, String path, String content) {
        dataManagerService.createOrUpdateData(instId, path, content);
        return Response.success();
    }

    @PostMapping(value = "/remove/{instId}")
    @ApiOperation(value = "创建节点")
    public Response removeData(@ApiParam(name = "instId", value = "实例Id", required = true) @PathVariable String instId, String path) {
        dataManagerService.removeNode(instId, path);
        return Response.success();
    }

}
