package com.ych.shcm.o2o.autohome.spider.carmodel.model;

import java.math.BigDecimal;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * 车型品牌的响应
 * 
 * @author U
 *
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class CarBrandItem {

	/**
	 * 车型ID
	 */
	private BigDecimal id;

	/**
	 * 首字母
	 */
	@JsonProperty("bfirstletter")
	private String firstChar;

	/**
	 * 车型名称
	 */
	private String name;

	/**
	 * @return 车型ID
	 */
	public BigDecimal getId() {
		return id;
	}

	/**
	 * @param id
	 *            车型ID
	 */
	public void setId(BigDecimal id) {
		this.id = id;
	}

	/**
	 * @return 首字母
	 */
	public String getFirstChar() {
		return firstChar;
	}

	/**
	 * @param firstChar
	 *            首字母
	 */
	public void setFirstChar(String firstChar) {
		this.firstChar = firstChar;
	}

	/**
	 * @return 车型名称
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            车型名称
	 */
	public void setName(String name) {
		this.name = name;
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
