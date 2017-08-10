package com.ych.shcm.o2o.autohome.spider.carmodel.model;

import java.math.BigDecimal;
import java.util.List;
import java.util.regex.Pattern;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * 车型年份项目
 * 
 * @author U
 *
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class CarSeriesYearItem {
	
	/**
	 * 取出车系年份的正则表达式
	 */
	public static final Pattern DRAWYEAR_PATTERN = Pattern.compile("(\\d+)\\D+");
	
	/**
	 * ID
	 */
	private BigDecimal id;

	/**
	 * 车型名称
	 */
	private String name;
	
	/**
	 * 车型
	 */
	@JsonProperty("specitems")
	private List<CarModelItem> models;

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

	/**
	 * @return 车型
	 */
	public List<CarModelItem> getModels() {
		return models;
	}

	/**
	 * @param models 车型
	 */
	public void setModels(List<CarModelItem> models) {
		this.models = models;
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
