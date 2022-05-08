package com.ctg.mes.config.console.metrics.promethus;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.Map;


public class RestTemplateUtils {

    private static final Logger log= LoggerFactory.getLogger(RestTemplateUtils.class);

    private static final int CONNECT_TIMEOUT = 6000;  // 设置连接超时时间，单位毫秒。
    private static final int SOCKET_TIMEOUT = 6000;   // 请求获取数据的超时时间(即响应时间)，单位毫秒

    private static RestTemplateUtils instance;

    private  RestTemplate restTemplate;

    private RestTemplateUtils(){}
    public static synchronized RestTemplateUtils getInstance(){
        if(instance == null){
            HttpComponentsClientHttpRequestFactory httpRequestFactory = new HttpComponentsClientHttpRequestFactory();
            httpRequestFactory.setConnectionRequestTimeout(CONNECT_TIMEOUT);
            httpRequestFactory.setConnectTimeout(CONNECT_TIMEOUT);
            httpRequestFactory.setReadTimeout(SOCKET_TIMEOUT);

            instance = new RestTemplateUtils();
            instance.restTemplate = new RestTemplate(httpRequestFactory);
        }
        return instance;
    }

    // ----------------------------------GET-------------------------------------------------------

    /**
     * GET请求调用方式
     *
     * @param url          请求URL
     * @param responseType 返回对象类型
     * @return ResponseEntity 响应对象封装类
     */
    public <T> ResponseEntity<T> get(String url, Class<T> responseType) throws RestClientException {
        return restTemplate.getForEntity(url, responseType);
    }

    /**
     * GET请求调用方式
     *
     * @param url          请求URL
     * @param responseType 返回对象类型
     * @param uriVariables URL中的变量，按顺序依次对应
     * @return ResponseEntity 响应对象封装类
     */
    public <T> ResponseEntity<T> get(String url, Class<T> responseType, Object... uriVariables)
            throws RestClientException {
        return restTemplate.getForEntity(url, responseType, uriVariables);
    }

    /**
     * GET请求调用方式
     *
     * @param url          请求URL
     * @param responseType 返回对象类型
     * @param uriVariables URL中的变量，与Map中的key对应
     * @return ResponseEntity 响应对象封装类
     */
    public <T> ResponseEntity<T> get(String url, Class<T> responseType, Map<String, ?> uriVariables)
            throws RestClientException {
        return restTemplate.getForEntity(url, responseType, uriVariables);
    }

    /**
     * 带请求头的GET请求调用方式
     *
     * @param url          请求URL
     * @param headers      请求头参数
     * @param responseType 返回对象类型
     * @param uriVariables URL中的变量，按顺序依次对应
     * @return ResponseEntity 响应对象封装类
     */
    public <T> ResponseEntity<T> get(String url, Map<String, String> headers, Class<T> responseType,
                                     Object... uriVariables) throws RestClientException {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setAll(headers);
        return get(url, httpHeaders, responseType, uriVariables);
    }

    /**
     * 带请求头的GET请求调用方式
     *
     * @param url          请求URL
     * @param headers      请求头参数
     * @param responseType 返回对象类型
     * @param uriVariables URL中的变量，按顺序依次对应
     * @return ResponseEntity 响应对象封装类
     */
    public <T> ResponseEntity<T> get(String url, HttpHeaders headers, Class<T> responseType, Object... uriVariables)
            throws RestClientException {
        HttpEntity<?> requestEntity = new HttpEntity<>(headers);
        return exchange(url, HttpMethod.GET, requestEntity, responseType, uriVariables);
    }

    /**
     * 带请求头的GET请求调用方式
     *
     * @param url          请求URL
     * @param headers      请求头参数
     * @param responseType 返回对象类型
     * @param uriVariables URL中的变量，与Map中的key对应
     * @return ResponseEntity 响应对象封装类
     */
    public <T> ResponseEntity<T> get(String url, Map<String, String> headers, Class<T> responseType,
                                     Map<String, ?> uriVariables) throws RestClientException {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setAll(headers);
        return get(url, httpHeaders, responseType, uriVariables);
    }

