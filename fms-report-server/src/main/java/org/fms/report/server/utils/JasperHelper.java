package org.fms.report.server.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.net.URLEncoder;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.print.DocFlavor;
import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

//import org.apache.log4j.Logger;
import org.apache.commons.io.FileUtils;
import org.apache.poi.util.IOUtils;
import org.fms.report.common.util.FileUtil;
import org.fms.report.common.webapp.bean.TableDataBean;
import org.springframework.boot.system.ApplicationHome;
import org.springframework.core.io.ClassPathResource;

import net.sf.jasperreports.engine.JRAbstractExporter;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporter;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.base.JRBaseReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.export.JRPrintServiceExporter;
import net.sf.jasperreports.engine.export.JRPrintServiceExporterParameter;
import net.sf.jasperreports.engine.export.JRRtfExporter;
import net.sf.jasperreports.engine.export.JRXlsAbstractExporterParameter;
import net.sf.jasperreports.engine.export.JRXlsExporter;
import net.sf.jasperreports.engine.export.JRXlsExporterParameter;
import net.sf.jasperreports.engine.export.JRXmlExporter;
import net.sf.jasperreports.j2ee.servlets.ImageServlet;

@SuppressWarnings("deprecation")
public class JasperHelper {
    //    private static Logger logger = Logger.getLogger(JasperHelper.class);
    public static final String PRINT_TYPE = "print";
    public static final String PDF_TYPE = "pdf";
    public static final String EXCEL_TYPE = "excel";
    public static final String HTML_TYPE = "html";
    public static final String WORD_TYPE = "word";
    public String path;
    public String baseUrl;

    public static void prepareReport(JasperReport jasperReport, String type) {
//        logger.debug("The method======= prepareReport() start.......................");
        /*
         * 如果导出的是excel，则需要去掉周围的margin
         */
        if ("excel".equals(type))
            try {
                Field margin = JRBaseReport.class
                        .getDeclaredField("leftMargin");
                margin.setAccessible(true);
                margin.setInt(jasperReport, 0);
                margin = JRBaseReport.class.getDeclaredField("topMargin");
                margin.setAccessible(true);
                margin.setInt(jasperReport, 0);
                margin = JRBaseReport.class.getDeclaredField("bottomMargin");
                margin.setAccessible(true);
                margin.setInt(jasperReport, 0);
                Field pageHeight = JRBaseReport.class
                        .getDeclaredField("pageHeight");
                pageHeight.setAccessible(true);
                pageHeight.setInt(jasperReport, 2147483647);
            } catch (Exception exception) {
            }
    }

    /**
     * 导出excel
     */
    public static void exportExcel(JasperPrint jasperPrint,
                                   String defaultFilename, HttpServletRequest request,
                                   HttpServletResponse response) throws IOException, JRException {
        /*
         * 设置头信息
         */
        response.setContentType("application/vnd.ms-excel");
        String defaultname = null;
        if (defaultFilename.trim() != null && defaultFilename != null) {
            defaultname = defaultFilename + ".xls";
        } else {
            defaultname = "export.xls";
        }

        response.setHeader("Content-Disposition", "attachment; filename=\""
                + URLEncoder.encode(defaultname, "UTF-8") + "\"");


        ServletOutputStream ouputStream = response.getOutputStream();
        JRXlsExporter exporter = new JRXlsExporter();

        exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);

        exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, ouputStream);

        exporter.setParameter(
                JRXlsExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_ROWS,
                Boolean.TRUE); // 删除记录最下面的空行

        exporter.setParameter(JRXlsExporterParameter.IS_ONE_PAGE_PER_SHEET,
                Boolean.FALSE);// 删除多余的ColumnHeader
        //
        exporter.setParameter(JRXlsExporterParameter.IS_WHITE_PAGE_BACKGROUND,
                Boolean.FALSE);// 显示边框
        exporter.exportReport();
        ouputStream.flush();
        ouputStream.close();
    }

    public static enum DocType {
        PDF, HTML, XLS, XML, RTF
    }

    @SuppressWarnings("rawtypes")
    public static JRAbstractExporter getJRExporter(DocType docType) {
        JRAbstractExporter exporter = null;
        switch (docType) {
            case PDF:
                exporter = new JRPdfExporter();
                break;
//            case HTML:
//                exporter = new JRHtmlExporter();
//                break;
            case XLS:
                exporter = new JRXlsExporter();
                break;
            case XML:
                exporter = new JRXmlExporter();
                break;
            case RTF:
                exporter = new JRRtfExporter();
                break;
        }
        return exporter;
    }

    /**
     * 导出pdf，注意此处中文问题，
     * <p>
     * 这里应该详细说：主要在ireport里变下就行了。看图
     * <p>
     * 1）在ireport的classpath中加入iTextAsian.jar 2）在ireport画jrxml时，看ireport最左边有个属性栏。
     * <p>
     * 下边的设置就在点字段的属性后出现。 pdf font name ：STSong-Light ，pdf encoding ：UniGB-UCS2-H
     */
    public static void exportPdf(String defaultFilename, String reportfile, HttpServletRequest request,
                                 HttpServletResponse response, Map parameters,
                                 JRDataSource conn) throws IOException, JRException {
        response.setContentType("application/pdf");
        String defaultname = null;
        if (defaultFilename.trim() != null && defaultFilename != null) {
//            defaultname = defaultFilename + ".pdf";
            defaultname = defaultFilename;
        } else {
            defaultname = "export.pdf";
        }
        JasperPrint jasperPrint = JasperFillManager.fillReport(reportfile,
                parameters, conn);
        String fileName = new String(defaultname.getBytes("GBK"), "ISO8859_1");
        response.setHeader("Content-disposition", "attachment; filename="
                + fileName);
//        ServletOutputStream ouputStream = response.getOutputStream();
        File file = new File("D:/ttt.pdf");
        FileOutputStream outputStream = new FileOutputStream(file);
        JasperExportManager.exportReportToPdfStream(jasperPrint, outputStream);
        outputStream.flush();
        outputStream.close();
    }

    public boolean noShowPrint(String filePath, String reportfile, Map parameters,
                               JRDataSource conn) throws IOException, JRException {
        JasperPrint jasperPrint = JasperFillManager.fillReport(reportfile,
                parameters, conn);

//        5、获取打印机列表：
        PrintRequestAttributeSet pras = new HashPrintRequestAttributeSet();
        DocFlavor flavor = DocFlavor.BYTE_ARRAY.PNG;
        PrintService[] printService = PrintServiceLookup.lookupPrintServices(flavor, pras);
//        6、指定打印机打印：
        for (PrintService p : printService) {
            if ("Generic 28BW-8 PCL".equals(p.getName())) {
                JRAbstractExporter je = new JRPrintServiceExporter();
//传入jasperPrint对象
                je.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
                je.setParameter(JRPrintServiceExporterParameter.PRINT_SERVICE, p);
                je.setParameter(JRPrintServiceExporterParameter.DISPLAY_PAGE_DIALOG, false);
                je.setParameter(JRPrintServiceExporterParameter.DISPLAY_PRINT_DIALOG, false);
                je.exportReport();
                return true;
            }

        }
        return true;
    }

    public boolean exportPdf(String filePath, String reportfile, Map parameters,
                             JRDataSource conn) throws IOException, JRException {

        JasperPrint jasperPrint = JasperFillManager.fillReport(reportfile,
                parameters, conn);
        ApplicationHome h = new ApplicationHome(getClass());
        File jarF = h.getSource();
        baseUrl = FileUtil.getSaveFilePath();
        this.path = baseUrl + filePath;
        File file = new File(path);
        if(file.exists()){
            file.delete();
        }
        /*if (!file.exists()) {
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            } else {
                try {
                    file.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }*/
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }
        try (FileOutputStream outputStream = new FileOutputStream(file)) {
            JasperExportManager.exportReportToPdfStream(jasperPrint, outputStream);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean exportPdfs(String filePath,
                              List<TableDataBean> tableDataList, String reportName) throws IOException, JRException {
        List<JasperPrint> list = new ArrayList<>();


        for (TableDataBean tableDataBean : tableDataList) {
            List<TableDataBean> BeanList = new ArrayList<>();
            BeanList.add(tableDataBean);
            JRDataSource jrDataSource = new JRBeanCollectionDataSource(BeanList);
            Map<String, Object> m = new HashMap<>();
            m.put("query", "其他参数测试");
            ClassPathResource cpr = new ClassPathResource("static/jasperreport/" + reportName + ".jasper");
            InputStream is = cpr.getInputStream();
            File file = File.createTempFile("Zero", "jasper");
            try {
                FileUtils.copyInputStreamToFile(is, file);
            } finally {
                IOUtils.closeQuietly(is);
            }
            JasperPrint jasperPrint = JasperFillManager.fillReport(file.getPath(),
                    m, jrDataSource);
            list.add(jasperPrint);
        }
        ApplicationHome h = new ApplicationHome(getClass());
        File jarF = h.getSource();
        baseUrl = FileUtil.getSaveFilePath();
        this.path = baseUrl + filePath;
        File file = new File(path);

        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }
        JRAbstractExporter exporter = new JRPdfExporter();
        try (FileOutputStream outputStream = new FileOutputStream(file)) {
            exporter.setParameter(JRExporterParameter.JASPER_PRINT_LIST, list);
            exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, outputStream);
            exporter.setParameter(JRExporterParameter.CHARACTER_ENCODING, "UTF-8");
            exporter.exportReport();
            outputStream.flush();
            outputStream.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

    }


    // 本地生成excel
    public boolean exportExcel(String filePath, String reportfile, Map parameters, JRDataSource conn) throws JRException, IOException {
        ApplicationHome h = new ApplicationHome(getClass());
        File jarF = h.getSource();
        baseUrl = FileUtil.getSaveFilePath();;
        path = baseUrl + filePath;
        File file = new File(path);
       /* if (!file.exists()) {
            //先得到文件的上级目录，并创建上级目录，在创建文件
            if (!file.getParentFile().exists()) {
                newFile(file.getParentFile().getPath());
            } else {
                try {
                    file.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }*/
        if(file.exists()){
            file.delete();
        }
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }
        JRAbstractExporter exporter = getJRExporter(DocType.XLS);
        JasperPrint jasperPrint = JasperFillManager.fillReport(reportfile, parameters, conn);
        FileOutputStream outputStream = new FileOutputStream(file);
        try {
            JasperExportManager.exportReportToXml(jasperPrint);
            exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
            exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, outputStream);
            exporter.setParameter(JRXlsAbstractExporterParameter.IS_DETECT_CELL_TYPE, Boolean.TRUE);
            exporter.exportReport();
            outputStream.flush();
            return true;
        } catch (Exception e) {
            return false;
        }
    }


    /**
     * 导出html
     */
