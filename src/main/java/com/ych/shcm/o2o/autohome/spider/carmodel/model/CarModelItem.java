package com.ych.shcm.o2o.autohome.spider.carmodel.model;

import java.math.BigDecimal;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * 车型项
 * 
 * @author U
 *
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class CarModelItem {

	/**
	 * ID
	 */
	private BigDecimal id;

	/**
	 * 车型名称
	 */
	private String name;

	/**
	 * @return ID
	 */
	public BigDecimal getId() {
		return id;
	}

	/**
	 * @param id
	 *            ID
	 */
	public void setId(BigDecimal id) {
		this.id = id;
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