    /**
     * 带请求头的GET请求调用方式
     *
     * @param url          请求URL
     * @param headers      请求头参数
     * @param responseType 返回对象类型
     * @param uriVariables URL中的变量，与Map中的key对应
     * @return ResponseEntity 响应对象封装类
     */
    public <T> ResponseEntity<T> get(String url, HttpHeaders headers, Class<T> responseType,
                                     Map<String, ?> uriVariables) throws RestClientException {
        HttpEntity<?> requestEntity = new HttpEntity<>(headers);
        return exchange(url, HttpMethod.GET, requestEntity, responseType, uriVariables);
    }

    // ----------------------------------POST-------------------------------------------------------

    /**
     * POST请求调用方式
     *
     * @param url          请求URL
     * @param responseType 返回对象类型
     * @return
     */
    public <T> ResponseEntity<T> post(String url, Class<T> responseType) throws RestClientException {
        return restTemplate.postForEntity(url, HttpEntity.EMPTY, responseType);
    }

    /**
     * POST请求调用方式
     *
     * @param url          请求URL
     * @param requestBody  请求参数体
     * @param responseType 返回对象类型
     * @return ResponseEntity 响应对象封装类
     */
    public <T> ResponseEntity<T> post(String url, Object requestBody, Class<T> responseType)
            throws RestClientException {
        return restTemplate.postForEntity(url, requestBody, responseType);
    }

    /**
     * POST请求调用方式
     *
     * @param url          请求URL
     * @param requestBody  请求参数体
     * @param responseType 返回对象类型
     * @param uriVariables URL中的变量，按顺序依次对应
     * @return ResponseEntity 响应对象封装类
     */
    public <T> ResponseEntity<T> post(String url, Object requestBody, Class<T> responseType, Object... uriVariables)
            throws RestClientException {
        return restTemplate.postForEntity(url, requestBody, responseType, uriVariables);
    }

    /**
     * POST请求调用方式
     *
     * @param url          请求URL
     * @param requestBody  请求参数体
     * @param responseType 返回对象类型
     * @param uriVariables URL中的变量，与Map中的key对应
     * @return ResponseEntity 响应对象封装类
     */
    public <T> ResponseEntity<T> post(String url, Object requestBody, Class<T> responseType,
                                      Map<String, ?> uriVariables) throws RestClientException {
        return restTemplate.postForEntity(url, requestBody, responseType, uriVariables);
    }

    /**
     * 带请求头的POST请求调用方式
     *
     * @param url          请求URL
     * @param headers      请求头参数
     * @param requestBody  请求参数体
     * @param responseType 返回对象类型
     * @param uriVariables URL中的变量，按顺序依次对应
     * @return ResponseEntity 响应对象封装类
     */
    public <T> ResponseEntity<T> post(String url, Map<String, String> headers, Object requestBody,
                                      Class<T> responseType, Object... uriVariables) throws RestClientException {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setAll(headers);
        return post(url, httpHeaders, requestBody, responseType, uriVariables);
    }

    /**
     * 带请求头的POST请求调用方式
     *
     * @param url          请求URL
     * @param headers      请求头参数
     * @param requestBody  请求参数体
     * @param responseType 返回对象类型
     * @param uriVariables URL中的变量，按顺序依次对应
     * @return ResponseEntity 响应对象封装类
     */
    public <T> ResponseEntity<T> post(String url, HttpHeaders headers, Object requestBody, Class<T> responseType,
                                      Object... uriVariables) throws RestClientException {
        HttpEntity<Object> requestEntity = new HttpEntity<Object>(requestBody, headers);
        return post(url, requestEntity, responseType, uriVariables);
    }

