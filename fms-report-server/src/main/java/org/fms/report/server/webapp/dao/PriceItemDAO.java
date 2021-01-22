package org.fms.report.server.webapp.dao;

import java.util.List;

import org.fms.report.common.webapp.domain.PriceItemDomain;

import com.riozenc.titanTool.annotation.TransactionDAO;
import com.riozenc.titanTool.spring.webapp.dao.AbstractTransactionDAOSupport;

@TransactionDAO("billing")
public class PriceItemDAO extends AbstractTransactionDAOSupport{
//	@PaginationSupport
	public List<PriceItemDomain> findByWhere(PriceItemDomain priceItemDomain){
		return getPersistanceManager().find(getNamespace() + ".findByWhere", priceItemDomain);
	}
	public int insert(PriceItemDomain priceItemDomain) {
		return getPersistanceManager().insert(getNamespace() + ".insert", priceItemDomain);
	};
	public int update(PriceItemDomain priceItemDomain) {
		return getPersistanceManager().update(getNamespace() + ".update", priceItemDomain);
	}

	public List<PriceItemDomain> priceItemDorp(PriceItemDomain priceItemDomain){
		return getPersistanceManager().find(getNamespace() + ".priceItemDorp", priceItemDomain);
	}

}
