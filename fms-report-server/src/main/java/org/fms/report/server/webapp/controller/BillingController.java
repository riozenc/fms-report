package org.fms.report.server.webapp.controller;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.io.FileUtils;
import org.apache.poi.util.IOUtils;
import org.fms.report.common.webapp.bean.TableDataBean;
import org.fms.report.common.webapp.bean.UnPublishBean;
import org.fms.report.common.webapp.domain.ChargeInfoDomain;
import org.fms.report.common.webapp.domain.DeptDomain;
import org.fms.report.common.webapp.domain.MeterDomain;
import org.fms.report.common.webapp.domain.ParamDomain;
import org.fms.report.common.webapp.returnDomain.UserInfoEntity;
import org.fms.report.server.utils.JasperHelper;
import org.fms.report.server.webapp.service.BillingService;
import org.fms.report.server.webapp.service.ChargeService;
import org.fms.report.server.webapp.service.CimService;
import org.fms.report.server.webapp.service.DeptService;
import org.fms.report.server.webapp.service.MeterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.riozenc.titanTool.spring.web.http.HttpResult;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

@ControllerAdvice
@RequestMapping(value = "Billing")
public class BillingController {

	@Autowired
	private BillingService billingService;

	@Autowired
	private ChargeService chargeService;

	@Autowired
	private MeterService meterService;

	@Autowired
	private DeptService deptService;

	@Autowired
	private CimService cimService;

	// 本月预收余额清单
	@ResponseBody
	@RequestMapping(value = "/monBalance")
	public Object feeRecStatistics(@RequestBody String json) throws JRException, IOException {
		JSONObject jsonObject = JSONObject.parseObject(json);

		UserInfoEntity userInfoEntity = JSONObject.parseObject(jsonObject.getString("userInfo"), UserInfoEntity.class);

		ChargeInfoDomain chargeInfoDomain = JSONObject.parseObject(jsonObject.getString("data"),
				ChargeInfoDomain.class);

		String pdfFileName = "monBalance-" + userInfoEntity.getUserId() + "-" + String.valueOf(new Date().getTime());

		String filePath = "/static/pdf/" + chargeInfoDomain.getMon() + "/monBalance/" + pdfFileName + ".pdf";

		String returnUrl = "report" + filePath;

		List<TableDataBean> tableDataList = new ArrayList<>();

		tableDataList = chargeService.monBalance(chargeInfoDomain);

		JRDataSource jrDataSource = new JRBeanCollectionDataSource(tableDataList);
		Map<String, Object> m = new HashMap<>();
		m.put("query", "其他参数测试");
		ClassPathResource cpr = new ClassPathResource("static/jasperreport/AdvanceBalance.jasper");
		InputStream is = cpr.getInputStream();
		File file = File.createTempFile("Zero", "jasper");
		try {
			FileUtils.copyInputStreamToFile(is, file);
		} finally {
			IOUtils.closeQuietly(is);
		}
		JasperHelper jasperHelper = new JasperHelper();
		Boolean result = jasperHelper.exportPdf(filePath, file.getPath(), m, jrDataSource);
		if (result.equals(true)) {
			return new HttpResult(HttpResult.SUCCESS, "执行成功", returnUrl);
		} else {
			return new HttpResult(HttpResult.ERROR, "执行失败");
		}
	}

	// 未发行户明细表
	@ResponseBody
	@RequestMapping(value = "/unPublishReport")
	public Object unPublishReport(@RequestBody String json) throws JRException, IOException {
		JSONObject jsonObject = JSONObject.parseObject(json);

		// 参数
		Integer mon = jsonObject.getInteger("mon");

		Long businessPlaceCode = jsonObject.getLong("businessPlaceCode");

		String pdfFileName = "unPublishReport-" + businessPlaceCode;

		String filePath = "/static/pdf/" + mon + "/unPublishReport/" + pdfFileName + ".pdf";

		String returnUrl = "report" + filePath;

		List<TableDataBean> tableDataList = new ArrayList<>();

		List<Integer> statuss = new ArrayList<>();
		statuss.add(-2);
		statuss.add(0);
		statuss.add(1);
		statuss.add(2);

		MeterDomain meterDomain = new MeterDomain();
		meterDomain.setMon(mon);
		meterDomain.setBusinessPlaceCode(businessPlaceCode);
		meterDomain.setStatuss(statuss);
		meterDomain.setPageSize(-1);

		TableDataBean tableData = new TableDataBean();

		List<MeterDomain> meterDomains = meterService.getMeterByBusinessPlaceCode(meterDomain);

		List<UnPublishBean> unPublishBeans = new ArrayList<>();

		tableData.setMon(mon);

		if (meterDomains == null || meterDomains.size() < 1) {
			tableDataList = new ArrayList<>();
		} else {
			Map<Long, String> meterCalStatusMap = cimService.findSystemCommonConfigByType("METER_CAL_STATUS");

			ParamDomain paramDomain = new ParamDomain();
			// paramDomain.setPageSize(-1);
			List<DeptDomain> deptList = deptService.findByWhere(paramDomain);
			Map<Long, DeptDomain> deptDomainMap = deptList.stream().filter(m -> m.getId() != null)
					.collect(Collectors.toMap(DeptDomain::getId, a -> a, (k1, k2) -> k1));

			for (MeterDomain t : meterDomains) {
				UnPublishBean unPublishBean = new UnPublishBean();
				unPublishBean.setMeterNo(t.getMeterNo());
				unPublishBean.setUserNo(t.getUserNo());
				unPublishBean.setUserName(t.getUserName());
				unPublishBean.setBusinessPlaceCodeName(deptDomainMap.get(t.getBusinessPlaceCode()).getDeptName());
				unPublishBean.setWriteSectName(t.getWriteSectNo());
				unPublishBean.setStatusName(meterCalStatusMap.get(Long.valueOf(t.getStatus())));
				unPublishBean.setWriteSectionId(t.getWriteSectionId());
				unPublishBeans.add(unPublishBean);
			}
			
			unPublishBeans = unPublishBeans.stream()
					.sorted(Comparator
							.comparing(UnPublishBean::getWriteSectionId, Comparator.nullsLast(Long::compareTo))
							.thenComparing(UnPublishBean::getUserNo, Comparator.nullsLast(String::compareTo)))
					.collect(Collectors.toList());
			tableData.setTableData(new JRBeanCollectionDataSource(unPublishBeans));
			tableDataList.add(tableData);
		}

		JRDataSource jrDataSource = new JRBeanCollectionDataSource(tableDataList);
		Map<String, Object> m = new HashMap<>();
		m.put("query", "其他参数测试");
		ClassPathResource cpr = new ClassPathResource("static/jasperreport/UnIssued.jasper");
		InputStream is = cpr.getInputStream();
		File file = File.createTempFile("Zero", "jasper");
		try {
			FileUtils.copyInputStreamToFile(is, file);
		} finally {
			IOUtils.closeQuietly(is);
		}
		JasperHelper jasperHelper = new JasperHelper();
		Boolean result = jasperHelper.exportPdf(filePath, file.getPath(), m, jrDataSource);
		if (result.equals(true)) {
			return new HttpResult(HttpResult.SUCCESS, "执行成功", returnUrl);
		} else {
			return new HttpResult(HttpResult.ERROR, "执行失败");
		}
	}

