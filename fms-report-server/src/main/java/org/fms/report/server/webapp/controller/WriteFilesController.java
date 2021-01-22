package org.fms.report.server.webapp.controller;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.apache.poi.util.IOUtils;
import org.fms.report.common.util.FileUtil;
import org.fms.report.common.webapp.bean.TableDataBean;
import org.fms.report.common.webapp.domain.MeterDomain;
import org.fms.report.common.webapp.domain.WriteFilesDomain;
import org.fms.report.server.utils.JasperHelper;
import org.fms.report.server.webapp.service.DeptService;
import org.fms.report.server.webapp.service.WriteFilesService;
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
@RequestMapping(value = "WriteFiles")
public class WriteFilesController {

    @Autowired
    private WriteFilesService writeFilesService;

    @Autowired
    private DeptService deptService;

    // 实抄率pdf预览
    @ResponseBody
    @RequestMapping(value = "/RealShow")
    public Object realShow(@RequestBody String json) throws Exception {
        WriteFilesDomain writeFiles = GsonUtils.readValue(json, WriteFilesDomain.class);
        String pdfFileName = "";
        if (writeFiles.getWritorId() != null) {
            pdfFileName = writeFiles.getMon() + "WriteFiles-Writor" + writeFiles.getWritorId() + ".pdf";
        }
        if (writeFiles.getWriteSectionId() != null) {
            pdfFileName = writeFiles.getMon() + "WriteFiles-WriteSec" + writeFiles.getWriteSectionId() + ".pdf";
        }
        if (writeFiles.getBusinessPlaceCode() != null) {
            pdfFileName = writeFiles.getMon() + "WriteFiles-Dept" + writeFiles.getBusinessPlaceCode() + ".pdf";
        }
        String filePath = "/static/pdf/" + DateUtil.getDate("yyyyMM") + "/" + pdfFileName;
        String returnUrl = "report" + filePath;
        if (!writeFiles.getAgainStat()) {
            ApplicationHome h = new ApplicationHome(getClass());
            String baseUrl = FileUtil.getSaveFilePath();
            File file = new File(baseUrl + filePath);
            if (file.exists()) {
                return new HttpResult(HttpResult.SUCCESS, "执行成功", returnUrl);
            }else{
                return new HttpResult<String>(HttpResult.ERROR, "未统计过的报表请选中重新统计",null);
            }
        }
        List<TableDataBean> tableDataList = writeFilesService.realShow(writeFiles);
        JRDataSource jrDataSource = new JRBeanCollectionDataSource(tableDataList);
        Map<String, Object> m = new HashMap<>();
        m.put("query", "其他参数测试");
        ClassPathResource cpr = new ClassPathResource("static/jasperreport/RealWrite.jasper");
        InputStream is = cpr.getInputStream();
        File file = File.createTempFile("RealWrite", "jasper");
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

    // 实抄率excel
    @ResponseBody
    @RequestMapping(value = "/RealExcel")
    public Object realExcel(@RequestBody String json) throws Exception {
        WriteFilesDomain writeFiles = GsonUtils.readValue(json, WriteFilesDomain.class);
        String pdfFileName = "";
        if (writeFiles.getWritorId() != null) {
            pdfFileName = writeFiles.getMon() + "WriteFiles-Writor" + writeFiles.getWritorId() + ".xls";
        }
        if (writeFiles.getWriteSectionId() != null) {
            pdfFileName = writeFiles.getMon() + "WriteFiles-WriteSec" + writeFiles.getWriteSectionId() + ".xls";
        }
        if (writeFiles.getBusinessPlaceCode() != null) {
            pdfFileName = writeFiles.getMon() + "WriteFiles-Dept" + writeFiles.getBusinessPlaceCode() + ".xls";
        }
        String filePath = "/static/excel/" + DateUtil.getDate("yyyyMM") + "/" + pdfFileName;
        String returnUrl = "report" + filePath;
        if (!writeFiles.getAgainStat()) {
            ApplicationHome h = new ApplicationHome(getClass());
            String baseUrl = FileUtil.getSaveFilePath();
            File file = new File(baseUrl + filePath);
            if (file.exists()) {
                return new HttpResult(HttpResult.SUCCESS, "执行成功", returnUrl);
            }else{
                return new HttpResult<String>(HttpResult.ERROR, "未统计过的报表请选中重新统计",null);
            }
        }
        List<TableDataBean> tableDataList = writeFilesService.realShow(writeFiles);

        JRDataSource jrDataSource = new JRBeanCollectionDataSource(tableDataList);
        Map<String, Object> m = new HashMap<>();
        m.put("query", "其他参数测试");
        ClassPathResource cpr = new ClassPathResource("static/jasperreport/RealWrite.jasper");
        InputStream is = cpr.getInputStream();
        File file = File.createTempFile("RealWrite", "jasper");
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

    // 划零户pdf预览
    @ResponseBody
    @RequestMapping(value = "/ZeroShow")
    public Object zeroShow(@RequestBody String json) throws JRException, IOException {
        WriteFilesDomain writeFiles = GsonUtils.readValue(json, WriteFilesDomain.class);
        writeFiles.setDiffNum(BigDecimal.ZERO);
        String pdfFileName = "";
        if (writeFiles.getWritorId() != null) {
            pdfFileName = writeFiles.getMon() + "Zero-Writor" + writeFiles.getWritorId() + ".pdf";
        }
        if (writeFiles.getWriteSectionId() != null) {
            pdfFileName = writeFiles.getMon() + "Zero-WriteSec" + writeFiles.getWriteSectionId() + ".pdf";
        }
        if (writeFiles.getBusinessPlaceCode() != null) {
            pdfFileName = writeFiles.getMon() + "Zero-Dept" + writeFiles.getBusinessPlaceCode() + ".pdf";
        }
        String filePath = "/static/pdf/" + DateUtil.getDate("yyyyMM") + "/" + pdfFileName;
        String returnUrl = "report" + filePath;
        if (!writeFiles.getAgainStat()) {
            ApplicationHome h = new ApplicationHome(getClass());
            String baseUrl = FileUtil.getSaveFilePath();
            File file = new File(baseUrl + filePath);
            if (file.exists()) {
                return new HttpResult(HttpResult.SUCCESS, "执行成功", returnUrl);
            }else{
                return new HttpResult<String>(HttpResult.ERROR, "未统计过的报表请选中重新统计",null);
            }
        }
        List<TableDataBean> tableDataList = writeFilesService.findWriteFiles(writeFiles);
        JRDataSource jrDataSource = new JRBeanCollectionDataSource(tableDataList);
        Map<String, Object> m = new HashMap<>();
        m.put("query", "其他参数测试");
        ClassPathResource cpr = new ClassPathResource("static/jasperreport/Zero.jasper");
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

    // 划零户 excel
    @ResponseBody
    @RequestMapping(value = "/ZeroExcel")
    public Object zeroExcel(@RequestBody String json) throws JRException, IOException {
        WriteFilesDomain writeFiles = GsonUtils.readValue(json, WriteFilesDomain.class);
        writeFiles.setDiffNum(BigDecimal.ZERO);
        String pdfFileName = "";
        if (writeFiles.getWritorId() != null) {
            pdfFileName = writeFiles.getMon() + "Zero-Writor" + writeFiles.getWritorId() + ".xls";
        }
        if (writeFiles.getWriteSectionId() != null) {
            pdfFileName = writeFiles.getMon() + "Zero-WriteSec" + writeFiles.getWriteSectionId() + ".xls";
        }
        if (writeFiles.getBusinessPlaceCode() != null) {
            pdfFileName = writeFiles.getMon() + "Zero-Dept" + writeFiles.getBusinessPlaceCode() + ".xls";
        }
        String filePath = "/static/excel/" + DateUtil.getDate("yyyyMM") + "/" + pdfFileName;
        String returnUrl = "report" + filePath;
        if (!writeFiles.getAgainStat()) {
            ApplicationHome h = new ApplicationHome(getClass());
            String baseUrl =FileUtil.getSaveFilePath();
            File file = new File(baseUrl + filePath);
            if (file.exists()) {
                return new HttpResult(HttpResult.SUCCESS, "执行成功", returnUrl);
            }else{
                return new HttpResult<String>(HttpResult.ERROR, "未统计过的报表请选中重新统计",null);
            }
        }
        List<TableDataBean> tableDataList = writeFilesService.findWriteFiles(writeFiles);

        JRDataSource jrDataSource = new JRBeanCollectionDataSource(tableDataList);
        Map<String, Object> m = new HashMap<>();
        m.put("query", "其他参数测试");
        ClassPathResource cpr = new ClassPathResource("static/jasperreport/Zero.jasper");
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

    // 未抄表户pdf预览
    @ResponseBody
    @RequestMapping(value = "/UnwriteShow")
    public Object unwriteShow(@RequestBody String json) throws JRException, IOException {
        WriteFilesDomain writeFiles = GsonUtils.readValue(json, WriteFilesDomain.class);
        writeFiles.setWriteFlag((byte) 0);
        String pdfFileName = "";
        if (writeFiles.getWritorId() != null) {
            pdfFileName = writeFiles.getMon() + "Unwrite-Writor" + writeFiles.getWritorId() + ".pdf";
        }
        if (writeFiles.getWriteSectionId() != null) {
            pdfFileName = writeFiles.getMon() + "Unwrite-WriteSec" + writeFiles.getWriteSectionId() + ".pdf";
        }
        if (writeFiles.getBusinessPlaceCode() != null) {
            pdfFileName = writeFiles.getMon() + "Unwrite-Dept" + writeFiles.getBusinessPlaceCode() + ".pdf";
        }
        String filePath = "/static/pdf/" + DateUtil.getDate("yyyyMM") + "/" + pdfFileName;
        String returnUrl = "report" + filePath;
        if (!writeFiles.getAgainStat()) {
            ApplicationHome h = new ApplicationHome(getClass());
            String baseUrl = FileUtil.getSaveFilePath();
            File file = new File(baseUrl + filePath);
            if (file.exists()) {
                return new HttpResult(HttpResult.SUCCESS, "执行成功", returnUrl);
            }else{
                return new HttpResult<String>(HttpResult.ERROR, "未统计过的报表请选中重新统计",null);
            }
        }
        List<TableDataBean> tableDataList = writeFilesService.findWriteFiles(writeFiles);

        JRDataSource jrDataSource = new JRBeanCollectionDataSource(tableDataList);
        Map<String, Object> m = new HashMap<>();
        m.put("query", "其他参数测试");
        ClassPathResource cpr = new ClassPathResource("static/jasperreport/Unwrite.jasper");
        InputStream is = cpr.getInputStream();
        File file = File.createTempFile("Unwrite", "jasper");
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

    // 未抄表户excel
    @ResponseBody
    @RequestMapping(value = "/UnwriteExcel")
    public Object unwriteExcel(@RequestBody String json) throws JRException, IOException {
        WriteFilesDomain writeFiles = GsonUtils.readValue(json, WriteFilesDomain.class);
        writeFiles.setWriteFlag((byte) 0);
        String pdfFileName = "";
        if (writeFiles.getWritorId() != null) {
            pdfFileName = writeFiles.getMon() + "Unwrite-Writor" + writeFiles.getWritorId() + ".xls";
        }
        if (writeFiles.getWriteSectionId() != null) {
            pdfFileName = writeFiles.getMon() + "Unwrite-WriteSec" + writeFiles.getWriteSectionId() + ".xls";
        }
        if (writeFiles.getBusinessPlaceCode() != null) {
            pdfFileName = writeFiles.getMon() + "Unwrite-Dept" + writeFiles.getBusinessPlaceCode() + ".xls";
        }
        String filePath = "/static/excel/" + DateUtil.getDate("yyyyMM") + "/" + pdfFileName;
        String returnUrl = "report" + filePath;
        if (!writeFiles.getAgainStat()) {
            ApplicationHome h = new ApplicationHome(getClass());
            String baseUrl = FileUtil.getSaveFilePath();
            File file = new File(baseUrl + filePath);
            if (file.exists()) {
                return new HttpResult(HttpResult.SUCCESS, "执行成功", returnUrl);
            }else{
                return new HttpResult<String>(HttpResult.ERROR, "未统计过的报表请选中重新统计",null);
            }
        }
        List<TableDataBean> tableDataList = writeFilesService.findWriteFiles(writeFiles);

        JRDataSource jrDataSource = new JRBeanCollectionDataSource(tableDataList);
        Map<String, Object> m = new HashMap<>();
        m.put("query", "其他参数测试");
        ClassPathResource cpr = new ClassPathResource("static/jasperreport/Unwrite.jasper");
        InputStream is = cpr.getInputStream();
        File file = File.createTempFile("Unwrite", "jasper");
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

    // 电量波动异常pdf预览
    @ResponseBody
    @RequestMapping(value = "/PowerShow")
    public Object powerShow(@RequestBody String json) throws JRException, IOException {
        WriteFilesDomain writeFiles = GsonUtils.readValue(json, WriteFilesDomain.class);
        String pdfFileName = "";
        if (writeFiles.getWritorId() != null) {
            pdfFileName = writeFiles.getMon() + "Power-Writor" + writeFiles.getWritorId() + ".pdf";
        }
        if (writeFiles.getWriteSectionId() != null) {
            pdfFileName = writeFiles.getMon() + "Power-WriteSec" + writeFiles.getWriteSectionId() + ".pdf";
        }
        if (writeFiles.getBusinessPlaceCode() != null) {
            pdfFileName = writeFiles.getMon() + "Power-Dept" + writeFiles.getBusinessPlaceCode() + ".pdf";
        }
        String filePath = "/static/pdf/" + DateUtil.getDate("yyyyMM") + "/" + pdfFileName;
        String returnUrl = "report" + filePath;
        if (!writeFiles.getAgainStat()) {
            ApplicationHome h = new ApplicationHome(getClass());
            String baseUrl = FileUtil.getSaveFilePath();
            File file = new File(baseUrl + filePath);
            if (file.exists()) {
                return new HttpResult(HttpResult.SUCCESS, "执行成功", returnUrl);
            }else{
                return new HttpResult<String>(HttpResult.ERROR, "未统计过的报表请选中重新统计",null);
            }
        }
        List<TableDataBean> tableDataList = writeFilesService.findWriteFiles(writeFiles);

        JRDataSource jrDataSource = new JRBeanCollectionDataSource(tableDataList);
        Map<String, Object> m = new HashMap<>();
        m.put("query", "其他参数测试");
        ClassPathResource cpr = new ClassPathResource("static/jasperreport/Power.jasper");
        InputStream is = cpr.getInputStream();
        File file = File.createTempFile("Power", "jasper");
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

    // 电量波动异常excel
    @ResponseBody
    @RequestMapping(value = "/PowerExcel")
    public Object powerExcel(@RequestBody String json) throws JRException, IOException {
        WriteFilesDomain writeFiles = GsonUtils.readValue(json, WriteFilesDomain.class);
        String pdfFileName = "";
        if (writeFiles.getWritorId() != null) {
            pdfFileName = writeFiles.getMon() + "Power-Writor" + writeFiles.getWritorId() + ".xls";
        }
        if (writeFiles.getWriteSectionId() != null) {
            pdfFileName = writeFiles.getMon() + "Power-WriteSec" + writeFiles.getWriteSectionId() + ".xls";
        }
        if (writeFiles.getBusinessPlaceCode() != null) {
            pdfFileName = writeFiles.getMon() + "Power-Dept" + writeFiles.getBusinessPlaceCode() + ".xls";
        }
        String filePath = "/static/excel/" + DateUtil.getDate("yyyyMM") + "/" + pdfFileName;
        String returnUrl = "report" + filePath;
        if (!writeFiles.getAgainStat()) {
            ApplicationHome h = new ApplicationHome(getClass());
            String baseUrl = FileUtil.getSaveFilePath();
            File file = new File(baseUrl + filePath);
            if (file.exists()) {
                return new HttpResult(HttpResult.SUCCESS, "执行成功", returnUrl);
            }else{
                return new HttpResult<String>(HttpResult.ERROR, "未统计过的报表请选中重新统计",null);
            }
        }
        List<TableDataBean> tableDataList = writeFilesService.findWriteFiles(writeFiles);
        JRDataSource jrDataSource = new JRBeanCollectionDataSource(tableDataList);
        Map<String, Object> m = new HashMap<>();
        m.put("query", "其他参数测试");
        ClassPathResource cpr = new ClassPathResource("static/jasperreport/Power.jasper");
        InputStream is = cpr.getInputStream();
        File file = File.createTempFile("Power", "jasper");
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


    // 表吗查询pdf预览
    @ResponseBody
    @RequestMapping(value = "/BmQueryShow")
    public Object bmQueryShow(@RequestBody String json) throws JRException, IOException {
        JSONObject jsonObject=JSONObject.parseObject(json);
        Integer mon=jsonObject.getInteger("mon");
        WriteFilesDomain writeFiles =
                JSONObject.parseObject(jsonObject.getString("data"), WriteFilesDomain.class);
        writeFiles.setMon(mon);
        //writeFiles.setUserNo("2070000026");
        String pdfFileName = "";
        if (writeFiles.getUserNo() != null) {
            pdfFileName = writeFiles.getMon() + "bmQuery-UserNo" + writeFiles.getUserNo() + ".pdf";
        }
        if (writeFiles.getWriteSectionId() != null) {
            pdfFileName = writeFiles.getMon() + "bmQuery-WriteSec" + writeFiles.getWriteSectionId() + ".pdf";
        }
        if (writeFiles.getBusinessPlaceCode() != null) {
            pdfFileName = writeFiles.getMon() + "bmQuery-Dept" + writeFiles.getBusinessPlaceCode() + ".pdf";
        }
        String filePath = "/static/pdf/" + DateUtil.getDate("yyyyMM") +
                "/bmQuery/" + pdfFileName;
        String returnUrl = "report" + filePath;
        List<TableDataBean> tableDataList = writeFilesService.bmQueryShow(writeFiles);
        if(tableDataList!=null && tableDataList.size()>0){
            tableDataList.get(0).setMon(writeFiles.getMon());
        }
        JRDataSource jrDataSource = new JRBeanCollectionDataSource(tableDataList);
        Map<String, Object> m = new HashMap<>();
        m.put("query", "其他参数测试");
        ClassPathResource cpr = new ClassPathResource("static/jasperreport/queryTable.jasper");
        InputStream is = cpr.getInputStream();
        File file = File.createTempFile("queryTable", "jasper");
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

    // 表吗查询excel预览
    @ResponseBody
    @RequestMapping(value = "/BmQueryShowToExcel")
    public Object BmQueryShowToExcel(@RequestBody String json) throws JRException, IOException {
        JSONObject jsonObject=JSONObject.parseObject(json);
        Integer mon=jsonObject.getInteger("mon");
        WriteFilesDomain writeFiles =
                JSONObject.parseObject(jsonObject.getString("data"), WriteFilesDomain.class);
        writeFiles.setMon(mon);
        String pdfFileName = "";
        if (writeFiles.getUserNo() != null) {
            pdfFileName =
                    writeFiles.getMon() + "bmQuery-UserNo" + writeFiles.getUserNo() + ".xls";
        }
        if (writeFiles.getWriteSectionId() != null) {
            pdfFileName = writeFiles.getMon() + "bmQuery-WriteSec" + writeFiles.getWriteSectionId() + ".xls";
        }
        if (writeFiles.getBusinessPlaceCode() != null) {
            pdfFileName = writeFiles.getMon() + "bmQuery-Dept" + writeFiles.getBusinessPlaceCode() + ".xls";
        }
        String filePath = "/static/xls/" + DateUtil.getDate("yyyyMM") +
                "/bmQuery/" + pdfFileName;
        String returnUrl = "report" + filePath;
        List<TableDataBean> tableDataList = writeFilesService.bmQueryShow(writeFiles);
        tableDataList.get(0).setMon(writeFiles.getMon());
        JRDataSource jrDataSource = new JRBeanCollectionDataSource(tableDataList);
        Map<String, Object> m = new HashMap<>();
        m.put("query", "其他参数测试");
        ClassPathResource cpr = new ClassPathResource("static/jasperreport/queryTable.jasper");
        InputStream is = cpr.getInputStream();
        File file = File.createTempFile("queryTable", "jasper");
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

    //打印抄表卡

    /**
     * @param json
     * @return
     * @throws JRException
     * @throws IOException
     * 根据用户编号或者抄表区段号 查询计量点=》计量点与表计关系（过滤虚拟表）=>电能表资产信息完善
     */
    @ResponseBody
    @RequestMapping(value = "/printMeterReadingCard")
    public Object printMeterReadingCard(@RequestBody String json) throws JRException, IOException {
        JSONObject jsonObject=JSONObject.parseObject(json);
        String userNo=jsonObject.getString("userNo");
        Long writeSectId=jsonObject.getLong("writeSectId");
        String pdfFileName = "";
        if (userNo != null) {
            pdfFileName =
                    "printMeterReadingCard-UserNo" + Calendar.getInstance().getTimeInMillis() + ".pdf";
        }
        if (writeSectId!= null) {
            pdfFileName = "printMeterReadingCard-WriteSec" + Calendar.getInstance().getTimeInMillis() + ".pdf";
        }
        String filePath = "/static/pdf/" + DateUtil.getDate("yyyyMM") +
                "/printMeterReadingCard/" + pdfFileName;
        String returnUrl = "report" + filePath;
        MeterDomain paramMeterDomain=new MeterDomain();
        paramMeterDomain.setUserNo(userNo);
        paramMeterDomain.setWriteSectionId(writeSectId);
        List<TableDataBean> tableDataList = writeFilesService.printMeterReadingCard(paramMeterDomain);



        if (tableDataList == null || tableDataList.size() < 1) {
            return new HttpResult(HttpResult.ERROR, "无抄表卡数据");
        }
        JasperHelper jasperHelper = new JasperHelper();
        Boolean result = jasperHelper.exportPdfs(filePath, tableDataList,"PrintMeterReadingCard");
        if (result.equals(true)) {
            return new HttpResult(HttpResult.SUCCESS, "执行成功", returnUrl);
        } else {
            return new HttpResult(HttpResult.ERROR, "执行失败");
        }
    }

}
