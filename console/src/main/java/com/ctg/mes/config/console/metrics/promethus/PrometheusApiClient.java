package com.ctg.mes.config.console.metrics.promethus;

import com.ctg.mes.config.console.metrics.promethus.model.KeyValResponse;
import com.ctg.mes.config.console.metrics.promethus.model.MatrixResponse;
import com.ctg.mes.config.console.metrics.promethus.model.VectorResponse;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.Map;

public class PrometheusApiClient {
    private final static Logger logger = LoggerFactory.getLogger(PrometheusApiClient.class);

    /**
     * baseUrl : "http://ip:port/api/v1/"
     */
    private String baseUrl;
    private final static String API_QUERY = "query";
    private final static String API_QUERY_RANGE = "query_range";
    private final static String API_SERIES = "series";


    public PrometheusApiClient(String prometheusHost, int prometheusPort) {
        baseUrl = String.format("http://%s:%d/api/v1/", prometheusHost, prometheusPort);
    }

    public PrometheusApiClient(String prometheusIpAndPort) {
        baseUrl = String.format("http://%s/api/v1/", prometheusIpAndPort);
    }


    public VectorResponse query(String query) {
        return query(query, null, null);
    }

    public VectorResponse query(String query, String time) {
        return query(query, time, null);
    }

    public VectorResponse query(String query, String time, String timeout) {
        String url = baseUrl + API_QUERY + "?query={query}";
        if (!StringUtils.isEmpty(time)) {
            url += "&time={time}";
        }
        if (!StringUtils.isEmpty(timeout)) {
            url += "&timeout={timeout}";
        }
        Map<String, String> uriVariables = new HashMap<>();
        uriVariables.put("query", query);
        uriVariables.put("time", time);
        uriVariables.put("timeout", timeout);


        ResponseEntity<VectorResponse> responseEntity = RestTemplateUtils.getInstance().get(url, VectorResponse.class, uriVariables);
        logger.info("vectorResponse : {}", responseEntity.getBody().toString());
        return responseEntity.getBody();
    }


    public MatrixResponse queryRange(String query, String start, String end, String step) {
        return queryRange(query, start, end, step, null);
    }

    public MatrixResponse queryRange(String query, String start, String end, String step, String timeout) {
        String url = baseUrl + API_QUERY_RANGE + "?query={query}";
        if (!StringUtils.isEmpty(start)) {
            url += "&start={start}";
        }
        if (!StringUtils.isEmpty(end)) {
            url += "&end={end}";
        }
        if (!StringUtils.isEmpty(step)) {
            url += "&step={step}";
        }
        if (!StringUtils.isEmpty(timeout)) {
            url += "&timeout={timeout}";
        }

        Map<String, String> uriVariables = new HashMap<>();
        uriVariables.put("query", query);
        uriVariables.put("start", start);
        uriVariables.put("end", end);
        uriVariables.put("step", step);
        uriVariables.put("timeout", timeout);


        ResponseEntity<MatrixResponse> responseEntity = RestTemplateUtils.getInstance().get(url, MatrixResponse.class, uriVariables);
        logger.info("MatrixResponse : {}", responseEntity.getBody().toString());
        return responseEntity.getBody();
    }

    public KeyValResponse findSeries(String match) {
        return findSeries(match, null, null);
    }

    public KeyValResponse findSeries(String match, String start, String end) {
        String url = baseUrl + API_SERIES + "?match[]={match}";
        if (!StringUtils.isEmpty(start)) {
            url += "&start={start}";
        }
        if (!StringUtils.isEmpty(end)) {
            url += "&end={end}";
        }

        Map<String, String> uriVariables = new HashMap<>();
        uriVariables.put("match", match);
        uriVariables.put("start", start);
        uriVariables.put("end", end);


        ResponseEntity<KeyValResponse> responseEntity = RestTemplateUtils.getInstance().get(url, KeyValResponse.class, uriVariables);
        logger.info("KeyValResponse : {}", responseEntity.getBody().toString());
        return responseEntity.getBody();
    }


    public static void main(String[] args) {
        PrometheusApiClient client = new PrometheusApiClient("10.50.208.172" ,   8001);
        VectorResponse response = client.query("ctg_cache_access_free_redis_conn_count");
        System.out.println(response);
    }
}
