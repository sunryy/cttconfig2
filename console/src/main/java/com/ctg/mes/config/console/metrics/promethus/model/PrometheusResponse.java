package com.ctg.mes.config.console.metrics.promethus.model;



import org.springframework.util.StringUtils;

import java.util.List;

public class PrometheusResponse {
    public final static String STATUS_SUCCESS = "success";
    public final static String STATUS_ERROR = "error";

    private String status;
    private String errorType;
    private String error;
    private List<String> warnings;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getErrorType() {
        return errorType;
    }

    public void setErrorType(String errorType) {
        this.errorType = errorType;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public List<String> getWarnings() {
        return warnings;
    }

    public void setWarnings(List<String> warnings) {
        this.warnings = warnings;
    }


    public boolean ifSuccess(){
        return status.equals(STATUS_SUCCESS);
    }

    public static PrometheusResponse fail(String errMsg) {
        PrometheusResponse response = new PrometheusResponse();
        response.setError(errMsg);
        response.setStatus(STATUS_ERROR);
        return response;
    }

    public String getErrorMsg(){
        if(StringUtils.isEmpty(errorType)){
            return "";
        }
        return String.format("errorType:%s, error:%s", errorType, error);
    }

}
