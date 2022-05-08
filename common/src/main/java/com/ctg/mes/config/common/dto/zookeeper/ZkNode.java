package com.ctg.mes.config.common.dto.zookeeper;

import lombok.Builder;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

@Data
@Builder
public class ZkNode {

    private String instId;                  //实例Id
    private String nodeId;                  //节点Id
    private String ipv4;                    //ipv4
    private String ipv6;                    //ipv6
    private int clientPort;                 //客户端端口
    private int mgrPort;                    //mgr端口
    private String installPath;             //安装部署目录
    private String state;                   //节点状态 leader follower;


    /**
     *  获取代理的restful地址
     */
    public String getMrgAddr() {
        String ip = !StringUtils.isEmpty(ipv4) ? ipv4 : ipv6;
        String addr = String.format("http://%s:%d", ip, mgrPort);
        return addr;
    }

    /**
     *  获取zk的客户端地址
     */
    public String getConnectionString() {
        String ip = !StringUtils.isEmpty(ipv4) ? ipv4 : ipv6;
        String addr = String.format("%s:%d", ip, clientPort);
        return addr;
    }

    /**
     *  判断是否是主
     */
    public boolean isLeader() {
        //TODO 定期扫描
        // return "leader".equalsIgnoreCase(state);
        return true;
    }
}