//    private static void exportHtml(JasperPrint jasperPrint,
//                                   String defaultFilename, HttpServletRequest request,
//                                   HttpServletResponse response) throws IOException, JRException {
//        response.setContentType("text/html");
//        ServletOutputStream ouputStream = response.getOutputStream();
//        JRHtmlExporter exporter = new JRHtmlExporter();
//        exporter.setParameter(JRHtmlExporterParameter.IS_USING_IMAGES_TO_ALIGN,
//                Boolean.FALSE);
//        exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
//        exporter.setParameter(JRExporterParameter.CHARACTER_ENCODING, "UTF-8");
//        exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, ouputStream);
//        //设置图片文件存放路径，此路径为服务器上的绝对路径
//        String imageDIR =request.getSession().getServletContext().getRealPath("/");
//        exporter.setParameter(JRHtmlExporterParameter.IMAGES_DIR_NAME, imageDIR);
//
//        //设置图片请求URI
//        String imageURI = request.getContextPath() + "/";
//        exporter.setParameter(JRHtmlExporterParameter.IMAGES_URI, imageURI);
//
//        //设置导出图片到图片存放路径
//        exporter.setParameter(JRHtmlExporterParameter.IS_USING_IMAGES_TO_ALIGN, Boolean.TRUE);
//        exporter.setParameter(JRHtmlExporterParameter.IS_OUTPUT_IMAGES_TO_DIR, Boolean.TRUE);
//
//        exporter.exportReport();
//
//        ouputStream.flush();
//        ouputStream.close();
//    }

    /**
     * 导出word
     */
    @SuppressWarnings("rawtypes")
    private static void exportWord(JasperPrint jasperPrint,
                                   String defaultFilename, HttpServletRequest request,
                                   HttpServletResponse response) throws JRException, IOException {
        response.setContentType("application/msword;charset=utf-8");
        String defaultname = null;
        if (defaultFilename.trim() != null && defaultFilename != null) {
            defaultname = defaultFilename + ".doc";
        } else {
            defaultname = "export.doc";
        }
        String fileName = new String(defaultname.getBytes("GBK"), "utf-8");
        response.setHeader("Content-disposition", "attachment; filename="
                + fileName);
        JRExporter exporter = new JRRtfExporter();
        exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
        exporter.setParameter(JRExporterParameter.OUTPUT_STREAM,
                response.getOutputStream());

        exporter.exportReport();
    }

