package org.fms.report.server.webapp.controller;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.apache.poi.util.IOUtils;
import org.fms.report.common.webapp.bean.TableDataBean;
import org.fms.report.common.webapp.domain.AppInfoQueryEntity;
import org.fms.report.server.utils.JasperHelper;
import org.fms.report.server.webapp.service.BemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.riozenc.titanTool.common.date.DateUtil;
import com.riozenc.titanTool.common.json.utils.GsonUtils;
import com.riozenc.titanTool.spring.web.http.HttpResult;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

@ControllerAdvice
@RequestMapping(value = "bem")
public class BemController {

    @Autowired
    private BemService bemService;


    //业扩信息查询报表
    @RequestMapping("appInfoQuery")
    @ResponseBody
    public HttpResult appInfoQuery(@RequestBody(required = false) String json) throws IOException, JRException {
        AppInfoQueryEntity appInfoQueryEntity = GsonUtils.readValue(json,AppInfoQueryEntity.class);
        String pdfFileName =
                "appInfoQuery"  + Calendar.getInstance().getTimeInMillis() + ".pdf";
        String filePath =
                "/static/pdf/" + DateUtil.getDate("yyyyMM") +
                        "/appInfoQuery/" + pdfFileName;

        String returnUrl = "report" + filePath;
        List<TableDataBean> tableDataList = bemService.appInfoQuery(appInfoQueryEntity);
        JRDataSource jrDataSource = new JRBeanCollectionDataSource(tableDataList);
        Map<String, Object> m = new HashMap<>();
        m.put("query", "其他参数测试");
        ClassPathResource cpr = new ClassPathResource("static/jasperreport/AppInfoQuery.jasper");
        InputStream is = cpr.getInputStream();
        File file = File.createTempFile("AppInfoQuery", "jasper");
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

    //业扩信息查询报表
    @RequestMapping("appInfoQueryToExcel")
    @ResponseBody
    public HttpResult appInfoQueryToExcel(@RequestBody(required = false) String json) throws IOException, JRException {
        AppInfoQueryEntity appInfoQueryEntity = GsonUtils.readValue(json,AppInfoQueryEntity.class);
        String pdfFileName =
                "appInfoQuery"  + Calendar.getInstance().getTimeInMillis() +
                        ".xls";
        String filePath =
                "/static/xls/" + DateUtil.getDate("yyyyMM") +
                        "/appInfoQuery/" + pdfFileName;

        String returnUrl = "report" + filePath;
        List<TableDataBean> tableDataList = bemService.appInfoQuery(appInfoQueryEntity);
        JRDataSource jrDataSource = new JRBeanCollectionDataSource(tableDataList);
        Map<String, Object> m = new HashMap<>();
        m.put("query", "其他参数测试");
        ClassPathResource cpr = new ClassPathResource("static/jasperreport/AppInfoQuery.jasper");
        InputStream is = cpr.getInputStream();
        File file = File.createTempFile("AppInfoQuery", "jasper");
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
