package com.ctg.mes.config.common.util;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;

public class HttpClient {

    private static final ThreadLocal<RestTemplate> template = ThreadLocal.withInitial(RestTemplate::new);

    /**
     * 使用GET请求，如果出现网络不通的情况或任务处理有问题会返回异常
     */
    public static String get(String url) {
        return template.get().getForObject(url, String.class);
    }

    public static String get(String url, String params) {
        return template.get().getForObject(url, String.class, params);
    }

    public static String post(String url, Object params) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Object> requestEntity = new HttpEntity<>(params, headers);
        return template.get().postForObject(url, requestEntity, String.class);
    }
}