//    /**
//     * 按照类型导出不同格式文件
//     *
//     * @param datas
//     *            数据
//     * @param type
//     *            文件类型
//     * @param is
//     *            jasper文件的来源
//     * @param request
//     * @param response
//     * @param defaultFilename默认的导出文件的名称
//     */
//    public static void export(String type, String defaultFilename, File is,
//                              HttpServletRequest request, HttpServletResponse response,
//                              Map parameters, Connection conn) {
//        try {
//            JasperReport jasperReport = (JasperReport) JRLoader.loadObject(is);
//            prepareReport(jasperReport, type);
//
//            JasperPrint jasperPrint = JasperFillManager.fillReport(
//                    jasperReport, parameters, conn);
//
//            if (EXCEL_TYPE.equals(type)) {
//                exportExcel(jasperPrint, defaultFilename, request, response);
//            } else if (PDF_TYPE.equals(type)) {
//                exportPdf(jasperPrint, defaultFilename, request, response);
//            } else if (HTML_TYPE.equals(type)) {
//                exportHtml(jasperPrint, defaultFilename, request, response);
//            } else if (WORD_TYPE.equals(type)) {
//                exportWord(jasperPrint, defaultFilename, request, response);
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//    public static void export(String type, String defaultFilename, File is,
//                              HttpServletRequest request, HttpServletResponse response,
//                              Map parameters, JRDataSource conn) {
//        try {
//            JasperReport jasperReport = (JasperReport) JRLoader.loadObject(is);
//            prepareReport(jasperReport, type);
//
//            JasperPrint jasperPrint = JasperFillManager.fillReport(
//                    jasperReport, parameters, conn);
//
//            if (EXCEL_TYPE.equals(type)) {
//                exportExcel(jasperPrint, defaultFilename, request, response);
//            } else if (PDF_TYPE.equals(type)) {
//                exportPdf(jasperPrint, defaultFilename, request, response);
//            } else if (HTML_TYPE.equals(type)) {
//                exportHtml(jasperPrint, defaultFilename, request, response);
//            } else if (WORD_TYPE.equals(type)) {
//                exportWord(jasperPrint, defaultFilename, request, response);
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }


    //    /**