    /**
     * 带请求头的POST请求调用方式
     *
     * @param url          请求URL
     * @param headers      请求头参数
     * @param requestBody  请求参数体
     * @param responseType 返回对象类型
     * @param uriVariables URL中的变量，与Map中的key对应
     * @return ResponseEntity 响应对象封装类
     */
    public <T> ResponseEntity<T> post(String url, Map<String, String> headers, Object requestBody,
                                      Class<T> responseType, Map<String, ?> uriVariables) throws RestClientException {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setAll(headers);
        return post(url, httpHeaders, requestBody, responseType, uriVariables);
    }

    /**
     * 带请求头的POST请求调用方式
     *
     * @param url          请求URL
     * @param headers      请求头参数
     * @param requestBody  请求参数体
     * @param responseType 返回对象类型
     * @param uriVariables URL中的变量，与Map中的key对应
     * @return ResponseEntity 响应对象封装类
     */
    public <T> ResponseEntity<T> post(String url, HttpHeaders headers, Object requestBody, Class<T> responseType,
                                      Map<String, ?> uriVariables) throws RestClientException {
        HttpEntity<Object> requestEntity = new HttpEntity<Object>(requestBody, headers);
        return post(url, requestEntity, responseType, uriVariables);
    }

    /**
     * 自定义请求头和请求体的POST请求调用方式
     *
     * @param url           请求URL
     * @param requestEntity 请求头和请求体封装对象
     * @param responseType  返回对象类型
     * @param uriVariables  URL中的变量，按顺序依次对应
     * @return ResponseEntity 响应对象封装类
     */
    public <T> ResponseEntity<T> post(String url, HttpEntity<?> requestEntity, Class<T> responseType,
                                      Object... uriVariables) throws RestClientException {
        return restTemplate.exchange(url, HttpMethod.POST, requestEntity, responseType, uriVariables);
    }

    /**
     * 自定义请求头和请求体的POST请求调用方式
     *
     * @param url           请求URL
     * @param requestEntity 请求头和请求体封装对象
     * @param responseType  返回对象类型
     * @param uriVariables  URL中的变量，与Map中的key对应
     * @return ResponseEntity 响应对象封装类
     */
    public <T> ResponseEntity<T> post(String url, HttpEntity<?> requestEntity, Class<T> responseType,
                                      Map<String, ?> uriVariables) throws RestClientException {
        return restTemplate.exchange(url, HttpMethod.POST, requestEntity, responseType, uriVariables);
    }

    // ----------------------------------PUT-------------------------------------------------------

    /**
     * PUT请求调用方式
     *
     * @param url          请求URL
     * @param responseType 返回对象类型
     * @param uriVariables URL中的变量，按顺序依次对应
     * @return ResponseEntity 响应对象封装类
     * @return ResponseEntity 响应对象封装类
     */
    public <T> ResponseEntity<T> put(String url, Class<T> responseType, Object... uriVariables)
            throws RestClientException {
        return put(url, HttpEntity.EMPTY, responseType, uriVariables);
    }

    /**
     * PUT请求调用方式
     *
     * @param url          请求URL
     * @param requestBody  请求参数体
     * @param responseType 返回对象类型
     * @param uriVariables URL中的变量，按顺序依次对应
     * @return ResponseEntity 响应对象封装类
     */
    public <T> ResponseEntity<T> put(String url, Object requestBody, Class<T> responseType, Object... uriVariables)
            throws RestClientException {
        HttpEntity<Object> requestEntity = new HttpEntity<Object>(requestBody);
        return put(url, requestEntity, responseType, uriVariables);
    }

