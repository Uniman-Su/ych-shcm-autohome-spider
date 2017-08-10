package com.ych.shcm.o2o.autohome.spider.carmodel.service;

import java.io.InputStream;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.*;
import java.util.regex.Matcher;
import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

import com.ych.core.model.CommonOperationResult;
import com.ych.shcm.o2o.autohome.spider.carmodel.model.*;
import com.ych.shcm.o2o.dao.*;
import com.ych.shcm.o2o.model.*;
import com.ych.shcm.o2o.service.UploadService;

/**
 * 导入车型数据服务
 * 
 * @author U
 *
 */
@Lazy
@Component("shcm.o2o.autohome.spider.carmodel.service.ImportCarModelService")
public class ImportCarModelService {

	/**
	 * 品牌DAO
	 */
	@Autowired
	private CarBrandDao brandDao;

	/**
	 * 制造厂DAO
	 */
	@Autowired
	private CarFactoryDao factoryDao;

	/**
	 * 车系DAO
	 */
	@Autowired
	private CarSeriesDao seriesDao;

	/**
	 * 车系年份DAO
	 */
	@Autowired
	private CarSeriesYearDao seriesYearDao;

	/**
	 * 车型DAO
	 */
	@Autowired
	private CarModelDao modelDao;

	/**
	 * 事务模板
	 */
	@Resource(name = Constants.TRANSACTION_TEMPLATE)
	private TransactionTemplate transactionTempalte;

	/**
	 * 上传服务
	 */
	@Autowired
	private UploadService uploadSvc;

	/**
	 * 插入车型品牌
	 * 
	 * @param brandResult
	 *            车型品牌的请求结果
	 * @return 操作结果
	 * @throws SQLException
	 *             发生数据访问错误时
	 */
	public CommonOperationResult importBrands(CarBrandResult brandResult) {
		List<CarBrand> brands = new ArrayList<CarBrand>(brandResult.getBrands().size());

		for (CarBrandItem brandItem : brandResult.getBrands()) {
			CarBrand brand = new CarBrand();
			brand.setId(brandItem.getId());
			brand.setName(brandItem.getName());
			brand.setFirstChar(brandItem.getFirstChar());
			brand.setEnabled(true);
            brands.add(brand);
		}

		brandDao.importBrands(brands);
		return CommonOperationResult.Succeeded;
	}

	/**
	 * 导入车系
	 * 
	 * @param brandId
	 *            品牌ID
	 * @param factoryResult
	 *            请求车系的结果
	 * @return 操作结果
	 * @throws SQLException
	 *             发生数据访问错误时
	 */
	@Transactional(transactionManager = Constants.TRANSACTION_MANAGER)
	public CommonOperationResult importSeries(BigDecimal brandId, CarFactoryResult factoryResult) {
		List<CarFactory> factories = new ArrayList<>(factoryResult.getFactories().size());
		List<CarBrandFactory> brandFactories = new ArrayList<>(factoryResult.getFactories().size());
		List<CarSeries> serieses = new ArrayList<>();

		for (CarFactoryItem factoryItem : factoryResult.getFactories()) {
			CarFactory factory = new CarFactory();
			factory.setId(factoryItem.getId());
			factory.setName(factoryItem.getName());
			factory.setFirstChar(factoryItem.getFirstChar());
			factory.setEnabled(true);
			factories.add(factory);
			
			CarBrandFactory brandFactory = new CarBrandFactory();
			brandFactory.setBrandId(brandId);
			brandFactory.setFactoryId(factory.getId());
			brandFactories.add(brandFactory);

			for (CarSeriesItem seriesItem : factoryItem.getSeries()) {
				CarSeries series = new CarSeries();
				series.setId(seriesItem.getId());
				series.setBrandId(brandId);
				series.setFactoryId(factory.getId());
				series.setName(seriesItem.getName());
				series.setFirstChar(seriesItem.getFirstChar());
				series.setEnabled(true);
				serieses.add(series);
			}
		}

		factoryDao.importFactories(factories);
		factoryDao.insertBrandFactories(brandFactories);
		seriesDao.importSerieses(serieses);

		return CommonOperationResult.Succeeded;
	}

