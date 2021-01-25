package org.fms.report.server.webapp.controller;

import static java.util.stream.Collectors.toList;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.commons.io.FileUtils;
import org.apache.poi.util.IOUtils;
import org.fms.report.common.util.FileUtil;
import org.fms.report.common.util.MonUtils;
import org.fms.report.common.webapp.bean.TableDataBean;
import org.fms.report.common.webapp.domain.ArrearageDomain;
import org.fms.report.common.webapp.domain.BankCollectionEntity;
import org.fms.report.common.webapp.returnDomain.SettlementBean;
import org.fms.report.common.webapp.returnDomain.UserInfoEntity;
import org.fms.report.server.utils.JasperHelper;
import org.fms.report.server.webapp.service.ArrearageService;
import org.fms.report.server.webapp.service.CimService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.system.ApplicationHome;
import org.springframework.core.io.ClassPathResource;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.riozenc.titanTool.common.date.DateUtil;
import com.riozenc.titanTool.common.json.utils.GsonUtils;
import com.riozenc.titanTool.spring.web.http.HttpResult;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

@ControllerAdvice
@RequestMapping(value = "Arrearage")
public class ArrearageController {
    @Autowired
    private ArrearageService arrearageService;
    @Autowired
    private CimService cimService;

    // 客户欠费汇总pdf预览
    @ResponseBody
    @RequestMapping(value = "/SummaryShow")
    public Object summaryShow(@RequestBody String json) throws IOException,
            JRException {
        ArrearageDomain arrearageDomain = GsonUtils.readValue(json, ArrearageDomain.class);
        arrearageDomain.setIsSettle(0);
        String pdfFileName = "";
        if (arrearageDomain.getWritorId() != null) {
            pdfFileName = arrearageDomain.getStartMon() + "-" + arrearageDomain.getEndMon() + "ArrearageSum-Writor" + arrearageDomain.getWritorId() + "-" + arrearageDomain.getGroupBy() + ".pdf";
        }
        if (arrearageDomain.getWriteSectId() != null) {
            pdfFileName = arrearageDomain.getStartMon() + "-" + arrearageDomain.getEndMon() + "ArrearageSum-WriteSec" + arrearageDomain.getWriteSectId() + "-" + arrearageDomain.getGroupBy() + ".pdf";
        }
        if (arrearageDomain.getBusinessPlaceCode() != null) {
            pdfFileName = arrearageDomain.getStartMon() + "-" + arrearageDomain.getEndMon() + "ArrearageSum-Dept" + arrearageDomain.getBusinessPlaceCode() + "-" + arrearageDomain.getGroupBy() + ".pdf";
        }

        String filePath = "/static/pdf/" + DateUtil.getDate("yyyyMM") + "/" + pdfFileName;
        String returnUrl = "report" + filePath;

       /* if(arrearageDomain.getStartMon()<Integer.valueOf(Global.getConfig("mon"))){
            return new HttpResult(HttpResult.ERROR, Global.getConfig("mon")+
                    "之前的月份请在老系统统计");
        }*/

        if (!arrearageDomain.getAgainStat()) {
            ApplicationHome h = new ApplicationHome(getClass());
            File jarF = h.getSource();
            String baseUrl = FileUtil.getSaveFilePath();
            File file = new File(baseUrl + filePath);
            if (file.exists()) {
                return new HttpResult<String>(HttpResult.SUCCESS, "执行成功", returnUrl);
            }else{
                return new HttpResult<String>(HttpResult.ERROR, "未统计过的报表请选中重新统计",null);
            }
        }
        List<TableDataBean> tableDataList = arrearageService.summary(arrearageDomain);
        JRDataSource jrDataSource = new JRBeanCollectionDataSource(tableDataList);
        Map<String, Object> m = new HashMap<>();
        m.put("query", "其他参数测试");
        ClassPathResource cpr = new ClassPathResource("static/jasperreport/ArrearageSum.jasper");
        InputStream is = cpr.getInputStream();
        File file = File.createTempFile("ArrearageSum", "jasper");
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

    // 客户欠费汇总excel
    @ResponseBody
    @RequestMapping(value = "/SummaryExcel")
    public Object summaryExcel(@RequestBody String json) throws IOException, JRException {
        ArrearageDomain arrearageDomain = GsonUtils.readValue(json, ArrearageDomain.class);
        arrearageDomain.setIsSettle(0);
        String pdfFileName = "";
        if (arrearageDomain.getWritorId() != null) {
            pdfFileName =
                    arrearageDomain.getStartMon() + "-" + arrearageDomain.getEndMon() + "ArrearageSum-Writor" + arrearageDomain.getWritorId() + "-" + arrearageDomain.getGroupBy() + ".xls";
        }
        if (arrearageDomain.getWriteSectId() != null) {
            pdfFileName = arrearageDomain.getStartMon() + "-" + arrearageDomain.getEndMon() + "ArrearageSum-WriteSec" + arrearageDomain.getWriteSectId() + "-" + arrearageDomain.getGroupBy() + ".xls";
        }
        if (arrearageDomain.getBusinessPlaceCode() != null) {
            pdfFileName = arrearageDomain.getStartMon() + "-" + arrearageDomain.getEndMon() + "ArrearageSum-Dept" + arrearageDomain.getBusinessPlaceCode() + "-" + arrearageDomain.getGroupBy() + ".xls";
        }

        String filePath = "/static/xls/" + DateUtil.getDate("yyyyMM") + "/" + pdfFileName;
        String returnUrl = "report" + filePath;

        if (!arrearageDomain.getAgainStat()) {
            ApplicationHome h = new ApplicationHome(getClass());
            String baseUrl = FileUtil.getSaveFilePath();
            File file = new File(baseUrl + filePath);
            if (file.exists()) {
                return new HttpResult<String>(HttpResult.SUCCESS, "执行成功", returnUrl);
            }else{
                return new HttpResult<String>(HttpResult.ERROR, "未统计过的报表请选中重新统计",null);
            }
        }
        List<TableDataBean> tableDataList = arrearageService.summary(arrearageDomain);
        JRDataSource jrDataSource = new JRBeanCollectionDataSource(tableDataList);
        Map<String, Object> m = new HashMap<>();
        m.put("query", "其他参数测试");
        ClassPathResource cpr = new ClassPathResource("static/jasperreport/ArrearageSum.jasper");
        InputStream is = cpr.getInputStream();
        File file = File.createTempFile("ArrearageSum", "jasper");
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

    // 通知单
    @ResponseBody
    @RequestMapping(value = "/SettlementNoticeBySect")
    public Object settlementNotice(@RequestBody String json) throws IOException, JRException {

        JSONObject jsonObject = JSONObject.parseObject(json);

        UserInfoEntity userInfoEntity =
                JSONObject.parseObject(jsonObject.getString("userInfo"),
                        UserInfoEntity.class);
        //月份
        Integer mon = jsonObject.getInteger("mon");
        //分组方式
        String searchType = jsonObject.getString("searchType");

        List<ArrearageDomain> arrearageDomainList =
                JSONObject.parseArray(jsonObject.getString("data"), ArrearageDomain.class);

        //文件名规则 最小抄表区段id_长度_最大抄表区段id
        Optional<ArrearageDomain> maxArrearageDomains =
                arrearageDomainList.stream().max(Comparator.comparingLong(ArrearageDomain::getId));
        ArrearageDomain maxArrearageDomain = maxArrearageDomains.get();

        Optional<ArrearageDomain> minArrearageDomains =
                arrearageDomainList.stream().max(Comparator.comparingLong(ArrearageDomain::getId));
        ArrearageDomain minArrearageDomain = minArrearageDomains.get();

        ArrearageDomain arrearageDomain = new ArrearageDomain();

        //id为writesectId对象待修改
        arrearageDomain.setWriteSectIds(arrearageDomainList.stream().filter(a -> a.getId() != null).map(ArrearageDomain::getId).collect(Collectors.toList()));
        arrearageDomain.setMon(mon);
        arrearageDomain.setIsSettle(0);
        arrearageDomain.setAgainStat(true);
        arrearageDomain.setGroupBy(searchType);

        String pdfFileName =
                "SettlementNoticeBySect-WriteSec" + minArrearageDomain.getId() + "_"
                        + arrearageDomainList.size() + "_"
                        + maxArrearageDomain.getId() + "-"
                        + arrearageDomain.getGroupBy() + String.valueOf(new Date().getTime()) + ".pdf";


        String filePath =
                "/static/pdf/" + mon + "/SettlementNoticeBySect/" + pdfFileName;
        String returnUrl = "report" + filePath;
        //赋值操作员
        arrearageDomain.setOperatorName(userInfoEntity.getUserName());
        List<TableDataBean> tableDataList = arrearageService.settlementNotice(arrearageDomain);
        if (tableDataList == null || tableDataList.size() < 1) {
            return new HttpResult(HttpResult.ERROR, "无欠费数据");
        }
        JasperHelper jasperHelper = new JasperHelper();
        Boolean result = jasperHelper.exportPdfs(filePath, tableDataList, "Notice");
        if (result.equals(true)) {
            return new HttpResult(HttpResult.SUCCESS, "执行成功", returnUrl);
        } else {
            return new HttpResult(HttpResult.ERROR, "执行失败");
        }
    }


    // 通知单
    @ResponseBody
    @RequestMapping(value = "/SettlementNoticeBySettle")
    public Object SettlementNoticeBySettle(@RequestBody String json) throws IOException, JRException {
        JSONObject jsonObject = JSONObject.parseObject(json);
        //月份
        Integer mon = jsonObject.getInteger("mon");
        //分组方式
        String searchType = jsonObject.getString("searchType");

        UserInfoEntity userInfoEntity =
                JSONObject.parseObject(jsonObject.getString("userInfo"),
                        UserInfoEntity.class);

        List<SettlementBean> settlementBeans =
                JSONObject.parseArray(jsonObject.getString("data"), SettlementBean.class);

        //文件名规则 最小抄表区段id_长度_最大抄表区段id
        Optional<SettlementBean> maxSettlements =
                settlementBeans.stream().max(Comparator.comparingLong(SettlementBean::getId));
        SettlementBean maxArrearageDomain = maxSettlements.get();

        Optional<SettlementBean> minSettlements =
                settlementBeans.stream().max(Comparator.comparingLong(SettlementBean::getId));
        SettlementBean minArrearageDomain = minSettlements.get();

        ArrearageDomain arrearageDomain = new ArrearageDomain();

        //id为writesectId对象待修改
        arrearageDomain.setSettlementIds(settlementBeans.stream().filter(a -> a.getId() != null).map(SettlementBean::getId).collect(Collectors.toList()));
        arrearageDomain.setMon(mon);
        //arrearageDomain.setIsSettle(0);
        arrearageDomain.setAgainStat(true);
        arrearageDomain.setGroupBy(searchType);

        String pdfFileName =
                "SettlementNoticeBySect-Settle" + minArrearageDomain.getId() + "_"
                        + settlementBeans.size() + "_"
                        + maxArrearageDomain.getId() + "-"
                        + arrearageDomain.getGroupBy() + String.valueOf(new Date().getTime()) + ".pdf";

        String filePath =
                "/static/pdf/" + mon + "/SettlementNoticeBySettle/" + pdfFileName;

        String returnUrl = "report" + filePath;

        //赋值操作员
        arrearageDomain.setOperatorName(userInfoEntity.getUserName());
        List<TableDataBean> tableDataList = arrearageService.settlementNotice(arrearageDomain);
        if (tableDataList == null || tableDataList.size() < 1) {
            return new HttpResult(HttpResult.ERROR, "无欠费数据");
        }
        JasperHelper jasperHelper = new JasperHelper();
        Boolean result = jasperHelper.exportPdfs(filePath, tableDataList, "Notice");
        if (result.equals(true)) {
            return new HttpResult(HttpResult.SUCCESS, "执行成功", returnUrl);
        } else {
            return new HttpResult(HttpResult.ERROR, "执行失败");
        }
    }


    //银行托收
    @ResponseBody
    @RequestMapping(value = "/bankCollection")
    public Object bankCollection(@RequestBody String json) throws IOException, JRException {
        JSONObject jsonObject=JSONObject.parseObject(json);

        UserInfoEntity userInfoEntity =
                JSONObject.parseObject(jsonObject.getString("userInfo"),
                        UserInfoEntity.class);

        BankCollectionEntity bankCollectionEntity =
                JSONObject.parseObject(jsonObject.getString("data"),
                        BankCollectionEntity.class);

        String pdfFileName =
                "BankCollection-"
                        + userInfoEntity.getUserId() + "_" + String.valueOf(new Date().getTime()) + ".pdf";

        String filePath =
                "/static/pdf/" + bankCollectionEntity.getMon() + "/BankCollection/" + pdfFileName;

        String returnUrl = "report" + filePath;

        List<TableDataBean> bankCollectBeans =
                arrearageService.bankCollection(bankCollectionEntity);

       // bankCollectBeans=bankCollectBeans.subList(0,1);

        bankCollectBeans=
                bankCollectBeans.stream().sorted(Comparator.comparing(TableDataBean::getSettlementNo)).collect(toList());

        if (bankCollectBeans == null || bankCollectBeans.size() < 1) {
            return new HttpResult(HttpResult.ERROR, "无托收欠费数据");
        }
        JasperHelper jasperHelper = new JasperHelper();
        Boolean result = jasperHelper.exportPdfs(filePath, bankCollectBeans, "BankCollection");
        if (result.equals(true)) {
            return new HttpResult(HttpResult.SUCCESS, "执行成功", returnUrl);
        } else {
            return new HttpResult(HttpResult.ERROR, "执行失败");
        }
    }


    // 客户欠费明细报表
    @ResponseBody
    @RequestMapping(value = "/queryArrearageDetail")
    public Object queryArrearageDetail(@RequestBody String json) throws IOException, JRException {
        ArrearageDomain arrearageDomain = GsonUtils.readValue(json, ArrearageDomain.class);
        String pdfFileName = "";
        if (arrearageDomain.getWritorId() != null) {
            pdfFileName =
                    arrearageDomain.getStartMon() + "-" + arrearageDomain.getEndMon() + "ArrearageDetail-Writor" + arrearageDomain.getWritorId() + "-" + arrearageDomain.getGroupBy() + ".pdf";
        }
        if (arrearageDomain.getWriteSectId() != null) {
            pdfFileName = arrearageDomain.getStartMon() + "-" + arrearageDomain.getEndMon() + "ArrearageDetail-WriteSec" + arrearageDomain.getWriteSectId() + "-" + arrearageDomain.getGroupBy() + ".pdf";
        }
        if (arrearageDomain.getBusinessPlaceCode() != null) {
            pdfFileName = arrearageDomain.getStartMon() + "-" + arrearageDomain.getEndMon() + "ArrearageDetail-Dept" + arrearageDomain.getBusinessPlaceCode() + "-" + arrearageDomain.getGroupBy() + ".pdf";
        }

        String filePath =
                "/static/pdf/ArrearageDetail/" + pdfFileName;

        String returnUrl = "report" + filePath;
        if (!arrearageDomain.getAgainStat()) {
            ApplicationHome h = new ApplicationHome(getClass());
            String baseUrl = FileUtil.getSaveFilePath();
            File file = new File(baseUrl + filePath);
            if (file.exists()) {
                return new HttpResult<>(HttpResult.SUCCESS, "执行成功", returnUrl);
            }else{
                return new HttpResult<String>(HttpResult.ERROR, "未统计过的报表请选中重新统计",null);
            }
        }
        arrearageDomain.setPageSize(-1);
        List<TableDataBean> tableDataList =
                arrearageService.arrearageDetail(arrearageDomain);
        JRDataSource jrDataSource = new JRBeanCollectionDataSource(tableDataList);
        Map<String, Object> m = new HashMap<>();
        m.put("query", "其他参数测试");
        ClassPathResource cpr = new ClassPathResource("static/jasperreport/ArrearageDetail.jasper");
        InputStream is = cpr.getInputStream();
        File file = File.createTempFile("ArrearageDetail", "jasper");
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


    //打印催费通知单
    @ResponseBody
    @RequestMapping(value = "/printReminderNotice")
    public Object printReminderNotice(@RequestBody String json) throws IOException, JRException {
        List<TableDataBean> tableDataList = JSONObject.parseArray(json, TableDataBean.class);
        TableDataBean tableDataBean=tableDataList.get(0);
        String pdfFileName =
                    "printReminderNotice-Settlement" +Calendar.getInstance().getTimeInMillis() +".pdf";

        tableDataList.forEach(t->{
            t.setPrintDate(MonUtils.getDay());
        });

        String filePath =
                "/static/pdf/printReminderNotice/" + pdfFileName;
        String returnUrl = "report" + filePath;

        if (tableDataList == null || tableDataList.size() < 1) {
            return new HttpResult(HttpResult.ERROR, "无欠费数据");
        }
        JasperHelper jasperHelper = new JasperHelper();
        Boolean result = jasperHelper.exportPdfs(filePath, tableDataList, "PrintReminderNotice");
        if (result.equals(true)) {
            return new HttpResult(HttpResult.SUCCESS, "执行成功", returnUrl);
        } else {
            return new HttpResult(HttpResult.ERROR, "执行失败");
        }
    }
}
