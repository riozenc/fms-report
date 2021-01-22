package org.fms.report.server.webapp.service;

import java.util.ArrayList;
import java.util.List;

import org.fms.report.common.webapp.domain.MeterMeterAssetsRelDomain;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.riozenc.titanTool.common.json.utils.GsonUtils;
import com.riozenc.titanTool.spring.web.client.TitanTemplate;

@Service
public class MeterMeterAssetsRelService {
    @Autowired
    private TitanTemplate titanTemplate;


    public List<MeterMeterAssetsRelDomain> getRel(MeterMeterAssetsRelDomain rel) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        List<MeterMeterAssetsRelDomain> relList = new ArrayList<>();
        try {
            relList = titanTemplate.post("BILLING-SERVER",
                    "billingServer/meterMeterAssetsRel/getRel", httpHeaders,
                    GsonUtils.toJson(rel),
                    new TypeReference<List<MeterMeterAssetsRelDomain>>() {
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
        return relList;
    }
}
