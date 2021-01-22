package org.fms.report.server.webapp.dao;

import java.util.List;
import java.util.Map;

import org.fms.report.common.webapp.domain.UserDomain;

import com.riozenc.titanTool.annotation.TransactionDAO;
import com.riozenc.titanTool.spring.webapp.dao.AbstractTransactionDAOSupport;

@TransactionDAO()
public class UserDAO extends AbstractTransactionDAOSupport {
    public UserDomain findByKey(UserDomain t) {
        // TODO Auto-generated method stub
        return getPersistanceManager().load(getNamespace() + ".findByKey", t);
    }
    public List<Map<Long,Object>> findMapByDomain(UserDomain t) {
        // TODO Auto-generated method stub
        return getPersistanceManager().find(getNamespace() + ".findMapByDomain", t);
    }
    public List<UserDomain> findByIds(List<Long> ids) {
        // TODO Auto-generated method stub
        return getPersistanceManager().find(getNamespace() + ".findByIds", ids);
    }
}
