package org.fms.report.server.webapp.service;

import java.util.ArrayList;
import java.util.List;

import org.fms.report.common.webapp.domain.WriteSectDomain;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.riozenc.titanTool.common.json.utils.GsonUtils;
import com.riozenc.titanTool.spring.web.client.TitanTemplate;

@Service
public class WriteSectService {
    @Autowired
    private TitanTemplate titanTemplate;



    public List<WriteSectDomain> getWriteSect(WriteSectDomain writeSectDomain) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        List<WriteSectDomain> writeSec = new ArrayList<>();
        try {
            writeSec = titanTemplate.post("BILLING-SERVER",
                    "billingServer/writeSect/getWriteSectDomain", httpHeaders,
                    GsonUtils.toJson(writeSectDomain),
                    new TypeReference<List<WriteSectDomain>>() {
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
        return writeSec;
    }

}
