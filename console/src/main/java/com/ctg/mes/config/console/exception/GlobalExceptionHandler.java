package com.ctg.mes.config.console.exception;

import com.ctg.mes.config.common.dto.response.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;
import java.util.Objects;

/**
 * @author xiongzy
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Response<Object> bindException(MethodArgumentNotValidException e) {
        BindingResult bindingResult = e.getBindingResult();
        String defaultMessage = Objects.requireNonNull(bindingResult.getFieldError()).getDefaultMessage();
        return Response.fail(defaultMessage);
    }


    /**
     * 捕获全局异常,处理所有不可知的异常
     *
     * @param e       异常
     * @param request 请求
     * @return response
     */
    @ExceptionHandler({Exception.class})
    public Response<Object> exception(Exception e, HttpServletRequest request) {
        log.error("url {} request failed！", request.getRequestURL(), e);
        return Response.fail("系统内部异常");

    }
}
