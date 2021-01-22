package org.fms.report.server.webapp.dao;

import java.util.List;

import org.fms.report.common.webapp.domain.NoteInfoDomain;

import com.riozenc.titanTool.annotation.PaginationSupport;
import com.riozenc.titanTool.annotation.TransactionDAO;
import com.riozenc.titanTool.spring.webapp.dao.AbstractTransactionDAOSupport;

@TransactionDAO("billing")
public class NoteInfoDAO extends AbstractTransactionDAOSupport {

    @PaginationSupport
    public List<NoteInfoDomain> findByWhere(NoteInfoDomain a) {
        // TODO Auto-generated method stub
        List<NoteInfoDomain> list = getPersistanceManager().find(getNamespace() + ".findByWhere", a);
        return list;
//        return getPersistanceManager().find(getNamespace() + ".findByWhere", a);
    }

    @PaginationSupport
    public List<NoteInfoDomain> getMonBalance(NoteInfoDomain a) {
        // TODO Auto-generated method stub
        List<NoteInfoDomain> list = getPersistanceManager().find(getNamespace() + ".getMonBalance", a);
        return list;
    }
}