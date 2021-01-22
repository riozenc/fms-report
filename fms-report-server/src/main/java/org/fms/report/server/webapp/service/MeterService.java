package org.fms.report.server.webapp.service;

import java.util.ArrayList;
import java.util.List;

import org.fms.report.common.webapp.domain.MeterDomain;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import com.fasterxml.jackson.core.type.TypeReference;
import com.riozenc.titanTool.annotation.TransactionService;
import com.riozenc.titanTool.common.json.utils.GsonUtils;
import com.riozenc.titanTool.spring.web.client.TitanTemplate;

@TransactionService
public class MeterService {

    @Autowired
    private TitanTemplate titanTemplate;

    //mongo
    public List<MeterDomain> getMeter(MeterDomain m) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        List<MeterDomain> meter = new ArrayList<>();
        try {
            meter = titanTemplate.post("BILLING-SERVER",
                    "billingServer/meter/findMeterDomain", httpHeaders,
                    GsonUtils.toJson(m),
                    new TypeReference<List<MeterDomain>>() {
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
        return meter;
    }

    //根据营业区域获取 不同状态的计量点
    public List<MeterDomain> getMeterByBusinessPlaceCode(MeterDomain m) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        List<MeterDomain> meter = new ArrayList<>();
        try {
            meter = titanTemplate.post("BILLING-SERVER",
                    "billingServer/meter/getMeterByBusinessPlaceCode", httpHeaders,
                    GsonUtils.toJson(m),
                    new TypeReference<List<MeterDomain>>() {
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
        return meter;
    }
}
