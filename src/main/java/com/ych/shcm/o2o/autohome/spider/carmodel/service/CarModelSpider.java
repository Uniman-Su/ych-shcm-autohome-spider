package com.ych.shcm.o2o.autohome.spider.carmodel.service;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.text.MessageFormat;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.htmlparser.Node;
import org.htmlparser.NodeFilter;
import org.htmlparser.Parser;
import org.htmlparser.Tag;
import org.htmlparser.util.NodeList;
import org.htmlparser.util.ParserException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.ych.shcm.o2o.autohome.spider.carmodel.Constants;
import com.ych.shcm.o2o.autohome.spider.carmodel.model.CarBrandResponse;
import com.ych.shcm.o2o.autohome.spider.carmodel.model.CarModelResponse;
import com.ych.shcm.o2o.autohome.spider.carmodel.model.CarSeriesResponse;
import com.ych.core.fasterxml.jackson.MapperUtils;
import com.ych.core.model.CommonOperationResult;
import com.ych.shcm.o2o.model.CarBrand;
import com.ych.shcm.o2o.model.CarSeries;

/**
 * 车型爬虫
 * 
 * @author U
 *
 */
public class CarModelSpider {

	private static final Logger logger = LoggerFactory.getLogger(CarModelSpider.class);

	/**
	 * 获取车型的结果请求地址
	 */
	public static final String FIND_CAR_URL = "http://www.autohome.com.cn/ashx/AjaxIndexCarFind.ashx";

	/**
	 * 抓取品牌图标的URL
	 */
	public static final String DARW_BRAND_LOGO_SEG_URL = "http://www.autohome.com.cn/grade/carhtml/{0}.html";

	/**
	 * 抓取品牌图标URL的格式化器
	 */
	public static final MessageFormat DRAW_BRAND_LOGO_SEG_URL_FORMATTER = new MessageFormat(DARW_BRAND_LOGO_SEG_URL);

	/**
	 * 包含品牌ID的链接HREF属性模式
	 */
	public static final Pattern BRANID_ANCHOR_LINK_HREF_PATTERN = Pattern
			.compile("http://car.autohome.com.cn/pic/brand-(\\d+).html");

	/**
	 * 品牌ID链接过滤器
	 */
	public static final NodeFilter BRANID_ANCHOR_LINK_FILTER = new NodeFilter() {

		private static final long serialVersionUID = 2033695387249155305L;

		@Override
		public boolean accept(Node node) {
			if (node instanceof Tag) {
				Tag tag = (Tag) node;

				if (tag.isEndTag() || !"A".equals(tag.getTagName())) {
					return false;
				}

				String href = StringUtils.trimToNull(tag.getAttribute("href"));
				if (href == null) {
					return false;
				}

				if (!BRANID_ANCHOR_LINK_HREF_PATTERN.matcher(href).matches()) {
					return false;
				}

				NodeList children = tag.getChildren();
				return (children.size() == 1 && children.elementAt(0) instanceof Tag
						&& ((Tag) children.elementAt(0)).getTagName().equals("IMG"));
			} else {
				return false;
			}
		}
	};

	/**
	 * Rest模板板
	 */
	private RestTemplate restTemplate;

	/**
	 * 导入车型服务
	 */
	private ImportCarModelService importModelSvc;

	/**
	 * @param restTemplate
	 *            Rest模板
	 */
	public void setRestTemplate(RestTemplate restTemplate) {
		this.restTemplate = restTemplate;
	}

	/**
	 * @param importModelSvc
	 *            导入车型服务
	 */
	public void setImportModelSvc(ImportCarModelService importModelSvc) {
		this.importModelSvc = importModelSvc;
	}

