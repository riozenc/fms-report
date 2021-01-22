package org.fms.report.server.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.fms.report.common.webapp.bean.TableDataBean;

public class BeanFactory {
    public static Collection<TableDataBean> createBeanCollection() {
        List<TableDataBean> list = new ArrayList<>();
        TableDataBean bean = new TableDataBean();
        list.add(bean);
        return list;
    }
}
