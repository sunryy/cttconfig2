package com.ctg.mes.config.common.dto.zookeeper;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class ZkData {

    private String path;            //节点路径
    private String content;         //节点内容
    private List<ZkData> children;  //子节点
    private int maxDepth = 0;          //获取子节点的最大深度，默认只返回当前节点的数据

    public ZkData(String path, String content, int maxDepth) {
        this.path = path;
        this.content = content;
        this.maxDepth = maxDepth;
    }
}
