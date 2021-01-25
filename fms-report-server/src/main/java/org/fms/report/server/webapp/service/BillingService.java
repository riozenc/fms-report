package org.fms.report.server.webapp.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.fms.report.common.webapp.domain.ArrearageDomain;
import org.fms.report.common.webapp.domain.BankCollectionEntity;
import org.fms.report.common.webapp.domain.ChargeInfoDetailEntity;
import org.fms.report.common.webapp.domain.ChargeInfoDomain;
import org.fms.report.common.webapp.domain.ChargeInfoEntity;
import org.fms.report.common.webapp.domain.ElectricityTariffRankEntity;
import org.fms.report.common.webapp.domain.MeterDomain;
import org.fms.report.common.webapp.domain.MeterMoneyDomain;
import org.fms.report.common.webapp.domain.MeterRelationDomain;
import org.fms.report.common.webapp.domain.NoteInfoDomain;
import org.fms.report.common.webapp.domain.PriceExecutionDomain;
import org.fms.report.common.webapp.domain.PriceLadderRelaDomain;
import org.fms.report.common.webapp.domain.PriceTypeDomain;
import org.fms.report.common.webapp.domain.WriteFilesDomain;
import org.fms.report.common.webapp.domain.WriteSectMongoDomain;
import org.fms.report.server.webapp.dao.ArrearageDAO;
import org.fms.report.server.webapp.dao.ChargeInfoDAO;
import org.fms.report.server.webapp.dao.DeptDAO;
import org.fms.report.server.webapp.dao.NoteInfoDAO;
import org.fms.report.server.webapp.dao.SystemCommonConfigDAO;
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
import com.riozenc.titanTool.spring.web.http.HttpResultPagination;

@TransactionService
public class BillingService {
    @Autowired
    private TitanTemplate titanTemplate;

    @TransactionDAO
    private UserDAO userDAO;

    @TransactionDAO
    private SystemCommonConfigDAO systemCommonConfigDAO;

    @TransactionDAO
    private DeptDAO deptDAO;

    @Autowired
    private CimService cimService;

    @TransactionDAO
    private ArrearageDAO arrearageDAO;

    @TransactionDAO
    private ChargeInfoDAO chargeInfoDAO;

    @TransactionDAO
    private NoteInfoDAO noteInfoDao;

    @Autowired
    private WriteSectService writeSectService;

    @Autowired
    private MeterService meterService;



