package com.ctg.mes.config.mgr.monitor.exporter;

import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Getter
public class ExporterConfig {

    private static final Logger logger = LoggerFactory.getLogger(ExporterConfig.class);

    private int httpPort ;            //服务端口
    private boolean authEnable ;       //开启basic_auth   true | false
    private String authUser;          //basic_auth  用户名
    private String authPwd;           //basic_auth  密码

    public ExporterConfig(int httpPort) {
        this.httpPort = httpPort;
        this.authEnable = false;
    }
    public ExporterConfig(int httpPort, boolean authEnable, String authUser, String authPwd) {
        this.httpPort = httpPort;
        this.authEnable = authEnable;
        this.authUser = authUser;
        this.authPwd = authPwd;
    }

    @Override
    public String toString() {
        return "ExporterConfig{" +
                "httpPort=" + httpPort +
                ", authEnable=" + authEnable +
                ", authUser='" + authUser + '\'' +
                '}';
    }
}
