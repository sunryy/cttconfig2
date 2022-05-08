package com.ctg.mes.config.common.exception;


import com.ctg.mes.config.common.constants.Errors;

/**
 * @author xiongzy
 */
public class BusinessException extends RuntimeException {

    private static final long serialVersionUID = 4722404995028817581L;

    private final int code;

    public BusinessException(String message) {
        super(message);
        this.code = Errors.OTHER_ERROR.getCode();
    }

    public BusinessException(Errors errors, String message) {
        super(message);
        this.code = errors.getCode();
    }

    public BusinessException(int code, String message) {
        super(message);
        this.code = code;
    }

    public BusinessException(int code, String message, Throwable cause) {
        super(message, cause);
        this.code = code;
    }

    public BusinessException(Throwable cause) {
        super(cause.getMessage());
        this.code = Errors.OTHER_ERROR.getCode();
    }

    public BusinessException(Errors errors) {
        super(errors.getMsg());
        this.code = errors.getCode();
    }

    public BusinessException(Errors errors, Throwable cause) {
        super(errors.getMsg(), cause);
        this.code = errors.getCode();
    }

    public int getCode() {
        return code;
    }
}
