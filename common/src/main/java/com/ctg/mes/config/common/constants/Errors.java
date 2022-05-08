package com.ctg.mes.config.common.constants;

public enum Errors {

    /**
     * 错误码
     */
    OTHER_ERROR(9999, "");

    private final int code;

    private final String msg;

    Errors(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

}
