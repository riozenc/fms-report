package org.fms.report.server.webapp.controller;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.io.FileUtils;
import org.apache.poi.util.IOUtils;
import org.fms.report.common.util.FileUtil;
import org.fms.report.common.util.FormatterUtil;
import org.fms.report.common.util.MonUtils;
import org.fms.report.common.webapp.bean.RecRateBean;
import org.fms.report.common.webapp.bean.TableDataBean;
import org.fms.report.common.webapp.domain.ArrearageDomain;
import org.fms.report.common.webapp.domain.ChargeInfoDomain;
import org.fms.report.common.webapp.domain.NoteInfoDomain;
import org.fms.report.common.webapp.returnDomain.FeeRecStatisticsBean;
import org.fms.report.common.webapp.returnDomain.UserInfoEntity;
import org.fms.report.server.utils.JasperHelper;
import org.fms.report.server.webapp.service.BillingService;
import org.fms.report.server.webapp.service.ChargeService;
import org.fms.report.server.webapp.service.DeptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.system.ApplicationHome;
import org.springframework.core.io.ClassPathResource;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.riozenc.titanTool.spring.web.http.HttpResult;
import com.riozenc.titanTool.spring.web.http.HttpResultPagination;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

@ControllerAdvice
@RequestMapping(value = "Charge")
public class ChargeController {

    @Autowired
    private ChargeService chargeService;


    @Autowired
    private BillingService billingService;

    @Autowired
    private DeptService deptService;

