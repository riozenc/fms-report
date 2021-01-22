package org.fms.report.server.webapp.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.fms.report.common.webapp.domain.BankEntity;
import org.fms.report.common.webapp.domain.CustomerDomain;
import org.fms.report.common.webapp.domain.LineDomain;
import org.fms.report.common.webapp.domain.MeterAssetsDomain;
import org.fms.report.common.webapp.domain.MeterDomain;
import org.fms.report.common.webapp.domain.MeterMeterAssetsRelDomain;
import org.fms.report.common.webapp.domain.MeterRelationDomain;
import org.fms.report.common.webapp.domain.SettlementDomain;
import org.fms.report.common.webapp.domain.SettlementMeterRelDomain;
import org.fms.report.common.webapp.domain.SfPowerBankDomain;
import org.fms.report.common.webapp.domain.SystemCommonConfigEntity;
import org.fms.report.common.webapp.domain.TransformerDomain;
import org.fms.report.common.webapp.domain.TransformerLineRelDomain;
import org.fms.report.common.webapp.domain.UserDomain;
import org.fms.report.common.webapp.domain.WriteSectDomain;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.core.type.TypeReference;
import com.riozenc.titanTool.annotation.TransactionService;
import com.riozenc.titanTool.common.json.utils.GsonUtils;
import com.riozenc.titanTool.spring.web.client.TitanTemplate;
import com.riozenc.titanTool.spring.web.http.HttpResult;
import com.riozenc.titanTool.spring.web.http.HttpResultPagination;

@TransactionService
public class CimService {
    @Autowired
    private TitanTemplate titanTemplate;


    public Map<Long, String> findSystemCommonConfigByType(String type) {
        //获取银行代码
        SystemCommonConfigEntity systemCommonConfigEntity = new SystemCommonConfigEntity();

        systemCommonConfigEntity.setType(type);


        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);