	/**
	 * 导入车型
	 * 
	 * @param seriesId
	 *            车系ID
	 * @param modelResult
	 *            查询车型列表结果
	 * @return 操作结果
	 */
	@Transactional(transactionManager = Constants.TRANSACTION_MANAGER)
	public CommonOperationResult importModels(BigDecimal seriesId, CarModelResult modelResult) {
		List<CarSeriesYear> seriesYears = new ArrayList<>();
		List<CarModel> models = new ArrayList<>();

		for (CarSeriesYearItem seriesYearItem : modelResult.getYears()) {
			CarSeriesYear year = new CarSeriesYear();
			year.setId(seriesYearItem.getId());
			year.setSeriesId(seriesId);
			year.setEnabled(true);
			year.setName(seriesYearItem.getName());

			Matcher matcher = CarSeriesYearItem.DRAWYEAR_PATTERN.matcher(seriesYearItem.getName());

			if (matcher.matches()) {
				year.setYear(Integer.valueOf(matcher.group(1), 10));
			} else {
				return CommonOperationResult.Failed;
			}

			seriesYears.add(year);

			for (int i = 0, c = seriesYearItem.getModels().size(); i < c; ++i) {
				CarModelItem modelItem = seriesYearItem.getModels().get(i);

				CarModel model = new CarModel();
				model.setId(modelItem.getId());
				model.setSeriesId(seriesId);
				model.setName(modelItem.getName());
				model.setYear(year.getYear());
				model.setSort(i);
				model.setEnabled(true);

				models.add(model);
			}
		}

		seriesYearDao.importSeriesYears(seriesYears);
		modelDao.importModels(models);

		return CommonOperationResult.Succeeded;
	}

	/**
	 * 所有品牌可用的首字母
	 * 
	 * @return 首字母集合
	 */
	public Set<String> getBrandFirstChars() {
		List<CarBrand> brands = brandDao.selectList();

		Set<String> ret;

		if (brands.size() > 0) {
			ret = new HashSet<String>();
			for (CarBrand brand : brands) {
				ret.add(brand.getFirstChar().toUpperCase());
			}
		} else {
			ret = Collections.emptySet();
		}

		return ret;
	}

	/**
	 * 更新品牌的图标
	 * 
	 * @param brandId
	 *            品牌ID
	 * @param orginUrl
	 *            原资源URL
	 * @param is
	 *            输入流
	 * @return 操作结果,如果传入的ID不存在时会返回NotExists
	 */
	public CommonOperationResult saveBrandLogo(final BigDecimal brandId, String orginUrl, InputStream is) {
		final String filePath = uploadSvc.saveFileToTempPath("CARBRAND", orginUrl, is);

		CommonOperationResult ret = CommonOperationResult.Failed;
		final String[] toDelete = new String[1];

		if (filePath != null && uploadSvc.copyFileToFormalFolder(filePath)) {
			ret = transactionTempalte.execute(new TransactionCallback<CommonOperationResult>() {

				@Override
				public CommonOperationResult doInTransaction(TransactionStatus status) {
					CarBrand brand = brandDao.selectById(brandId, true);

					if (brand == null) {
						return CommonOperationResult.NotExists;
					}

					toDelete[0] = brand.getLogoPath();

					brand.setLogoPath(filePath);
					brand.setModifierId(BigDecimal.ZERO);
					brandDao.update(brand);

					return CommonOperationResult.Succeeded;
				}
			});

			if (ret == CommonOperationResult.Succeeded && StringUtils.isNotEmpty(toDelete[0])) {
				uploadSvc.deleteFromalFile(toDelete[0]);
			}
		}

		return ret;
	}

}
