package org.fms.report.common.util;

/**
 * 根据Domain 创建xml
 */
public class SystemUtil {
    public static void main(String[] args) throws Exception{
        String docpath = System.getProperty("user.dir");
        System.out.println(docpath);//user.dir指定了当前的路径
        docpath = docpath.replaceAll("\\\\","/");
        System.out.println(docpath);//user.dir指定了当前的路径

        docpath += "/src/main/java/com/riozenc/report/webapp";
        docpath += "/dao";
        System.out.println(docpath);
//        String docpath ="C:/workspaces/wisdomZhgl/ftp/dao/ftp/dao";
//        ClassDAOXmlUtil.buildXML(docpath, DeptInfo1Domain.class,"DEPT_INFO");
    }



}