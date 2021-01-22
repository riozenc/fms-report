package org.fms.report.common.util;

import com.riozenc.titanTool.properties.Global;
import org.springframework.web.multipart.MultipartFile;

public class FileUtil {
    //上传文件
    public static String getSaveFilePath() {
        //判断系统
        String os = System.getProperty("os.name");
        String sysFile = Global.getConfig("linux.project.path");
        if (os.toLowerCase().startsWith("win")) {
            sysFile = Global.getConfig("win.project.path");
        }
        return sysFile;
    }
}
