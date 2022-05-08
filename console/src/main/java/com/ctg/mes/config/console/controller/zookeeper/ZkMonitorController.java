package com.ctg.mes.config.console.controller.zookeeper;

import com.ctg.mes.config.common.dto.response.Response;
import com.ctg.mes.config.console.VO.metrics.MetricsReqVO;
import com.ctg.mes.config.console.VO.metrics.MetricsVO;
import com.ctg.mes.config.console.service.zookeeper.IMonitorService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = "ZkMonitorController", description = "指标监控")
@RestController
@RequestMapping("/zk/monitor")
public class ZkMonitorController {
    private static final Logger LOGGER = LoggerFactory.getLogger(ZkMonitorController.class);

    @Autowired
    IMonitorService monitorService;

    @ApiOperation("查询实例监控信息")
    @RequestMapping(value = "/monitor/query", method = RequestMethod.POST)
    public Response<?> queryMonitor(@RequestBody MetricsReqVO reqVO) {
        try {
            reqVO.check();
            MetricsVO vo = monitorService.queryMetrics(reqVO);
            return Response.success(vo);
        } catch (Exception e) {
            LOGGER.warn("queryMonitor ---> error: ", e);
            return Response.fail(e.getMessage());
        }
    }


}