    /**
     * PUT请求调用方式
     *
     * @param url          请求URL
     * @param requestBody  请求参数体
     * @param responseType 返回对象类型
     * @param uriVariables URL中的变量，与Map中的key对应
     * @return ResponseEntity 响应对象封装类
     */
    public <T> ResponseEntity<T> put(String url, Object requestBody, Class<T> responseType, Map<String, ?> uriVariables)
            throws RestClientException {
        HttpEntity<Object> requestEntity = new HttpEntity<Object>(requestBody);
        return put(url, requestEntity, responseType, uriVariables);
    }

    /**
     * 带请求头的PUT请求调用方式
     *
     * @param url          请求URL
     * @param headers      请求头参数
     * @param requestBody  请求参数体
     * @param responseType 返回对象类型
     * @param uriVariables URL中的变量，按顺序依次对应
     * @return ResponseEntity 响应对象封装类
     */
    public <T> ResponseEntity<T> put(String url, Map<String, String> headers, Object requestBody, Class<T> responseType,
                                     Object... uriVariables) throws RestClientException {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setAll(headers);
        return put(url, httpHeaders, requestBody, responseType, uriVariables);
    }

    /**
     * 带请求头的PUT请求调用方式
     *
     * @param url          请求URL
     * @param headers      请求头参数
     * @param requestBody  请求参数体
     * @param responseType 返回对象类型
     * @param uriVariables URL中的变量，按顺序依次对应
     * @return ResponseEntity 响应对象封装类
     */
    public <T> ResponseEntity<T> put(String url, HttpHeaders headers, Object requestBody, Class<T> responseType,
                                     Object... uriVariables) throws RestClientException {
        HttpEntity<Object> requestEntity = new HttpEntity<Object>(requestBody, headers);
        return put(url, requestEntity, responseType, uriVariables);
    }

    /**
     * 带请求头的PUT请求调用方式
     *
     * @param url          请求URL
     * @param headers      请求头参数
     * @param requestBody  请求参数体
     * @param responseType 返回对象类型
     * @param uriVariables URL中的变量，与Map中的key对应
     * @return ResponseEntity 响应对象封装类
     */
    public <T> ResponseEntity<T> put(String url, Map<String, String> headers, Object requestBody, Class<T> responseType,
                                     Map<String, ?> uriVariables) throws RestClientException {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setAll(headers);
        return put(url, httpHeaders, requestBody, responseType, uriVariables);
    }

    /**
     * 带请求头的PUT请求调用方式
     *
     * @param url          请求URL
     * @param headers      请求头参数
     * @param requestBody  请求参数体
     * @param responseType 返回对象类型
     * @param uriVariables URL中的变量，与Map中的key对应
     * @return ResponseEntity 响应对象封装类
     */
    public <T> ResponseEntity<T> put(String url, HttpHeaders headers, Object requestBody, Class<T> responseType,
                                     Map<String, ?> uriVariables) throws RestClientException {
        HttpEntity<Object> requestEntity = new HttpEntity<Object>(requestBody, headers);
        return put(url, requestEntity, responseType, uriVariables);
    }

    /**
     * 自定义请求头和请求体的PUT请求调用方式
     *
     * @param url           请求URL
     * @param requestEntity 请求头和请求体封装对象
     * @param responseType  返回对象类型
     * @param uriVariables  URL中的变量，按顺序依次对应
     * @return ResponseEntity 响应对象封装类
     */
    public <T> ResponseEntity<T> put(String url, HttpEntity<?> requestEntity, Class<T> responseType,
                                     Object... uriVariables) throws RestClientException {
        return restTemplate.exchange(url, HttpMethod.PUT, requestEntity, responseType, uriVariables);
    }

    /**
     * 自定义请求头和请求体的PUT请求调用方式
     *
     * @param url           请求URL
     * @param requestEntity 请求头和请求体封装对象
     * @param responseType  返回对象类型
     * @param uriVariables  URL中的变量，与Map中的key对应
     * @return ResponseEntity 响应对象封装类
     */
    public <T> ResponseEntity<T> put(String url, HttpEntity<?> requestEntity, Class<T> responseType,
                                     Map<String, ?> uriVariables) throws RestClientException {
        return restTemplate.exchange(url, HttpMethod.PUT, requestEntity, responseType, uriVariables);
    }