        List<SystemCommonConfigEntity> list = new ArrayList<>();
        try {
            list = titanTemplate.post("CIM-SERVER",
                    "cimServer/cim_bill?method=findSystemCommonConfigByType", httpHeaders,
                    GsonUtils.toJson(systemCommonConfigEntity),
                    new TypeReference<List<SystemCommonConfigEntity>>() {
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
        Map<Long, String> systemCommonConfigEntityMap =
                list.stream().collect(Collectors.toMap(SystemCommonConfigEntity::getParamKey, a -> a.getParamValue(), (k1, k2) -> k1));

        return systemCommonConfigEntityMap;
    }

    public List<SettlementDomain> findBankSettlement(SettlementDomain settlementDomain) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);

        List<SettlementDomain> list = new ArrayList<>();
        try {
            list = titanTemplate.post("CIM-SERVER",
                    "cimServer/cim_bill?method=findBankSettlement", httpHeaders,
                    GsonUtils.toJson(settlementDomain),
                    new TypeReference<List<SettlementDomain>>() {
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    //获取结算户信息
    public List<SettlementDomain> getSettlementbyMeterIds(List<Long> meterIds) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        HttpResult httpResult = new HttpResult();
        try {
            httpResult = titanTemplate.post("CIM-SERVER",
                    "cimServer/settlement?method=getSettlementbyMeterIds", httpHeaders,
                    GsonUtils.toJson(meterIds),
                    new TypeReference<HttpResult>() {
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
        List<SettlementDomain> list =
                JSONObject.parseArray(JSONObject.toJSONString(httpResult.getResultData()),
                        SettlementDomain.class);
        return list;
    }


    //获取结算户信息
    public List<SettlementDomain> findSettlementByIds(List<Long> settlementIds) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("settleIds", settlementIds);
        HttpResult httpResult = new HttpResult();
        try {
            httpResult = titanTemplate.post("CIM-SERVER",
                    "cimServer/cim_bill?method=findSettlementByIds", httpHeaders,
                    GsonUtils.toJson(jsonObject),
                    new TypeReference<HttpResult>() {
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
        List<SettlementDomain> list =
                JSONObject.parseArray(JSONObject.toJSONString(httpResult.getResultData()),
                        SettlementDomain.class);
        return list;
    }

    //获取开户银行下拉
    public List<BankEntity> getBank(BankEntity bankEntity) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        List<BankEntity> list = new ArrayList<>();
        try {
            list = titanTemplate.post("CIM-SERVER",
                    "cimServer/bank?method=getBankList", httpHeaders,
                    GsonUtils.toJson(bankEntity),
                    new TypeReference<List<BankEntity>>() {
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }


    //托收总行
    public List<SfPowerBankDomain> getSfPowerBank(SfPowerBankDomain sfPowerBankDomain) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        List<SfPowerBankDomain> list = new ArrayList<>();
        try {
            list = titanTemplate.post("CIM-SERVER",
                    "cimServer/sfPowerBank?method=getSfPowerBankList",
                    httpHeaders,
                    GsonUtils.toJson(sfPowerBankDomain),
                    new TypeReference<List<SfPowerBankDomain>>() {
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }


    //根据抄表区段获取结算户id集合
    public List<Long> getSettlementIdsByWriteSectionId(List<Long> writeSectionIds) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        List<Long> list = new ArrayList<>();
        try {
            list = titanTemplate.post("CIM-SERVER",
                    "cimServer/settlementMeterRel?method=getSettlementIdsByWriteSectionId",
                    httpHeaders,
                    GsonUtils.toJson(writeSectionIds),
                    new TypeReference<List<Long>>() {
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }


    //结算户findbywhere
    public List<SettlementDomain> getSettlement(SettlementDomain settlementDomain) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        HttpResultPagination<SettlementDomain> httpResultPagination = null;
        try {
            httpResultPagination = titanTemplate.post("CIM-SERVER",
                    "cimServer/settlement?method=findClearSettlementByWhere",
                    httpHeaders,
                    GsonUtils.toJson(settlementDomain),
                    new TypeReference<HttpResultPagination<SettlementDomain>>() {
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }


        return httpResultPagination.getList();
    }


    //获取档案抄表区段集合
    public List<WriteSectDomain> getWriteSectFindByWhere(WriteSectDomain writeSectDomain) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        List<WriteSectDomain> list = new ArrayList<>();
        try {
            list = titanTemplate.post("CIM-SERVER",
                    "cimServer/writeSect?method=findByWhere",
                    httpHeaders,
                    GsonUtils.toJson(writeSectDomain),
                    new TypeReference<List<WriteSectDomain>>() {
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    //获取计量点集合
    public List<MeterDomain> getMeterFindByWhere(Map<String, Object> meterMaps) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        List<MeterDomain> list = new ArrayList<>();
        try {
            list = titanTemplate.post("CIM-SERVER",
                    "cimServer/meter?method=findMeterByMeterIds",
                    httpHeaders,
                    GsonUtils.toJson(meterMaps),
                    new TypeReference<List<MeterDomain>>() {
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    //获取用户集合
    public List<UserDomain> getUserByIds(List<Long> userIds) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        List<UserDomain> list = new ArrayList<>();
        try {
            list = titanTemplate.post("CIM-SERVER",
                    "cimServer/user?method=getUserByIds",
                    httpHeaders,
                    GsonUtils.toJson(userIds),
                    new TypeReference<List<UserDomain>>() {
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    //根据计量点获取线路变压器
    public List<TransformerLineRelDomain> findTransformerLineByMeterIds(List<Long> meterIds) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        List<TransformerLineRelDomain> list = new ArrayList<>();
        try {
            list = titanTemplate.post("CIM-SERVER",
                    "cimServer/transformerMeterRel?method=findTransformerLineByMeterIds",
                    httpHeaders,
                    GsonUtils.toJson(meterIds),
                    new TypeReference<List<TransformerLineRelDomain>>() {
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    //查线路
    public List<LineDomain> findByLineIds(LineDomain lineDomain) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        List<LineDomain> list = new ArrayList<>();
        try {
            list = titanTemplate.post("CIM-SERVER",
                    "cimServer/line?method=findByLineIds",
                    httpHeaders,
                    GsonUtils.toJson(lineDomain),
                    new TypeReference<List<LineDomain>>() {
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    //计算户与计量点关系
    public List<SettlementMeterRelDomain> getMeterIdsBySettlements(List<SettlementDomain> settlementDomains) {
        JSONObject paramJsonObject = new JSONObject();
        paramJsonObject.put("settlement", settlementDomains);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        List<SettlementMeterRelDomain> list = new ArrayList<>();
        try {
            list = titanTemplate.post("CIM-SERVER",
                    "cimServer/settlementMeterRel?method=getMeterIdsBySettlements",
                    httpHeaders,
                    GsonUtils.toJson(paramJsonObject),
                    new TypeReference<List<SettlementMeterRelDomain>>() {
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    //计量点标记关系
    public List<MeterMeterAssetsRelDomain> findMeterMeterAssetsRelByWhere(MeterMeterAssetsRelDomain meterMeterAssetsRelDomain) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        List<MeterMeterAssetsRelDomain> list = new ArrayList<>();
        try {
            list = titanTemplate.post("CIM-SERVER",
                    "cimServer/meterMeterAssets?method=findByWhere",
                    httpHeaders,
                    GsonUtils.toJson(meterMeterAssetsRelDomain),
                    new TypeReference<List<MeterMeterAssetsRelDomain>>() {
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    //根据资产id获取资产信息
    public List<MeterAssetsDomain> getMeterAssetsByAssetsIds(Map map) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        List<MeterAssetsDomain> list = new ArrayList<>();
        try {
            list = titanTemplate.postJsonToList("CIM-SERVER",
                    "cimServer/meterAssets?method=getMeterAssetsByAssetsIds",
                    httpHeaders,
                    map,
                    MeterAssetsDomain.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    //根据变压器ids 获取变压器线路关系
    public List<TransformerLineRelDomain> findRelByTranformIds(TransformerLineRelDomain transformerLineRelDomain) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        List<TransformerLineRelDomain> list = new ArrayList<>();
        try {
            list = titanTemplate.post("CIM-SERVER",
                    "cimServer/transformer?method=findRelByTranformIds",
                    httpHeaders,
                    GsonUtils.toJson(transformerLineRelDomain),
                    new TypeReference<List<TransformerLineRelDomain>>() {
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    //根据户号找用户信息
    public List<UserDomain> findUserByNo(String userNo) {
        UserDomain userDomain = new UserDomain();
        userDomain.setUserNo(userNo);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        List<UserDomain> list = new ArrayList<>();
        try {
            list = titanTemplate.post("CIM-SERVER",
                    "cimServer/user?method=findByNo",
                    httpHeaders,
                    GsonUtils.toJson(userDomain),
                    new TypeReference<List<UserDomain>>() {
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }


    //meterdomian findbywhere
    public List<MeterDomain> findClearMeterDoaminByWhere(MeterDomain meterDomain) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        HttpResultPagination<MeterDomain> httpResultPagination = null;
        try {
            httpResultPagination = titanTemplate.post("CIM-SERVER",
                    "cimServer/meter?method=findClearMeterDoaminByWhere",
                    httpHeaders,
                    GsonUtils.toJson(meterDomain),
                    new TypeReference<HttpResultPagination<MeterDomain>>() {
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }


        return httpResultPagination.getList();
    }

    //查询线路
    public List<LineDomain> getLine(LineDomain lineDomain) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        HttpResultPagination<LineDomain> httpResultPagination = null;
        try {
            httpResultPagination = titanTemplate.post("CIM-SERVER",
                    "cimServer/line?method=getLine",
                    httpHeaders,
                    GsonUtils.toJson(lineDomain),
                    new TypeReference<HttpResultPagination<LineDomain>>() {
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
        return httpResultPagination.getList();
    }

    //根据计量点查计量点和结算户
    public List<SettlementMeterRelDomain> getSettlementByBillingMeterIds(List<Long> meterIds) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        HttpResult<List<SettlementMeterRelDomain>> httpResult = null;
        Map<String, List<Long>> params = new HashMap<>();
        params.put("meterIds", meterIds);
        try {
            httpResult = titanTemplate.postJson("CIM-SERVER",
                    "cimServer/cim_bill?method=getSettlementByBillingMeterIds", new HttpHeaders(), params,
                    new TypeReference<HttpResult<List<SettlementMeterRelDomain>>>() {
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
        return httpResult.getResultData();
    }

    //变压器信息
    public List<TransformerDomain> getAvaTransformerByWhere(TransformerDomain transformerDomain) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        GsonUtils.toJson(transformerDomain);
        HttpResultPagination<TransformerDomain> httpResultPagination = null;
        try {
            httpResultPagination = titanTemplate.post("CIM-SERVER",
                    "cimServer/transformer?method=getAvaTransformerByWhere",
                    httpHeaders,
                    GsonUtils.toJson(transformerDomain),
                    new TypeReference<HttpResultPagination<TransformerDomain>>() {
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
        return httpResultPagination.getList();
    }


    //计量点
    public List<MeterDomain> getMeterByMeterIds(List<Long> meterIds) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        HttpResult<List<MeterDomain>> httpResult = null;
        try {
            httpResult = titanTemplate.post("CIM-SERVER",
                    "cimServer/cim_bill?method=getMeterByMeterIds",
                    new HttpHeaders(),
                    GsonUtils.toJson(meterIds),
                    new TypeReference<HttpResult<List<MeterDomain>>>() {
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
        return httpResult.getResultData();
    }

    //客户
    public List<CustomerDomain> getCustomer(CustomerDomain customerDomain) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        HttpResultPagination<CustomerDomain> httpResult = null;
        try {
            httpResult = titanTemplate.post("CIM-SERVER",
                    "cimServer/customer?method=getCustomer",
                    new HttpHeaders(),
                    GsonUtils.toJson(customerDomain),
                    new TypeReference<HttpResultPagination<CustomerDomain>>() {
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
        return httpResult.getList();
    }

    //套扣关系
    public List<MeterRelationDomain> findMeterRelationByWhere(MeterRelationDomain meterRelationDomain) {

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        HttpResultPagination<MeterRelationDomain> httpResultPagination = null;
        try {
            httpResultPagination = titanTemplate.post("CIM-SERVER",
                    "cimServer/meterRelation?method=getMeterRel",
                    httpHeaders,
                    GsonUtils.toJson(meterRelationDomain),
                    new TypeReference<HttpResultPagination<MeterRelationDomain>>() {
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
        return httpResultPagination.getList();
    }
}
