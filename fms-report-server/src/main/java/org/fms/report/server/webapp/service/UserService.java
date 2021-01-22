package org.fms.report.server.webapp.service;

import java.util.ArrayList;
import java.util.List;

import org.fms.report.common.webapp.domain.UserDomain;
import org.fms.report.common.webapp.returnDomain.MapEntity;
import org.fms.report.server.webapp.dao.UserDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import com.fasterxml.jackson.core.type.TypeReference;
import com.riozenc.titanTool.annotation.TransactionDAO;
import com.riozenc.titanTool.annotation.TransactionService;
import com.riozenc.titanTool.common.json.utils.GsonUtils;
import com.riozenc.titanTool.spring.web.client.TitanTemplate;
import com.riozenc.titanTool.spring.web.http.HttpResult;

@TransactionService
public class UserService {

    @TransactionDAO()
    private UserDAO userDAO;

    @Autowired
    private TitanTemplate titanTemplate;

    public List<UserDomain> getUser(UserDomain m) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        List<UserDomain> user = new ArrayList<>();
        try {
            user = titanTemplate.post("BILLING-SERVER",
                    "billingServer/userMongo/findUserMongoByWhere", httpHeaders,
                    GsonUtils.toJson(m),
                    new TypeReference<List<UserDomain>>() {
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
        return user;
    }

    public List<UserDomain> findByIds(List<Long> ids) {
        List<UserDomain> list = userDAO.findByIds(ids);
        return list;
    }

    public List<MapEntity> findMapByDomain(UserDomain userDomain) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);

        HttpResult<List<MapEntity>> result = new HttpResult<>();
        try {
            result = titanTemplate.post("AUTH-CENTER",
                    "auth/user/findMapByDomain", httpHeaders,
                    GsonUtils.toJson(userDomain),
                    new TypeReference<HttpResult<List<MapEntity>>>() {
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
        List<MapEntity> list=  result.getResultData();

        return list;
    }
}
