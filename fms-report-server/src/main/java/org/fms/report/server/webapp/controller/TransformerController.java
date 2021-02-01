package org.fms.report.server.webapp.controller;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.apache.ibatis.javassist.expr.NewArray;
import org.apache.poi.util.IOUtils;
import org.fms.report.common.util.FileUtil;
import org.fms.report.common.webapp.bean.TableDataBean;
import org.fms.report.common.webapp.domain.TransformerDomain;
import org.fms.report.server.utils.JasperHelper;
import org.fms.report.server.webapp.service.TransformerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.system.ApplicationHome;
import org.springframework.core.io.ClassPathResource;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.riozenc.titanTool.common.date.DateUtil;
import com.riozenc.titanTool.common.json.utils.GsonUtils;
import com.riozenc.titanTool.spring.web.http.HttpResult;
import com.riozenc.titanTool.spring.web.http.HttpResultPagination;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

@ControllerAdvice
@RequestMapping(value = "Transformer")
public class TransformerController {

    @Autowired
    private TransformerService transformerService;

    // 台区抄表收费pdf预览
    @ResponseBody
    @RequestMapping(value = "/WriteChargeShow")
    public Object writeChargeShow(@RequestBody String json) throws IOException, JRException {
        TransformerDomain transformer = GsonUtils.readValue(json, TransformerDomain.class);
        String pdfFileName = "";
        if (transformer.getWritorId() != null) {
            pdfFileName = transformer.getMon() + "WriteCharge-Writor" + transformer.getWritorId() + ".pdf";
        }
        if (transformer.getBusinessPlaceCode() != null) {
            pdfFileName = transformer.getMon() + "WriteCharge-Dept" + transformer.getBusinessPlaceCode() + ".pdf";
        }
        if (transformer.getId() != null) {
            pdfFileName = transformer.getMon() + "WriteCharge-Trans" + transformer.getId() + ".pdf";
        }
        String filePath = "/static/pdf/" + DateUtil.getDate("yyyyMM") + "/" + pdfFileName;
        String returnUrl = "report" + filePath;
        if (!transformer.getAgainStat()) {
            ApplicationHome h = new ApplicationHome(getClass());
            String baseUrl = FileUtil.getSaveFilePath();
            File file = new File(baseUrl + filePath);
            if (file.exists()) {
                return new HttpResult<String>(HttpResult.SUCCESS, "执行成功", returnUrl);
            }
        }
        List<TableDataBean> tableDataList = transformerService.writeCharge(transformer);
        JRDataSource jrDataSource = new JRBeanCollectionDataSource(tableDataList);
        Map<String, Object> m = new HashMap<>();
        m.put("query", "其他参数测试");
        ClassPathResource cpr = new ClassPathResource("static/jasperreport/TgReadCharge.jasper");
        InputStream is = cpr.getInputStream();
        File file = File.createTempFile("TgReadCharge", "jasper");
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

    // 台区抄表收费pdf预览
    @ResponseBody
    @RequestMapping(value = "/WriteChargeExcel")
    public Object writeChargeExcel(@RequestBody String json) throws IOException, JRException {
        TransformerDomain transformer = GsonUtils.readValue(json, TransformerDomain.class);
        String pdfFileName = "";
        if (transformer.getWritorId() != null) {
            pdfFileName =
                    transformer.getMon() + "WriteCharge-Writor" + transformer.getWritorId() + ".xls";
        }
        if (transformer.getBusinessPlaceCode() != null) {
            pdfFileName = transformer.getMon() + "WriteCharge-Dept" + transformer.getBusinessPlaceCode() + ".xls";
        }
        if (transformer.getId() != null) {
            pdfFileName = transformer.getMon() + "WriteCharge-Trans" + transformer.getId() + ".xls";
        }
        String filePath = "/static/xls/" + DateUtil.getDate("yyyyMM") + "/" + pdfFileName;
        String returnUrl = "report" + filePath;
        if (!transformer.getAgainStat()) {
            ApplicationHome h = new ApplicationHome(getClass());
            String baseUrl = FileUtil.getSaveFilePath();
            File file = new File(baseUrl + filePath);
            if (file.exists()) {
                return new HttpResult<String>(HttpResult.SUCCESS, "执行成功", returnUrl);
            }
        }
        List<TableDataBean> tableDataList = transformerService.writeCharge(transformer);
        JRDataSource jrDataSource = new JRBeanCollectionDataSource(tableDataList);
        Map<String, Object> m = new HashMap<>();
        m.put("query", "其他参数测试");
        ClassPathResource cpr = new ClassPathResource("static/jasperreport/TgReadCharge.jasper");
        InputStream is = cpr.getInputStream();
        File file = File.createTempFile("TgReadCharge", "jasper");
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
    
 // 台区抄表收费
    @ResponseBody
    @RequestMapping(value = "/WriteCharge")
    public Object writeCharge(@RequestBody String json) throws IOException{
        TransformerDomain transformer = GsonUtils.readValue(json, TransformerDomain.class);
        List<TableDataBean> tableDataList = transformerService.writeCharge(transformer);
        transformer.setTotalRow(tableDataList.size());
        List<TableDataBean> tableData = new ArrayList<TableDataBean>();
        Integer pageCurrent = transformer.getPageCurrent();
        Integer pageSize = transformer.getPageSize();
        for (int i = (pageCurrent-1)*pageSize; i < Math.min(tableDataList.size(), pageCurrent*pageSize); i++) {
			tableData.add(tableDataList.get(i));
		}
        return new HttpResultPagination(transformer, tableDataList);
    }
}
