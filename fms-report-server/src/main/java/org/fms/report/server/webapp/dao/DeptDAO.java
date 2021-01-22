package org.fms.report.server.webapp.dao;

import java.util.List;
import java.util.Map;

import org.fms.report.common.webapp.domain.DeptDomain;
import org.fms.report.common.webapp.domain.ParamDomain;
import org.fms.report.common.webapp.returnDomain.MapEntity;

import com.riozenc.titanTool.annotation.TransactionDAO;
import com.riozenc.titanTool.spring.webapp.dao.AbstractTransactionDAOSupport;

@TransactionDAO()
public class DeptDAO extends AbstractTransactionDAOSupport {
    public List<DeptDomain> findByParentId(DeptDomain t) {
        // TODO Auto-generated method stub
        return getPersistanceManager().find(getNamespace() + ".findByParentId", t);
    }

    public DeptDomain findByKey(DeptDomain t) {
        // TODO Auto-generated method stub
        return getPersistanceManager().load(getNamespace() + ".findByKey", t);
    }

    public List<DeptDomain> findByParentIdList(List<Long> parentIdList) {
        // TODO Auto-generated method stub
        return getPersistanceManager().find(getNamespace() + ".findByParentIdList", parentIdList);
    }

    public List<MapEntity> findIdMapByDomain(DeptDomain t) {
        // TODO Auto-generated method stub
        return getPersistanceManager().find(getNamespace() + ".findIdMapByDomain", t);
    }
    public List<Map<Long,Object>> findDeptIdMapByDomain(DeptDomain t) {
        // TODO Auto-generated method stub
        return getPersistanceManager().find(getNamespace() + ".findDeptIdMapByDomain", t);
    }
    public List<Long> getParamDeptList(ParamDomain t) {
        // TODO Auto-generated method stub
        return getPersistanceManager().find(getNamespace() + ".getParamDeptList", t);
    }
}