//     * 输出html静态页面，必须注入request和response
//     *
//     * @param jasperPath
//     * @param params
//     * @param sourceList
//     * @param imageUrl
//     *            报表文件使用的图片路径，比如 ../servlets/image?image=
//     * @throws JRException
//     * @throws IOException
//     * @throws ServletException
//     */
//    public static void showHtml(String defaultFilename, String reportfile,
//                                HttpServletRequest request, HttpServletResponse response, Map parameters,
//                                JRDataSource conn) throws JRException, IOException {
//
//
//        request.setCharacterEncoding("utf-8");
//        response.setCharacterEncoding("utf-8");
//        response.setContentType("text/html");
//
//        JRAbstractExporter exporter = getJRExporter(DocType.HTML);
//
//        JasperPrint jasperPrint = JasperFillManager.fillReport(reportfile,
//                parameters, conn);
//        request.getSession().setAttribute(
//                ImageServlet.DEFAULT_JASPER_PRINT_SESSION_ATTRIBUTE,
//                jasperPrint);
//
//        PrintWriter out = response.getWriter();
//
//        exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
//        exporter.setParameter(JRExporterParameter.OUTPUT_WRITER, out);
//        exporter.setParameter(
//                JRHtmlExporterParameter.IS_USING_IMAGES_TO_ALIGN,
//                Boolean.FALSE);
//
//        //设置图片文件存放路径，此路径为服务器上的绝对路径
//        String imageDIR =request.getSession().getServletContext().getRealPath("/");
//        exporter.setParameter(JRHtmlExporterParameter.IMAGES_DIR_NAME, imageDIR);
//
//        //设置图片请求URI
//        String imageURI = request.getContextPath() + "/";
//        exporter.setParameter(JRHtmlExporterParameter.IMAGES_URI, imageURI);
//
//        //设置导出图片到图片存放路径
//        exporter.setParameter(JRHtmlExporterParameter.IS_USING_IMAGES_TO_ALIGN, Boolean.TRUE);
//        exporter.setParameter(JRHtmlExporterParameter.IS_OUTPUT_IMAGES_TO_DIR, Boolean.TRUE);
//        exporter.exportReport();
//
//        out.flush();
//
//    }
//    public static void showHtml(String defaultFilename, String reportfile,
//                                HttpServletRequest request, HttpServletResponse response, Map parameters,
//                                Connection conn) throws JRException, IOException {
//
//
//        request.setCharacterEncoding("utf-8");
//        response.setCharacterEncoding("utf-8");
//        response.setContentType("text/html");
//
//        JRAbstractExporter exporter = getJRExporter(DocType.HTML);
//
//        JasperPrint jasperPrint = JasperFillManager.fillReport(reportfile,
//                parameters, conn);
//        request.getSession().setAttribute(
//                ImageServlet.DEFAULT_JASPER_PRINT_SESSION_ATTRIBUTE,
//                jasperPrint);
//
//        PrintWriter out = response.getWriter();
//
//        exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
//        exporter.setParameter(JRExporterParameter.OUTPUT_WRITER, out);
//        exporter.setParameter(
//                JRHtmlExporterParameter.IS_USING_IMAGES_TO_ALIGN,
//                Boolean.FALSE);
//        //设置图片文件存放路径，此路径为服务器上的绝对路径
//        String imageDIR =request.getSession().getServletContext().getRealPath("/");
//        exporter.setParameter(JRHtmlExporterParameter.IMAGES_DIR_NAME, imageDIR);
//
//        //设置图片请求URI
//        String imageURI = request.getContextPath() + "/";
//        exporter.setParameter(JRHtmlExporterParameter.IMAGES_URI, imageURI);
//
//        //设置导出图片到图片存放路径
//        exporter.setParameter(JRHtmlExporterParameter.IS_USING_IMAGES_TO_ALIGN, Boolean.TRUE);
//        exporter.setParameter(JRHtmlExporterParameter.IS_OUTPUT_IMAGES_TO_DIR, Boolean.TRUE);
//        exporter.exportReport();
//        out.flush();
//
//    }
    @SuppressWarnings({"rawtypes", "unchecked"})
    public static void showPdf(String defaultFilename, String reportfile,
                               HttpServletRequest request, HttpServletResponse response, Map parameters,
                               JRDataSource conn) throws JRException, IOException {

        request.setCharacterEncoding("utf-8");
        response.setCharacterEncoding("utf-8");
//        response.setContentType("text/html");
        response.setContentType("application/pdf");
        JRAbstractExporter exporter = getJRExporter(DocType.PDF);
        JasperPrint jasperPrint = JasperFillManager.fillReport(reportfile,
                parameters, conn);
        request.getSession().setAttribute(
                ImageServlet.DEFAULT_JASPER_PRINT_SESSION_ATTRIBUTE,
                jasperPrint);
        OutputStream out = response.getOutputStream();
        exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
        exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, out);
        exporter.exportReport();
        ;
        out.flush();
    }


    // 页面生成excel下载
    @SuppressWarnings({"rawtypes", "unchecked"})
    public static void showExcel(String defaultname, String reportfile,
                                 HttpServletRequest request, HttpServletResponse response, Map parameters,
                                 JRDataSource conn) throws JRException, IOException {


        request.setCharacterEncoding("utf-8");
        response.setCharacterEncoding("utf-8");
        response.setContentType("application/vnd.ms-excel");
        if (defaultname.trim() != null && defaultname != null) {
            defaultname = defaultname + ".xls";
        } else {
            defaultname = "export.xls";
        }
        String fileName = new String(defaultname.getBytes("GBK"), "ISO8859_1");
        response.setHeader("Content-disposition", "attachment; filename="
                + fileName);

        JRAbstractExporter exporter = getJRExporter(DocType.XLS);

        JasperPrint jasperPrint = JasperFillManager.fillReport(reportfile,
                parameters, conn);
        request.getSession().setAttribute(
                ImageServlet.DEFAULT_JASPER_PRINT_SESSION_ATTRIBUTE,
                jasperPrint);

        OutputStream out = response.getOutputStream();
        JasperExportManager.exportReportToXml(jasperPrint);
        exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
        exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, out);
        exporter.exportReport();
        out.flush();

    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    public static void showPdf(String defaultFilename, String reportfile,
                               HttpServletRequest request, HttpServletResponse response, Map parameters,
                               Connection conn) throws JRException, IOException {


        request.setCharacterEncoding("utf-8");
        response.setCharacterEncoding("utf-8");
        response.setContentType("text/html");
        response.setContentType("application/pdf");

        JRAbstractExporter exporter = getJRExporter(DocType.PDF);

        JasperPrint jasperPrint = JasperFillManager.fillReport(reportfile,
                parameters, conn);
        request.getSession().setAttribute(
                ImageServlet.DEFAULT_JASPER_PRINT_SESSION_ATTRIBUTE,
                jasperPrint);

        OutputStream out = response.getOutputStream();

        exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
        exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, out);
        exporter.exportReport();
        out.flush();
    }

    public void newFile(String filePath) {
        File file = new File(filePath);
        String s = file.getPath();
        if (!file.exists()) {
            if (!file.getParentFile().exists()) {
                newFile(file.getParentFile().getPath());
            } else {
                file.mkdir();
                File f = new File(this.path);
                if (!f.getParentFile().exists()) {
                    newFile(f.getParentFile().getPath());
                }
            }
        }
    }


}