    // ----------------------------------DELETE-------------------------------------------------------

    /**
     * DELETE请求调用方式
     *
     * @param url          请求URL
     * @param responseType 返回对象类型
     * @param uriVariables URL中的变量，按顺序依次对应
     * @return ResponseEntity 响应对象封装类
     */
    public <T> ResponseEntity<T> delete(String url, Class<T> responseType, Object... uriVariables)
            throws RestClientException {
        return delete(url, HttpEntity.EMPTY, responseType, uriVariables);
    }

    /**
     * DELETE请求调用方式
     *
     * @param url          请求URL
     * @param responseType 返回对象类型
     * @param uriVariables URL中的变量，与Map中的key对应
     * @return ResponseEntity 响应对象封装类
     */
    public <T> ResponseEntity<T> delete(String url, Class<T> responseType, Map<String, ?> uriVariables)
            throws RestClientException {
        return delete(url, HttpEntity.EMPTY, responseType, uriVariables);
    }

    /**
     * DELETE请求调用方式
     *
     * @param url          请求URL
     * @param requestBody  请求参数体
     * @param responseType 返回对象类型
     * @param uriVariables URL中的变量，按顺序依次对应
     * @return ResponseEntity 响应对象封装类
     */
    public <T> ResponseEntity<T> delete(String url, Object requestBody, Class<T> responseType, Object... uriVariables)
            throws RestClientException {
        HttpEntity<Object> requestEntity = new HttpEntity<Object>(requestBody);
        return delete(url, requestEntity, responseType, uriVariables);
    }

    /**
     * DELETE请求调用方式
     *
     * @param url          请求URL
     * @param requestBody  请求参数体
     * @param responseType 返回对象类型
     * @param uriVariables URL中的变量，与Map中的key对应
     * @return ResponseEntity 响应对象封装类
     */
    public <T> ResponseEntity<T> delete(String url, Object requestBody, Class<T> responseType,
                                        Map<String, ?> uriVariables) throws RestClientException {
        HttpEntity<Object> requestEntity = new HttpEntity<Object>(requestBody);
        return delete(url, requestEntity, responseType, uriVariables);
    }

    /**
     * 带请求头的DELETE请求调用方式
     *
     * @param url          请求URL
     * @param headers      请求头参数
     * @param responseType 返回对象类型
     * @param uriVariables URL中的变量，按顺序依次对应
     * @return ResponseEntity 响应对象封装类
     */
    public <T> ResponseEntity<T> delete(String url, Map<String, String> headers, Class<T> responseType,
                                        Object... uriVariables) throws RestClientException {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setAll(headers);
        return delete(url, httpHeaders, responseType, uriVariables);
    }

    /**
     * 带请求头的DELETE请求调用方式
     *
     * @param url          请求URL
     * @param headers      请求头参数
     * @param responseType 返回对象类型
     * @param uriVariables URL中的变量，按顺序依次对应
     * @return ResponseEntity 响应对象封装类
     */
    public <T> ResponseEntity<T> delete(String url, HttpHeaders headers, Class<T> responseType, Object... uriVariables)
            throws RestClientException {
        HttpEntity<Object> requestEntity = new HttpEntity<Object>(headers);
        return delete(url, requestEntity, responseType, uriVariables);
    }

    /**
     * 带请求头的DELETE请求调用方式
     *
     * @param url          请求URL
     * @param headers      请求头参数
     * @param responseType 返回对象类型
     * @param uriVariables URL中的变量，与Map中的key对应
     * @return ResponseEntity 响应对象封装类
     */
    public <T> ResponseEntity<T> delete(String url, Map<String, String> headers, Class<T> responseType,
                                        Map<String, ?> uriVariables) throws RestClientException {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setAll(headers);
        return delete(url, httpHeaders, responseType, uriVariables);
    }