	/**
	 * 抓取车型品牌数据
	 * 
	 * @return 操作结果
	 */
	public CommonOperationResult drawCarBrands() {
		logger.trace("Draw car brands");

		UriComponentsBuilder ucBuilder = UriComponentsBuilder.fromHttpUrl(FIND_CAR_URL).queryParam("type", 1);

		URI uri = ucBuilder.build().toUri();
		logger.info("Pull car serieses from URL:{}", uri);

		String str = restTemplate.getForObject(uri, String.class);
		CarBrandResponse resp;
		try {
			resp = MapperUtils.MAPPER.get().readValue(str, CarBrandResponse.class);
		} catch (IOException e) {
			logger.error("Read JSON failed", e);
			return CommonOperationResult.Failed;
		}
		logger.info("Response:{}", resp);

		CommonOperationResult result = CommonOperationResult.Failed;

		if (resp.getResultCode() == Constants.RESULTCODE_SUCCEEDED) {
			result = importModelSvc.importBrands(resp.getResult());
		}

		logger.trace("Result:{}", result);

		return result;
	}

	/**
	 * 抓取指定品牌的车系数据
	 * 
	 * @param brand
	 *            品牌
	 * @return 操作结果
	 */
	public CommonOperationResult drawCarSerieses(CarBrand brand) {
		logger.trace("Draw car serieses for {}", brand);

		UriComponentsBuilder ucBuilder = UriComponentsBuilder.fromHttpUrl(FIND_CAR_URL).queryParam("type", 3)
				.queryParam("value", brand.getId());

		URI uri = ucBuilder.build().toUri();
		logger.info("Draw car serieses from URL:{}", uri);

		String str = restTemplate.getForObject(uri, String.class);
		CarSeriesResponse resp;
		try {
			resp = MapperUtils.MAPPER.get().readValue(str, CarSeriesResponse.class);
		} catch (IOException e) {
			logger.error("Read JSON failed", e);
			return CommonOperationResult.Failed;
		}

		logger.info("Response:{}", resp);

		CommonOperationResult result = CommonOperationResult.Failed;

		if (resp.getResultCode() == Constants.RESULTCODE_SUCCEEDED) {
			result = importModelSvc.importSeries(brand.getId(), resp.getResult());
		}

		logger.trace("Result:{}", result);

		return result;
	}

	/**
	 * 获取车型
	 * 
	 * @param series
	 *            车系
	 * @return 操作结果
	 */
	public CommonOperationResult drawCarModels(CarSeries series) {
		logger.trace("Draw car model for {}", series);

		UriComponentsBuilder ucBuilder = UriComponentsBuilder.fromHttpUrl(FIND_CAR_URL).queryParam("type", 5)
				.queryParam("value", series.getId());

		URI uri = ucBuilder.build().toUri();
		logger.info("Draw car models from URL:{}", uri);

		String str = restTemplate.getForObject(uri, String.class);
		CarModelResponse resp;
		try {
			resp = MapperUtils.MAPPER.get().readValue(str, CarModelResponse.class);
		} catch (IOException e) {
			logger.error("Read JSON failed", e);
			return CommonOperationResult.Failed;
		}
		;
		logger.info("Response:{}", resp);

		CommonOperationResult result = CommonOperationResult.Failed;

		if (resp.getResultCode() == Constants.RESULTCODE_SUCCEEDED) {
			result = importModelSvc.importModels(series.getId(), resp.getResult());
		}

		logger.trace("Result:{}", result);

		return result;
	}

	/**
	 * 抓取品牌的图标URL
	 * 
	 * @param firstChar
	 *            首字母
	 * @return 品牌ID对图标URL的映射
	 */
	private Map<BigDecimal, String> drawLogoUrls(String firstChar) {
		logger.trace("Draw brand logo for first char:{}", firstChar);

		String url = DRAW_BRAND_LOGO_SEG_URL_FORMATTER.format(new String[] { firstChar });
		logger.info("Draw brand logo HTML segment from URL:{}", url);
		
		String htmlSeg;
		InputStream is = null;
		try {
			URL urlObj = new URL(url);
			HttpURLConnection conn =  (HttpURLConnection) urlObj.openConnection();
			conn.setConnectTimeout(10000);
			conn.setReadTimeout(10000);
			
			is = conn.getInputStream();
			
			if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
				InputStreamReader isr = conn.getContentEncoding() == null ? new InputStreamReader(is) : new InputStreamReader(is, conn.getContentEncoding());
				BufferedReader reader = new BufferedReader(isr);
				StringBuilder buffer = new StringBuilder();
				
				for (String line = reader.readLine(); line != null; line = reader.readLine()) {
					buffer.append(line);
				}
				
				htmlSeg = buffer.toString();
			} else {
				logger.error("Draw brand logo HTML segmen response code:{}", conn.getResponseCode());
				return null;
			}
			
		} catch (IOException e) {
			logger.error("Draw brand logo HTML segment failed", e);
			return null;
		} finally {
			IOUtils.closeQuietly(is);
		}
		
