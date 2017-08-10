package com.ych.shcm.o2o.autohome.spider.carmodel.model;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * 查找车型的返回结果
 * 
 * @author chengxuewen
 *
 * @param <T>
 *            返回结果的类型
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class CarFinderResponse<T> {

	/**
	 * 错误码
	 */
	@JsonProperty("returncode")
	private int resultCode;

	/**
	 * 返回消息
	 */
	private String message;

	/**
	 * 返回结果
	 */
	private T result;

	/**
	 * @return 错误码
	 */
	public int getResultCode() {
		return resultCode;
	}

	/**
	 * @param resultCode
	 *            错误码
	 */
	public void setResultCode(int resultCode) {
		this.resultCode = resultCode;
	}

	/**
	 * @return 返回消息
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * @param message
	 *            返回消息
	 */
	public void setMessage(String message) {
		this.message = message;
	}

	/**
	 * @return 返回结果
	 */
	public T getResult() {
		return result;
	}

	/**
	 * @param result
	 *            返回结果
	 */
	public void setResult(T result) {
		this.result = result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
	}

}
