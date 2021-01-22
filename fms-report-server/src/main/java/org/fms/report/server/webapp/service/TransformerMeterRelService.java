package org.fms.report.server.webapp.service;


import java.util.ArrayList;
import java.util.List;

import org.fms.report.common.webapp.domain.TransformerMeterRelDomain;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import com.fasterxml.jackson.core.type.TypeReference;
import com.riozenc.titanTool.annotation.TransactionService;
import com.riozenc.titanTool.common.json.utils.GsonUtils;
import com.riozenc.titanTool.spring.web.client.TitanTemplate;

@TransactionService
public class TransformerMeterRelService {

    @Autowired
    private TitanTemplate titanTemplate;

    public List<TransformerMeterRelDomain> getTransMeterRel(TransformerMeterRelDomain transMeterRel) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        List<TransformerMeterRelDomain> transMeterRelList = new ArrayList<>();
        try {
            transMeterRelList = titanTemplate.post("BILLING-SERVER",
                    "billingServer/transMeterRel/getTransMeterRel", httpHeaders,
                    GsonUtils.toJson(transMeterRel),
                    new TypeReference<List<TransformerMeterRelDomain>>() {
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
        return transMeterRelList;
    }
}
