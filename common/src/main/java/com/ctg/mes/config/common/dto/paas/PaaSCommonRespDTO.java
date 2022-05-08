package com.ctg.mes.config.common.dto.paas;

/**
 * PaaS平台响应对象
 * @author chenjj
 */
public class PaaSCommonRespDTO {
	private static final Integer CODE_SUCCESS = 800;
	private static final Integer CODE_FAIL = 900;
	/**
	 * 响应码
	 */
	private Integer statusCode;
	/**
	 * 相应消息
	 */
	private String message;
	
	public Integer getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(Integer statusCode) {
		this.statusCode = statusCode;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public PaaSCommonRespDTO() {
		super();
	}
	
	public PaaSCommonRespDTO(Integer statusCode, String message) {
		super();
		this.statusCode = statusCode;
		this.message = message;
	}

	public static PaaSCommonRespDTO fail(String message) {
		return new PaaSCommonRespDTO(CODE_FAIL, message);
	}

	public static PaaSCommonRespDTO success(String message) {
		return new PaaSCommonRespDTO(CODE_SUCCESS, message);
	}

	@Override
	public String toString() {
		return "PaaSCommonRespDTO{" +
				"statusCode=" + statusCode +
				", message='" + message + '\'' +
				'}';
	}
}