		logger.trace("HTML segment:{}", htmlSeg);

		Map<BigDecimal, String> ret = null;
		try {
			Parser parser = new Parser(htmlSeg);

			NodeList nodeList = parser.parse(BRANID_ANCHOR_LINK_FILTER);

			if (nodeList.size() > 0) {
				HashMap<BigDecimal, String> retMap = new HashMap<>();

				for (int i = 0, c = nodeList.size(); i < c; ++i) {
					Tag brandATag = (Tag) nodeList.elementAt(i);

					String href = StringUtils.trimToNull(brandATag.getAttribute("href"));
					Matcher matcher = BRANID_ANCHOR_LINK_HREF_PATTERN.matcher(href);
					matcher.matches();
					BigDecimal brandId = new BigDecimal(matcher.group(1));

					Tag imgTag = (Tag) brandATag.getChildren().elementAt(0);
					String imgUrl = imgTag.getAttribute("src").replace("/brand/50/", "/brand/100/");

					logger.info("Find brand[{}] URL:{}", brandId, imgUrl);

					retMap.put(brandId, imgUrl);
				}

				ret = retMap;
			} else {
				ret = Collections.emptyMap();
			}
		} catch (ParserException e) {
			logger.error("Parse Brand Logo html segment failed", e);
		}

		logger.trace("Result:{}", ret);

		return ret;
	}

	/**
	 * 抓取品牌的图标
	 * 
	 * @param brandId
	 *            品牌ID
	 * @param url
	 *            图标URL
	 * @return 操作结果
	 */
	private CommonOperationResult drawBrandLogo(BigDecimal brandId, String url) {
		logger.trace("Draw car brand logo from URL:{}", url);

		ResponseEntity<byte[]> resp = restTemplate.getForEntity(url, byte[].class);

		CommonOperationResult ret = CommonOperationResult.Failed;

		if (resp.getStatusCode() == HttpStatus.OK) {
			ret = importModelSvc.saveBrandLogo(brandId, url, new ByteArrayInputStream(resp.getBody()));
		}

		logger.trace("Result:{}", ret);

		return ret;
	}

	/**
	 * 抓取品牌图标
	 * 
	 * @param firstChar
	 *            首字母
	 * @return 操作结果
	 */
	private CommonOperationResult drawBrandLogos(String firstChar) {
		logger.trace("Draw Car Brand logos for first char:{}", firstChar);

		Map<BigDecimal, String> logos = drawLogoUrls(firstChar);

		CommonOperationResult ret = CommonOperationResult.Failed;

		if (logos != null) {
			CommonOperationResult entryResult;
			for (Map.Entry<BigDecimal, String> entry : logos.entrySet()) {
				entryResult = drawBrandLogo(entry.getKey(), entry.getValue());

				if (entryResult != CommonOperationResult.Succeeded && entryResult != CommonOperationResult.NotExists) {
					ret = CommonOperationResult.Failed;
					break;
				} else {
					ret = CommonOperationResult.Succeeded;
				}
			}
		}

		logger.trace("Result:{}", ret);

		return ret;
	}

	/**
	 * 抓取品牌图标
	 * 
	 * @return 操作结果
	 */
	public CommonOperationResult drawBrandLogo() {
		logger.trace("Draw brands logos");

		Set<String> firstChars = importModelSvc.getBrandFirstChars();
		logger.trace("All brand first chars:{}", firstChars);

		CommonOperationResult ret = CommonOperationResult.Succeeded;

		for (String firstChar : firstChars) {
			ret = drawBrandLogos(firstChar);
			if (ret != CommonOperationResult.Succeeded) {
				break;
			}
		}

		logger.trace("Result:{}", ret);

		return ret;
	}

}
