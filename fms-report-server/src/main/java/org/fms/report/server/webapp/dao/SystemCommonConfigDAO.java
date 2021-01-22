package org.fms.report.server.webapp.dao;

import java.util.List;
import java.util.Map;

import org.fms.report.common.webapp.domain.SystemCommonConfigDomain;

import com.riozenc.titanTool.annotation.TransactionDAO;
import com.riozenc.titanTool.spring.webapp.dao.AbstractTransactionDAOSupport;

@TransactionDAO("cim")
public class SystemCommonConfigDAO extends AbstractTransactionDAOSupport {

    public List<Map<Integer,Object>> findMapByDomain(SystemCommonConfigDomain t) {
        // TODO Auto-generated method stub
        return getPersistanceManager().find(getNamespace() + ".findMapByDomain", t);
    }

}
