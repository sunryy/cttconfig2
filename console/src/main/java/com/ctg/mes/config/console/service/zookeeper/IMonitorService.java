package com.ctg.mes.config.console.service.zookeeper;

import com.ctg.mes.config.console.VO.metrics.MetricsReqVO;
import com.ctg.mes.config.console.VO.metrics.MetricsVO;

public interface IMonitorService {

    MetricsVO queryMetrics(MetricsReqVO reqVO) throws Exception;
}
