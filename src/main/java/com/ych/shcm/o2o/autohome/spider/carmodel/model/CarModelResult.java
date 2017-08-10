package com.ych.shcm.o2o.autohome.spider.carmodel.model;

import java.util.List;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * 请求车型的结果
 * 
 * @author U
 *
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class CarModelResult {

	/**
	 * 车系年份
	 */
	@JsonProperty("yearitems")
	private List<CarSeriesYearItem> years;

	/**
	 * @return 车系年份
	 */
	public List<CarSeriesYearItem> getYears() {
		return years;
	}

	/**
	 * @param years
	 *            车系年份
	 */
	public void setYears(List<CarSeriesYearItem> years) {
		this.years = years;
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