	// 未发行户明细表
	@ResponseBody
	@RequestMapping(value = "/unPublishReportToExcel")
	public Object unPublishReportToExcel(@RequestBody String json) throws JRException, IOException {
		JSONObject jsonObject = JSONObject.parseObject(json);

		// 参数
		Integer mon = jsonObject.getInteger("mon");

		Long businessPlaceCode = jsonObject.getLong("businessPlaceCode");

		String pdfFileName = "unPublishReport-" + businessPlaceCode;

		String filePath = "/static/pdf/" + mon + "/unPublishReport/" + pdfFileName + ".xls";

		String returnUrl = "report" + filePath;

		List<TableDataBean> tableDataList = new ArrayList<>();

		List<Integer> statuss = new ArrayList<>();
		statuss.add(-2);
		statuss.add(0);
		statuss.add(1);
		statuss.add(2);

		MeterDomain meterDomain = new MeterDomain();
		meterDomain.setMon(mon);
		meterDomain.setBusinessPlaceCode(businessPlaceCode);
		meterDomain.setStatuss(statuss);
		meterDomain.setPageSize(-1);

		TableDataBean tableData = new TableDataBean();

		List<MeterDomain> meterDomains = meterService.getMeterByBusinessPlaceCode(meterDomain);

		List<UnPublishBean> unPublishBeans = new ArrayList<>();

		tableData.setMon(mon);

		if (meterDomains == null || meterDomains.size() < 1) {
			tableDataList = new ArrayList<>();
		} else {
			Map<Long, String> meterCalStatusMap = cimService.findSystemCommonConfigByType("METER_CAL_STATUS");

			ParamDomain paramDomain = new ParamDomain();
			// paramDomain.setPageSize(-1);
			List<DeptDomain> deptList = deptService.findByWhere(paramDomain);
			Map<Long, DeptDomain> deptDomainMap = deptList.stream().filter(m -> m.getId() != null)
					.collect(Collectors.toMap(DeptDomain::getId, a -> a, (k1, k2) -> k1));

			meterDomains.forEach(t -> {
				UnPublishBean unPublishBean = new UnPublishBean();
				unPublishBean.setMeterNo(t.getMeterNo());
				unPublishBean.setUserNo(t.getUserNo());
				unPublishBean.setUserName(t.getUserName());
				unPublishBean.setBusinessPlaceCodeName(deptDomainMap.get(t.getBusinessPlaceCode()).getDeptName());
				unPublishBean.setWriteSectName(t.getWriteSectName());
				unPublishBean.setStatusName(meterCalStatusMap.get(t.getStatus()));
				unPublishBean.setStatusName(meterCalStatusMap.get(Long.valueOf(t.getStatus())));
				unPublishBeans.add(unPublishBean);
			});

			tableData.setTableData(new JRBeanCollectionDataSource(unPublishBeans));
			tableDataList.add(tableData);
		}

		JRDataSource jrDataSource = new JRBeanCollectionDataSource(tableDataList);
		Map<String, Object> m = new HashMap<>();
		m.put("query", "其他参数测试");
		ClassPathResource cpr = new ClassPathResource("static/jasperreport/UnIssued.jasper");
		InputStream is = cpr.getInputStream();
		File file = File.createTempFile("Zero", "jasper");
		try {
			FileUtils.copyInputStreamToFile(is, file);
		} finally {
			IOUtils.closeQuietly(is);
		}
		JasperHelper jasperHelper = new JasperHelper();
		Boolean result = jasperHelper.exportExcel(filePath, file.getPath(), m, jrDataSource);
		if (result.equals(true)) {
			return new HttpResult(HttpResult.SUCCESS, "执行成功", returnUrl);
		} else {
			return new HttpResult(HttpResult.ERROR, "执行失败");
		}
	}
}
