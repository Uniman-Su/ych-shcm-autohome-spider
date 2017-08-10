package com.ych.shcm.o2o.autohome.spider.carmodel.model;

import java.math.BigDecimal;
import java.util.List;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * 车厂的项目
 * 
 * @author U
 *
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class CarFactoryItem {

	/**
	 * ID
	 */
	private BigDecimal id;

	/**
	 * 名称
	 */
	private String name;

	/**
	 * 首字母
	 */
	@JsonProperty("firstletter")
	private String firstChar;

	/**
	 * 车系的项目
	 */
	@JsonProperty("seriesitems")
	private List<CarSeriesItem> series;

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
	 * @return 名称
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            名称
	 */
	public void setName(String name) {
		this.name = name;
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
	 * @return 车系的项目
	 */
	public List<CarSeriesItem> getSeries() {
		return series;
	}

	/**
	 * @param series
	 *            车系的项目
	 */
	public void setSeries(List<CarSeriesItem> series) {
		this.series = series;
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