    /**
     * 带请求头的DELETE请求调用方式
     *
     * @param url          请求URL
     * @param headers      请求头参数
     * @param responseType 返回对象类型
     * @param uriVariables URL中的变量，与Map中的key对应
     * @return ResponseEntity 响应对象封装类
     */
    public <T> ResponseEntity<T> delete(String url, HttpHeaders headers, Class<T> responseType,
                                        Map<String, ?> uriVariables) throws RestClientException {
        HttpEntity<Object> requestEntity = new HttpEntity<Object>(headers);
        return delete(url, requestEntity, responseType, uriVariables);
    }

    /**
     * 带请求头的DELETE请求调用方式
     *
     * @param url          请求URL
     * @param headers      请求头参数
     * @param requestBody  请求参数体
     * @param responseType 返回对象类型
     * @param uriVariables URL中的变量，按顺序依次对应
     * @return ResponseEntity 响应对象封装类
     */
    public <T> ResponseEntity<T> delete(String url, Map<String, String> headers, Object requestBody,
                                        Class<T> responseType, Object... uriVariables) throws RestClientException {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setAll(headers);
        return delete(url, httpHeaders, requestBody, responseType, uriVariables);
    }

    /**
     * 带请求头的DELETE请求调用方式
     *
     * @param url          请求URL
     * @param headers      请求头参数
     * @param requestBody  请求参数体
     * @param responseType 返回对象类型
     * @param uriVariables URL中的变量，按顺序依次对应
     * @return ResponseEntity 响应对象封装类
     */
    public <T> ResponseEntity<T> delete(String url, HttpHeaders headers, Object requestBody, Class<T> responseType,
                                        Object... uriVariables) throws RestClientException {
        HttpEntity<Object> requestEntity = new HttpEntity<Object>(requestBody, headers);
        return delete(url, requestEntity, responseType, uriVariables);
    }

    /**
     * 带请求头的DELETE请求调用方式
     *
     * @param url          请求URL
     * @param headers      请求头参数
     * @param requestBody  请求参数体
     * @param responseType 返回对象类型
     * @param uriVariables URL中的变量，与Map中的key对应
     * @return ResponseEntity 响应对象封装类
     */
    public <T> ResponseEntity<T> delete(String url, Map<String, String> headers, Object requestBody,
                                        Class<T> responseType, Map<String, ?> uriVariables) throws RestClientException {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setAll(headers);
        return delete(url, httpHeaders, requestBody, responseType, uriVariables);
    }

    /**
     * 带请求头的DELETE请求调用方式
     *
     * @param url          请求URL
     * @param headers      请求头参数
     * @param requestBody  请求参数体
     * @param responseType 返回对象类型
     * @param uriVariables URL中的变量，与Map中的key对应
     * @return ResponseEntity 响应对象封装类
     */
    public <T> ResponseEntity<T> delete(String url, HttpHeaders headers, Object requestBody, Class<T> responseType,
                                        Map<String, ?> uriVariables) throws RestClientException {
        HttpEntity<Object> requestEntity = new HttpEntity<Object>(requestBody, headers);
        return delete(url, requestEntity, responseType, uriVariables);
    }

    /**
     * 自定义请求头和请求体的DELETE请求调用方式
     *
     * @param url           请求URL
     * @param requestEntity 请求头和请求体封装对象
     * @param responseType  返回对象类型
     * @param uriVariables  URL中的变量，按顺序依次对应
     * @return ResponseEntity 响应对象封装类
     */
    public <T> ResponseEntity<T> delete(String url, HttpEntity<?> requestEntity, Class<T> responseType,
                                        Object... uriVariables) throws RestClientException {
        //return restTemplate.exchange(url, HttpMethod.DELETE, requestEntity, responseType, uriVariables);
        try{
            ResponseEntity object = restTemplate.exchange(url, HttpMethod.DELETE, requestEntity, responseType, uriVariables);
            HttpStatus status = object.getStatusCode();
            if(!status.is2xxSuccessful()){
                log.warn("delete response not equal 2xx. url:{}, code:{}, response:{}", url, status.toString(), object.getBody().toString());
            }
            return object;
        }catch (RestClientException e){
            log.error("delete exception. url:{}, message:{}", url, e.getMessage());
            throw e;
        }
    }

