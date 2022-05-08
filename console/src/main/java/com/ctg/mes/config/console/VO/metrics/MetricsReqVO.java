package com.ctg.mes.config.console.VO.metrics;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Setter
@Getter
public class MetricsReqVO {

    //实例ID
    private String instId;
    //节点ID
    private String nodeId;
    // 开始时间
    private Date startTime;
    // 结束
    private Date endTime;
    // monitor类型
    private String type;

    public void check() {
        if (null == instId) {
            throw new RuntimeException("instId is null");
        }
        if (null == startTime) {
            throw new RuntimeException("startTime is null");
        }
        if (null == endTime) {
            throw new RuntimeException("endTime is null");
        }
        if (null == type) {
            throw new RuntimeException("types is null");
        }
    }
}
