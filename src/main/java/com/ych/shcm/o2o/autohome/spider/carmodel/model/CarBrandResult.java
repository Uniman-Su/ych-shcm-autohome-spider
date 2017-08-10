package com.ych.shcm.o2o.autohome.spider.carmodel.model;

import java.util.List;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author U
 *
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class CarBrandResult {

	/**
	 * 品牌项目列表
	 */
	@JsonProperty("branditems")
	private List<CarBrandItem> brands;

	/**
	 * @return 品牌项目列表
	 */
	public List<CarBrandItem> getBrands() {
		return brands;
	}

	/**
	 * @param brands
	 *            品牌项目列表
	 */
	public void setBrands(List<CarBrandItem> brands) {
		this.brands = brands;
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
