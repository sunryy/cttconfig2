package com.ctg.mes.config.common.dto.paas;


/**
 * @author chenjj
 */
public class OrderResponse {

	/**
	 * 订单ID，唯一标识
	 */
	private String operOrderId;

	/**
	 * 施工结果，0成功，1失败
	 */
	private int operResult;

	/**
	 * 施工结果描述，譬如失败原因
	 */
	private String operResultDesc;




	public String getOperOrderId() {
		return operOrderId;
	}

	public void setOperOrderId(String operOrderId) {
		this.operOrderId = operOrderId;
	}



	public int getOperResult() {
		return operResult;
	}

	public void setOperResult(int operResult) {
		this.operResult = operResult;
	}

	public String getOperResultDesc() {
		return operResultDesc;
	}

	public void setOperResultDesc(String operResultDesc) {
		this.operResultDesc = operResultDesc;
	}



}
