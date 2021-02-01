package org.fms.report.server.webapp.controller;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.apache.poi.util.IOUtils;
import org.fms.report.common.util.FileUtil;
import org.fms.report.common.webapp.bean.TableDataBean;
import org.fms.report.common.webapp.domain.ArrearageDetailDomain;
import org.fms.report.common.webapp.domain.ElectricityTariffRankEntity;
import org.fms.report.common.webapp.domain.SettlementDomain;
import org.fms.report.server.utils.JasperHelper;
import org.fms.report.server.webapp.service.MeterMoneyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.system.ApplicationHome;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.riozenc.titanTool.common.date.DateUtil;
import com.riozenc.titanTool.common.json.utils.GsonUtils;
import com.riozenc.titanTool.properties.Global;
import com.riozenc.titanTool.spring.web.http.HttpResult;
import com.riozenc.titanTool.spring.web.http.HttpResultPagination;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

@Controller
@RequestMapping("MeterMoney")
public class MeterMoneyController {

    @Autowired
    private MeterMoneyService meterMoneyService;
    //电量电费事实查询
    @ResponseBody
    @RequestMapping("meterMoneyDetailQuery")
    public Object meterMoneyDetailQuery(@RequestBody String json) throws IOException, JRException {
    	ArrearageDetailDomain arrearageDetailDomain = GsonUtils.readValue(json, ArrearageDetailDomain.class);

        if(arrearageDetailDomain.getStartMon()<Integer.valueOf(Global.getConfig("mon"))){
            return new HttpResult(HttpResult.ERROR, Global.getConfig("mon")+
                    "之前的月份请在老系统统计");
        }
        List<TableDataBean> tableDataList = meterMoneyService.select(arrearageDetailDomain);
        arrearageDetailDomain.setTotalRow(tableDataList.size());
        List<TableDataBean> tableData = new ArrayList<TableDataBean>();
        Integer pageCurrent = arrearageDetailDomain.getPageCurrent();
        Integer pageSize = arrearageDetailDomain.getPageSize();
        for (int i = (pageCurrent-1)*pageSize; i < Math.min(tableDataList.size(), pageCurrent*pageSize); i++) {
			tableData.add(tableDataList.get(i));
		}
        return new  HttpResultPagination(arrearageDetailDomain, tableData);
    }
    
