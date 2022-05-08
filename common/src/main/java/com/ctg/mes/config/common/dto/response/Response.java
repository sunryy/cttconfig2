package com.ctg.mes.config.common.dto.response;


import lombok.NoArgsConstructor;

/**
 * 服务调用响应对象
 *
 * @author xiongzy
 * @date 2020/06/15
 */
@NoArgsConstructor
public class Response<T> {
    private Integer code;
    private String message;
    private T data;

    public static final int SUCCESS_CODE = 200;
    public static final int FAIL_CODE = 500;

    public Response(Integer code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public static Response<Object> success() {
        return new Response<>(SUCCESS_CODE, "success", null);
    }

    public static <T> Response<T> success(T data) {
        return new Response<>(SUCCESS_CODE,"success", data);
    }

    public static Response<Object> fail(String message) {
        return new Response<>(FAIL_CODE, message, null);
    }

    public static Response<Object> fail(int code, String message) {
        return new Response<>(code, message, null);
    }

    public Integer getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public T getData() {
        return data;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setData(T data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "ResponseVO{" +
                "code=" + code +
                ", message='" + message + '\'' +
                ", data=" + data +
                '}';
    }
}
