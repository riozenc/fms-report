package org.fms.report.server.webapp.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.fms.report.common.webapp.domain.DeptDomain;
import org.fms.report.common.webapp.domain.ParamDomain;
import org.fms.report.common.webapp.returnDomain.MapEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.riozenc.titanTool.annotation.TransactionService;
import com.riozenc.titanTool.common.json.utils.GsonUtils;
import com.riozenc.titanTool.common.json.utils.JSONUtil;
import com.riozenc.titanTool.spring.web.client.TitanTemplate;
import com.riozenc.titanTool.spring.web.http.HttpResult;

@TransactionService
public class DeptService {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private TitanTemplate titanTemplate;

//    @TransactionDAO
//    private DeptDAO deptDAO;
//
//    public DeptDomain findByKey(DeptDomain t) {
//        return deptDAO.findByKey(t);
//    }
//
//    public List<DeptDomain> findByParentId(DeptDomain t) {
//        return deptDAO.findByParentId(t);
//    }
//
//    public List<DeptDomain> findAllByParentId(List<DeptDomain> list, List<Long> parentIdList) {
//        List<DeptDomain> l = deptDAO.findByParentIdList(parentIdList);
//        if (l.size() > 0) {
//            list.addAll(l);
//            List<Long> ids = l.stream().map(DeptDomain::getId).collect(Collectors.toList());
//            findAllByParentId(list, ids);
//        }
//        return list;
//    }

    public List<DeptDomain> getDeptList(Long businessPlaceCode) {
        List<DeptDomain> businessPlaceCodes = new ArrayList<>();
        try {

            Map<String, Object> params = new HashMap<>();
            params.put("id", businessPlaceCode);
            HttpResult<List<DeptDomain>> httpResult = titanTemplate.postJson("AUTH-CENTER", "auth/dept/tree", null, params,
                    new TypeReference<HttpResult<List<DeptDomain>>>() {
                    });
            List<DeptDomain> deptList = httpResult.getResultData();
            businessPlaceCodes = getList(deptList, businessPlaceCodes);
        } catch (JsonParseException e) {
            e.printStackTrace();
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return businessPlaceCodes;
    }

    public DeptDomain getDept(Long businessPlaceCode) {
        DeptDomain dept = null;
        try {
            String resultJson = restTemplate.getForObject("http://AUTH-CENTER/auth/dept/getDeptById/" + businessPlaceCode,
                    String.class);
            HttpResult<DeptDomain> result = JSONUtil.readValue(resultJson,
                    new TypeReference<HttpResult<DeptDomain>>() {
                    });
            dept = result.getResultData();
        } catch (JsonParseException e) {
            e.printStackTrace();
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return dept;
    }

    public List<DeptDomain> getList(List<DeptDomain> list, List<DeptDomain> businessPlaceCodes) {
        businessPlaceCodes.addAll(list);
        List<List<DeptDomain>> tree = list.stream().filter(i -> i.getChildren().size() > 0).map(DeptDomain::getChildren).collect(Collectors.toList());
        if (tree.size() > 0) {
            List<DeptDomain> child = new ArrayList<>();
            tree.forEach(child::addAll);
            return getList(child, businessPlaceCodes);
        } else {
            return businessPlaceCodes;
        }
    }

    public List<MapEntity> findIdMapByDomain(DeptDomain deptDomain) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);

        HttpResult<List<MapEntity>> result = new HttpResult<>();
        try {
            result = titanTemplate.post("AUTH-CENTER",
                    "auth/dept/findIdMapByDomain", httpHeaders,
                    GsonUtils.toJson(deptDomain),
                    new TypeReference<HttpResult<List<MapEntity>>>() {
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result.getResultData();
    }

    public List<MapEntity> findDeptIdMapByDomain(DeptDomain deptDomain) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);

        HttpResult<List<MapEntity>> result = new HttpResult<>();
        try {
            result = titanTemplate.post("AUTH-CENTER",
                    "auth/dept/findDeptIdMapByDomain", httpHeaders,
                    GsonUtils.toJson(deptDomain),
                    new TypeReference<HttpResult<List<MapEntity>>>() {
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
        List<MapEntity> list=  result.getResultData();

        return list;
    }


    public List<DeptDomain> findByWhere(ParamDomain deptDomain) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);

        HttpResult<List<DeptDomain>> result = new HttpResult<>();
        try {
            result = titanTemplate.post("AUTH-CENTER",
                    "auth/dept/findByWhere", httpHeaders,
                    GsonUtils.toJson(deptDomain),
                    new TypeReference<HttpResult<List<DeptDomain>>>() {
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
        List<DeptDomain> list=  result.getResultData();

        return list;
    }

    public List<DeptDomain> getDeptListByUserId(Long userId) {
        List<DeptDomain> businessPlaceCodes = new ArrayList<>();
        try {
            String resultJson = restTemplate.getForObject("http://AUTH-CENTER" +
                            "/auth/dept/user/" + userId,
                    String.class);
            HttpResult<List<DeptDomain>> result = JSONUtil.readValue(resultJson,
                    new TypeReference<HttpResult<List<DeptDomain>>>() {
                    });
            businessPlaceCodes = result.getResultData();
        } catch (JsonParseException e) {
            e.printStackTrace();
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return businessPlaceCodes;
    }


}