    public Map<Long, List<PriceExecutionDomain>> findMongoPriceExecution(Integer mon) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        Map<Long, List<PriceExecutionDomain>> list = new HashMap<>();
        try {
            list = titanTemplate.post("BILLING-SERVER",
                    "billingServer/priceExecution/findMongoPriceExecution", httpHeaders,
                    GsonUtils.toJson(mon),
                    new TypeReference<Map<Long, List<PriceExecutionDomain>>>() {
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }


    public List<MeterMoneyDomain> findMeterMoneyByMonAndMeterIds(MeterMoneyDomain meterMoneyDomain) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);

        List<MeterMoneyDomain> list = new ArrayList<>();
        try {
            list = titanTemplate.post("BILLING-SERVER",
                    "billingServer/MeterMoney/select", httpHeaders,
                    GsonUtils.toJson(meterMoneyDomain),
                    new TypeReference<List<MeterMoneyDomain>>() {
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<MeterMoneyDomain> findMeterMoneyByIds(List<Long> ids) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);

        List<MeterMoneyDomain> list = new ArrayList<>();
        try {
            list = titanTemplate.post("BILLING-SERVER",
                    "billingServer/MeterMoney/findMeterMoneyByIds", httpHeaders,
                    GsonUtils.toJson(ids),
                    new TypeReference<List<MeterMoneyDomain>>() {
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }


    public HttpResultPagination<ChargeInfoDetailEntity> findChargeInfoDetails(ChargeInfoEntity chargeInfoEntity) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);

        HttpResultPagination<ChargeInfoDetailEntity> httpResultPagination = new HttpResultPagination<ChargeInfoDetailEntity>();
        try {
            httpResultPagination = titanTemplate.post("BILLING-SERVER",
                    "billingServer/charge/findChargeInfoDetails", httpHeaders,
                    GsonUtils.toJson(chargeInfoEntity),
                    new TypeReference<HttpResultPagination<ChargeInfoDetailEntity>>() {
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
        return httpResultPagination;
    }

    public List<WriteFilesDomain> findWriteFilesByMonAndMeterIds(WriteFilesDomain writeFilesDomain) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);

        List<WriteFilesDomain> list = new ArrayList<>();
        try {
            list = titanTemplate.post("BILLING-SERVER",
                    "billingServer/WriteFiles/findByWhere", httpHeaders,
                    GsonUtils.toJson(writeFilesDomain),
                    new TypeReference<List<WriteFilesDomain>>() {
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }


    //电价类型
    public List<PriceTypeDomain> findPriceType() {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        List<PriceTypeDomain> list = new ArrayList<>();
        try {
            list = titanTemplate.post("BILLING-SERVER",
                    "billingServer/PriceType/select", httpHeaders,
                    null,
                    new TypeReference<List<PriceTypeDomain>>() {
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    //结算单
    public List<ChargeInfoDomain> findChargeByIds(List<Long> chargeIds) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        List<ChargeInfoDomain> list = new ArrayList<>();
        try {
            list = titanTemplate.post("BILLING-SERVER",
                    "billingServer/charge/findChargeByIds", httpHeaders,
                    GsonUtils.toJson(chargeIds),
                    new TypeReference<List<ChargeInfoDomain>>() {
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<PriceLadderRelaDomain> findMongoPriceLadderRela(Integer mon) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        List<PriceLadderRelaDomain> list = new ArrayList<>();
        try {
            list = titanTemplate.post("BILLING-SERVER",
                    "billingServer/priceLadderRela/findMongoPriceLadderRela", httpHeaders,
                    GsonUtils.toJson(mon),
                    new TypeReference<List<PriceLadderRelaDomain>>() {
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<ChargeInfoDomain> chargeBySettleIds(ChargeInfoDomain chargeInfoDomain) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        HttpResult<List<ChargeInfoDomain>> list = new HttpResult<>();
        try {
            list = titanTemplate.post("BILLING-SERVER",
                    "billingServer/charge/select", httpHeaders,
                    GsonUtils.toJson(chargeInfoDomain),
                    new TypeReference<HttpResult<List<ChargeInfoDomain>>>() {
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list.getResultData();
    }

    public List<ChargeInfoDomain> chargeFindByWhere(ChargeInfoDomain chargeInfoDomain) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        HttpResult<List<ChargeInfoDomain>> list = new HttpResult<>();
        try {
            list = titanTemplate.post("BILLING-SERVER",
                    "billingServer/charge/select", httpHeaders,
                    GsonUtils.toJson(chargeInfoDomain),
                    new TypeReference<HttpResult<List<ChargeInfoDomain>>>() {
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list.getResultData();
    }


    //结算户汇总欠费 打印托收单使用
    public List<BankCollectionEntity> findArrearageGroupbySettleId(ArrearageDomain arrearageDomain) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        List<BankCollectionEntity> list = new ArrayList<>();
        try {
            list = titanTemplate.post("BILLING-SERVER",
                    "billingServer/arrearage/findArrearageGroupbySettleId", httpHeaders,
                    GsonUtils.toJson(arrearageDomain),
                    new TypeReference<List<BankCollectionEntity>>() {
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public ChargeInfoDomain chargeByKey(ChargeInfoDomain chargeInfoDomain) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        ChargeInfoDomain returnChargeInfo = new ChargeInfoDomain();
        try {
            returnChargeInfo = titanTemplate.post("BILLING-SERVER",
                    "billingServer/charge/findByKey", httpHeaders,
                    GsonUtils.toJson(chargeInfoDomain),
                    new TypeReference<ChargeInfoDomain>() {
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
        return returnChargeInfo;
    }

    //根据计算户id 月份 次数 查欠费记录
    public List<ArrearageDomain> findArrearageBySettleIdMonAndSn(ArrearageDomain arrearageDomain) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        List<ArrearageDomain> arrearageDomains = new ArrayList<>();
        try {
            arrearageDomains = titanTemplate.post("BILLING-SERVER",
                    "billingServer/arrearage/findBySettleIdMonAndSn", httpHeaders,
                    GsonUtils.toJson(arrearageDomain),
                    new TypeReference<List<ArrearageDomain>>() {
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
        return arrearageDomains;
    }

    //结算单打印 更新结算单状态
    public HttpResult updateNoteByIds(NoteInfoDomain noteInfoDomain) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        HttpResult httpResult = new HttpResult();
        try {
            httpResult = titanTemplate.post("BILLING-SERVER",
                    "billingServer/invoice/updateList", httpHeaders,
                    GsonUtils.toJson(noteInfoDomain),
                    new TypeReference<HttpResult>() {
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
        return httpResult;
    }


    //预收余额查询--时点余额查询
    public List<ChargeInfoDomain> findMaxIdBySettlementIds(ChargeInfoEntity chargeInfoEntity) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        List<ChargeInfoDomain> chargeDomains=new ArrayList<>();
        try {
            chargeDomains = titanTemplate.post("BILLING-SERVER",
                    "billingServer/charge/findMaxIdBySettlementIds", httpHeaders,
                    GsonUtils.toJson(chargeInfoEntity),
                    new TypeReference<List<ChargeInfoDomain>>() {
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
        return chargeDomains;
    }

    //预收余额查询--时点余额查询 根据实收id查询发票表记录
    public List<NoteInfoDomain> findNoteInfoByWhere(NoteInfoDomain noteInfoDomain) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        List<NoteInfoDomain> noteInfoDomains=new ArrayList<>();
        try {
            noteInfoDomains = titanTemplate.post("BILLING-SERVER",
                    "billingServer/invoice/findNoteInfoByWhere", httpHeaders,
                    GsonUtils.toJson(noteInfoDomain),
                    new TypeReference<List<NoteInfoDomain>>() {
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
        return noteInfoDomains;
    }


    //mongo抄表区段
    public List<WriteSectMongoDomain> getMongoWriteSect(WriteSectMongoDomain writeSectMongoDomain) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        List<WriteSectMongoDomain> writeSectMongoDomains=new ArrayList<>();
        try {
            writeSectMongoDomains = titanTemplate.post("BILLING-SERVER",
                    "billingServer/writeSect/getMongoWriteSect", httpHeaders,
                    GsonUtils.toJson(writeSectMongoDomain),
                    new TypeReference<List<WriteSectMongoDomain>>() {
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
        return writeSectMongoDomains;
    }

    //mongo计量点
    public List<MeterDomain> getMeterByWriteSectionIds(MeterDomain meterDomain) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        List<MeterDomain> meterDomains=new ArrayList<>();
        try {
            meterDomains = titanTemplate.post("BILLING-SERVER",
                    "billingServer/meter/getMeterByWriteSectionIds", httpHeaders,
                    GsonUtils.toJson(meterDomain),
                    new TypeReference<List<MeterDomain>>() {
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
        return meterDomains;
    }

    //按天或者支付方式分组 收费员月结
    public List<ChargeInfoDetailEntity> findChargeInfoDetailsGroupByDay(ChargeInfoEntity chargeInfoEntity) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);

        List<ChargeInfoDetailEntity> chargeInfoDetailEntities =new ArrayList<>();
        try {
            chargeInfoDetailEntities = titanTemplate.post("BILLING-SERVER",
                    "billingServer/charge/findChargeInfoDetailsGroupByDay", httpHeaders,
                    GsonUtils.toJson(chargeInfoEntity),
                    new TypeReference<List<ChargeInfoDetailEntity>>() {
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
        return chargeInfoDetailEntities;
    }

    //跨区收费报表
    public List<ChargeInfoDetailEntity> findCrossChargeInfoDetails(ChargeInfoEntity chargeInfoEntity) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);

        List<ChargeInfoDetailEntity> chargeInfoDetailEntities =new ArrayList<>();
        try {
            chargeInfoDetailEntities = titanTemplate.post("BILLING-SERVER",
                    "billingServer/charge/findCrossChargeInfoDetails", httpHeaders,
                    GsonUtils.toJson(chargeInfoEntity),
                    new TypeReference<List<ChargeInfoDetailEntity>>() {
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
        return chargeInfoDetailEntities;
    }
    //metermoney mysql findbywhere
    public List<MeterMoneyDomain> meterMoneyFindByWhere(MeterMoneyDomain meterMoneyDomain) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        List<MeterMoneyDomain> list = new ArrayList<>();
        try {
            list = titanTemplate.post("BILLING-SERVER",
                    "billingServer/MeterMoney/select", httpHeaders,
                    GsonUtils.toJson(meterMoneyDomain),
                    new TypeReference<List<MeterMoneyDomain>>() {
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    //电价值 分时电价取平段 阶梯电价取第一阶梯
    public List<PriceExecutionDomain> findTimeLadderPrice() {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        List<PriceExecutionDomain> list = new ArrayList<>();
        try {
            list = titanTemplate.post("BILLING-SERVER",
                    "billingServer/priceExecution/findTimeLadderPrice", httpHeaders,
                    GsonUtils.toJson(null),
                    new TypeReference<List<PriceExecutionDomain>>() {
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }


    //电量电费排行
    public List<ArrearageDomain> electricityTariffRankQuery(ElectricityTariffRankEntity electricityTariffRankEntity) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        List<ArrearageDomain> list = new ArrayList<>();
        try {
            list = titanTemplate.post("BILLING-SERVER",
                    "billingServer/arrearage/electricityTariffRankQuery", httpHeaders,
                    GsonUtils.toJson(electricityTariffRankEntity),
                    new TypeReference<List<ArrearageDomain>>() {
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<WriteFilesDomain> getWriteFiles(WriteFilesDomain writeFiles) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        List<WriteFilesDomain> writeFilesDomainList = new ArrayList<>();
        try {
            writeFilesDomainList = titanTemplate.post("BILLING-SERVER",
                    "billingServer/WriteFiles/getWriteFilesDomain", httpHeaders,
                    GsonUtils.toJson(writeFiles),
                    new TypeReference<List<WriteFilesDomain>>() {
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
        return writeFilesDomainList;
    }

    public List<MeterMoneyDomain> mongoFind(MeterMoneyDomain meterMoney) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        List<MeterMoneyDomain> list = new ArrayList<>();
        try {
            list = titanTemplate.post("BILLING-SERVER",
                    "billingServer/MeterMoney/mongoFind", httpHeaders,
                    GsonUtils.toJson(meterMoney),
                    new TypeReference<List<MeterMoneyDomain>>() {
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<MeterRelationDomain> getMeterRelation(MeterRelationDomain meterRelationDomain) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        List<MeterRelationDomain> list = new ArrayList<>();
        try {
            list = titanTemplate.post("BILLING-SERVER",
                    "billingServer/meterRelation/getMeterRelation", httpHeaders,
                    GsonUtils.toJson(meterRelationDomain),
                    new TypeReference<List<MeterRelationDomain>>() {
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public Integer getCurrentMon() {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        Integer currentMon = null;
        try {
            currentMon = titanTemplate.postJson("TITAN-CONFIG", "titan" +
                            "-config/sysCommConfig/getCurrentMon",
                    httpHeaders, null, Integer.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return currentMon;
    }


}