    /**
     * 自定义请求头和请求体的DELETE请求调用方式
     *
     * @param url           请求URL
     * @param requestEntity 请求头和请求体封装对象
     * @param responseType  返回对象类型
     * @param uriVariables  URL中的变量，与Map中的key对应
     * @return ResponseEntity 响应对象封装类
     */
    public <T> ResponseEntity<T> delete(String url, HttpEntity<?> requestEntity, Class<T> responseType,
                                        Map<String, ?> uriVariables) throws RestClientException {
        //return restTemplate.exchange(url, HttpMethod.DELETE, requestEntity, responseType, uriVariables);
        try{
            ResponseEntity object = restTemplate.exchange(url, HttpMethod.DELETE, requestEntity, responseType, uriVariables);
            HttpStatus status = object.getStatusCode();
            if(!status.is2xxSuccessful()){
                log.warn("delete response not equal 2xx. url:{}, code:{}, response:{}", url, status.toString(), object.getBody().toString());
            }
            return object;
        }catch (RestClientException e){
            log.error("delete exception. url:{}, message:{}", url, e.getMessage());
            throw e;
        }
    }

    // ----------------------------------通用方法-------------------------------------------------------

    /**
     * 通用调用方式
     *
     * @param url           请求URL
     * @param method        请求方法类型
     * @param requestEntity 请求头和请求体封装对象
     * @param responseType  返回对象类型
     * @param uriVariables  URL中的变量，按顺序依次对应
     * @return ResponseEntity 响应对象封装类
     */
    public <T> ResponseEntity<T> exchange(String url, HttpMethod method, HttpEntity<?> requestEntity,
                                          Class<T> responseType, Object... uriVariables) throws RestClientException {
        //return restTemplate.exchange(url, method, requestEntity, responseType, uriVariables);
        try{
            ResponseEntity object = restTemplate.exchange(url, method, requestEntity, responseType, uriVariables);
            HttpStatus status = object.getStatusCode();
            if(!status.is2xxSuccessful()){
                log.warn("exchange response not equal 2xx. url:{}, code:{}, response:{}", url, status.toString(), object.getBody().toString());
            }
            return object;
        }catch (RestClientException e){
            log.error("exchange exception. url:{}, message:{}", url, e.getMessage());
            throw e;
        }
    }

    /**
     * 通用调用方式
     *
     * @param url           请求URL
     * @param method        请求方法类型
     * @param requestEntity 请求头和请求体封装对象
     * @param responseType  返回对象类型
     * @param uriVariables  URL中的变量，与Map中的key对应
     * @return ResponseEntity 响应对象封装类
     */
    public <T> ResponseEntity<T> exchange(String url, HttpMethod method, HttpEntity<?> requestEntity,
                                          Class<T> responseType, Map<String, ?> uriVariables) throws RestClientException {
        //return restTemplate.exchange(url, method, requestEntity, responseType, uriVariables);
        try{
            ResponseEntity object = restTemplate.exchange(url, method, requestEntity, responseType, uriVariables);
            HttpStatus status = object.getStatusCode();
            if(!status.is2xxSuccessful()){
                log.warn("exchange response not equal 2xx. url:{}, code:{}, response:{}", url, status.toString(), object.getBody().toString());
            }
            return object;
        }catch (RestClientException e){
            log.error("exchange exception. url:{}, message:{}", url, e.getMessage());
            throw e;
        }
    }

}