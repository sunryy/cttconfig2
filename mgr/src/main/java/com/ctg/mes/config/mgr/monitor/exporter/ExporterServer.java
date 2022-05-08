package com.ctg.mes.config.mgr.monitor.exporter;

import com.ctg.mes.config.mgr.monitor.exporter.collector.MonitorRingBufferCollector;
import com.ctg.mes.config.mgr.monitor.ringbuffer.RingBufferFactory;
import com.sun.net.httpserver.Authenticator;
import com.sun.net.httpserver.BasicAuthenticator;
import io.prometheus.client.exporter.HTTPServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class ExporterServer {
    private final static Logger log = LoggerFactory.getLogger(ExporterServer.class);

    public static void exporterHttpServer(ExporterConfig config) {
        try {
            int port = config.getHttpPort();
            //注册collector
            new MonitorRingBufferCollector(RingBufferFactory.getMonitorRingBuffer()).register();

            //启动采集服务
            if (config.isAuthEnable()) {
                new HTTPServer.Builder()
                        .withPort(port)
                        .withAuthenticator(buildBaseAuthenticator(config))
                        .build();
            } else {
                new HTTPServer(port);
            }

            log.info("start exporter server, httpPort :" + port);
        }catch (IOException e){
            log.error("buildExporterHttpServer exception:{}", e.getMessage(),e);
        }
    }

    private static Authenticator buildBaseAuthenticator(ExporterConfig config){
        String authUser = config.getAuthUser();
        String authPwd = config.getAuthPwd();
        return new BasicAuthenticator("access") {
            @Override
            public boolean checkCredentials(String name, String pwd) {
                if(name.equals(authUser) && pwd.equals(authPwd)){
                    return true;
                }
                return false;
            }
        };
    }
}