    @ResponseBody
    @RequestMapping("meterMoneyDetailQueryShow")
    public Object meterMoneyDetailQueryShow(@RequestBody String json) throws IOException, JRException {
        ArrearageDetailDomain arrearageDetailDomain = GsonUtils.readValue(json, ArrearageDetailDomain.class);

        if(arrearageDetailDomain.getStartMon()<Integer.valueOf(Global.getConfig("mon"))){
            return new HttpResult(HttpResult.ERROR, Global.getConfig("mon")+
                    "之前的月份请在老系统统计");
        }
        String pdfFileName = "";
        if (arrearageDetailDomain.getWritorId() != null) {
            pdfFileName = arrearageDetailDomain.getStartMon()+"-" + arrearageDetailDomain.getEndMon() + "MeterMoney-Writor" + arrearageDetailDomain.getWritorId() + arrearageDetailDomain.getGroupBy() + ".pdf";
        }
        if (arrearageDetailDomain.getWriteSectId() != null) {
            pdfFileName = arrearageDetailDomain.getStartMon()+"-" + arrearageDetailDomain.getEndMon() + "MeterMoney-WriteSec" + arrearageDetailDomain.getWriteSectId() + arrearageDetailDomain.getGroupBy() + ".pdf";
        }
        if (arrearageDetailDomain.getBusinessPlaceCode() != null) {
            pdfFileName = arrearageDetailDomain.getStartMon()+"-" + arrearageDetailDomain.getEndMon() + "MeterMoney-Dept" + arrearageDetailDomain.getBusinessPlaceCode() + arrearageDetailDomain.getGroupBy() + ".pdf";
        }
        String filePath = "/static/pdf/" + DateUtil.getDate("yyyyMM") + "/" + pdfFileName;
        //重新统计
        String returnUrl = "report" + filePath;
        if (!arrearageDetailDomain.getAgainStat()) {
            ApplicationHome h = new ApplicationHome(getClass());
            String baseUrl = FileUtil.getSaveFilePath();
            File file = new File(baseUrl + filePath);
            if (file.exists()) {
                return new HttpResult(HttpResult.SUCCESS, "执行成功", returnUrl);
            }else{
                return new HttpResult<String>(HttpResult.ERROR, "未统计过的报表请选中重新统计",null);
            }
        }
        List<TableDataBean> tableDataList = meterMoneyService.select(arrearageDetailDomain);
        JRDataSource jrDataSource = new JRBeanCollectionDataSource(tableDataList);
        Map<String, Object> m = new HashMap<>();
        m.put("query", "其他参数测试");
        ClassPathResource cpr = new ClassPathResource("static/jasperreport/MeterMoney.jasper");
        InputStream is = cpr.getInputStream();
        File file = File.createTempFile("MeterMoney", "jasper");
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

    @ResponseBody
    @RequestMapping("excelExport")
    public Object excel(@RequestBody String json) throws IOException, JRException {
        ArrearageDetailDomain arrearageDetailDomain = GsonUtils.readValue(json, ArrearageDetailDomain.class);

        if(arrearageDetailDomain.getStartMon()<Integer.valueOf(Global.getConfig("mon"))){
            return new HttpResult(HttpResult.ERROR, Global.getConfig("mon")+
                    "之前的月份请在老系统统计");
        }
        String pdfFileName = "";
        if (arrearageDetailDomain.getWritorId() != null) {
            pdfFileName =
                    arrearageDetailDomain.getStartMon()+"-" + arrearageDetailDomain.getEndMon() + "MeterMoney-Writor" + arrearageDetailDomain.getWritorId() + arrearageDetailDomain.getGroupBy() + ".xls";
        }
        if (arrearageDetailDomain.getWriteSectId() != null) {
            pdfFileName = arrearageDetailDomain.getStartMon()+"-" + arrearageDetailDomain.getEndMon() + "MeterMoney-WriteSec" + arrearageDetailDomain.getWriteSectId() + arrearageDetailDomain.getGroupBy() + ".xls";
        }
        if (arrearageDetailDomain.getBusinessPlaceCode() != null) {
            pdfFileName = arrearageDetailDomain.getStartMon()+"-" + arrearageDetailDomain.getEndMon() + "MeterMoney-Dept" + arrearageDetailDomain.getBusinessPlaceCode() + arrearageDetailDomain.getGroupBy() + ".xls";
        }
        String filePath = "/static/xls/" + DateUtil.getDate("yyyyMM") + "/" + pdfFileName;
        //重新统计
        String returnUrl = "report" + filePath;
        if (!arrearageDetailDomain.getAgainStat()) {
            ApplicationHome h = new ApplicationHome(getClass());
            String baseUrl = FileUtil.getSaveFilePath();
            File file = new File(baseUrl + filePath);
            if (file.exists()) {
                return new HttpResult(HttpResult.SUCCESS, "执行成功", returnUrl);
            }else{
                return new HttpResult<String>(HttpResult.ERROR, "未统计过的报表请选中重新统计",null);
            }
        }
        List<TableDataBean> tableDataList = meterMoneyService.select(arrearageDetailDomain);
        JRDataSource jrDataSource = new JRBeanCollectionDataSource(tableDataList);
        Map<String, Object> m = new HashMap<>();
        m.put("query", "其他参数测试");
        ClassPathResource cpr = new ClassPathResource("static/jasperreport/MeterMoney.jasper");
        InputStream is = cpr.getInputStream();
        File file = File.createTempFile("MeterMoney", "jasper");
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

    //托收清单
    @ResponseBody
    @RequestMapping("findBankCollectDetail")
    public Object findBankCollectDetail(@RequestBody String json) throws IOException, JRException {

        SettlementDomain settlementDomain = GsonUtils.readValue(json,SettlementDomain.class);
        if(settlementDomain.getMon()<Integer.valueOf(Global.getConfig("mon"))){
            return new HttpResult(HttpResult.ERROR, Global.getConfig("mon")+
                    "之前的月份请在老系统统计");
        }
        String pdfFileName = "";
        if (settlementDomain.getSettlementNo() != null) {
            pdfFileName =
                    settlementDomain.getMon()+"-"  +
                            "BankCollectDetail-SettlementNo" + settlementDomain.getSettlementNo()+ ".pdf";
        }
        if (settlementDomain.getBankNo() != null) {
            pdfFileName = settlementDomain.getMon()+"-"  +
                    "BankCollectDetail-BankNo" + settlementDomain.getBankNo()+ ".pdf";
        }
        if (settlementDomain.getBusinessPlaceCode() != null) {
            pdfFileName = settlementDomain.getMon()+"-"  +
                    "BankCollectDetail-BusinessPlaceCode" + settlementDomain.getBusinessPlaceCode()+ ".pdf";
        }
        String filePath = "/static/pdf/" + settlementDomain.getMon() + "/BankCollectDetail/" + pdfFileName;
        String returnUrl = "report" + filePath;
        if(settlementDomain.getIsBankCollection()!=null && settlementDomain.getIsBankCollection()==1){
            settlementDomain.setChargeModeType((byte)5);
        }else{
            settlementDomain.setUnChargeModeType((byte)5);
        }
        settlementDomain.setPageSize(-1);
        List<TableDataBean> tableDataList = meterMoneyService.findBankCollectDetail(settlementDomain);
        JRDataSource jrDataSource = new JRBeanCollectionDataSource(tableDataList);
        Map<String, Object> m = new HashMap<>();
        m.put("query", "其他参数测试");
        ClassPathResource cpr = new ClassPathResource("static/jasperreport/CollectionList.jasper");
        InputStream is = cpr.getInputStream();
        File file = File.createTempFile("CollectionList", "jasper");
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


    //托收清单
    @ResponseBody
    @RequestMapping("findBankCollectDetailToExcel")
    public Object findBankCollectDetailToExcel(@RequestBody String json) throws IOException, JRException {

        SettlementDomain settlementDomain = GsonUtils.readValue(json,SettlementDomain.class);
        if(settlementDomain.getMon()<Integer.valueOf(Global.getConfig("mon"))){
            return new HttpResult(HttpResult.ERROR, Global.getConfig("mon")+
                    "之前的月份请在老系统统计");
        }
        String pdfFileName = "";
        if (settlementDomain.getSettlementNo() != null) {
            pdfFileName =
                    settlementDomain.getMon()+"-"  +
                            "BankCollectDetail-SettlementNo" + settlementDomain.getSettlementNo()+ ".xls";
        }
        if (settlementDomain.getBankNo() != null) {
            pdfFileName = settlementDomain.getMon()+"-"  +
                    "BankCollectDetail-BankNo" + settlementDomain.getBankNo()+ ".xls";
        }
        if (settlementDomain.getBusinessPlaceCode() != null) {
            pdfFileName = settlementDomain.getMon()+"-"  +
                    "BankCollectDetail-BusinessPlaceCode" + settlementDomain.getBusinessPlaceCode()+ ".pdf";
        }
        String filePath = "/static/xls/" + settlementDomain.getMon() + "/BankCollectDetail/" + pdfFileName;
        String returnUrl = "report" + filePath;
        if(settlementDomain.getIsBankCollection()!=null && settlementDomain.getIsBankCollection()==1){
            settlementDomain.setChargeModeType((byte)5);
        }else{
            settlementDomain.setUnChargeModeType((byte)5);
        }
        settlementDomain.setPageSize(-1);
        List<TableDataBean> tableDataList = meterMoneyService.findBankCollectDetail(settlementDomain);
        JRDataSource jrDataSource = new JRBeanCollectionDataSource(tableDataList);
        Map<String, Object> m = new HashMap<>();
        m.put("query", "其他参数测试");
        ClassPathResource cpr = new ClassPathResource("static/jasperreport/CollectionList.jasper");
        InputStream is = cpr.getInputStream();
        File file = File.createTempFile("CollectionList", "jasper");
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


    //大工业用户电费明细
    @ResponseBody
    @RequestMapping("bigIndustryMeterMoneyDetailReport")
    public Object bigIndustryMeterMoneyDetailReport(@RequestBody String json) throws IOException, JRException {
        SettlementDomain settlementDomain = GsonUtils.readValue(json, SettlementDomain.class);
        String pdfFileName = "";
        if (settlementDomain.getSettlementNo() != null) {
            pdfFileName =
                    settlementDomain.getStartMon() +"_"+ settlementDomain.getEndMon()+
                    "bigIndustryMeterMoneyDetailReport" + settlementDomain.getSettlementNo()  + ".pdf";
        }
        if (settlementDomain.getSettlementName() != null) {
            pdfFileName = settlementDomain.getStartMon() +"_"+ settlementDomain.getEndMon()+
                    "bigIndustryMeterMoneyDetailReport" + settlementDomain.getSettlementName()  + ".pdf";
        }
        String filePath =
                "/static/pdf/bigIndustryMeterMoneyDetailReport/" + DateUtil.getDate("yyyyMM") + "/" + pdfFileName;

        //重新统计
        String returnUrl = "report" + filePath;
        List<TableDataBean> tableDataList = meterMoneyService.bigIndustryMeterMoneyDetailReport(settlementDomain);
        JRDataSource jrDataSource = new JRBeanCollectionDataSource(tableDataList);
        Map<String, Object> m = new HashMap<>();
        m.put("query", "其他参数测试");
        ClassPathResource cpr = new ClassPathResource("static/jasperreport/bigIndustryMeterMoneyDetailReport.jasper");
        InputStream is = cpr.getInputStream();
        File file = File.createTempFile("MeterMoney", "jasper");
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

    //粤桂分时电价查询
    @ResponseBody
    @RequestMapping("ygTimeOfUseQuery")
    public Object ygTimeOfUseQuery(@RequestBody String json) throws IOException, JRException {
        SettlementDomain settlementDomain = GsonUtils.readValue(json, SettlementDomain.class);
        String pdfFileName = "ygTimeOfUseQuery"  + Calendar.getInstance().getTimeInMillis() + ".pdf";
        String filePath =
                "/static/pdf/" + DateUtil.getDate("yyyyMM") +
                        "/ygTimeOfUseQuery/" + pdfFileName;

        //重新统计
        String returnUrl = "report" + filePath;
        List<TableDataBean> tableDataList = meterMoneyService.ygTimeOfUseQuery(settlementDomain);
        JRDataSource jrDataSource = new JRBeanCollectionDataSource(tableDataList);
        Map<String, Object> m = new HashMap<>();
        m.put("query", "其他参数测试");
        ClassPathResource cpr = new ClassPathResource("static/jasperreport/ygTimeOfUseQuery.jasper");
        InputStream is = cpr.getInputStream();
        File file = File.createTempFile("ygTimeOfUseQuery", "jasper");
        try {
            FileUtils.copyInputStreamToFile(is, file);
        } finally {
            IOUtils.closeQuietly(is);
        }
        JasperHelper jasperHelper = new JasperHelper();
        Boolean result = jasperHelper.exportPdf(filePath, file.getPath(), m,
                jrDataSource);
        if (result.equals(true)) {
            return new HttpResult(HttpResult.SUCCESS, "执行成功", returnUrl);
        } else {
            return new HttpResult(HttpResult.ERROR, "执行失败");
        }
    }

    //粤桂分时电价查询
    @ResponseBody
    @RequestMapping("ygTimeOfUseQueryToExcel")
    public Object ygTimeOfUseQueryToExcel(@RequestBody String json) throws IOException, JRException {
        SettlementDomain settlementDomain = GsonUtils.readValue(json, SettlementDomain.class);
        String pdfFileName =
                "ygTimeOfUseQuery"  + Calendar.getInstance().getTimeInMillis() + ".xls";
        String filePath =
                "/static/xls/" + DateUtil.getDate("yyyyMM") +
                        "/ygTimeOfUseQuery/" + pdfFileName;

        //重新统计
        String returnUrl = "report" + filePath;
        List<TableDataBean> tableDataList = meterMoneyService.ygTimeOfUseQuery(settlementDomain);
        JRDataSource jrDataSource = new JRBeanCollectionDataSource(tableDataList);
        Map<String, Object> m = new HashMap<>();
        m.put("query", "其他参数测试");
        ClassPathResource cpr = new ClassPathResource("static/jasperreport/ygTimeOfUseQuery.jasper");
        InputStream is = cpr.getInputStream();
        File file = File.createTempFile("ygTimeOfUseQuery", "jasper");
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

    //电量电费排行
    @ResponseBody
    @RequestMapping("electricityTariffRankQuery")
    public Object electricityTariffRankQuery(@RequestBody String json) throws IOException, JRException {
        ElectricityTariffRankEntity electricityTariffRankEntity = GsonUtils.readValue(json,  ElectricityTariffRankEntity.class);
        String pdfFileName = "electricityTariffRankQuery"  + Calendar.getInstance().getTimeInMillis() + ".pdf";
        String filePath =
                "/static/pdf/" + DateUtil.getDate("yyyyMM") +
                        "/electricityTariffRankQuery/" + pdfFileName;

        String returnUrl = "report" + filePath;
        List<TableDataBean> tableDataList = meterMoneyService.electricityTariffRankQuery(electricityTariffRankEntity);
        JRDataSource jrDataSource = new JRBeanCollectionDataSource(tableDataList);
        Map<String, Object> m = new HashMap<>();
        m.put("query", "其他参数测试");
        ClassPathResource cpr = new ClassPathResource("static/jasperreport" +
                "/ElectricityTariffRank.jasper");
        InputStream is = cpr.getInputStream();
        File file = File.createTempFile("ElectricityTariffRank", "jasper");
        try {
            FileUtils.copyInputStreamToFile(is, file);
        } finally {
            IOUtils.closeQuietly(is);
        }
        JasperHelper jasperHelper = new JasperHelper();
        Boolean result = jasperHelper.exportPdf(filePath, file.getPath(), m,
                jrDataSource);
        if (result.equals(true)) {
            return new HttpResult(HttpResult.SUCCESS, "执行成功", returnUrl);
        } else {
            return new HttpResult(HttpResult.ERROR, "执行失败");
        }
    }


    //电量电费排行
    @ResponseBody
    @RequestMapping("electricityTariffRankQueryToExcel")
    public Object electricityTariffRankQueryToExcel(@RequestBody String json) throws IOException, JRException {
        ElectricityTariffRankEntity electricityTariffRankEntity = GsonUtils.readValue(json,  ElectricityTariffRankEntity.class);
        String pdfFileName = "electricityTariffRankQuery"  + Calendar.getInstance().getTimeInMillis() + ".xls";
        String filePath =
                "/static/xls/" + DateUtil.getDate("yyyyMM") +
                        "/electricityTariffRankQuery/" + pdfFileName;

        String returnUrl = "report" + filePath;
        List<TableDataBean> tableDataList = meterMoneyService.electricityTariffRankQuery(electricityTariffRankEntity);
        JRDataSource jrDataSource = new JRBeanCollectionDataSource(tableDataList);
        Map<String, Object> m = new HashMap<>();
        m.put("query", "其他参数测试");
        ClassPathResource cpr = new ClassPathResource("static/jasperreport" +
                "/ElectricityTariffRank.jasper");
        InputStream is = cpr.getInputStream();
        File file = File.createTempFile("ElectricityTariffRank", "jasper");
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



    //电费明细报表
    @ResponseBody
    @RequestMapping("electricityChargeDetail")
    public Object electricityChargeDetail(@RequestBody String json) throws Exception {

        SettlementDomain settlementDomain= JSONObject.parseObject(json, SettlementDomain.class);

        if(settlementDomain.getSettlementNo()==null || "".equals(settlementDomain.getSettlementNo())){
            return new HttpResult(HttpResult.ERROR, "该记录无结算户号");
        }

        String pdfFileName =
                "electricityChargeDetail"  + Calendar.getInstance().getTimeInMillis() + ".pdf";

        String filePath =
                "/static/pdf/" + DateUtil.getDate("yyyyMM") +
                        "/electricityChargeDetail/" + pdfFileName;

        String returnUrl = "report" + filePath;

        List<TableDataBean> tableDataList = meterMoneyService.electricityChargeDetail(settlementDomain);

        if (tableDataList == null || tableDataList.size() < 1) {
            return new HttpResult(HttpResult.ERROR, "无电费明细数据");
        }
        JasperHelper jasperHelper = new JasperHelper();
        Boolean result = jasperHelper.exportPdfs(filePath, tableDataList,
                "ElectricityChargeDetail");
        if (result.equals(true)) {
            return new HttpResult(HttpResult.SUCCESS, "执行成功", returnUrl);
        } else {
            return new HttpResult(HttpResult.ERROR, "执行失败");
        }
    }
}
