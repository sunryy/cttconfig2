package com.ctg.mes.config.console.controller.zookeeper;

import com.ctg.mes.config.common.dto.response.Response;
import com.ctg.mes.config.console.service.zookeeper.IZkParamService;
import com.google.gson.internal.LinkedTreeMap;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Api(tags = "ZkParamController", description = "参数配置")
@RestController
@RequestMapping("/zk/param")
public class ZkParamController {

    @Autowired
    IZkParamService paramService;

    @ApiOperation(value = "获取参数配置")
    @GetMapping(value = "/get/{instId}")
    public Response getParams(@ApiParam(name = "instId", value = "实例Id", required = true) @PathVariable String instId) {
        LinkedTreeMap params = paramService.getParams(instId);
        return Response.success(params);
    }

    @PostMapping(value = "/update/{instId}")
    @ApiOperation(value = "更新参数配置")
    public Response updateParams(@ApiParam(name = "instId", value = "实例Id", required = true) @PathVariable String instId, @RequestBody HashMap<String,String> params){
        paramService.updateParams(instId, params);
        return Response.success();
    }

}
