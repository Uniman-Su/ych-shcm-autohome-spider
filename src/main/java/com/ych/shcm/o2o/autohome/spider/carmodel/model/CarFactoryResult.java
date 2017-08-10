package com.ych.shcm.o2o.autohome.spider.carmodel.model;

import java.util.List;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * 车厂请求结果
 * 
 * @author U
 *
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class CarFactoryResult {

	/**
	 * 车厂
	 */
	@JsonProperty("factoryitems")
	private List<CarFactoryItem> factories;

	/**
	 * @return 车厂
	 */
	public List<CarFactoryItem> getFactories() {
		return factories;
	}

	/**
	 * @param factories
	 *            车厂
	 */
	public void setFactories(List<CarFactoryItem> factories) {
		this.factories = factories;
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
