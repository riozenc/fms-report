package org.fms.report.server.webapp.dao;

import java.util.ArrayList;
import java.util.List;

import org.fms.report.common.webapp.domain.MeterMoneyDomain;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import com.fasterxml.jackson.core.type.TypeReference;
import com.riozenc.titanTool.annotation.TransactionDAO;
import com.riozenc.titanTool.spring.web.client.TitanTemplate;
import com.riozenc.titanTool.spring.webapp.dao.AbstractTransactionDAOSupport;

@TransactionDAO
public class MeterMoneyDAO extends AbstractTransactionDAOSupport {
    @Autowired
    private TitanTemplate titanTemplate;

    public List<MeterMoneyDomain> mongoFind(MeterMoneyDomain meterMoney) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        List<MeterMoneyDomain> list = new ArrayList<>();
        try {
            list = titanTemplate.post("BILLING-SERVER",
                    "billingServer/MeterMoney/mongoFind", httpHeaders,
                    meterMoney,
                    new TypeReference<List<MeterMoneyDomain>>() {
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
}
