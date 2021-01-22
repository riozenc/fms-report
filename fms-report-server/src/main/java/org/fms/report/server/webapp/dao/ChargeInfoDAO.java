package org.fms.report.server.webapp.dao;



import java.util.List;

import org.fms.report.common.webapp.domain.ChargeInfoDomain;
import org.fms.report.common.webapp.returnDomain.FeeRecStatisticsBean;

import com.riozenc.titanTool.annotation.PaginationSupport;
import com.riozenc.titanTool.annotation.TransactionDAO;
import com.riozenc.titanTool.spring.webapp.dao.AbstractTransactionDAOSupport;

@TransactionDAO("billing")
public class ChargeInfoDAO extends AbstractTransactionDAOSupport {

    @PaginationSupport
    public List<ChargeInfoDomain> findByWhere(ChargeInfoDomain a) {
        // TODO Auto-generated method stub
        List<ChargeInfoDomain> list = getPersistanceManager().find(getNamespace() + ".findByWhere", a);
        return list;
//        return getPersistanceManager().find(getNamespace() + ".findByWhere", a);
    }

    @PaginationSupport
    public List<FeeRecStatisticsBean> getFeeRecStatistics(ChargeInfoDomain a) {
        // TODO Auto-generated method stub
        List<FeeRecStatisticsBean> list = getPersistanceManager().find(getNamespace() + ".getFeeRecStatistics", a);
        return list;
    }

    public List<FeeRecStatisticsBean> getChargeDetails(ChargeInfoDomain a) {
        // TODO Auto-generated method stub
        List<FeeRecStatisticsBean> list = getPersistanceManager().find(getNamespace() + ".getChargeDetails", a);
        return list;
    }

}