    // 回收率pdf预览
    @ResponseBody
    @RequestMapping(value = "/recRateShow")
    public Object recRateShow(@RequestBody String json) throws JRException, IOException {

        ArrearageDomain arrearageDmoain = JSONObject.parseObject(json, ArrearageDomain.class);

        String pdfFileName = "ChargeRecRate-" + Calendar.getInstance().getTimeInMillis();
        pdfFileName = FormatterUtil.getMD5String(pdfFileName) + ".pdf";
        String filePath = "/static/pdf/" + arrearageDmoain.getStartMon() + "" + arrearageDmoain.getEndMon() + "/" + pdfFileName;
        String returnUrl = "report" + filePath;

        if (arrearageDmoain.getAgainStat() != null && !arrearageDmoain.getAgainStat()) {
            ApplicationHome h = new ApplicationHome(getClass());
            String baseUrl = FileUtil.getSaveFilePath();
            File file = new File(baseUrl + filePath);
            if (file.exists()) {
                return new HttpResult(HttpResult.SUCCESS, "执行成功", returnUrl);
            }else{
                return new HttpResult<String>(HttpResult.ERROR, "未统计过的报表请选中重新统计",null);
            }
        }
        List<TableDataBean> tableDataList = chargeService.recRate(arrearageDmoain);
        JRDataSource jrDataSource = new JRBeanCollectionDataSource(tableDataList);
        Map<String, Object> m = new HashMap<>();
        m.put("query", "其他参数测试");
        ClassPathResource cpr = new ClassPathResource("static/jasperreport/RecoveryRate.jasper");
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

    // 回收率pdf预览
    @ResponseBody
    @RequestMapping(value = "/recRateToExcel")
    public Object recRateToExcel(@RequestBody String json) throws JRException, IOException {

        ArrearageDomain arrearageDmoain = JSONObject.parseObject(json, ArrearageDomain.class);

        String pdfFileName = "ChargeRecRate-" + Calendar.getInstance().getTimeInMillis();
        pdfFileName = FormatterUtil.getMD5String(pdfFileName) + ".xls";
        String filePath = "/static/xls/" + arrearageDmoain.getStartMon() + "" + arrearageDmoain.getEndMon() + "/" + pdfFileName;
        String returnUrl = "report" + filePath;

        if (arrearageDmoain.getAgainStat() != null && !arrearageDmoain.getAgainStat()) {
            ApplicationHome h = new ApplicationHome(getClass());
            String baseUrl = FileUtil.getSaveFilePath();
            File file = new File(baseUrl + filePath);
            if (file.exists()) {
                return new HttpResult(HttpResult.SUCCESS, "执行成功", returnUrl);
            }else{
                return new HttpResult<String>(HttpResult.ERROR, "未统计过的报表请选中重新统计",null);
            }
        }
        List<TableDataBean> tableDataList = chargeService.recRate(arrearageDmoain);
        JRDataSource jrDataSource = new JRBeanCollectionDataSource(tableDataList);
        Map<String, Object> m = new HashMap<>();
        m.put("query", "其他参数测试");
        ClassPathResource cpr = new ClassPathResource("static/jasperreport/RecoveryRate.jasper");
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
    
    // 超级报表-回收率查询
    @ResponseBody
    @RequestMapping(value = "/recRate")
    public HttpResultPagination<?> recRate(@RequestBody String json) {
        ArrearageDomain arrearageDmoain = JSONObject.parseObject(json, ArrearageDomain.class);
        Integer pageCurrent = arrearageDmoain.getPageCurrent();
        Integer pageSize = arrearageDmoain.getPageSize();
        List<TableDataBean> tableDataList = chargeService.recRate(arrearageDmoain);

        arrearageDmoain.setPageSize(-1);
        Integer totalRow = 0;
        //假分页
        List<TableDataBean> tableDatas = new ArrayList<TableDataBean>();
        List<RecRateBean> recRateBeans = new ArrayList<RecRateBean>();
        TableDataBean tableData = new TableDataBean();
        if (tableDataList !=null && tableDataList.size()>0) {
            totalRow = tableDataList.get(0).getTableData().getData().size();
            if (pageSize==-1) {
                return new HttpResultPagination(arrearageDmoain,tableDataList);
            }
            List<RecRateBean> recRateBeans2=(List<RecRateBean>) tableDataList.get(0).getTableData().getData();
            for (int i = (pageCurrent-1)*pageSize; i < Math.min(totalRow, pageCurrent*pageSize); i++) {
            	recRateBeans.add(recRateBeans2.get(i));
            }

        }
        arrearageDmoain.setTotalRow(totalRow);
        tableData.setTableData(new JRBeanCollectionDataSource(recRateBeans));
        tableDatas.add(tableData);

//        List<TableDataBean> tableData = new ArrayList<TableDataBean>();
//        for (int i = (pageCurrent-1)*pageSize; i < Math.min(tableDataList.size(), pageCurrent*pageSize); i++) {
//			tableData.add(tableDataList.get(i));
//		}
        return new HttpResultPagination(arrearageDmoain,tableDatas);
    }
    
    //超级报表-收费汇总统计
    @ResponseBody
    @RequestMapping(value = "/chargeSummary")
    public HttpResultPagination<?> chargeSummary(@RequestBody String json) throws JRException, IOException {

        ChargeInfoDomain chargeInfoDomain = JSONObject.parseObject(json, ChargeInfoDomain.class);
        List<TableDataBean> tableDataList = chargeService.feeRecStatistics(chargeInfoDomain);
//        chargeInfoDomain.setTotalRow(tableDataList.size());
//        List<TableDataBean> tableData = new ArrayList<TableDataBean>();
        Integer pageCurrent = chargeInfoDomain.getPageCurrent();
        Integer pageSize = chargeInfoDomain.getPageSize();
        chargeInfoDomain.setPageSize(-1);
        Integer totalRow = 0;
        //假分页
        List<TableDataBean> tableDatas = new ArrayList<TableDataBean>();
        List<FeeRecStatisticsBean> chargeInfoDomains = new ArrayList<FeeRecStatisticsBean>();
        TableDataBean tableData = new TableDataBean();
        if (tableDataList !=null && tableDataList.size()>0) {
            totalRow = tableDataList.get(0).getTableData().getData().size();
            if (pageSize==-1) {
                return new HttpResultPagination(chargeInfoDomain,tableDataList);
            }
            List<FeeRecStatisticsBean> chargeInfoDomain1=(List<FeeRecStatisticsBean>) tableDataList.get(0).getTableData().getData();
            for (int i = (pageCurrent-1)*pageSize; i < Math.min(totalRow, pageCurrent*pageSize); i++) {
                chargeInfoDomains.add(chargeInfoDomain1.get(i));
            }

        }
        chargeInfoDomain.setTotalRow(totalRow);
        tableData.setTableData(new JRBeanCollectionDataSource(chargeInfoDomains));
        tableDatas.add(tableData);

//        for (int i = (pageCurrent-1)*pageSize; i < Math.min(tableDataList.size(), pageCurrent*pageSize); i++) {
//			tableData.add(tableDataList.get(i));
//		}
        return  new  HttpResultPagination(chargeInfoDomain,tableDatas);
    }

    // 收费汇总统计预览
    @ResponseBody
    @RequestMapping(value = "/chargeSummaryShow")
    public Object chargeSummaryShow(@RequestBody String json) throws JRException, IOException {

        ChargeInfoDomain chargeInfoDomain = JSONObject.parseObject(json, ChargeInfoDomain.class);

        String pdfFileName = "";
        Long BusinessPlace = chargeInfoDomain.getBusinessPlaceCode() == null ? null : chargeInfoDomain.getBusinessPlaceCode();
        String groupType = chargeInfoDomain.getGroupBy() == null ? null : chargeInfoDomain.getGroupBy();

        pdfFileName = "ChargeSummary-" + chargeInfoDomain.getStartDate().toString() + chargeInfoDomain.getEndDate().toString() + groupType + "-" + BusinessPlace + "";

        pdfFileName = FormatterUtil.getMD5String(pdfFileName) + ".pdf";
        String filePath = "/static/pdf/" + MonUtils.getMon()+ "/" + pdfFileName;
        String returnUrl = "report" + filePath;

        if (chargeInfoDomain.getAgainStat() != null && !chargeInfoDomain.getAgainStat()) {
            ApplicationHome h = new ApplicationHome(getClass());
            String baseUrl = FileUtil.getSaveFilePath();
            File file = new File(baseUrl + filePath);
            if (file.exists()) {
                return new HttpResult(HttpResult.SUCCESS, "执行成功", returnUrl);
            }else{
                return new HttpResult<String>(HttpResult.ERROR, "未统计过的报表请选中重新统计",null);
            }
        }
        List<TableDataBean> tableDataList = chargeService.feeRecStatistics(chargeInfoDomain);
        if (tableDataList == null || tableDataList.size() < 1) {
            return new HttpResult(HttpResult.ERROR, "暂无数据");
        }
        JRDataSource jrDataSource = new JRBeanCollectionDataSource(tableDataList);
        Map<String, Object> m = new HashMap<>();
        m.put("query", "其他参数测试");
        ClassPathResource cpr = new ClassPathResource("static/jasperreport/ChargeSummary.jasper");
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


    // 收费汇总统计预览
    @ResponseBody
    @RequestMapping(value = "/chargeSummaryToExcel")
    public Object chargeSummaryToExcel(@RequestBody String json) throws JRException, IOException {

        ChargeInfoDomain chargeInfoDomain = JSONObject.parseObject(json, ChargeInfoDomain.class);

        String pdfFileName = "";
        Long BusinessPlace = chargeInfoDomain.getBusinessPlaceCode() == null ? null : chargeInfoDomain.getBusinessPlaceCode();
        String groupType = chargeInfoDomain.getGroupBy() == null ? null : chargeInfoDomain.getGroupBy();

        pdfFileName = "ChargeSummary-" + chargeInfoDomain.getStartDate().toString() + chargeInfoDomain.getEndDate().toString() + groupType + "-" + BusinessPlace + "";

        pdfFileName = FormatterUtil.getMD5String(pdfFileName) + ".xls";
        String filePath = "/static/xls/" + MonUtils.getMon()+ "/" + pdfFileName;
        String returnUrl = "report" + filePath;

        if (chargeInfoDomain.getAgainStat() != null && !chargeInfoDomain.getAgainStat()) {
            ApplicationHome h = new ApplicationHome(getClass());
            String baseUrl = FileUtil.getSaveFilePath();
            File file = new File(baseUrl + filePath);
            if (file.exists()) {
                return new HttpResult(HttpResult.SUCCESS, "执行成功", returnUrl);
            }else{
                return new HttpResult<String>(HttpResult.ERROR, "未统计过的报表请选中重新统计",null);
            }
        }
        List<TableDataBean> tableDataList = chargeService.feeRecStatistics(chargeInfoDomain);
        if (tableDataList == null || tableDataList.size() < 1) {
            return new HttpResult(HttpResult.ERROR, "暂无数据");
        }
        JRDataSource jrDataSource = new JRBeanCollectionDataSource(tableDataList);
        Map<String, Object> m = new HashMap<>();
        m.put("query", "其他参数测试");
        ClassPathResource cpr = new ClassPathResource("static/jasperreport/ChargeSummary.jasper");
        InputStream is = cpr.getInputStream();
        File file = File.createTempFile("Zero", "jasper");
        try {
            FileUtils.copyInputStreamToFile(is, file);
        } finally {
            IOUtils.closeQuietly(is);
        }
        JasperHelper jasperHelper = new JasperHelper();
        Boolean result = jasperHelper.exportExcel(filePath, file.getPath(), m,
                jrDataSource);
        if (result.equals(true)) {
            return new HttpResult(HttpResult.SUCCESS, "执行成功", returnUrl);
        } else {
            return new HttpResult(HttpResult.ERROR, "执行失败");
        }
    }


    // 超级报表-收费情况明细统计预览
    @ResponseBody
    @RequestMapping(value = "/chargeDetails")
    public Object chargeDetails(@RequestBody String json) {

        ChargeInfoDomain chargeInfoDomain = JSONObject.parseObject(json, ChargeInfoDomain.class);
        //qq
        Integer pageCurrent = chargeInfoDomain.getPageCurrent();
        Integer pageSize = chargeInfoDomain.getPageSize();
        Integer totalRow = 0;
        chargeInfoDomain.setPageSize(-1);
        List<TableDataBean> tableDataList = chargeService.chargeDetails(chargeInfoDomain);
        //假分页
        List<TableDataBean> tableDatas = new ArrayList<TableDataBean>();
        List<FeeRecStatisticsBean> FeeRecStatisticsBeans=new ArrayList<FeeRecStatisticsBean>();
        TableDataBean tableData = new TableDataBean();
        if (tableDataList !=null && tableDataList.size()>0) {
        	totalRow = tableDataList.get(0).getTableData().getData().size();
        	if (pageSize==-1) {
				return new HttpResultPagination(chargeInfoDomain,tableDataList);
			}
        	List<FeeRecStatisticsBean> FeeRecStatisticsBeans1=(List<FeeRecStatisticsBean>) tableDataList.get(0).getTableData().getData();
        	for (int i = (pageCurrent-1)*pageSize; i < Math.min(totalRow, pageCurrent*pageSize); i++) {
        		FeeRecStatisticsBeans.add(FeeRecStatisticsBeans1.get(i));
        	}
			
		}
        chargeInfoDomain.setTotalRow(totalRow);
        tableData.setTableData(new JRBeanCollectionDataSource(FeeRecStatisticsBeans));
        tableDatas.add(tableData);
        return new HttpResultPagination(chargeInfoDomain,tableDatas);
        
        //        Integer pageCurrent = chargeInfoDomain.getPageCurrent();
//        Integer pageSize = chargeInfoDomain.getPageSize();
//        List<TableDataBean> tableDataList = chargeService.chargeDetails(chargeInfoDomain);
//        chargeInfoDomain.setTotalRow(tableDataList.size());
//        List<TableDataBean> tableData = new ArrayList<TableDataBean>();
//        for (int i = (pageCurrent-1)*pageSize; i < Math.min(tableDataList.size(), pageCurrent*pageSize); i++) {
//			tableData.add(tableDataList.get(i));
//		}
//        
//        return new HttpResultPagination(chargeInfoDomain, tableDataList);
    }
    
    // 收费情况明细统计预览
    @ResponseBody
    @RequestMapping(value = "/chargeDetailsShow")
    public Object chargeDetailsShow(@RequestBody String json) throws JRException, IOException {

        ChargeInfoDomain chargeInfoDomain = JSONObject.parseObject(json, ChargeInfoDomain.class);
        String pdfFileName = json;


        pdfFileName = FormatterUtil.getMD5String(pdfFileName) + ".pdf";
        String filePath = "/static/pdf/charge/" + pdfFileName;
        String returnUrl = "report" + filePath;

        if (chargeInfoDomain.getAgainStat() != null && !chargeInfoDomain.getAgainStat()) {
            ApplicationHome h = new ApplicationHome(getClass());
            String baseUrl =FileUtil.getSaveFilePath();
            File file = new File(baseUrl + filePath);
            if (file.exists()) {
                return new HttpResult(HttpResult.SUCCESS, "执行成功", returnUrl);
            }else{
                return new HttpResult<String>(HttpResult.ERROR, "未统计过的报表请选中重新统计",null);
            }
        }
        List<TableDataBean> tableDataList = chargeService.chargeDetails(chargeInfoDomain);
        if (tableDataList == null || tableDataList.size() < 1) {
            return new HttpResult(HttpResult.ERROR, "暂无数据");
        }
        JRDataSource jrDataSource = new JRBeanCollectionDataSource(tableDataList);
        Map<String, Object> m = new HashMap<>();
        m.put("query", "其他参数测试");
        ClassPathResource cpr = new ClassPathResource("static/jasperreport/ChargeCheck.jasper");
        InputStream is = cpr.getInputStream();
        File file = File.createTempFile("ChargeCheck", "jasper");
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

    // 收费情况明细统计预览
    @ResponseBody
    @RequestMapping(value = "/chargeDetailsToExcel")
    public Object chargeDetailsToExcel(@RequestBody String json) throws JRException, IOException {

        ChargeInfoDomain chargeInfoDomain = JSONObject.parseObject(json, ChargeInfoDomain.class);

        String pdfFileName = json;


        pdfFileName = FormatterUtil.getMD5String(pdfFileName) + ".xls";
        String filePath = "/static/xls/charge/" + pdfFileName;
        String returnUrl = "report" + filePath;

        if (chargeInfoDomain.getAgainStat() != null && !chargeInfoDomain.getAgainStat()) {
            ApplicationHome h = new ApplicationHome(getClass());
            String baseUrl = FileUtil.getSaveFilePath();
            File file = new File(baseUrl + filePath);
            if (file.exists()) {
                return new HttpResult(HttpResult.SUCCESS, "执行成功", returnUrl);
            }else{
                return new HttpResult<String>(HttpResult.ERROR, "未统计过的报表请选中重新统计",null);
            }
        }
        List<TableDataBean> tableDataList = chargeService.chargeDetails(chargeInfoDomain);
        if (tableDataList == null || tableDataList.size() < 1) {
            return new HttpResult(HttpResult.ERROR, "暂无数据");
        }
        JRDataSource jrDataSource = new JRBeanCollectionDataSource(tableDataList);
        Map<String, Object> m = new HashMap<>();
        m.put("query", "其他参数测试");
        ClassPathResource cpr = new ClassPathResource("static/jasperreport/ChargeCheck.jasper");
        InputStream is = cpr.getInputStream();
        File file = File.createTempFile("ChargeCheck", "jasper");
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

    //结算单
    @ResponseBody
    @RequestMapping(value = "/statement")
    public HttpResult Statement(@RequestBody String json) throws JRException, IOException {
        JSONObject jsonObject = JSONObject.parseObject(json, JSONObject.class);

        UserInfoEntity userInfoEntity =
                JSONObject.parseObject(jsonObject.getString("userInfo"),
                        UserInfoEntity.class);

        List<NoteInfoDomain> noteInfoDomainList =
                JSONObject.parseArray(jsonObject.getString("data"), NoteInfoDomain.class);
        //随机文件名
        String pdfFileName = FormatterUtil.getFileNameNew();

        pdfFileName = "statement" + pdfFileName + ".pdf";
        String filePath = "/static/pdf/statement/" + pdfFileName;
        String returnUrl = "report" + filePath;

        List<TableDataBean> tableDataList =
                chargeService.Statement(noteInfoDomainList, userInfoEntity.getUserName());

        JRDataSource jrDataSource = new JRBeanCollectionDataSource(tableDataList);
        Map<String, Object> m = new HashMap<>();
        m.put("query", "其他参数测试");
        ClassPathResource cpr = new ClassPathResource("static/jasperreport/Statement.jasper");
        InputStream is = cpr.getInputStream();
        File file = File.createTempFile("Zero", "jasper");
        try {
            FileUtils.copyInputStreamToFile(is, file);
        } finally {
            IOUtils.closeQuietly(is);
        }
        JasperHelper jasperHelper = new JasperHelper();
        Boolean result = jasperHelper.exportPdfs(filePath, tableDataList, "Statement");

        //更新打印标识和打票人
        List<Long> ids = noteInfoDomainList.stream().map(NoteInfoDomain::getId).collect(Collectors.toList());
        NoteInfoDomain noteInfoDomain = new NoteInfoDomain();
        noteInfoDomain.setIds(ids);
        //noteInfoDomain.setIsPrint(1);
        noteInfoDomain.setPrintDate(new Date());
        noteInfoDomain.setPrintMan(userInfoEntity.getId());
        noteInfoDomain.setPrintManName(userInfoEntity.getUserName());
        noteInfoDomain.setNotePrintNo("结算单已打印");
        HttpResult httpResult = billingService.updateNoteByIds(noteInfoDomain);
        if (result.equals(true) && httpResult.getStatusCode() == HttpResult.SUCCESS) {
            return new HttpResult(HttpResult.SUCCESS, "执行成功", returnUrl);
        } else {
            return new HttpResult(HttpResult.ERROR, "执行失败");
        }
    }


    //合打结算单
    @ResponseBody
    @RequestMapping(value = "/jointStatement")
    public HttpResult jointStatement(@RequestBody String json) throws JRException, IOException {
        JSONObject jsonObject = JSONObject.parseObject(json, JSONObject.class);

        UserInfoEntity userInfoEntity =
                JSONObject.parseObject(jsonObject.getString("userInfo"),
                        UserInfoEntity.class);

        List<NoteInfoDomain> noteInfoDomainList =
                JSONObject.parseArray(jsonObject.getString("data"), NoteInfoDomain.class);
        //随机文件名
        String pdfFileName = FormatterUtil.getFileNameNew();

        pdfFileName = "jointStatement" + pdfFileName + ".pdf";
        String filePath = "/static/pdf/jointStatement/" + pdfFileName;
        String returnUrl = "report" + filePath;

        List<TableDataBean> tableDataList =
                chargeService.jointStatement(noteInfoDomainList, userInfoEntity.getUserName());

        JRDataSource jrDataSource = new JRBeanCollectionDataSource(tableDataList);
        Map<String, Object> m = new HashMap<>();
        m.put("query", "其他参数测试");
        ClassPathResource cpr = new ClassPathResource("static/jasperreport/Statement.jasper");
        InputStream is = cpr.getInputStream();
        File file = File.createTempFile("Zero", "jasper");
        try {
            FileUtils.copyInputStreamToFile(is, file);
        } finally {
            IOUtils.closeQuietly(is);
        }
        JasperHelper jasperHelper = new JasperHelper();
        Boolean result = jasperHelper.exportPdfs(filePath, tableDataList, "Statement");

        //更新打印标识和打票人
        List<Long> ids = noteInfoDomainList.stream().map(NoteInfoDomain::getId).collect(Collectors.toList());
        NoteInfoDomain noteInfoDomain = new NoteInfoDomain();
        noteInfoDomain.setIds(ids);
        //noteInfoDomain.setIsPrint(1);
        noteInfoDomain.setPrintDate(new Date());
        noteInfoDomain.setPrintMan(userInfoEntity.getId());
        noteInfoDomain.setPrintManName(userInfoEntity.getUserName());
        noteInfoDomain.setNotePrintNo("结算单已打印");
        HttpResult httpResult = billingService.updateNoteByIds(noteInfoDomain);
        if (result.equals(true) && httpResult.getStatusCode() == HttpResult.SUCCESS) {
            return new HttpResult(HttpResult.SUCCESS, "执行成功", returnUrl);
        } else {
            return new HttpResult(HttpResult.ERROR, "执行失败");
        }
    }


    // 收费员月结查询
    @ResponseBody
    @RequestMapping(value = "/monBalanceByDay")
    public Object monBalanceByDay(@RequestBody String json) throws JRException, IOException {
        JSONObject jsonObject = JSONObject.parseObject(json, JSONObject.class);

        //操作员信息
        UserInfoEntity userInfoEntity =
                JSONObject.parseObject(jsonObject.getString("userInfo"),
                        UserInfoEntity.class);

        ChargeInfoDomain chargeInfoDomain =
                JSONObject.parseObject(jsonObject.getString("data"),ChargeInfoDomain.class);

        //赋值操作员信息
        chargeInfoDomain.setOperator(Long.valueOf(userInfoEntity.getId()));
        chargeInfoDomain.setOperatorName(userInfoEntity.getUserName());
        String pdfFileName = json;

        pdfFileName = FormatterUtil.getMD5String(pdfFileName) + ".pdf";
        String filePath = "/static/pdf/charge/monBalanceByDay/" + pdfFileName;
        String returnUrl = "report" + filePath;

        List<TableDataBean> tableDataList = chargeService.monBalanceByDay(chargeInfoDomain);
        if (tableDataList == null || tableDataList.size() < 1) {
            return new HttpResult(HttpResult.ERROR, "暂无数据");
        }
        JRDataSource jrDataSource = new JRBeanCollectionDataSource(tableDataList);
        Map<String, Object> m = new HashMap<>();
        m.put("query", "其他参数测试");
        ClassPathResource cpr = new ClassPathResource("static/jasperreport/monBalance.jasper");
        InputStream is = cpr.getInputStream();
        File file = File.createTempFile("ChargeCheck", "jasper");
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

    // 收费员月结查询
    @ResponseBody
    @RequestMapping(value = "/monBalanceByDayToExcel")
    public Object monBalanceByDayToExcel(@RequestBody String json) throws JRException, IOException {

        JSONObject jsonObject = JSONObject.parseObject(json, JSONObject.class);

        //操作员信息
        UserInfoEntity userInfoEntity =
                JSONObject.parseObject(jsonObject.getString("userInfo"),
                        UserInfoEntity.class);

        ChargeInfoDomain chargeInfoDomain =
                JSONObject.parseObject(jsonObject.getString("data"),ChargeInfoDomain.class);

        //赋值操作员信息
        chargeInfoDomain.setOperator(Long.valueOf(userInfoEntity.getId()));
        chargeInfoDomain.setOperatorName(userInfoEntity.getUserName());
        String pdfFileName = json;

        pdfFileName = FormatterUtil.getMD5String(pdfFileName) + ".xls";
        String filePath = "/static/pdf/charge/monBalanceByDay/" + pdfFileName;
        String returnUrl = "report" + filePath;

        List<TableDataBean> tableDataList = chargeService.monBalanceByDay(chargeInfoDomain);
        if (tableDataList == null || tableDataList.size() < 1) {
            return new HttpResult(HttpResult.ERROR, "暂无数据");
        }
        JRDataSource jrDataSource = new JRBeanCollectionDataSource(tableDataList);
        Map<String, Object> m = new HashMap<>();
        m.put("query", "其他参数测试");
        ClassPathResource cpr = new ClassPathResource("static/jasperreport/monBalance.jasper");
        InputStream is = cpr.getInputStream();
        File file = File.createTempFile("ChargeCheck", "jasper");
        try {
            FileUtils.copyInputStreamToFile(is, file);
        } finally {
            IOUtils.closeQuietly(is);
        }
        JasperHelper jasperHelper = new JasperHelper();
        Boolean result = jasperHelper.exportExcel(filePath, file.getPath(), m,
                jrDataSource);
        if (result.equals(true)) {
            return new HttpResult(HttpResult.SUCCESS, "执行成功", returnUrl);
        } else {
            return new HttpResult(HttpResult.ERROR, "执行失败");
        }
    }


    // 跨区收费
    @ResponseBody
    @RequestMapping(value = "/findCrossChargeInfoDetails")
    public Object findCrossChargeInfoDetails(@RequestBody String json) throws JRException, IOException {

        ChargeInfoDomain chargeInfoDomain =
                JSONObject.parseObject(json, ChargeInfoDomain.class);

        String pdfFileName = json;

        pdfFileName = FormatterUtil.getMD5String(pdfFileName) + ".pdf";
        String filePath = "/static/pdf/charge/CrossChargeInfoDetails/" + pdfFileName;
        String returnUrl = "report" + filePath;

        List<TableDataBean> tableDataList = chargeService.findCrossChargeInfoDetails(chargeInfoDomain);
        if (tableDataList == null || tableDataList.size() < 1) {
            return new HttpResult(HttpResult.ERROR, "暂无数据");
        }
        JRDataSource jrDataSource = new JRBeanCollectionDataSource(tableDataList);
        Map<String, Object> m = new HashMap<>();
        m.put("query", "其他参数测试");
        ClassPathResource cpr = new ClassPathResource("static/jasperreport/CrossCharge.jasper");
        InputStream is = cpr.getInputStream();
        File file = File.createTempFile("CrossCharge", "jasper");
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

    // 跨区收费
    @ResponseBody
    @RequestMapping(value = "/findCrossChargeInfoDetailsToExcel")
    public Object findCrossChargeInfoDetailsToExcel(@RequestBody String json) throws JRException, IOException {

        ChargeInfoDomain chargeInfoDomain =
                JSONObject.parseObject(json, ChargeInfoDomain.class);

        String pdfFileName = json;

        pdfFileName = FormatterUtil.getMD5String(pdfFileName) + ".xls";
        String filePath = "/static/pdf/charge/CrossChargeInfoDetails/" + pdfFileName;
        String returnUrl = "report" + filePath;

        List<TableDataBean> tableDataList = chargeService.findCrossChargeInfoDetails(chargeInfoDomain);
        if (tableDataList == null || tableDataList.size() < 1) {
            return new HttpResult(HttpResult.ERROR, "暂无数据");
        }
        JRDataSource jrDataSource = new JRBeanCollectionDataSource(tableDataList);
        Map<String, Object> m = new HashMap<>();
        m.put("query", "其他参数测试");
        ClassPathResource cpr = new ClassPathResource("static/jasperreport/CrossCharge.jasper");
        InputStream is = cpr.getInputStream();
        File file = File.createTempFile("CrossCharge", "jasper");
        try {
            FileUtils.copyInputStreamToFile(is, file);
        } finally {
            IOUtils.closeQuietly(is);
        }
        JasperHelper jasperHelper = new JasperHelper();
        Boolean result = jasperHelper.exportExcel(filePath, file.getPath(), m,
                jrDataSource);
        if (result.equals(true)) {
            return new HttpResult(HttpResult.SUCCESS, "执行成功", returnUrl);
        } else {
            return new HttpResult(HttpResult.ERROR, "执行失败");
        }
    }


}
