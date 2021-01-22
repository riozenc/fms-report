package org.fms.report.server.webapp.dao;

import java.util.List;

import org.fms.report.common.webapp.domain.ParamDomain;

import com.riozenc.titanTool.annotation.TransactionDAO;
import com.riozenc.titanTool.spring.webapp.dao.AbstractTransactionDAOSupport;

@TransactionDAO("cim")
public class CimDAO extends AbstractTransactionDAOSupport {

    public List<Long> getParamSettlementList(ParamDomain t) {
        // TODO Auto-generated method stub
        return getPersistanceManager().find(getNamespace() + ".getParamSettlementList", t);
    }
}
