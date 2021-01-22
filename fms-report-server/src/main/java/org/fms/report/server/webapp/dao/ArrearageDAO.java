package org.fms.report.server.webapp.dao;

import java.util.List;

import org.fms.report.common.webapp.domain.ArrearageDomain;

import com.riozenc.titanTool.annotation.PaginationSupport;
import com.riozenc.titanTool.annotation.TransactionDAO;
import com.riozenc.titanTool.spring.webapp.dao.AbstractTransactionDAOSupport;

@TransactionDAO("billing")
public class ArrearageDAO extends AbstractTransactionDAOSupport {

    @PaginationSupport
    public List<ArrearageDomain> findByWhere(ArrearageDomain a) {
        // TODO Auto-generated method stub
        List<ArrearageDomain> list = getPersistanceManager().find(getNamespace() + ".findByWhere", a);
        return list;
//        return getPersistanceManager().find(getNamespace() + ".findByWhere", a);
    }

    @PaginationSupport
    public List<ArrearageDomain> findReport(ArrearageDomain a) {
        // TODO Auto-generated method stub
        List<ArrearageDomain> list = getPersistanceManager().find(getNamespace() + ".findReport", a);
        return list;
//        return getPersistanceManager().find(getNamespace() + ".findByWhere", a);
    }
}
