package org.fms.report.server.webapp.service;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;
import java.util.stream.Collectors;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.fms.report.common.util.CustomCollectors;
import org.fms.report.common.util.FormatterUtil;
import org.fms.report.common.util.MonUtils;
import org.fms.report.common.webapp.bean.BankCollectionDetailBean;
import org.fms.report.common.webapp.bean.ElectricityTariffRankBean;
import org.fms.report.common.webapp.bean.FeeRecStatisticsBean;
import org.fms.report.common.webapp.bean.MeterBean;
import org.fms.report.common.webapp.bean.MeterMeterRelBean;
import org.fms.report.common.webapp.bean.MeterMoneyBean;
import org.fms.report.common.webapp.bean.TableDataBean;
import org.fms.report.common.webapp.bean.TransformerBean;
import org.fms.report.common.webapp.bean.WriteFilesBean;
import org.fms.report.common.webapp.domain.ArrearageDetailDomain;
import org.fms.report.common.webapp.domain.ArrearageDomain;
import org.fms.report.common.webapp.domain.BankEntity;
import org.fms.report.common.webapp.domain.ChargeInfoDomain;
import org.fms.report.common.webapp.domain.CustomerDomain;
import org.fms.report.common.webapp.domain.DeptDomain;
import org.fms.report.common.webapp.domain.ElectricityTariffRankEntity;
import org.fms.report.common.webapp.domain.LadderMongoDomain;
import org.fms.report.common.webapp.domain.LineDomain;
import org.fms.report.common.webapp.domain.MeterAssetsDomain;
import org.fms.report.common.webapp.domain.MeterDomain;
import org.fms.report.common.webapp.domain.MeterMeterAssetsRelDomain;
import org.fms.report.common.webapp.domain.MeterMoneyDomain;
import org.fms.report.common.webapp.domain.MeterRelationDomain;
import org.fms.report.common.webapp.domain.ParamDomain;
import org.fms.report.common.webapp.domain.PriceExecutionDomain;
import org.fms.report.common.webapp.domain.PriceLadderRelaDomain;
import org.fms.report.common.webapp.domain.PriceTypeDomain;
import org.fms.report.common.webapp.domain.SettlementDomain;
import org.fms.report.common.webapp.domain.SettlementMeterRelDomain;
import org.fms.report.common.webapp.domain.TransformerDomain;
import org.fms.report.common.webapp.domain.TransformerLineRelDomain;
import org.fms.report.common.webapp.domain.UserDomain;
import org.fms.report.common.webapp.domain.WriteFilesDomain;
import org.fms.report.common.webapp.domain.WriteFilesMongoDomain;
import org.fms.report.common.webapp.domain.WriteSectDomain;
import org.fms.report.common.webapp.returnDomain.MapEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.riozenc.titanTool.annotation.TransactionService;
import com.riozenc.titanTool.common.json.utils.GsonUtils;
import com.riozenc.titanTool.common.json.utils.JSONUtil;
import com.riozenc.titanTool.spring.web.client.TitanTemplate;

import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

@TransactionService
public class MeterMoneyService {
    private Log logger = LogFactory.getLog(MeterMoneyService.class);

    @Autowired
    private BillingService billingService;
    @Autowired
    private TitanTemplate titanTemplate;

    @Autowired
    private MeterService meterService;

    @Autowired
    private UserService userService;

    @Autowired
    private DeptService deptService;

    @Autowired
    private CimService cimService;

    @Autowired
    private MeterMeterAssetsRelService relService;


    //电量电费实时查询报表
    public List<MeterMoneyDomain> meterMoneyDetailQuery(MeterMoneyDomain meterMoneyDomain) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        List<MeterMoneyDomain> list = new ArrayList<>();
        try {
            list = titanTemplate.post("BILLING-SERVER",
                    "billingServer/MeterMoney/meterMoneyDetailQuery", httpHeaders,
                    GsonUtils.toJson(meterMoneyDomain),
                    new TypeReference<List<MeterMoneyDomain>>() {
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }


    public List<TableDataBean> select(ArrearageDetailDomain arrearageDetailDomain) {
        //月份集合
        List<Integer> mons = new ArrayList<>();
        mons.add(arrearageDetailDomain.getStartMon());
        if (!arrearageDetailDomain.getEndMon().equals(arrearageDetailDomain.getStartMon())) {
            mons = MonUtils.getMons(arrearageDetailDomain.getStartMon(), arrearageDetailDomain.getEndMon(), mons);
        }

        /*List<WriteSectDomain> writeSectListSummary = new ArrayList<>();*/
        DeptDomain dept = new DeptDomain();
        Map<Long, DeptDomain> deptMap = null;
        //遍历月份 按月份查询
        /*for (Integer mon : mons) {
            WriteSectDomain writeSect = new WriteSectDomain();
            writeSect.setMon(mon);
            //营业区域不为空时
            if (arrearageDetailDomain.getBusinessPlaceCode() != null) {
                dept = deptService.getDept(arrearageDetailDomain.getBusinessPlaceCode());
                List<DeptDomain> deptList = deptService.getDeptList(arrearageDetailDomain.getBusinessPlaceCode());
                deptList.add(dept);
                deptMap = deptList.stream().collect(Collectors.toMap(DeptDomain::getId, k -> k));
                if (deptList.size() > 1) {
                    List<Long> businessPlaceCodes = deptList.stream().map(DeptDomain::getId).collect(Collectors.toList());
                    writeSect.setBusinessPlaceCodes(businessPlaceCodes);
                } else {
                    writeSect.setBusinessPlaceCode(arrearageDetailDomain.getBusinessPlaceCode());
                }
            }
            //抄表区段不为空时
            if (arrearageDetailDomain.getWriteSectId() != null) {
                writeSect.setId(arrearageDetailDomain.getWriteSectId());
            }
            //抄表员
            if (arrearageDetailDomain.getWritorId() != null) {
                writeSect.setWritorId(arrearageDetailDomain.getWritorId());
            }
            writeSect.setPageSize(-1);
            List<WriteSectDomain> list = writeSectService.getWriteSect(writeSect);
            writeSectListSummary.addAll(list);
        }*/

        //抄表区段 mysql库
        WriteSectDomain writeSect = new WriteSectDomain();

        //营业区域不为空时
        if (arrearageDetailDomain.getBusinessPlaceCode() != null) {
            dept = deptService.getDept(arrearageDetailDomain.getBusinessPlaceCode());
            List<DeptDomain> deptList = deptService.getDeptList(arrearageDetailDomain.getBusinessPlaceCode());
            deptList.add(dept);
            deptMap = deptList.stream().collect(Collectors.toMap(DeptDomain::getId, k -> k));
            if (deptList.size() > 1) {
                List<Long> businessPlaceCodes = deptList.stream().map(DeptDomain::getId).collect(Collectors.toList());
                writeSect.setBusinessPlaceCodes(businessPlaceCodes);
            } else {
                writeSect.setBusinessPlaceCode(arrearageDetailDomain.getBusinessPlaceCode());
            }
        }
        //抄表区段不为空时
        if (arrearageDetailDomain.getWriteSectId() != null) {
            writeSect.setId(arrearageDetailDomain.getWriteSectId());
        }
        //抄表员
        if (arrearageDetailDomain.getWritorId() != null) {
            writeSect.setWritorId(arrearageDetailDomain.getWritorId());
        }
        writeSect.setPageSize(-1);
        List<WriteSectDomain> writeSectDomains = cimService.getWriteSectFindByWhere(writeSect);

        Map<Long, WriteSectDomain> writeSectDomainMap = writeSectDomains
                .stream().collect(Collectors.toMap(WriteSectDomain::getId, k -> k));

        //营业区域
        Map<Long, String> businssPlaceCodeMap = new HashMap<>();

        DeptDomain deptDomain = new DeptDomain();
        List<MapEntity> deptDeptIdlistmap = deptService.findIdMapByDomain(deptDomain);
        businssPlaceCodeMap = FormatterUtil.ListMapEntityToMap(deptDeptIdlistmap);


        //获取抄表区段id
        List<Long> writeSectIds =
                writeSectDomains.stream().map(WriteSectDomain::getId).distinct().collect(Collectors.toList());

        //电费数据
        List<MeterMoneyDomain> meterMoneyList = new ArrayList<>();

        for (Integer mon : mons) {
            MeterMoneyDomain m = new MeterMoneyDomain();
            m.setWriteSectIds(writeSectIds);
            m.setMon(mon);
            meterMoneyList.addAll(meterMoneyDetailQuery(m));
        }

       /* //获取退补转预存的退补记录
        ChargeInfoEntity chargeInfoEntity=new ChargeInfoEntity();
        chargeInfoEntity.setStartMon(String.valueOf(arrearageDetailDomain.getStartMon()));
        chargeInfoEntity.setEndMon(String.valueOf(arrearageDetailDomain.getEndMon()));
        chargeInfoEntity.setfChargeMode("6");
        chargeInfoEntity.setPageSize(-1);
        HttpResultPagination<ChargeInfoDetailEntity> chargeInfoDetails=
                billingService.findChargeInfoDetails(chargeInfoEntity);

        List<ChargeInfoDetailEntity> chargeInfoDetailEntities =
                                        chargeInfoDetails.getList();

        Map<Long,List<ChargeInfoDetailEntity>> ChargeInfoDetailMap=
                chargeInfoDetailEntities.stream().collect(Collectors.groupingBy(ChargeInfoDetailEntity::getMeterMoneyId));
*/
        //查抄表区段信息 mysql库
        WriteSectDomain paramWriteSectDomain = new WriteSectDomain();
        paramWriteSectDomain.setPageSize(-1);
        paramWriteSectDomain.setWriteSectionIds(writeSectIds);
        List<WriteSectDomain> writeSectList =
                cimService.getWriteSectFindByWhere(paramWriteSectDomain);

        Map<Long, WriteSectDomain> writeSectMap = writeSectList.stream().collect(Collectors.toMap(WriteSectDomain::getId, a -> a, (k1, k2) -> k1));

        //查计量点mysql库
        List<Long> meterIds = meterMoneyList.stream().filter(a -> a.getMeterId() != null).map(MeterMoneyDomain::getMeterId).distinct().collect(Collectors.toList());
        logger.info("计量点数量================" + meterIds.size());
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("meterIds", meterIds);
        List<MeterDomain> meterList = cimService.getMeterFindByWhere(paramMap);
        Map<Long, MeterDomain> meterMap = meterList.stream().collect(Collectors.toMap(MeterDomain::getId, k -> k));


        //查用户信息 mysql库
        List<Long> userIds =
                meterList.stream().map(MeterDomain::getUserId).distinct().collect(Collectors.toList());

        List<UserDomain> userInfoEntities = cimService.getUserByIds(userIds);

        Map<Long, UserDomain> userMap =
                userInfoEntities.stream().collect(Collectors.toMap(UserDomain::getId, k -> k));

        //抄表员信息
        List<Long> writorIds = writeSectList.stream().filter(i -> i.getWritorId() != null).map(WriteSectDomain::getWritorId).distinct().collect(Collectors.toList());
        Map<Long, UserDomain> writorMap = userService.findByIds(writorIds).stream().collect(Collectors.toMap(UserDomain::getId, k -> k));


        //获取线路信息
        Map<Long, LineDomain> lineDomainMap = null;
        Map<Long, TransformerLineRelDomain> transformerLineRelDomainMap = null;
        if ("line".equals(arrearageDetailDomain.getGroupBy())) {
            //获取计量点与线路关系
            int len = meterIds.size();
            List<TransformerLineRelDomain> transformerLineRelDomains = new ArrayList<>();

            //无电费记录 预收之类
            if (len != 0) {
                for (int m = 0; m < len / 4999 + 1; m++) {// 遍历次数

                    List<Long> tl = meterIds.subList(m * 4999,
                            (m + 1) * 4999 > len ? len : (m + 1) * 4999);

                    transformerLineRelDomains.addAll(cimService.findTransformerLineByMeterIds(tl));
                }
            }

            List<Long> lineIds =
                    transformerLineRelDomains.stream().map(TransformerLineRelDomain::getLineId).distinct().collect(Collectors.toList());

            transformerLineRelDomainMap =
                    transformerLineRelDomains.stream().collect(Collectors.toMap(TransformerLineRelDomain::getMeterId, a -> a, (k1, k2) -> k1));
            //获取线路详细信息
            LineDomain lineDomain = new LineDomain();
            lineDomain.setLineIds(lineIds);
            List<LineDomain> lineDomains = cimService.findByLineIds(lineDomain);
            logger.info("查询的线路================" + lineDomains.size());
            logger.info("变压器线路关系=======" + transformerLineRelDomainMap.keySet().size());

            lineDomainMap = lineDomains.stream().collect(Collectors.toMap(LineDomain::getId, a -> a, (k1, k2) -> k1));
        }

        Map<Long, DeptDomain> finalDeptMap = deptMap;

       /* Map<Integer, List<MeterMoneyDomain>> map =
                meterMoneyList.stream().collect(Collectors.groupingBy(MeterMoneyDomain::getMon));*/

        for (MeterMoneyDomain meterMoneyDomain : meterMoneyList) {
            /*
            // 抄表区段
            List<WriteSectDomain> writeSectList=writeSectMapByMon.get(mon);

            Map<Long, WriteSectDomain> writeSectMap = writeSectList.stream().collect(Collectors.toMap(WriteSectDomain::getId, a -> a, (k1, k2) -> k1));


            // 获取计量点
            List<Long> meterIds = meterMoneyList.stream().filter(a -> a.getMeterId() != null).map(MeterMoneyDomain::getMeterId).distinct().collect(Collectors.toList());
            MeterDomain meter = new MeterDomain();
            meter.setIds(meterIds);
            meter.setMon(mon);
            meter.setPageSize(-1);
            List<MeterDomain> meterList = meterService.getMeter(meter);
            Map<Long, MeterDomain> meterMap = meterList.stream().collect(Collectors.toMap(MeterDomain::getId, k -> k));

            // 获取客户
            List<Long> userIds = meterList.stream().filter(i -> i.getUserId() != null).map(MeterDomain::getUserId).distinct().collect(Collectors.toList());
            UserDomain user = new UserDomain();
            user.setIds(userIds);
            user.setMon(mon);
            user.setPageSize(-1);
            Map<Long, UserDomain> userMap = userService.getUser(user).stream().collect(Collectors.toMap(UserDomain::getId, k -> k));

            // 抄表员
            List<Long> writorIds = writeSectList.stream().filter(i -> i.getWritorId() != null).map(WriteSectDomain::getWritorId).distinct().collect(Collectors.toList());
            Map<Long, UserDomain> writorMap = userService.findByIds(writorIds).stream().collect(Collectors.toMap(UserDomain::getId, k -> k));
            */
            /*List<ChargeInfoDetailEntity> paramChargeInfoDetails=
                                    ChargeInfoDetailMap.get(meterMoneyDomain.getId());*/

            /*BigDecimal refundChargeMoney =BigDecimal.ZERO;

            if(paramChargeInfoDetails!=null && paramChargeInfoDetails.size()>0){
                refundChargeMoney=
                        paramChargeInfoDetails.stream().map(ChargeInfoDetailEntity::getFactTotal).reduce(BigDecimal.ZERO, BigDecimal::add);
            }*/

            /*meterMoneyDomain.setRefundChargeMoney(refundChargeMoney);*/

            if (writeSectMap.get(meterMoneyDomain.getWriteSectId()) != null) {
                meterMoneyDomain.setWriteSectName(writeSectMap.get(meterMoneyDomain.getWriteSectId()).getWriteSectName());
                meterMoneyDomain.setWritorId(writeSectMap.get(meterMoneyDomain.getWriteSectId()).getWritorId());
                meterMoneyDomain.setBusinessPlaceCode(writeSectMap.get(meterMoneyDomain.getWriteSectId()).getBusinessPlaceCode());
            }
            if (meterMap.get(meterMoneyDomain.getMeterId()) != null) {
                meterMoneyDomain.setElecTypeCode(meterMap.get(meterMoneyDomain.getMeterId()).getElecTypeCode());
                meterMoneyDomain.setUserId(meterMap.get(meterMoneyDomain.getMeterId()).getUserId());
                meterMoneyDomain.setTradeType(meterMap.get(meterMoneyDomain.getMeterId()).getTradeType());
            }
            if (userMap.get(meterMoneyDomain.getUserId()) != null) {
                meterMoneyDomain.setUserType(userMap.get(meterMoneyDomain.getUserId()).getUserType());
            }
            if (writorMap.get(meterMoneyDomain.getWritorId()) != null) {
                meterMoneyDomain.setWritorName(writorMap.get(meterMoneyDomain.getWritorId()).getUserName());
            }
            if (finalDeptMap != null && finalDeptMap.get(meterMoneyDomain.getBusinessPlaceCode()) != null) {
                meterMoneyDomain.setDeptName(finalDeptMap.get(meterMoneyDomain.getBusinessPlaceCode()).getDeptName());
            }
            if (transformerLineRelDomainMap != null && transformerLineRelDomainMap.get(meterMoneyDomain.getMeterId()) != null) {
                LineDomain lineDomain =
                        lineDomainMap.get(transformerLineRelDomainMap.get(meterMoneyDomain.getMeterId()).getLineId());
                if (lineDomain != null) {
                    meterMoneyDomain.setLineId(lineDomain.getId());
                    meterMoneyDomain.setLineName(lineDomain.getLineName());
                }
            }
        }
        ;
        Map<Object, List<MeterMoneyDomain>> listMap = new HashMap<>();


        if (arrearageDetailDomain.getGroupBy() != null) {
            // 按抄表区段分组
            if ("writeSect".equals(arrearageDetailDomain.getGroupBy())) {
                meterMoneyList =
                        meterMoneyList.stream().sorted(Comparator.comparing(MeterMoneyDomain::getWriteSectId, Comparator.nullsLast(Long::compareTo))).collect(Collectors.toList());
                listMap =
                        meterMoneyList.stream().filter(m -> m.getWriteSectId() != null).collect(Collectors.groupingBy(MeterMoneyDomain::getWriteSectId));
            }
            // 按抄表员分组
            if ("writor".equals(arrearageDetailDomain.getGroupBy())) {
                meterMoneyList =
                        meterMoneyList.stream().sorted(Comparator.comparing(MeterMoneyDomain::getWritorId, Comparator.nullsLast(Long::compareTo))).collect(Collectors.toList());

                listMap = meterMoneyList.stream().filter(m -> m.getWritorId() != null).collect(Collectors.groupingBy(MeterMoneyDomain::getWritorId));
                //无抄表员
                List<MeterMoneyDomain> meterMoneyDomains =
                        meterMoneyList.stream().filter(m -> m.getWritorId() == null).collect(Collectors.toList());
                if (meterMoneyDomains != null && meterMoneyDomains.size() > 0) {
                    listMap.put("#Z#",
                            meterMoneyList.stream().filter(m -> m.getWritorId() == null).collect(Collectors.toList()));
                }
            }
            // 按电价分组
            if ("priceType".equals(arrearageDetailDomain.getGroupBy())) {
                //获取电价
                meterMoneyList =
                        meterMoneyList.stream().sorted(Comparator.comparing(MeterMoneyDomain::getPriceTypeId, Comparator.nullsLast(Long::compareTo))).collect(Collectors.toList());

                List<PriceTypeDomain> priceTypeDomains = billingService.findPriceType();

                Map<Long, PriceTypeDomain> priceTypeDomainMap =
                        priceTypeDomains.stream().filter(m -> m.getId() != null).collect(Collectors.toMap(PriceTypeDomain::getId, a -> a, (k1, k2) -> k1));

                listMap =
                        meterMoneyList.stream().filter(m -> m.getPriceTypeId() != null).collect(Collectors.groupingBy(MeterMoneyDomain::getPriceTypeId));

                listMap.forEach((k, v) -> {
                    v.get(0).setPriceName(priceTypeDomainMap.get(k).getPriceName());
                });
            }
            // 按客户类别分组
            if ("userType".equals(arrearageDetailDomain.getGroupBy())) {
                meterMoneyList =
                        meterMoneyList.stream().sorted(Comparator.comparing(MeterMoneyDomain::getUserType, Comparator.nullsLast(Byte::compareTo))).collect(Collectors.toList());
                Map<Long, String> sysMap =
                        cimService.findSystemCommonConfigByType("CUSTOMER_TYPE");

                listMap =
                        meterMoneyList.stream().filter(m -> m.getUserType() != null).collect(Collectors.groupingBy(MeterMoneyDomain::getUserType));

                listMap.forEach((k, v) -> {
                    if (k != null) {
                        v.get(0).setUserTypeName(sysMap.get(Long.valueOf(String.valueOf(k))));
                    }
                });
                List<MeterMoneyDomain> meterMoneyDomains =
                        meterMoneyList.stream().filter(m -> m.getUserType() == null).collect(Collectors.toList());
                if (meterMoneyDomains != null && meterMoneyDomains.size() > 0) {
                    listMap.put("#Z#",
                            meterMoneyList.stream().filter(m -> m.getUserType() == null).collect(Collectors.toList()));
                }

            }
            // 按营业区域分组
            if ("businessPlaceCode".equals(arrearageDetailDomain.getGroupBy())) {
                meterMoneyList =
                        meterMoneyList.stream().sorted(Comparator.comparing(MeterMoneyDomain::getBusinessPlaceCode, Comparator.nullsLast(Long::compareTo))).collect(Collectors.toList());

                listMap = meterMoneyList.stream().filter(m -> m.getBusinessPlaceCode() != null).collect(Collectors.groupingBy(MeterMoneyDomain::getBusinessPlaceCode));
            }
            // 按用电分类分组
            if ("elecTypeCode".equals(arrearageDetailDomain.getGroupBy())) {
                meterMoneyList =
                        meterMoneyList.stream().sorted(Comparator.comparing(MeterMoneyDomain::getElecTypeCode, Comparator.nullsLast(Integer::compareTo))).collect(Collectors.toList());

                Map<Long, String> sysMap =
                        cimService.findSystemCommonConfigByType("ELEC_TYPE");
                listMap =
                        meterMoneyList.stream().filter(m -> m.getElecTypeCode() != null).collect(Collectors.groupingBy(MeterMoneyDomain::getElecTypeCode));

                listMap.forEach((k, v) -> {
                    if (k != null) {
                        v.get(0).setElecTypeName(sysMap.get(Long.valueOf(String.valueOf(k))));
                    }
                });
                List<MeterMoneyDomain> meterMoneyDomains =
                        meterMoneyList.stream().filter(m -> m.getElecTypeCode() == null).collect(Collectors.toList());

                if (meterMoneyDomains != null && meterMoneyDomains.size() > 0) {
                    listMap.put("#Z#",
                            meterMoneyList.stream().filter(m -> m.getElecTypeCode() == null).collect(Collectors.toList()));
                }

            }

            // 按线路分组
            if ("line".equals(arrearageDetailDomain.getGroupBy())) {
                meterMoneyList =
                        meterMoneyList.stream().sorted(Comparator.comparing(MeterMoneyDomain::getLineId, Comparator.nullsLast(Long::compareTo))).collect(Collectors.toList());

                listMap =
                        meterMoneyList.stream().filter(m -> m.getLineId() != null).collect(Collectors.groupingBy(MeterMoneyDomain::getLineId));
                //无线路
                List<MeterMoneyDomain> meterMoneyDomains =
                        meterMoneyList.stream().filter(m -> m.getLineId() == null).collect(Collectors.toList());

                if (meterMoneyDomains != null && meterMoneyDomains.size() > 0) {
                    listMap.put("#Z#",
                            meterMoneyList.stream().filter(m -> m.getLineId() == null).collect(Collectors.toList()));

                    String nolinString =
                            meterMoneyDomains.stream().map(MeterMoneyDomain::getMeterId).map(String::valueOf).collect(Collectors.joining(","));
                    logger.info((arrearageDetailDomain.getBusinessPlaceCode() == null ? "" : arrearageDetailDomain.getBusinessPlaceCode()) + "无线路计量点======" + nolinString);
                }
            }

            // 按行业分类分组
            if ("tradeType".equals(arrearageDetailDomain.getGroupBy())) {
                meterMoneyList =
                        meterMoneyList.stream().sorted(Comparator.comparing(MeterMoneyDomain::getTradeType, Comparator.nullsLast(Integer::compareTo))).collect(Collectors.toList());

                Map<Long, String> sysMap =
                        cimService.findSystemCommonConfigByType("TRADE_TYPE");
                listMap =
                        meterMoneyList.stream().filter(m -> m.getTradeType() != null).filter(m -> sysMap.keySet().contains(Long.valueOf(m.getTradeType()))).collect(Collectors.groupingBy(MeterMoneyDomain::getTradeType));

                listMap.forEach((k, v) -> {
                    if (k != null) {
                        v.get(0).setTradeTypeName(sysMap.get(Long.valueOf(String.valueOf(k))));
                    }
                });
                List<MeterMoneyDomain> meterMoneyDomains =
                        meterMoneyList.stream().filter(m -> m.getTradeType() == null).collect(Collectors.toList());

                if (meterMoneyDomains != null && meterMoneyDomains.size() > 0) {
                    listMap.put("#Z#",
                            meterMoneyList.stream().filter(m -> m.getTradeType() == null).collect(Collectors.toList()));
                }

            }
        }

        TableDataBean tableData = new TableDataBean();
        List<MeterMoneyBean> beanList = new ArrayList<>();
        for (Object k : listMap.keySet()) {

            List<MeterMoneyDomain> list = listMap.get(k);
            try {
                MeterMoneyBean bean = null;
                bean = JSONUtil.readValue(JSONUtil.toJsonString(list.get(0)), new TypeReference<MeterMoneyBean>() {
                });
                if ("writeSect".equals(arrearageDetailDomain.getGroupBy())) {
                    bean.setName("抄表区段");
                    bean.setValue(writeSectDomainMap.get(list.get(0).getWriteSectId()).getWriteSectName());
                }
                // 按抄表员分组
                if ("writor".equals(arrearageDetailDomain.getGroupBy())) {
                    bean.setName("抄表员");
                    // 抄表员姓名
                    if ("#Z#".equals(k)) {
                        bean.setValue("无抄表员");
                    } else {
                        bean.setValue(list.get(0).getWritorName());
                    }
                }
                // 按电价分组
                if ("priceType".equals(arrearageDetailDomain.getGroupBy())) {
                    bean.setName("电价分类");
                    // 电价分类 待完善
                    bean.setValue(list.get(0).getPriceName());
                }
                // 按客户类别分组
                if ("userType".equals(arrearageDetailDomain.getGroupBy())) {
                    bean.setName("客户类别");
                    // 客户类别名称 待完善
                    if ("#Z#".equals(k)) {
                        bean.setValue("无客户类别");
                    } else {
                        bean.setValue(list.get(0).getUserTypeName());
                    }
                }
                // 按营业区域分组
                if ("businessPlaceCode".equals(arrearageDetailDomain.getGroupBy())) {
                    bean.setName("营业区域");
                    bean.setValue(businssPlaceCodeMap.get(list.get(0).getBusinessPlaceCode()));
                }
                // 按用电分类分组
                if ("elecTypeCode".equals(arrearageDetailDomain.getGroupBy())) {
                    bean.setName("用电分类");
                    if ("#Z#".equals(k)) {
                        bean.setValue("无用电分类");
                    } else {
                        bean.setValue(list.get(0).getElecTypeName());
                    }
                }
                // 按用电分类分组
                if ("line".equals(arrearageDetailDomain.getGroupBy())) {
                    bean.setName("线路");
                    if ("#Z#".equals(k)) {
                        bean.setValue("无线路");
                    } else {
                        bean.setValue(list.get(0).getLineName());
                    }
                }

                // 按用电分类分组
                if ("tradeType".equals(arrearageDetailDomain.getGroupBy())) {
                    bean.setName("行业用电分类");
                    if ("#Z#".equals(k)) {
                        bean.setValue("无行业用电分类");
                    } else {
                        bean.setValue(list.get(0).getTradeTypeName());
                    }
                }
                bean.setOne(list.stream().filter(m -> m.getAddMoney1() != null).map(MeterMoneyDomain::getAddMoney1).reduce(BigDecimal.ZERO, BigDecimal::add));
                bean.setTwo(list.stream().filter(m -> m.getAddMoney2() != null).map(MeterMoneyDomain::getAddMoney2).reduce(BigDecimal.ZERO, BigDecimal::add));
                bean.setThree(list.stream().filter(m -> m.getAddMoney3() != null).map(MeterMoneyDomain::getAddMoney3).reduce(BigDecimal.ZERO, BigDecimal::add));
                bean.setFour(list.stream().filter(m -> m.getAddMoney4() != null).map(MeterMoneyDomain::getAddMoney4).reduce(BigDecimal.ZERO, BigDecimal::add));
                bean.setFive(list.stream().filter(m -> m.getAddMoney5() != null).map(MeterMoneyDomain::getAddMoney5).reduce(BigDecimal.ZERO, BigDecimal::add));
                bean.setSix(list.stream().filter(m -> m.getAddMoney6() != null).map(MeterMoneyDomain::getAddMoney6).reduce(BigDecimal.ZERO, BigDecimal::add));
                bean.setSeven(list.stream().filter(m -> m.getAddMoney7() != null).map(MeterMoneyDomain::getAddMoney7).reduce(BigDecimal.ZERO, BigDecimal::add));
                bean.setEight(list.stream().filter(m -> m.getAddMoney8() != null).map(MeterMoneyDomain::getAddMoney8).reduce(BigDecimal.ZERO, BigDecimal::add));
                //bean.setNine(list.stream().filter(m -> m.getAddMoney9() !=
                // null).map(MeterMoneyDomain::getAddMoney9).reduce(BigDecimal.ZERO,BigDecimal::add));
                // 计量点数去重统计
                List<MeterMoneyDomain> meters = list.stream().filter(m -> m.getMeterId() != null).collect(Collectors.collectingAndThen(Collectors.toCollection(() ->
                        new TreeSet<>(Comparator.comparing(t -> t.getMeterId()))), ArrayList::new));
                /*BigDecimal refundChargeMoney=
                        list.stream().filter(m -> m.getRefundChargeMoney() != null).map(MeterMoneyDomain::getRefundChargeMoney).reduce(BigDecimal.ZERO, BigDecimal::add);
               */
                bean.setMeters(meters.size());
                bean.setRefundMoney(list.stream().filter(m -> m.getRefundMoney() != null).map(MeterMoneyDomain::getRefundMoney).reduce(BigDecimal.ZERO, BigDecimal::add));
                bean.setRefundMoney(bean.getRefundMoney().negate());
                bean.setTotalPower(list.stream().filter(m -> m.getTotalPower() != null).map(MeterMoneyDomain::getTotalPower).reduce(BigDecimal.ZERO, BigDecimal::add));
                bean.setVolumeCharge(list.stream().filter(m -> m.getVolumeCharge() != null).map(MeterMoneyDomain::getVolumeCharge).reduce(BigDecimal.ZERO, BigDecimal::add));
                bean.setPowerRateMoney(list.stream().filter(m -> m.getPowerRateMoney() != null).map(MeterMoneyDomain::getPowerRateMoney).reduce(BigDecimal.ZERO, BigDecimal::add));
                bean.setBasicMoney(list.stream().filter(m -> m.getBasicMoney() != null).map(MeterMoneyDomain::getBasicMoney).reduce(BigDecimal.ZERO, BigDecimal::add));
                bean.setShouldMoney(list.stream().filter(m -> m.getAmount() != null).map(MeterMoneyDomain::getAmount).reduce(BigDecimal.ZERO, BigDecimal::add));
                bean.setAmount(bean.getShouldMoney());
                beanList.add(bean);
            } catch (JsonParseException e) {
                e.printStackTrace();
            } catch (JsonMappingException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        ;
        tableData.setDate(new Date());
        if (beanList.size() > 0) {
            if (arrearageDetailDomain.getBusinessPlaceCode() != null) {
                tableData.setvName("营业区域:");
                tableData.setvValue(dept.getDeptName());
            }
            if (arrearageDetailDomain.getWriteSectId() != null) {
                tableData.setvName("抄表区段:");
                tableData.setvValue(beanList.get(0).getWriteSectName());
            }
            if (arrearageDetailDomain.getWritorId() != null) {
                tableData.setvName("抄表员:");
                tableData.setvValue(beanList.get(0).getWritorName());
            }
            tableData.setStartMon(arrearageDetailDomain.getStartMon());
            tableData.setEndMon(arrearageDetailDomain.getEndMon());
            tableData.setPrintDate(MonUtils.getMon());
        }
        beanList =
                beanList.stream().sorted(Comparator.comparing(MeterMoneyBean::getValue, Comparator.nullsLast(String::compareTo))).collect(Collectors.toList());
        tableData.setTableData(new JRBeanCollectionDataSource(beanList));
        List<TableDataBean> tableDataList = new ArrayList<>();
        tableDataList.add(tableData);
        return tableDataList;


    }

    //托收清单
    public List<TableDataBean> findBankCollectDetail(SettlementDomain settlementDomain) {
        //营业区域不为空时
        DeptDomain dept = new DeptDomain();
        Map<Long, DeptDomain> deptDomainMap = new HashMap<>();
        if (settlementDomain.getBusinessPlaceCode() != null) {
            dept = deptService.getDept(settlementDomain.getBusinessPlaceCode());
            List<DeptDomain> deptList = deptService.getDeptList(settlementDomain.getBusinessPlaceCode());
            deptList.add(dept);
            deptList.forEach(t -> {
                if (t.getTitle() == null || "".equals(t.getTitle())) {
                    t.setTitle(t.getDeptName());
                }
            });
            deptDomainMap =
                    deptList.stream().collect(Collectors.toMap(DeptDomain::getId, k -> k, (k1, k2) -> k1));
            if (deptList.size() > 1) {
                settlementDomain.setBusinessPlaceCode(null);
                List<Long> businessPlaceCodes = deptList.stream().map(DeptDomain::getId).collect(Collectors.toList());
                settlementDomain.setBusinessPlaceCodes(businessPlaceCodes);
            } else {
                settlementDomain.setBusinessPlaceCode(settlementDomain.getBusinessPlaceCode());
            }
        } else {
            ParamDomain paramDomain = new ParamDomain();
            // paramDomain.setPageSize(-1);
            List<DeptDomain> deptList = deptService.findByWhere(paramDomain);
            deptList.forEach(t -> {
                if (t.getTitle() == null || "".equals(t.getTitle())) {
                    t.setTitle(t.getDeptName());
                }
            });
            deptDomainMap = deptList.stream().filter(m -> m.getId() != null)
                    .collect(Collectors.toMap(DeptDomain::getId, a -> a, (k1, k2) -> k1));
        }
        //托收结算户
        List<SettlementDomain> settlementDomainList = cimService.findBankSettlement(settlementDomain);
        //无结算户信息
        if (settlementDomainList == null || settlementDomainList.size() < 1) {
            return new ArrayList<>();
        }
        List<Long> settlementIds =
                settlementDomainList.stream().map(SettlementDomain::getId).distinct().collect(Collectors.toList());
        //结算户id
        Map<Long, SettlementDomain> settlementDomainMap =
                settlementDomainList.stream().collect(Collectors.toMap(SettlementDomain::getId, a -> a, (k1, k2) -> k1));
        //获取结算户与计量点关系
        List<SettlementMeterRelDomain> settlementMeterRelDomains =
                cimService.getMeterIdsBySettlements(settlementDomainList);
        //获取计量点
        List<Long> meterIds =
                settlementMeterRelDomains.stream().map(SettlementMeterRelDomain::getMeterId).distinct().collect(Collectors.toList());

        //获取计量点和结算户map
        Map<Long, SettlementDomain> meterSettlementRelMap =
                settlementMeterRelDomains.stream().filter(t -> t.getMeterId() != null).filter(t -> t.getSettlementId() != null)
                        .collect(Collectors.toMap(SettlementMeterRelDomain::getMeterId, a -> settlementDomainMap.get(a.getSettlementId()), (k1, k2) -> k1));

        //获取抄表记录
        WriteFilesDomain paramWriteFile = new WriteFilesDomain();
        paramWriteFile.setMon(settlementDomain.getMon());
        paramWriteFile.setMeterIds(meterIds);
        List<WriteFilesDomain> writeFilesDomains =
                billingService.findWriteFilesByMonAndMeterIds(paramWriteFile);
        if (writeFilesDomains == null || writeFilesDomains.size() < 1) {
            return new ArrayList<>();
        }


        //获取电费记录
        MeterMoneyDomain paramMeterMoney = new MeterMoneyDomain();
        paramMeterMoney.setMon(settlementDomain.getMon());
        paramMeterMoney.setMeterIds(meterIds);
        List<MeterMoneyDomain> meterMoneyDomains =
                billingService.findMeterMoneyByMonAndMeterIds(paramMeterMoney);
        if (meterMoneyDomains == null || meterMoneyDomains.size() < 1) {
            return new ArrayList<>();
        }
        //删除电费是0的计量点
        List<Long> zeroMeterMoneyMeterId =
                meterMoneyDomains.stream().filter(t -> t.getAmount().compareTo(BigDecimal.ZERO) == 0)
                        .map(MeterMoneyDomain::getMeterId).distinct().collect(Collectors.toList());

        Map<String, MeterMoneyDomain> meterMoneyDomainMap =
                meterMoneyDomains.stream().filter(m -> m.getId() != null)
                        .collect(Collectors.toMap(o -> o.getMon() + "_" + o.getSn() + "_" + o.getMeterId(), a -> a, (k1, k2) -> k1));
        //获取电价
        List<PriceExecutionDomain> priceExecutionDomains = billingService.findTimeLadderPrice();

        Map<Long, PriceExecutionDomain> priceExecutionDomainMap =
                priceExecutionDomains.stream().filter(m -> m.getPriceTypeId() != null).collect(Collectors.toMap(PriceExecutionDomain::getPriceTypeId, a -> a, (k1, k2) -> k1));

        //营业区域
        /*ParamDomain paramDomain = new ParamDomain();
        //paramDomain.setPageSize(-1);
        List<DeptDomain> deptList = deptService.findByWhere(paramDomain);
        Map<Long, DeptDomain> deptDomainMap = deptList.stream().filter(m -> m.getId() != null).collect(Collectors.toMap(DeptDomain::getId, a -> a, (k1, k2) -> k1));
*/
        //收费记录
        ChargeInfoDomain chargeInfoDomain = new ChargeInfoDomain();
        chargeInfoDomain.setfChargeMode((short) 6);
        chargeInfoDomain.setMon(settlementDomain.getMon());
        chargeInfoDomain.setSettlementIds(settlementIds);
        List<ChargeInfoDomain> chargeInfoDomains =
                billingService.chargeBySettleIds(chargeInfoDomain);
        //分组求和 记录
        Map<Long, BigDecimal> chargeInfoDomainMap =
                chargeInfoDomains.stream().filter(t -> t.getFactTotal() != null)
                        .collect(Collectors.groupingBy(ChargeInfoDomain::getSettlementId, CustomCollectors.summingBigDecimal(ChargeInfoDomain::getFactTotal)));

        //表序号
        MeterMeterAssetsRelDomain meterMeterAssetsRelDomain =
                new MeterMeterAssetsRelDomain();
        meterMeterAssetsRelDomain.setMeterIds(meterIds);
        meterMeterAssetsRelDomain.setMon(settlementDomain.getMon());
        meterMeterAssetsRelDomain.setPageSize(-1);
        List<MeterMeterAssetsRelDomain> meterMeterAssetsRelDomains = relService.getRel(meterMeterAssetsRelDomain);
        Map<Long, MeterMeterAssetsRelDomain> meterMeterAssetsRelDomainMap = meterMeterAssetsRelDomains.stream()
                .collect(Collectors.toMap(MeterMeterAssetsRelDomain::getMeterAssetsId, a -> a, (k1, k2) -> k1));

        Map<Long, List<MeterMeterAssetsRelDomain>> meterMeterAssetsRelDomainMapByMeterId =
                meterMeterAssetsRelDomains.stream()
                        .collect(Collectors.groupingBy(MeterMeterAssetsRelDomain::getMeterId));


        //writeFilesDomains
        //赋值排序
        writeFilesDomains.stream().forEach(t -> {
            MeterMeterAssetsRelDomain meterMeterAssetsRelExp =
                    meterMeterAssetsRelDomainMap.get(t.getMeterAssetsId());
            if (meterMeterAssetsRelExp == null) {
                t.setMeterSn((long) 1);
                if (t.getFunctionCode() == null) {
                    t.setFunctionCode((long) 1);
                }
            } else {
                t.setMeterSn(meterMeterAssetsRelExp.getMeterSn() == null ? 1 :
                        meterMeterAssetsRelExp.getMeterSn().longValue());
                if (t.getFunctionCode() == null) {
                    t.setFunctionCode(meterMeterAssetsRelExp.getFunctionCode() == null ? 1 :
                            meterMeterAssetsRelExp.getFunctionCode().longValue());
                }
            }
            SettlementDomain settleExp =
                    meterSettlementRelMap.get(t.getMeterId());
            t.setSettlementId(settleExp.getId());

        });
        //按月份 结算户 表序号排序

        List<BankCollectionDetailBean> beanList = new ArrayList<>();
        //去掉电费是0的计量点
        writeFilesDomains =
                writeFilesDomains.stream().filter(t -> !zeroMeterMoneyMeterId.contains(t.getMeterId())).collect(Collectors.toList());

        writeFilesDomains =
                writeFilesDomains.stream()
                        .sorted(Comparator.comparing(WriteFilesDomain::getMon)
                                .thenComparing(WriteFilesDomain::getSettlementId)
                                .thenComparing(WriteFilesDomain::getFunctionCode, Comparator.nullsLast(Long::compareTo))
                                .thenComparing(WriteFilesDomain::getMeterSn, Comparator.nullsLast(Long::compareTo)))
                        .collect(Collectors.toList());

        for (WriteFilesDomain writeFilesDomain : writeFilesDomains) {

            List<Long> bankCollectionMeterIds =
                    beanList.stream().map(BankCollectionDetailBean::getMeterId).distinct().collect(Collectors.toList());

            BankCollectionDetailBean bankCollectionDetailBean =
                    new BankCollectionDetailBean();
            SettlementDomain settleExp =
                    meterSettlementRelMap.get(writeFilesDomain.getMeterId());
            if (settleExp == null) {
                continue;
            }
            bankCollectionDetailBean.setSettlementNo(settleExp.getSettlementNo());
            bankCollectionDetailBean.setSettlementName(settleExp.getSettlementName());
            bankCollectionDetailBean.setAddress(settleExp.getAddress());
            bankCollectionDetailBean.setBusinessPlaceCodeName(deptDomainMap.get(settleExp.getBusinessPlaceCode()).getTitle());
            bankCollectionDetailBean.setMon(writeFilesDomain.getMon());
            bankCollectionDetailBean.setStartNum(writeFilesDomain.getStartNum());
            bankCollectionDetailBean.setEndNum(writeFilesDomain.getEndNum());
            bankCollectionDetailBean.setFactorNum(writeFilesDomain.getFactorNum().intValue());
            bankCollectionDetailBean.setSn((int) writeFilesDomain.getSn());
            bankCollectionDetailBean.setMeterSn(writeFilesDomain.getMeterSn().intValue());
            bankCollectionDetailBean.setFunctionCode(writeFilesDomain.getFunctionCode());
            String meterMoneyKey =
                    writeFilesDomain.getMon() + "_" + writeFilesDomain.getSn() + "_" + writeFilesDomain.getMeterId();
            MeterMoneyDomain meterMoneyExp =
                    meterMoneyDomainMap.get(meterMoneyKey);
            if (meterMoneyExp != null && writeFilesDomain.getFunctionCode() != Long.valueOf(2) && !bankCollectionMeterIds.contains(writeFilesDomain.getMeterId())) {
                if (priceExecutionDomainMap.get(meterMoneyExp.getPriceTypeId()) == null || priceExecutionDomainMap.get(meterMoneyExp.getPriceTypeId()).getPrice() == null) {
                    bankCollectionDetailBean.setPriceName("0");
                } else {
                    bankCollectionDetailBean.setPriceName(priceExecutionDomainMap.get(meterMoneyExp.getPriceTypeId()).getPrice().toString());
                }
                bankCollectionDetailBean.setAddPower(meterMoneyExp.getAddPower().add(meterMoneyExp.getChgPower()).add(meterMoneyExp.getActiveDeductionPower()));
                bankCollectionDetailBean.setActiveTransformerLossPower(meterMoneyExp.getActiveTransformerLossPower());
                bankCollectionDetailBean.setTotalPower(meterMoneyExp.getTotalPower());
                bankCollectionDetailBean.setVolumeCharge(meterMoneyExp.getVolumeCharge());
                bankCollectionDetailBean.setBasicMoney(meterMoneyExp.getBasicMoney());
                bankCollectionDetailBean.setPowerRateMoney(meterMoneyExp.getPowerRateMoney());

                //批量退费
                BigDecimal rpMoney =
                        chargeInfoDomainMap.get(settleExp.getId()) == null ?
                                BigDecimal.ZERO : chargeInfoDomainMap.get(settleExp.getId());
                // 疫情退补+批量退费
                bankCollectionDetailBean.setRefundMoney(meterMoneyExp.getRefundMoney().add(rpMoney));
                bankCollectionDetailBean.setRefundMoney(bankCollectionDetailBean.getRefundMoney().negate());

                if (bankCollectionDetailBean.getRefundMoney() == null) {
                    bankCollectionDetailBean.setRefundMoney(BigDecimal.ZERO);
                }
                //总电费 100%
                bankCollectionDetailBean.setTotalMoney(meterMoneyExp.getAmount().add(meterMoneyExp.getRefundMoney()));
                // 应收电费
                bankCollectionDetailBean.setShouldMoney(bankCollectionDetailBean.getTotalMoney().add(bankCollectionDetailBean.getRefundMoney()));

                logger.info("===refundMoney:" + bankCollectionDetailBean.getRefundMoney() + "TotalMoney:" + bankCollectionDetailBean.getTotalMoney() + "ShouldMoeny:" + bankCollectionDetailBean.getShouldMoney());
            } else {
                bankCollectionDetailBean.setPriceName("-");
                bankCollectionDetailBean.setAddPower(BigDecimal.ZERO);
                bankCollectionDetailBean.setActiveTransformerLossPower(BigDecimal.ZERO);
                bankCollectionDetailBean.setTotalPower(BigDecimal.ZERO);
                bankCollectionDetailBean.setVolumeCharge(BigDecimal.ZERO);
                bankCollectionDetailBean.setBasicMoney(BigDecimal.ZERO);
                bankCollectionDetailBean.setPowerRateMoney(BigDecimal.ZERO);
                bankCollectionDetailBean.setRefundMoney(BigDecimal.ZERO);
                bankCollectionDetailBean.setTotalMoney(BigDecimal.ZERO);
                bankCollectionDetailBean.setShouldMoney(BigDecimal.ZERO);
            }
            bankCollectionDetailBean.setMeterId(writeFilesDomain.getMeterId());
            beanList.add(bankCollectionDetailBean);
        }

        List<String> writeFileMeterIds =
                beanList.stream().map(o -> o.getMon() + "_" + o.getSn() + "_" + o.getMeterId()).distinct().collect(Collectors.toList());

        meterMoneyDomains =
                meterMoneyDomains.stream().filter(t -> t.getAmount().compareTo(BigDecimal.ZERO) != 0).collect(Collectors.toList());

        //添加无抄表记录信息
        for (MeterMoneyDomain meterMoneyDomain : meterMoneyDomains) {

            String key =
                    meterMoneyDomain.getMon() + "_" + meterMoneyDomain.getSn() + "_" + meterMoneyDomain.getMeterId();

            if (writeFileMeterIds.contains(key)) {
                continue;
            }

            List<Long> bankCollectionMeterIds =
                    beanList.stream().map(BankCollectionDetailBean::getMeterId).distinct().collect(Collectors.toList());


            BankCollectionDetailBean bankCollectionDetailBean = new BankCollectionDetailBean();

            SettlementDomain settleExp = meterSettlementRelMap.get(meterMoneyDomain.getMeterId());
            if (settleExp == null) {
                continue;
            }
            //一个计量点下多个虚拟表
            List<MeterMeterAssetsRelDomain> meterAssetsRelDomains =
                    meterMeterAssetsRelDomainMapByMeterId.get(meterMoneyDomain.getMeterId());

            for (MeterMeterAssetsRelDomain meterAssetsRelDomain : meterAssetsRelDomains) {
                bankCollectionDetailBean.setSettlementNo(settleExp.getSettlementNo());
                bankCollectionDetailBean.setSettlementName(settleExp.getSettlementName());
                bankCollectionDetailBean.setAddress(settleExp.getAddress());
                bankCollectionDetailBean.setBusinessPlaceCodeName(deptDomainMap.get(settleExp.getBusinessPlaceCode()).getTitle());
                bankCollectionDetailBean.setMon(meterMoneyDomain.getMon());
                bankCollectionDetailBean.setStartNum(BigDecimal.ZERO);
                bankCollectionDetailBean.setEndNum(BigDecimal.ZERO);
                bankCollectionDetailBean.setSn(meterMoneyDomain.getSn().intValue());

                bankCollectionDetailBean.setFactorNum(meterAssetsRelDomain.getFactorNum() == null ? 1 : meterAssetsRelDomain.getFactorNum().intValue());
                bankCollectionDetailBean.setMeterSn(meterAssetsRelDomain.getMeterSn() == null ? 1 : meterAssetsRelDomain.getMeterSn().intValue());
                bankCollectionDetailBean.setFunctionCode(meterAssetsRelDomain.getFunctionCode() == null ? 1 : meterAssetsRelDomain.getFunctionCode().longValue());

                if (!bankCollectionMeterIds.contains(meterMoneyDomain.getMeterId())) {
                    if (priceExecutionDomainMap.get(meterMoneyDomain.getPriceTypeId()) == null || priceExecutionDomainMap.get(meterMoneyDomain.getPriceTypeId()).getPrice() == null) {
                        bankCollectionDetailBean.setPriceName("0");
                    } else {
                        bankCollectionDetailBean.setPriceName(priceExecutionDomainMap.get(meterMoneyDomain.getPriceTypeId()).getPrice().toString());
                    }
                    bankCollectionDetailBean.setAddPower(meterMoneyDomain.getAddPower());
                    bankCollectionDetailBean.setActiveTransformerLossPower(meterMoneyDomain.getActiveTransformerLossPower());
                    bankCollectionDetailBean.setTotalPower(meterMoneyDomain.getTotalPower());
                    bankCollectionDetailBean.setVolumeCharge(meterMoneyDomain.getVolumeCharge());
                    bankCollectionDetailBean.setBasicMoney(meterMoneyDomain.getBasicMoney());
                    bankCollectionDetailBean.setPowerRateMoney(meterMoneyDomain.getPowerRateMoney());
                    BigDecimal rpMoney =
                            chargeInfoDomainMap.get(settleExp.getId()) == null ?
                                    BigDecimal.ZERO : chargeInfoDomainMap.get(settleExp.getId());

                    // 疫情退补+批量退费
                    bankCollectionDetailBean.setRefundMoney(meterMoneyDomain.getRefundMoney().add(rpMoney));
                    bankCollectionDetailBean.setRefundMoney(bankCollectionDetailBean.getRefundMoney().negate());

                    if (bankCollectionDetailBean.getRefundMoney() == null) {
                        bankCollectionDetailBean.setRefundMoney(BigDecimal.ZERO);
                    }
                    //总电费 100%
                    bankCollectionDetailBean.setTotalMoney(meterMoneyDomain.getAmount().add(meterMoneyDomain.getRefundMoney()));
                    // 应收电费
                    bankCollectionDetailBean.setShouldMoney(bankCollectionDetailBean.getTotalMoney().add(bankCollectionDetailBean.getRefundMoney()));
                } else {
                    bankCollectionDetailBean.setPriceName("-");
                    bankCollectionDetailBean.setAddPower(BigDecimal.ZERO);
                    bankCollectionDetailBean.setActiveTransformerLossPower(BigDecimal.ZERO);
                    bankCollectionDetailBean.setTotalPower(BigDecimal.ZERO);
                    bankCollectionDetailBean.setVolumeCharge(BigDecimal.ZERO);
                    bankCollectionDetailBean.setBasicMoney(BigDecimal.ZERO);
                    bankCollectionDetailBean.setPowerRateMoney(BigDecimal.ZERO);
                    bankCollectionDetailBean.setRefundMoney(BigDecimal.ZERO);
                    bankCollectionDetailBean.setTotalMoney(BigDecimal.ZERO);

                }
                bankCollectionDetailBean.setMeterId(meterMoneyDomain.getMeterId());
                beanList.add(bankCollectionDetailBean);
            }
        }


        beanList =
                beanList.stream()
                        .sorted(Comparator.comparing(BankCollectionDetailBean::getMon, Comparator.nullsLast(Integer::compareTo))
                                .thenComparing(BankCollectionDetailBean::getBusinessPlaceCodeName, Comparator.nullsLast(String::compareTo))
                                .thenComparing(BankCollectionDetailBean::getSettlementNo, Comparator.nullsLast(String::compareTo))
                                .thenComparing(BankCollectionDetailBean::getFunctionCode, Comparator.nullsLast(Long::compareTo))
                                .thenComparing(BankCollectionDetailBean::getMeterSn, Comparator.nullsLast(Integer::compareTo)))
                        .collect(Collectors.toList());


        TableDataBean tableData = new TableDataBean();
        List<TableDataBean> tableDataList = new ArrayList<>();
        tableData.setTableData(new JRBeanCollectionDataSource(beanList));
        tableDataList.add(tableData);
        return tableDataList;

    }

    //大工业电量电费查询
    public List<TableDataBean> bigIndustryMeterMoneyDetailReport(SettlementDomain settlementDomain) {
        List<TableDataBean> tableDataList = new ArrayList<>();

        Map<Long, String> baseMoneyFlagMap =
                cimService.findSystemCommonConfigByType("BASE_MONEY_FLAG");
        //获取电价
        List<PriceTypeDomain> priceTypeDomains = billingService.findPriceType();
        Map<Long, PriceTypeDomain> priceTypeDomainMap =
                priceTypeDomains.stream().filter(m -> m.getId() != null).collect(Collectors.toMap(PriceTypeDomain::getId, a -> a, (k1, k2) -> k1));

        Map<Long, List<PriceExecutionDomain>> priceExecutionListMap = billingService.findMongoPriceExecution(settlementDomain.getMon());


        MeterDomain meterDomain = new MeterDomain();
        meterDomain.setElecTypeCode(10);
        meterDomain.setMon(settlementDomain.getMon());
        meterDomain.setPageSize(-1);
        List<MeterDomain> meterDomainList = meterService.getMeter(meterDomain);
        //计量点IDS
        List<Long> meterIds = meterDomainList.stream().filter(a -> a.getId() != null).map(MeterDomain::getId).distinct().collect(Collectors.toList());
        //计量点ID与对象MAP
        Map<Long, MeterDomain> meterDomainMap =
                meterDomainList.stream().collect(Collectors.toMap(MeterDomain::getId, a -> a, (k1, k2) -> k1));

        //根据计量点找结算户

        List<SettlementDomain> settlementDomainList = cimService.getSettlementbyMeterIds(meterIds);
        if (settlementDomain.getSettlementName() != null) {
            settlementDomainList = settlementDomainList.stream().filter(i -> i.getSettlementName().equals(settlementDomain.getSettlementName())).collect(Collectors.toList());
        } else if (settlementDomain.getSettlementNo() != null) {
            settlementDomainList = settlementDomainList.stream().filter(i -> i.getSettlementNo().equals(settlementDomain.getSettlementNo())).collect(Collectors.toList());
        }
        if (settlementDomainList.size() == 0) {
            return tableDataList;
        }
        Map<Long, SettlementDomain> settlementDomainMap = settlementDomainList.stream().collect(Collectors.toMap(SettlementDomain::getId, a -> a, (k1, k2) -> k1));

        List<SettlementMeterRelDomain> settlementMeterRelDomains = cimService.getMeterIdsBySettlements(settlementDomainList);
        Map<Long, SettlementDomain> meterSettlementRelMap =
                settlementMeterRelDomains.stream().filter(t -> t.getMeterId() != null).filter(t -> t.getSettlementId() != null)
                        .collect(Collectors.toMap(SettlementMeterRelDomain::getMeterId, a -> settlementDomainMap.get(a.getSettlementId()), (k1, k2) -> k1));


        if (settlementDomainList.size() == 1) {
            meterIds = settlementMeterRelDomains.stream().map(SettlementMeterRelDomain::getMeterId).collect(Collectors.toList());
        }
        MeterMoneyDomain meterMoneyDomain = new MeterMoneyDomain();
        meterMoneyDomain.setMeterIds(meterIds);
        meterMoneyDomain.setMon(settlementDomain.getMon());
        meterMoneyDomain.setPageSize(-1);
        List<MeterMoneyDomain> meterMoneyList =
                billingService.mongoFind(meterMoneyDomain);
        List<FeeRecStatisticsBean> resultList = new ArrayList();
        for (MeterMoneyDomain domain : meterMoneyList) {
            FeeRecStatisticsBean feeRecStatisticsBean = new FeeRecStatisticsBean();
            feeRecStatisticsBean.setSettlementNo(Long.parseLong(meterSettlementRelMap.get(domain.getMeterId()).getSettlementNo()));
            feeRecStatisticsBean.setSettlementName(meterSettlementRelMap.get(domain.getMeterId()).getSettlementName());
            feeRecStatisticsBean.setMon(settlementDomain.getMon());
            feeRecStatisticsBean.setMeterName(meterDomainMap.get(domain.getMeterId()).getMeterName());
            feeRecStatisticsBean.setPrice(priceExecutionListMap.get(domain.getPriceTypeId()).stream().filter(i -> i.getTimeSeg() == Byte.valueOf("0")).map(PriceExecutionDomain::getPrice).reduce(BigDecimal.ZERO, BigDecimal::add));
            feeRecStatisticsBean.setPriceName(priceTypeDomainMap.get(domain.getPriceTypeId()).getPriceName());
            feeRecStatisticsBean.setTotalPower(domain.getTotalPower());
            feeRecStatisticsBean.setAmount(domain.getAmount());
            feeRecStatisticsBean.setVolumeCharge(domain.getVolumeCharge());
            feeRecStatisticsBean.setSurcharges(domain.getSurcharges());
            feeRecStatisticsBean.setBaseMoneyFlagName(baseMoneyFlagMap.get(meterDomainMap.get(domain.getMeterId()).getBaseMoneyFlag() == null ? null : (long) meterDomainMap.get(domain.getMeterId()).getBaseMoneyFlag()));
            feeRecStatisticsBean.setCalCapacity(domain.getCalCapacity());
            feeRecStatisticsBean.setBasicMoney(domain.getBasicMoney());
            feeRecStatisticsBean.setPowerRateMoney(domain.getPowerRateMoney());
            resultList.add(feeRecStatisticsBean);
        }
        TableDataBean tableData = new TableDataBean();
        tableData.setTableData(new JRBeanCollectionDataSource(resultList));
        tableDataList.add(tableData);
        return tableDataList;
    }

    //粤桂分时电价查询
    public List<TableDataBean> ygTimeOfUseQuery(SettlementDomain settlementDomain) {
        List<TableDataBean> tableDataList = new ArrayList<>();
        //获取电价
        List<PriceTypeDomain> priceTypeDomains = billingService.findPriceType();
        //粤桂分时电价筛选
        String testStr = "粤桂";
        //获取含粤桂的电价
        priceTypeDomains =
                priceTypeDomains.stream().filter(t -> t.getPriceName().indexOf(testStr) != -1).collect(Collectors.toList());

        Map<Long, PriceTypeDomain> priceTypeDomainMap =
                priceTypeDomains.stream().filter(m -> m.getId() != null).collect(Collectors.toMap(PriceTypeDomain::getId, a -> a, (k1, k2) -> k1));

        List<Long> priceTypeIds =
                priceTypeDomains.stream().map(PriceTypeDomain::getId).distinct().collect(Collectors.toList());
        logger.info("电价数量===========" + priceTypeIds.size());


        //获取符合要求的结算户

        DeptDomain dept =
                deptService.getDept(settlementDomain.getBusinessPlaceCode());
        List<DeptDomain> deptList = new ArrayList<>();        // 最上级
        if (dept.getParentId() != null && dept.getParentId() == Long.valueOf(0)) {
            ParamDomain paramDomain = new ParamDomain();
            // paramDomain.setPageSize(-1);
            deptList = deptService.findByWhere(paramDomain);
        } else {
            deptList = deptService.getDeptList(settlementDomain.getBusinessPlaceCode());
            deptList.add(dept);
        }
        List<Long> businessPlaceCodes = deptList.stream().map(DeptDomain::getId).collect(Collectors.toList());

        //获取营业区域
        MeterDomain paramMeterDomian = new MeterDomain();
        paramMeterDomian.setPageSize(-1);
        paramMeterDomian.setBusinessPlaceCodes(businessPlaceCodes);
        paramMeterDomian.setPriceTypeIds(priceTypeIds);

        //获取计量点
        List<MeterDomain> meterDomains =
                cimService.findClearMeterDoaminByWhere(paramMeterDomian);
        logger.info("计量点数量===========" + meterDomains.size());
        if (meterDomains == null || meterDomains.size() < 1) {
            return new ArrayList<>();
        }

        //获取计量点
        List<Long> meterIds =
                meterDomains.stream().map(MeterDomain::getId).distinct().collect(Collectors.toList());

        List<SettlementMeterRelDomain> settlementMeterRelDomains =
                cimService.getSettlementByBillingMeterIds(meterIds);

        Map<Long, Long> settlementMeterRelMap =
                settlementMeterRelDomains.stream().collect(Collectors.toMap(SettlementMeterRelDomain::getMeterId, a -> a.getSettlementId(), (k1, k2) -> k1));

        List<Long> settlementIds =
                settlementMeterRelDomains.stream().map(SettlementMeterRelDomain::getSettlementId).distinct().collect(Collectors.toList());

        List<SettlementDomain> settlementDomains =
                cimService.findSettlementByIds(settlementIds);

        Map<Long, SettlementDomain> settlementDomainMap =
                settlementDomains.stream().collect(Collectors.toMap(SettlementDomain::getId, a -> a, (k1, k2) -> k1));
        //基础数据
        int len = meterIds.size();
        List<MeterMoneyDomain> meterMoneyDomains = new ArrayList<>();

        //无电费记录 预收之类
        if (len != 0) {
            for (int m = 0; m < len / 4999 + 1; m++) {// 遍历次数

                List<Long> tl = meterIds.subList(m * 4999,
                        (m + 1) * 4999 > len ? len : (m + 1) * 4999);
                MeterMoneyDomain paramMeterMoney = new MeterMoneyDomain();
                paramMeterMoney.setStartMon(settlementDomain.getStartMon());
                paramMeterMoney.setEndMon(settlementDomain.getEndMon());
                paramMeterMoney.setMeterIds(tl);
                paramMeterMoney.setPriceTypeIds(priceTypeIds);
                paramMeterMoney.setPageSize(-1);
                meterMoneyDomains.addAll(billingService.meterMoneyFindByWhere(paramMeterMoney));
                logger.info("电费查询记录参数===========" + settlementDomain.getStartMon() + "|" + settlementDomain.getEndMon() + "|" + tl.size() + "|" + priceTypeIds.size());

            }
        }
        logger.info("电费记录数量===========" + meterMoneyDomains.size());


        //查询记录的计量点
        List<Long> pageMeterIds =
                meterMoneyDomains.stream().filter(a -> a.getMeterId() != null).map(MeterMoneyDomain::getMeterId).distinct().collect(Collectors.toList());
        MeterDomain meterDomain = new MeterDomain();
        meterDomain.setMon(settlementDomain.getEndMon());
        meterDomain.setIds(pageMeterIds);
        meterDomain.setPageSize(-1);
        List<MeterDomain> pageMeterDomainList =
                meterService.getMeter(meterDomain);
        logger.info("计量点数量===========" + pageMeterDomainList.size());

        //计量点ID与对象MAP
        Map<Long, MeterDomain> meterDomainMap =
                pageMeterDomainList.stream().collect(Collectors.toMap(MeterDomain::getId, a -> a, (k1, k2) -> k1));

        meterMoneyDomains.forEach(t -> {
            Long settlementId = settlementMeterRelMap.get(t.getMeterId());
            SettlementDomain paramSettement =
                    settlementDomainMap.get(settlementId);
            if (paramSettement != null) {
                t.setSettlementNo(paramSettement.getSettlementNo());
                t.setSettlementName(paramSettement.getSettlementName());
            }

            t.setMeterNo(meterDomainMap.get(t.getMeterId()).getMeterNo());

            if (priceTypeDomainMap.get(t.getPriceTypeId()) != null) {
                t.setPriceName(priceTypeDomainMap.get(t.getPriceTypeId()).getPriceName());
            }

            t.setPlusTotal(t.getSurcharges());

        });

        List<FeeRecStatisticsBean> resultList = new ArrayList();
        for (MeterMoneyDomain domain : meterMoneyDomains) {

            FeeRecStatisticsBean feeRecStatisticsBean = new FeeRecStatisticsBean();
            feeRecStatisticsBean.setSettlementNo(Long.valueOf(domain.getSettlementNo()));
            feeRecStatisticsBean.setSettlementName(domain.getSettlementName());
            feeRecStatisticsBean.setMon(domain.getMon());
            feeRecStatisticsBean.setMeterNo(domain.getMeterNo());
            feeRecStatisticsBean.setPriceName(domain.getPriceName());
            if (domain.getTotalPower().compareTo(BigDecimal.ZERO) > 0 && domain.getActiveWritePower2().compareTo(BigDecimal.ZERO) == 0) {
                feeRecStatisticsBean.setActiveWritePower2(domain.getTotalPower());
            } else {
                feeRecStatisticsBean.setActiveWritePower2(domain.getActiveWritePower2());
            }
            feeRecStatisticsBean.setActiveWritePower1(domain.getActiveWritePower1());
            feeRecStatisticsBean.setActiveWritePower3(domain.getActiveWritePower3());
            feeRecStatisticsBean.setVolumeCharge(domain.getVolumeCharge());
            feeRecStatisticsBean.setPlusTotal(domain.getPlusTotal());
            feeRecStatisticsBean.setRefundMoney(domain.getRefundMoney().negate());
            feeRecStatisticsBean.setAmount(domain.getAmount());
            resultList.add(feeRecStatisticsBean);
        }
        resultList =
                resultList.stream().sorted(Comparator.comparing(FeeRecStatisticsBean::getSettlementNo, Comparator.nullsLast(Long::compareTo))).collect(Collectors.toList());
        TableDataBean tableData = new TableDataBean();
        logger.info("查询参数===========" + resultList.size());
        tableData.setTableData(new JRBeanCollectionDataSource(resultList));
        tableDataList.add(tableData);
        return tableDataList;
    }


    //电量电费排行
    public List<TableDataBean> electricityTariffRankQuery(ElectricityTariffRankEntity electricityTariffRankEntity) {
        if (electricityTariffRankEntity.getQueryType() == 2) {
            electricityTariffRankEntity.setQueryTypeString("sum(TOTAL_POWER)");
        } else {
            electricityTariffRankEntity.setQueryTypeString("sum(RECEIVABLE)");
        }
        DeptDomain dept =
                deptService.getDept(electricityTariffRankEntity.getBusinessPlaceCode());
        List<DeptDomain> deptList = new ArrayList<>();
        // 最上级
        if (dept.getParentId() != null && dept.getParentId() == Long.valueOf(0)) {
            ParamDomain paramDomain = new ParamDomain();
            // paramDomain.setPageSize(-1);
            deptList = deptService.findByWhere(paramDomain);
        } else {
            deptList = deptService.getDeptList(electricityTariffRankEntity.getBusinessPlaceCode());
            deptList.add(dept);
        }
        List<Long> businessPlaceCodes = deptList.stream().map(DeptDomain::getId).collect(Collectors.toList());
        electricityTariffRankEntity.setBusinessPlaceCodes(businessPlaceCodes);
        if (electricityTariffRankEntity.getBusinessPlaceCodes() != null) {
            electricityTariffRankEntity.setBusinessPlaceCode(null);
        }

        //基础数据
        List<ArrearageDomain> arrearageDomains =
                billingService.electricityTariffRankQuery(electricityTariffRankEntity);
        //结算户
        List<Long> settlementIds =
                arrearageDomains.stream().map(ArrearageDomain::getSettlementId).distinct().collect(Collectors.toList());

        List<SettlementDomain> settlementDomains =
                cimService.findSettlementByIds(settlementIds);

        Map<Long, SettlementDomain> settlementDomainMap =
                settlementDomains.stream().collect(Collectors.toMap(SettlementDomain::getId, a -> a, (k1, k2) -> k1));

        List<ElectricityTariffRankBean> resultList = new ArrayList();
        for (int i = 0; i < arrearageDomains.size(); i++) {
            ArrearageDomain arrearageDomain = arrearageDomains.get(i);
            ElectricityTariffRankBean electricityTariffRankBean = new ElectricityTariffRankBean();
            SettlementDomain settlementDomain =
                    settlementDomainMap.get(arrearageDomain.getSettlementId());
            electricityTariffRankBean.setSettlementNo(settlementDomain.getSettlementNo());
            electricityTariffRankBean.setSettlementName(settlementDomain.getSettlementName());
            electricityTariffRankBean.setStartMon(arrearageDomain.getStartMon());
            electricityTariffRankBean.setEndMon(arrearageDomain.getEndMon());
            electricityTariffRankBean.setTotalPower(arrearageDomain.getTotalPower());
            electricityTariffRankBean.setReceivable(arrearageDomain.getReceivable());
            electricityTariffRankBean.setOrder(i + 1);
            resultList.add(electricityTariffRankBean);
        }
        List<TableDataBean> tableDataList = new ArrayList<>();
        TableDataBean tableData = new TableDataBean();
        tableData.setDept(dept.getDeptName() == null ?
                dept.getTitle() : dept.getDeptName());
        logger.info("查询参数===========" + resultList.size());
        tableData.setTableData(new JRBeanCollectionDataSource(resultList));
        tableDataList.add(tableData);
        return tableDataList;
    }


    //电量电费明细表
    public List<TableDataBean> electricityChargeDetail(SettlementDomain settlementDomain) throws Exception {
        //获取参数 月份和结算户号
        SettlementDomain paramSettlement = new SettlementDomain();
        paramSettlement.setId(settlementDomain.getId());
        paramSettlement.setSettlementNo(settlementDomain.getSettlementNo());
        List<SettlementDomain> settlementDomains =
                cimService.getSettlement(paramSettlement);
        if (settlementDomains == null && settlementDomains.size() != 1) {
            return new ArrayList<>();
        }

        //下拉值
        Map<Long, String> chargeModeMap =
                cimService.findSystemCommonConfigByType("CHARGE_MODE");

        Map<Long, String> customerType =
                cimService.findSystemCommonConfigByType("CUSTOMER_TYPE");

        Map<Long, String> functionCodeMap =
                cimService.findSystemCommonConfigByType("FUNCTION_CODE");

        Map<Long, String> timeSegMap =
                cimService.findSystemCommonConfigByType("TIME_SEG");

        Map<Long, String> meterTypeMap =
                cimService.findSystemCommonConfigByType("METER_TYPE");

        Map<Long, String> msModeMap =
                cimService.findSystemCommonConfigByType("MS_MODE");

        Map<Long, String> tsMeterFlagMap =
                cimService.findSystemCommonConfigByType("TS_METER_FLAG");

        Map<Long, String> baseMoneyFlagMap =
                cimService.findSystemCommonConfigByType("BASE_MONEY_FLAG");

        Map<Long, String> cosTypeMap =
                cimService.findSystemCommonConfigByType("COS_FLAG");
        Map<Long, String> transShareFlagMap =
                cimService.findSystemCommonConfigByType("TRANS_SHARE_FLAG");

        Map<Long, String> isPubTrans =
                cimService.findSystemCommonConfigByType("IS_PUB_TRANS");

        Map<Long, String> transLostFlagMap =
                cimService.findSystemCommonConfigByType("TRANS_LOST_FLAG");

        Map<Long, String> transModelTypeMap =
                cimService.findSystemCommonConfigByType("TRANSFORMER_MODEL_TYPE");

        List<PriceTypeDomain> priceTypeDomains = billingService.findPriceType();

        Map<Long, PriceTypeDomain> priceTypeDomainMap =
                priceTypeDomains.stream().filter(m -> m.getId() != null).collect(Collectors.toMap(PriceTypeDomain::getId, a -> a, (k1, k2) -> k1));

        //电价
        Map<Long, List<PriceExecutionDomain>> priceExecutionListMap = billingService.findMongoPriceExecution(settlementDomain.getMon());

        //开户银行下拉
        BankEntity bankEntity = new BankEntity();
        bankEntity.setPageSize(-1);
        List<BankEntity> bankArray =
                cimService.getBank(bankEntity);
        Map<Long, BankEntity> bankEntityMap =
                bankArray.stream().collect(Collectors.toMap(BankEntity::getId, a -> a, (k1, k2) -> k1));
        //下拉取值结束

        //查询的结算户信息
        SettlementDomain returnSettlement = settlementDomains.get(0);

        //结算户下的计量点
        List<SettlementMeterRelDomain> settlementMeterIds =
                cimService.getMeterIdsBySettlements(settlementDomains);

        //获取计量点
        List<Long> pMeterIds =
                settlementMeterIds.stream().map(SettlementMeterRelDomain::getMeterId).distinct().collect(Collectors.toList());


        //获取子结算户计量点
        MeterRelationDomain paramMeterRelation = new MeterRelationDomain();
        paramMeterRelation.setPageSize(-1);
        paramMeterRelation.setpMeterIds(pMeterIds);
        List<MeterRelationDomain> meterRelationDomains =
                cimService.findMeterRelationByWhere(paramMeterRelation);

        Map<Long, List<MeterRelationDomain>> meterRelationMap = null;
        List<Long> allMeterIds = null;
        if (meterRelationDomains != null && meterRelationDomains.size() > 0) {
            meterRelationMap =
                    meterRelationDomains.stream().collect(Collectors.groupingBy(MeterRelationDomain::getpMeterId));
            allMeterIds =
                    meterRelationDomains.stream().filter(t -> t.getMeterId() != null).map(MeterRelationDomain::getMeterId).distinct().collect(Collectors.toList());
            for (Long t : pMeterIds) {
                if (allMeterIds.contains(t)) {
                    allMeterIds.remove(t);
                }
            }
            ;
        }
        //根据计量点 找到子结算户
        List<SettlementMeterRelDomain> settlementMeterRelDomains = null;
        if (allMeterIds != null) {
            settlementMeterRelDomains =
                    cimService.getSettlementByBillingMeterIds(allMeterIds);
        }

        //结算户-计量点map
        Map<Long, List<Long>> settlementMeterIdMap = new HashMap<>();

        List<Long> settlementIds = new ArrayList<>();
        settlementIds.add(returnSettlement.getId());

        if (settlementMeterRelDomains != null && settlementMeterRelDomains.size() > 0) {
            List<Long> cSettlementIds =
                    settlementMeterRelDomains.stream().filter(t -> t.getSettlementId() != null)
                            .map(SettlementMeterRelDomain::getSettlementId).distinct().collect(Collectors.toList());
            if (cSettlementIds != null && cSettlementIds.size() > 0) {
                settlementIds.addAll(cSettlementIds);
            }
            Map<Long, List<SettlementMeterRelDomain>> settlementMeterRelMap =
                    settlementMeterRelDomains.stream().collect(Collectors.groupingBy(SettlementMeterRelDomain::getSettlementId));

            for (Long key : settlementMeterRelMap.keySet()) {
                settlementMeterIdMap.put(key,
                        settlementMeterRelMap.get(key).stream().map(SettlementMeterRelDomain::getMeterId).collect(Collectors.toList()));
            }

        }

        //插入父户
        settlementMeterIdMap.put(returnSettlement.getId(), pMeterIds);
        List<TableDataBean> tableDataList = new ArrayList<>();

        //所有结算户信息
        SettlementDomain allParamSett = new SettlementDomain();
        allParamSett.setSettlementIds(settlementIds);
        List<SettlementDomain> allSettlementDomains =
                cimService.getSettlement(allParamSett);
        Map<Long, SettlementDomain> settlementDomainMap =
                allSettlementDomains.stream().collect(Collectors.toMap(SettlementDomain::getId, k -> k, (k1, k2) -> k1));

        //阶梯电价
        Map<Long, List<PriceExecutionDomain>> priceMap =
                billingService.findMongoPriceExecution(settlementDomain.getMon());

        List<PriceLadderRelaDomain> priceLadderRelaDomains =
                billingService.findMongoPriceLadderRela(settlementDomain.getMon());

        Map<Integer, List<PriceLadderRelaDomain>> priceLadderMap = new HashMap<>();

        List<Long> priceLadderPriceTypeIds =
                priceLadderRelaDomains.stream().map(PriceLadderRelaDomain::getPriceExecutionId).collect(Collectors.toList());

        //按电价及sn分组
        Map<String, PriceLadderRelaDomain> priceLadderRelaMapByPriceAndSn =
                priceLadderRelaDomains.stream().collect(Collectors.toMap(o -> o.getPriceExecutionId() + "_" + o.getLadderSn(), a -> a, (k1, k2) -> k1));

        //按电价分组
        Map<Long, List<PriceLadderRelaDomain>> priceLadderRelaMapByPrice =
                priceLadderRelaDomains.stream().collect(Collectors.groupingBy(PriceLadderRelaDomain::getPriceExecutionId));


        //settlementIds 为 要查询的计算户及其子户的id
        for (Long settlementId : settlementIds) {

            List<Long> meterIds = settlementMeterIdMap.get(settlementId);
            List<Long> meterIdsAndPare = FormatterUtil.deepCopy(meterIds);
            meterIdsAndPare.addAll(pMeterIds);
            //获取计量点信息
            MeterDomain paramMeterDomain = new MeterDomain();
            paramMeterDomain.setMeterIds(meterIdsAndPare);
            paramMeterDomain.setPageSize(-1);
            List<MeterDomain> meterDomains =
                    cimService.findClearMeterDoaminByWhere(paramMeterDomain);

            if (meterDomains == null || meterDomains.size() < 1) {
                return new ArrayList<>();
            }

            Map<Long, MeterDomain> meterMap =
                    meterDomains.stream().collect(Collectors.toMap(MeterDomain::getId, k -> k, (k1, k2) -> k1));

            List<Long> writeSectIds =
                    meterDomains.stream().map(MeterDomain::getWriteSectionId).distinct().collect(Collectors.toList());
            //获取用户信息
            List<Long> userIds =
                    meterDomains.stream().filter(t -> t.getUserId() != null).map(MeterDomain::getUserId).distinct().collect(Collectors.toList());

            List<UserDomain> userInfoEntities = null;

            if (userIds != null) {
                userInfoEntities = cimService.getUserByIds(userIds);
            }

            if (userInfoEntities == null || userInfoEntities.size() < 1) {
                return new ArrayList<>();
            }
            //客户信息
            List<Long> customerIds =
                    userInfoEntities.stream().filter(t -> t.getCustomerId() != null).map(UserDomain::getCustomerId).distinct().collect(Collectors.toList());

            List<CustomerDomain> customerDomains = null;
            if (customerIds != null) {
                CustomerDomain paramCustomer = new CustomerDomain();
                paramCustomer.setCustomerIds(customerIds);
                customerDomains = cimService.getCustomer(paramCustomer);
            }


            //获取抄表区段
            List<WriteSectDomain> writeSectDomains = null;
            WriteSectDomain paramWriteSect = new WriteSectDomain();
            paramWriteSect.setWriteSectionIds(writeSectIds);
            if (writeSectIds != null && writeSectIds.size() > 0) {
                writeSectDomains = cimService.getWriteSectFindByWhere(paramWriteSect);
            }

            List<Long> writorIds =
                    writeSectDomains.stream().map(WriteSectDomain::getWritorId).distinct().collect(Collectors.toList());
            Map<Long, String> writorMap =
                    userService.findByIds(writorIds).stream().collect(Collectors.toMap(UserDomain::getId, k -> k.getUserName(), (k1, k2) -> k1));

            //获取线路变压器台区信息
            List<LineDomain> lineDomains = null;
            Map<Long, TransformerLineRelDomain> transformerLineRelDomainMap = null;
            List<TransformerDomain> transformerEntities = null;
            Map<Long, TransformerDomain> transformerDomainMap = null;

            List<TransformerLineRelDomain> transformerLineRelDomains = cimService.findTransformerLineByMeterIds(meterIds);

            if (transformerLineRelDomains != null && transformerLineRelDomains.size() > 0) {
                List<Long> transformerIds =
                        transformerLineRelDomains.stream().map(TransformerLineRelDomain::getTransformerId).distinct().collect(Collectors.toList());

                //获取变压器详细信息
                if (transformerIds != null && transformerIds.size() > 0) {
                    TransformerDomain paramTransformerDomain = new TransformerDomain();
                    paramTransformerDomain.setTransformerIds(transformerIds);
                    paramTransformerDomain.setPageSize(-1);
                    transformerEntities =
                            cimService.getAvaTransformerByWhere(paramTransformerDomain);
                    transformerDomainMap =
                            transformerEntities.stream().collect(Collectors.toMap(TransformerDomain::getId, a -> a, (k1, k2) -> k1));
                }


                List<Long> lineIds =
                        transformerLineRelDomains.stream().map(TransformerLineRelDomain::getLineId).distinct().collect(Collectors.toList());

                transformerLineRelDomainMap =
                        transformerLineRelDomains.stream().collect(Collectors.toMap(TransformerLineRelDomain::getMeterId, a -> a, (k1, k2) -> k1));
                //获取线路详细信息
                LineDomain lineDomain = new LineDomain();
                lineDomain.setLineIds(lineIds);
                lineDomains = cimService.findByLineIds(lineDomain);
            }


            //抄表信息
            WriteFilesDomain paramWriteFile = new WriteFilesDomain();
            paramWriteFile.setMon(settlementDomain.getMon());
            paramWriteFile.setMeterIds(meterIds);
            paramWriteFile.setPageSize(-1);
            List<WriteFilesDomain> writeFilesDomains =
                    billingService.getWriteFiles(paramWriteFile);

            // 初始化未存储表计信息
            //表计信息
            List<Long> meterAssetsIds =
                    writeFilesDomains.stream().filter(t -> t.getMeterAssetsId() != null).map(WriteFilesDomain::getMeterAssetsId).distinct().collect(Collectors.toList());

            Map<Long, MeterAssetsDomain> meterAssetsDomainMap = null;
            if (meterAssetsIds != null && meterAssetsIds.size() > 0) {
                Map<String, List<Long>> paramMap = new HashMap<>();
                paramMap.put("ids", meterAssetsIds);
                List<MeterAssetsDomain> meterAssetsDomains =
                        cimService.getMeterAssetsByAssetsIds(paramMap);
                meterAssetsDomainMap = meterAssetsDomains.stream()
                        .collect(Collectors.toMap(o -> o.getId(), a -> a, (k1, k2) -> k1));
            }
            


            //获取电费记录
            MeterMoneyDomain paramMeterMoney = new MeterMoneyDomain();
            paramMeterMoney.setMon(settlementDomain.getMon());
            paramMeterMoney.setMeterIds(meterIds);
            paramMeterDomain.setPageSize(-1);
            List<MeterMoneyDomain> meterMoneyDomains =
                    billingService.mongoFind(paramMeterMoney);
            Map<Long, MeterMoneyDomain> moneyDomainMap = null;
            if (meterMoneyDomains != null && meterMoneyDomains.size() > 0) {

                moneyDomainMap =
                        meterMoneyDomains.stream().collect(Collectors.toMap(MeterMoneyDomain::getMeterId, a -> a, (k1, k2) -> k1));
            }

            TableDataBean tableData = new TableDataBean();
            //营业区域
            if (settlementDomainMap.get(settlementId).getBusinessPlaceCode() != null) {
                DeptDomain dept =
                        deptService.getDept(settlementDomains.get(0).getBusinessPlaceCode());
                tableData.setDept(dept.getDeptName() == null ? dept.getTitle() : dept.getDeptName());
            }

            tableData.setMon(settlementDomain.getMon());
            tableData.setSn(settlementDomain.getSn());
            tableData.setSettlementNo(Long.valueOf(settlementDomainMap.get(settlementId).getSettlementNo()));
            tableData.setSettlementName(settlementDomainMap.get(settlementId).getSettlementName());
            tableData.setSetAddress(settlementDomainMap.get(settlementId).getAddress());
            tableData.setPhoneNum(settlementDomainMap.get(settlementId).getSettlementPhone());
            tableData.setBankNo(settlementDomainMap.get(settlementId).getBankNo());
            tableData.setLadderNum(meterDomains.get(0).getLadderNum());
            if (writeSectDomains != null) {
                tableData.setWriteSectNo(writeSectDomains.get(0).getWriteSectNo());
                tableData.setWritorName(writorMap.get(writeSectDomains.get(0).getWritorId()));
            }

            if (transformerEntities != null) {
                tableData.setDeskName(transformerEntities.get(0).getDeskName());
            }

            if (lineDomains != null) {
                tableData.setLineName(lineDomains.get(0).getLineName());
            }

            if (userInfoEntities != null) {
                tableData.setCalCapacity(userInfoEntities.get(0).getAllCapacity());
                if (userInfoEntities.get(0).getUserType() != null) {
                    tableData.setUserTypeName(customerType.get(Long.valueOf(userInfoEntities.get(0).getUserType())));
                }
            }

            if (settlementDomainMap.get(settlementId).getChargeModeType() != null) {
                tableData.setChargeModeName(chargeModeMap.get(Long.valueOf(settlementDomainMap.get(settlementId).getChargeModeType())));
            }

            if (settlementDomainMap.get(settlementId).getOpendingBank() != null) {
                tableData.setOpendingName(bankEntityMap.get(settlementDomainMap.get(settlementId).getOpendingBank()).getBankName());
            }

            if (writeFilesDomains != null && writeFilesDomains.size() > 0) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                tableData.setLastWriteDate(writeFilesDomains.get(0).getLastWriteDate() == null ? "" : sdf.format(writeFilesDomains.get(0).getLastWriteDate()));
                tableData.setWriteDate(writeFilesDomains.get(0).getWriteDate() == null ? "" : sdf.format(writeFilesDomains.get(0).getWriteDate()));
            }

            if (customerDomains != null) {
                tableData.setLinkMan(customerDomains.get(0).getLinkMan() == null ? "" : customerDomains.get(0).getLinkMan());
            }

            if (meterMoneyDomains != null) {
                tableData.setDay(meterMoneyDomains.get(0).getTransformerRunDay() == null ? 30 : meterMoneyDomains.get(0).getTransformerRunDay().intValue());
            }

            //抄表记录
            List<WriteFilesBean> writeFilesBeans = new ArrayList<>();

            if (writeFilesDomains != null && writeFilesDomains.size() > 0) {
                for (WriteFilesDomain t : writeFilesDomains) {
                    WriteFilesBean writeFilesBean = new WriteFilesBean();
                    MeterDomain meterDomain = meterMap.get(t.getMeterId());
                    if (meterDomain != null) {
                        writeFilesBean.setMeterOrder((long) meterDomain.getMeterOrder());
                    }

                    if (meterAssetsDomainMap!=null && meterAssetsDomainMap.get(t.getMeterAssetsId()) != null) {
                        writeFilesBean.setMeterAssetsNo(meterAssetsDomainMap.get(t.getMeterAssetsId()).getMeterAssetsNo());
                    } else {
                        writeFilesBean.setMeterAssetsNo("");
                    }
                    writeFilesBean.setFunctionCode(t.getFunctionCode());
                    writeFilesBean.setFunctionCodeName(t.getFunctionCode() == null ? "" :
                            functionCodeMap.get(t.getFunctionCode()));

                    writeFilesBean.setTimeSegName(t.getTimeSeg() == null ? "" :
                            timeSegMap.get(Long.valueOf(t.getTimeSeg())));
                    writeFilesBean.setTimeSeg(t.getTimeSeg());
                    writeFilesBean.setStartNum(t.getStartNum());
                    writeFilesBean.setEndNum(t.getEndNum());
                    writeFilesBean.setDiffNum(t.getDiffNum());
                    writeFilesBean.setFactorNum(t.getFactorNum());
                    writeFilesBean.setWritePower(t.getWritePower());
                    writeFilesBean.setAddPower(t.getAddPower() == null ? BigDecimal.ZERO : t.getAddPower());
                    writeFilesBean.setAddPower(writeFilesBean.getAddPower().add(t.getChgPower() == null ? BigDecimal.ZERO : t.getChgPower()));
                    writeFilesBean.setTotalPower(writeFilesBean.getAddPower().add(writeFilesBean.getWritePower() == null ? BigDecimal.ZERO : writeFilesBean.getWritePower()));
                    writeFilesBeans.add(writeFilesBean);
                }
                //排序
                writeFilesBeans =
                        writeFilesBeans.stream().sorted(Comparator.comparing(WriteFilesBean::getMeterOrder, Comparator.nullsLast(Long::compareTo))
                                .thenComparing(WriteFilesBean::getFunctionCode, Comparator.nullsLast(Long::compareTo))
                                .thenComparing(WriteFilesBean::getTimeSeg, Comparator.nullsLast(Byte::compareTo))).collect(Collectors.toList());
                tableData.setTableData(new JRBeanCollectionDataSource(writeFilesBeans));
            }


            //计量点信息
            List<MeterBean> meterBeans = new ArrayList<>();
            for (MeterDomain t : meterDomains) {
                MeterBean meterBean = new MeterBean();
                meterBean.setMeterOrder(t.getMeterOrder());
                meterBean.setMeterTypeName(t.getMeterType() == null ? "" :
                        meterTypeMap.get(Long.valueOf(t.getMeterType())));
                meterBean.setMsModeName(t.getMsType() == null ? "" :
                        msModeMap.get(Long.valueOf(t.getMsType())));
                meterBean.setPriceTypeName(t.getPriceType() == null ? "" :
                        priceTypeDomainMap.get(t.getPriceType()).getPriceName());
                meterBean.setTsTypeName(t.getTsType() == null ? "" :
                        tsMeterFlagMap.get(Long.valueOf(t.getTsType())));

                meterBean.setBaseMoneyFlagName(t.getBaseMoneyFlag() == null ? "" :
                        baseMoneyFlagMap.get(Long.valueOf(t.getBaseMoneyFlag())));
                meterBean.setNeedIndex(t.getNeedIndex());
                meterBean.setCosTypeName(t.getCosType() == null ? "" :
                        cosTypeMap.get(Long.valueOf(t.getCosType())));

                TransformerLineRelDomain transformerLineRelDomain =
                        transformerLineRelDomainMap.get(t.getId());
                if (transformerLineRelDomain != null) {
                    meterBean.setTransLostTypeName(transformerLineRelDomain.getTransLostType() == null ? "" : transShareFlagMap.get(transformerLineRelDomain.getTransLostType()));
                } else {
                    meterBean.setTransLostTypeName("");
                }
                meterBeans.add(meterBean);
            }

            //排序
            meterBeans =
                    meterBeans.stream().sorted(Comparator.comparing(MeterBean::getMeterOrder, Comparator.nullsLast(Integer::compareTo))).collect(Collectors.toList());
            tableData.setTableData1(new JRBeanCollectionDataSource(meterBeans));

            //变压器信息
            if (transformerLineRelDomains != null && transformerLineRelDomains.size() > 0) {
                List<TransformerBean> transformerBeans = new ArrayList<>();
                for (TransformerLineRelDomain t : transformerLineRelDomains) {
                    TransformerBean transformerBean = new TransformerBean();
                    TransformerDomain transformerDomain =
                            transformerDomainMap.get(t.getTransformerId());
                    if (transformerDomain != null) {
                        transformerBean.setTransformerNo(transformerDomain.getTransformerNo());
                        transformerBean.setIsPubTypeName(transformerDomain.getIsPubType() == null ? "" : isPubTrans.get(Long.valueOf(transformerDomain.getIsPubType())));
                        transformerBean.setTransformerLossTypeName(transformerDomain.getTransformerLossType() == null ? "" : transLostFlagMap.get(Long.valueOf(transformerDomain.getTransformerLossType())));
                    }
                    MeterMoneyDomain meterMoneyDomain =
                            moneyDomainMap.get(t.getMeterId());
                    if (meterMoneyDomain != null) {
                        transformerBean.setDay(meterMoneyDomain.getTransformerRunDay());
                        transformerBean.setTransformerGroupNo(meterMoneyDomain.getTransformerGroupNo());
                        if (meterMoneyDomain.getTransformerCalculateInfo() != null) {
                            Long transformerModelCode =
                                    (long) meterMoneyDomain.getTransformerCalculateInfo().getTransformerModelType();
                            transformerBean.setTransformerModelTypeName(transModelTypeMap.get(transformerModelCode));
                            transformerBean.setDay(meterMoneyDomain.getTransformerCalculateInfo().getDay() == null ? BigDecimal.ZERO : BigDecimal.valueOf(meterMoneyDomain.getTransformerCalculateInfo().getDay()));
                        } else {
                            transformerBean.setTransformerModelTypeName("");
                            transformerBean.setDay(BigDecimal.ZERO);
                        }
                        transformerBean.setCapacity(meterMoneyDomain.getCalCapacity());
                    }
                    transformerBeans.add(transformerBean);
                }
                tableData.setTableData2(new JRBeanCollectionDataSource(transformerBeans));
            }

            //套扣关系
            List<MeterRelationDomain> meterRelationDomainsByP = new ArrayList<>();
            if (meterRelationDomains != null && meterRelationDomains.size() > 0) {

                for (Long meterId : meterIds) {
                    if (meterRelationMap.get(meterId) != null) {
                        meterRelationDomainsByP.addAll(meterRelationMap.get(meterId));
                    }
                }
                if (meterRelationDomainsByP != null && meterRelationDomainsByP.size() > 0) {
                    //获取子户计量点
                    List<Long> cmeterIds =
                            meterRelationDomainsByP.stream().map(MeterRelationDomain::getMeterId).distinct().collect(Collectors.toList());
                    List<MeterDomain> cMeterDomains =
                            cimService.getMeterByMeterIds(cmeterIds);

                    Map<Long, MeterDomain> cMeterMap = cMeterDomains.stream().collect(Collectors.toMap(MeterDomain::getId, k -> k, (k1, k2) -> k1));

                    //获取用户信息
                    List<Long> cUserIds =
                            cMeterDomains.stream().filter(t -> t.getUserId() != null).map(MeterDomain::getUserId).distinct().collect(Collectors.toList());

                    List<UserDomain> cUserInfoEntities = null;
                    Map<Long, UserDomain> cUserInfoMap = null;

                    if (cUserIds != null) {
                        cUserInfoEntities = cimService.getUserByIds(cUserIds);
                        cUserInfoMap = cUserInfoEntities.stream().collect(Collectors.toMap(UserDomain::getId, k -> k, (k1, k2) -> k1));
                    }

                    List<MeterMeterRelBean> meterMeterRelBeans = new ArrayList<>();
                    for (MeterRelationDomain t : meterRelationDomainsByP) {
                        MeterMeterRelBean meterMeterRelBean = new MeterMeterRelBean();
                        if (userInfoEntities != null && userInfoEntities.size() > 0) {
                            meterMeterRelBean.setpUserNo(userInfoEntities.get(0).getUserNo());
                            meterMeterRelBean.setpUserName(userInfoEntities.get(0).getUserName());
                        } else {
                            meterMeterRelBean.setpUserNo("");
                            meterMeterRelBean.setpUserName("");
                        }
                        meterMeterRelBean.setpMeterOrder(meterMap.get(t.getpMeterId()).getMeterOrder());

                        if (cUserInfoEntities != null && cUserInfoEntities.size() > 0 && cMeterMap.get(t.getMeterId()).getUserId() != null) {
                            Long userId = cMeterMap.get(t.getMeterId()).getUserId();
                            meterMeterRelBean.setUserNo(cUserInfoMap.get(userId).getUserNo());
                            meterMeterRelBean.setUserName(cUserInfoMap.get(userId).getUserName());
                        } else {
                            meterMeterRelBean.setUserNo("");
                            meterMeterRelBean.setUserName("");
                        }

                        meterMeterRelBean.setMeterOrder(cMeterMap.get(t.getMeterId()).getMeterOrder());
                        if (t.getMeterRelationType() == Byte.valueOf("1")) {
                            meterMeterRelBean.setMeterRelationType("定量关系");
                        } else if (t.getMeterRelationType() == Byte.valueOf("2")) {
                            meterMeterRelBean.setMeterRelationType("定比关系");
                        } else if (t.getMeterRelationType() == Byte.valueOf("3")) {
                            meterMeterRelBean.setMeterRelationType("总分关系");

                        } else {
                            meterMeterRelBean.setMeterRelationType("");
                        }
                        meterMeterRelBeans.add(meterMeterRelBean);
                    }
                    //排序
                    meterMeterRelBeans =
                            meterMeterRelBeans.stream().sorted(Comparator.comparing(MeterMeterRelBean::getMeterOrder, Comparator.nullsLast(Integer::compareTo))).collect(Collectors.toList());
                    tableData.setTableData3(new JRBeanCollectionDataSource(meterMeterRelBeans));
                }
            }

            //电费信息
            if (meterMoneyDomains != null && meterMoneyDomains.size() > 0) {
                List<MeterMoneyBean> meterMoneyBeans = new ArrayList<>();
                List<FeeRecStatisticsBean> feeRecStatisticsBeans = new ArrayList<FeeRecStatisticsBean>();

                for (MeterMoneyDomain t : meterMoneyDomains) {
                    List<WriteFilesMongoDomain> writeFilesMongoDomains = t.getMeterDataInfo();
                    Map<Byte, WriteFilesMongoDomain> reWriteFileMongo =
                            writeFilesMongoDomains.stream().collect(Collectors.toMap(WriteFilesMongoDomain::getFunctionCode, k -> k, (k1, k2) -> k1));
                    //总段
                    for (WriteFilesMongoDomain w : writeFilesMongoDomains) {
                        if (new Byte("2").equals(w.getFunctionCode())) {
                            continue;
                        }
                        MeterMoneyBean meterMoneyBean = new MeterMoneyBean();
                        meterMoneyBean.setFunctionCode(w.getFunctionCode());
                        meterMoneyBean.setMeterOrder(meterMap.get(t.getMeterId()).getMeterOrder());
                        meterMoneyBean.setTimeSeg((int) w.getTimeSeg());
                        meterMoneyBean.setTimeSegName(w.getTimeSeg() == null
                                ? "" :
                                timeSegMap.get(Long.valueOf(w.getTimeSeg())));
                        meterMoneyBean.setActiveWritePower(w.getReadPower());
                        meterMoneyBean.setChgPower(w.getChangePower());
                        meterMoneyBean.setAddPower(w.getAddPower());
                        meterMoneyBean.setActiveDeductionPower(w.getDeductionPower());
                        meterMoneyBean.setSharePower(w.getProtocolPower());
                        meterMoneyBean.setActiveTransformerLossPower(w.getTransformerLossPower());
                        meterMoneyBean.setTotalPower(w.getChargePower());
                        meterMoneyBean.setTotalMoney(w.getCharge());
                        if (priceExecutionListMap.get(t.getPriceTypeId()) != null && priceExecutionListMap.get(t.getPriceTypeId()).size() > 0) {
                            meterMoneyBean.setPrice(priceExecutionListMap.get(t.getPriceTypeId()).stream().filter(i -> i.getTimeSeg() == w.getTimeSeg()).filter(i -> i.getPriceItemId() == Long.valueOf("1")).map(PriceExecutionDomain::getPrice).reduce(BigDecimal.ZERO, BigDecimal::add));
                        }

                        if (new Byte("0").equals(w.getTimeSeg())) {
                            WriteFilesMongoDomain reWriteFilesMongoDomain =
                                    reWriteFileMongo.get(Byte.valueOf("2"));
                            if (reWriteFilesMongoDomain != null) {
                                meterMoneyBean.setReactiveWritePower(reWriteFilesMongoDomain.getReadPower());
                                meterMoneyBean.setQchgPower(reWriteFilesMongoDomain.getChangePower());
                                meterMoneyBean.setQaddPower(reWriteFilesMongoDomain.getAddPower());
                                meterMoneyBean.setReactiveTransformerLossPower(reWriteFilesMongoDomain.getTransformerLossPower());
                                meterMoneyBean.setQtotalPower(reWriteFilesMongoDomain.getChargePower());
                            } else {
                                meterMoneyBean.setReactiveWritePower(BigDecimal.ZERO);
                                meterMoneyBean.setQchgPower(BigDecimal.ZERO);
                                meterMoneyBean.setQaddPower(BigDecimal.ZERO);
                                meterMoneyBean.setReactiveTransformerLossPower(BigDecimal.ZERO);
                                meterMoneyBean.setQtotalPower(BigDecimal.ZERO);
                            }
                        } else {
                            meterMoneyBean.setReactiveWritePower(BigDecimal.ZERO);
                            meterMoneyBean.setQchgPower(BigDecimal.ZERO);
                            meterMoneyBean.setQaddPower(BigDecimal.ZERO);
                            meterMoneyBean.setReactiveTransformerLossPower(BigDecimal.ZERO);
                            meterMoneyBean.setQtotalPower(BigDecimal.ZERO);
                        }

                        meterMoneyBeans.add(meterMoneyBean);
                    }

                    BigDecimal surPrice =
                            priceMap.get(t.getPriceTypeId()).stream().filter(m -> m.getTimeSeg() == 0).filter(m -> m.getPriceItemId() != 1).map(PriceExecutionDomain::getPrice).reduce(BigDecimal.ZERO, BigDecimal::add);

                    //赋值阶梯信息
                    BigDecimal ladderSurcharges1 = BigDecimal.ZERO;
                    BigDecimal ladderSurcharges2 = BigDecimal.ZERO;
                    BigDecimal ladderSurcharges3 = BigDecimal.ZERO;
                    BigDecimal ladderSurcharges4 = BigDecimal.ZERO;
                    if (t.getLadderDataModels() != null && t.getLadderDataModels().size() > 0) {
                        logger.info("LadderDataModels==========="+t.getLadderDataModels().size());
                        Map<Integer,LadderMongoDomain> ladderMongoDomainMap=
                                t.getLadderDataModels().stream().collect(Collectors.toMap(LadderMongoDomain::getLadderSn, a -> a, (k1, k2) -> k1));
                        for (LadderMongoDomain ladderMongoDomain : t.getLadderDataModels()) {
                            if (ladderMongoDomain.getLadderSn().intValue()==1) {
                                FeeRecStatisticsBean ladderbean = new FeeRecStatisticsBean();
                                ladderbean.setMeterOrder(meterMap.get(t.getMeterId()).getMeterOrder());
                                ladderbean.setName("第一阶梯");
                                ladderbean.setPrice(priceLadderRelaMapByPriceAndSn.get(t.getPriceTypeId() + "_" + "1").getPrice().add(surPrice));
                                ladderbean.setTotalPower(ladderMongoDomain.getChargePower());
                                // 赋值附加费 考虑差值 有第二阶梯差值放在第二阶梯
                                if (ladderMongoDomainMap.get(Integer.valueOf("2")) != null) {
                                    ladderbean.setSurcharges(ladderMongoDomain.getChargePower().multiply(surPrice));
                                    ladderSurcharges1 = ladderbean.getSurcharges();
                                } else {
                                    ladderbean.setSurcharges(t.getSurcharges());
                                }
                                ladderbean.setAmount(ladderMongoDomain.getAmount().add(ladderbean.getSurcharges()));
                                feeRecStatisticsBeans.add(ladderbean);
                            }
                            if (ladderMongoDomain.getLadderSn().intValue()==2) {
                                FeeRecStatisticsBean ladderbean = new FeeRecStatisticsBean();
                                ladderbean.setMeterOrder(meterMap.get(t.getMeterId()).getMeterOrder());
                                ladderbean.setName("第二阶梯");
                                ladderbean.setPrice(priceLadderRelaMapByPriceAndSn.get(t.getPriceTypeId() + "_" + "2").getPrice().add(surPrice));
                                ladderbean.setTotalPower(ladderMongoDomain.getChargePower());
                                // 赋值附加费 考虑差值 有第三阶梯差值放在第三阶梯
                                if (ladderMongoDomainMap.get(Integer.valueOf("3")) != null) {
                                    ladderbean.setSurcharges(ladderMongoDomain.getChargePower().multiply(surPrice));
                                    ladderSurcharges2 =
                                            ladderSurcharges1.add(ladderbean.getSurcharges());
                                } else {
                                    ladderbean.setSurcharges(t.getSurcharges().subtract(ladderSurcharges1));
                                }
                                ladderbean.setAmount(ladderMongoDomain.getAmount().add(ladderbean.getSurcharges()));
                                feeRecStatisticsBeans.add(ladderbean);
                            }
                            if (ladderMongoDomain.getLadderSn().intValue()==3) {
                                FeeRecStatisticsBean ladderbean = new FeeRecStatisticsBean();
                                ladderbean.setMeterOrder(meterMap.get(t.getMeterId()).getMeterOrder());
                                ladderbean.setName("第三阶梯");
                                ladderbean.setPrice(priceLadderRelaMapByPriceAndSn.get(t.getPriceTypeId() + "_" + "3").getPrice().add(surPrice));
                                ladderbean.setTotalPower(ladderMongoDomain.getChargePower());
                                // 赋值附加费 考虑差值 有第四阶梯差值放在第四阶梯
                                if (ladderMongoDomainMap.get(Integer.valueOf("4")) != null) {
                                    ladderbean.setSurcharges(ladderMongoDomain.getChargePower().multiply(surPrice));
                                    ladderSurcharges3 =
                                            ladderSurcharges2.add(ladderbean.getSurcharges());
                                } else {
                                    ladderbean.setSurcharges(t.getSurcharges().subtract(ladderSurcharges2));
                                }
                                ladderbean.setAmount(ladderMongoDomain.getAmount().add(ladderbean.getSurcharges()));
                                feeRecStatisticsBeans.add(ladderbean);
                            }
                            if (ladderMongoDomain.getLadderSn().intValue()==4) {
                                FeeRecStatisticsBean ladderbean = new FeeRecStatisticsBean();
                                ladderbean.setMeterOrder(meterMap.get(t.getMeterId()).getMeterOrder());
                                ladderbean.setName("第四阶梯");
                                ladderbean.setPrice(priceLadderRelaMapByPriceAndSn.get(t.getPriceTypeId() + "_" + "4").getPrice().add(surPrice));
                                ladderbean.setTotalPower(ladderMongoDomain.getChargePower());
                                // 赋值附加费 考虑差值
                                ladderbean.setSurcharges(t.getSurcharges().subtract(ladderSurcharges3));
                                ladderbean.setAmount(ladderMongoDomain.getAmount().add(ladderbean.getSurcharges()));
                                feeRecStatisticsBeans.add(ladderbean);
                            }

                        }
                        logger.info("feeRecStatisticsBeans=========="+feeRecStatisticsBeans.size());
                    }

                }
                //排序
                meterMoneyBeans =
                        meterMoneyBeans.stream().sorted(Comparator.comparing(MeterMoneyBean::getMeterOrder, Comparator.nullsLast(Integer::compareTo))
                                .thenComparing(MeterMoneyBean::getFunctionCode, Comparator.nullsLast(Byte::compareTo))
                                .thenComparing(MeterMoneyBean::getTimeSeg, Comparator.nullsLast(Integer::compareTo))).collect(Collectors.toList());
                tableData.setTableData4(new JRBeanCollectionDataSource(meterMoneyBeans));

                tableData.setTableData7(new JRBeanCollectionDataSource(feeRecStatisticsBeans));

            }

            //加价信息
            if (meterMoneyDomains != null && meterMoneyDomains.size() > 0) {
                List<MeterMoneyBean> meterMoneyBeans = new ArrayList<>();

                for (MeterMoneyDomain t : meterMoneyDomains) {
                    MeterMoneyBean meterMoneyBean = new MeterMoneyBean();
                    meterMoneyBean.setMeterOrder(meterMap.get(t.getMeterId()).getMeterOrder());
                    //价格
                    List<PriceExecutionDomain> priceExecutionList =
                            priceExecutionListMap.get(t.getPriceTypeId());
                    meterMoneyBean.setAddMoney1Price(priceExecutionList.stream().filter(i -> i.getTimeSeg() == Byte.valueOf("0")).filter(i -> i.getPriceItemId() == Long.valueOf("2")).map(PriceExecutionDomain::getPrice).reduce(BigDecimal.ZERO, BigDecimal::add));
                    meterMoneyBean.setAddMoney2Price(priceExecutionList.stream().filter(i -> i.getTimeSeg() == Byte.valueOf("0")).filter(i -> i.getPriceItemId() == Long.valueOf("3")).map(PriceExecutionDomain::getPrice).reduce(BigDecimal.ZERO, BigDecimal::add));
                    meterMoneyBean.setAddMoney3Price(priceExecutionList.stream().filter(i -> i.getTimeSeg() == Byte.valueOf("0")).filter(i -> i.getPriceItemId() == Long.valueOf("4")).map(PriceExecutionDomain::getPrice).reduce(BigDecimal.ZERO, BigDecimal::add));
                    meterMoneyBean.setAddMoney4Price(priceExecutionList.stream().filter(i -> i.getTimeSeg() == Byte.valueOf("0")).filter(i -> i.getPriceItemId() == Long.valueOf("5")).map(PriceExecutionDomain::getPrice).reduce(BigDecimal.ZERO, BigDecimal::add));
                    meterMoneyBean.setAddMoney5Price(priceExecutionList.stream().filter(i -> i.getTimeSeg() == Byte.valueOf("0")).filter(i -> i.getPriceItemId() == Long.valueOf("6")).map(PriceExecutionDomain::getPrice).reduce(BigDecimal.ZERO, BigDecimal::add));
                    meterMoneyBean.setAddMoney6Price(priceExecutionList.stream().filter(i -> i.getTimeSeg() == Byte.valueOf("0")).filter(i -> i.getPriceItemId() == Long.valueOf("7")).map(PriceExecutionDomain::getPrice).reduce(BigDecimal.ZERO, BigDecimal::add));
                    meterMoneyBean.setAddMoney7Price(priceExecutionList.stream().filter(i -> i.getTimeSeg() == Byte.valueOf("0")).filter(i -> i.getPriceItemId() == Long.valueOf("8")).map(PriceExecutionDomain::getPrice).reduce(BigDecimal.ZERO, BigDecimal::add));
                    meterMoneyBean.setAddMoney8Price(priceExecutionList.stream().filter(i -> i.getTimeSeg() == Byte.valueOf("0")).filter(i -> i.getPriceItemId() == Long.valueOf("9")).map(PriceExecutionDomain::getPrice).reduce(BigDecimal.ZERO, BigDecimal::add));


                    meterMoneyBean.setAddMoney1(BigDecimal.ZERO);
                    meterMoneyBean.setAddMoney2(BigDecimal.ZERO);
                    meterMoneyBean.setAddMoney3(BigDecimal.ZERO);
                    meterMoneyBean.setAddMoney4(BigDecimal.ZERO);
                    meterMoneyBean.setAddMoney5(BigDecimal.ZERO);
                    meterMoneyBean.setAddMoney6(BigDecimal.ZERO);
                    meterMoneyBean.setAddMoney7(BigDecimal.ZERO);
                    meterMoneyBean.setAddMoney8(BigDecimal.ZERO);


                    if (t.getSurchargeDetail().get(t.getPriceTypeId() + "#" + "2" + "#" + "0") != null && t.getSurchargeDetail().get(t.getPriceTypeId() + "#" + "2" + "#" + "0").compareTo(BigDecimal.ZERO) > 0) {
                        BigDecimal addMoney1 =
                                t.getSurchargeDetail().get(t.getPriceTypeId() +
                                        "#" + "2" + "#" + "0");
                        meterMoneyBean.setAddMoney1(addMoney1 == null ?
                                BigDecimal.ZERO : addMoney1);
                        BigDecimal addMoney2 =
                                t.getSurchargeDetail().get(t.getPriceTypeId() +
                                        "#" + "3" + "#" + "0");
                        meterMoneyBean.setAddMoney2(addMoney2 == null ?
                                BigDecimal.ZERO : addMoney2);

                        BigDecimal addMoney3 =
                                t.getSurchargeDetail().get(t.getPriceTypeId() +
                                        "#" + "4" + "#" + "0");
                        meterMoneyBean.setAddMoney3(addMoney3 == null ?
                                BigDecimal.ZERO : addMoney3);

                        BigDecimal addMoney4 =
                                t.getSurchargeDetail().get(t.getPriceTypeId() +
                                        "#" + "5" + "#" + "0");
                        meterMoneyBean.setAddMoney4(addMoney4 == null ?
                                BigDecimal.ZERO : addMoney4);

                        BigDecimal addMoney5 =
                                t.getSurchargeDetail().get(t.getPriceTypeId() +
                                        "#" + "6" + "#" + "0");
                        meterMoneyBean.setAddMoney5(addMoney5 == null ?
                                BigDecimal.ZERO : addMoney5);

                        BigDecimal addMoney6 =
                                t.getSurchargeDetail().get(t.getPriceTypeId() +
                                        "#" + "7" + "#" + "0");
                        meterMoneyBean.setAddMoney6(addMoney6 == null ?
                                BigDecimal.ZERO : addMoney6);

                        BigDecimal addMoney7 =
                                t.getSurchargeDetail().get(t.getPriceTypeId() +
                                        "#" + "8" + "#" + "0");
                        meterMoneyBean.setAddMoney7(addMoney7 == null ?
                                BigDecimal.ZERO : addMoney7);

                        BigDecimal addMoney8 =
                                t.getSurchargeDetail().get(t.getPriceTypeId() +
                                        "#" + "9" + "#" + "0");
                        meterMoneyBean.setAddMoney8(addMoney8 == null ?
                                BigDecimal.ZERO : addMoney8);
                    } else {
                        for (int i = 0; i <= 4; i++) {
                            BigDecimal addMoney1 =
                                    t.getSurchargeDetail().get(t.getPriceTypeId() + "#" + "2" + "#" + i);
                            if (addMoney1 != null) {
                                meterMoneyBean.setAddMoney1(meterMoneyBean.getAddMoney1().add(addMoney1));
                            }
                            BigDecimal addMoney2 =
                                    t.getSurchargeDetail().get(t.getPriceTypeId() + "#" + "3" + "#" + i);
                            if (addMoney2 != null) {
                                meterMoneyBean.setAddMoney2(meterMoneyBean.getAddMoney2().add(addMoney2));
                            }
                            BigDecimal addMoney3 =
                                    t.getSurchargeDetail().get(t.getPriceTypeId() + "#" + "4" + "#" + i);
                            if (addMoney3 != null) {
                                meterMoneyBean.setAddMoney3(meterMoneyBean.getAddMoney3().add(addMoney3));
                            }
                            BigDecimal addMoney4 =
                                    t.getSurchargeDetail().get(t.getPriceTypeId() + "#" + "5" + "#" + i);
                            if (addMoney4 != null) {
                                meterMoneyBean.setAddMoney4(meterMoneyBean.getAddMoney4().add(addMoney4));
                            }
                            BigDecimal addMoney5 =
                                    t.getSurchargeDetail().get(t.getPriceTypeId() + "#" + "6" + "#" + i);
                            if (addMoney5 != null) {
                                meterMoneyBean.setAddMoney5(meterMoneyBean.getAddMoney5().add(addMoney5));
                            }
                            BigDecimal addMoney6 =
                                    t.getSurchargeDetail().get(t.getPriceTypeId() + "#" + "7" + "#" + i);
                            if (addMoney6 != null) {
                                meterMoneyBean.setAddMoney6(meterMoneyBean.getAddMoney6().add(addMoney6));
                            }
                            BigDecimal addMoney7 =
                                    t.getSurchargeDetail().get(t.getPriceTypeId() + "#" + "8" + "#" + i);
                            if (addMoney7 != null) {
                                meterMoneyBean.setAddMoney7(meterMoneyBean.getAddMoney7().add(addMoney7));
                            }

                            BigDecimal addMoney8 =
                                    t.getSurchargeDetail().get(t.getPriceTypeId() + "#" + "9" + "#" + i);
                            if (addMoney8 != null) {
                                meterMoneyBean.setAddMoney8(meterMoneyBean.getAddMoney8().add(addMoney8));
                            }
                        }
                    }
                    meterMoneyBeans.add(meterMoneyBean);
                }
                //排序
                meterMoneyBeans =
                        meterMoneyBeans.stream().sorted(Comparator.comparing(MeterMoneyBean::getMeterOrder, Comparator.nullsLast(Integer::compareTo))).collect(Collectors.toList());
                tableData.setTableData5(new JRBeanCollectionDataSource(meterMoneyBeans));
            }

            //总电费信息
            if (meterMoneyDomains != null && meterMoneyDomains.size() > 0) {
                List<MeterMoneyBean> meterMoneyBeans = new ArrayList<>();

                for (MeterMoneyDomain t : meterMoneyDomains) {
                    Long basicPriceTypeId =
                            meterMap.get(t.getMeterId()).getBasicPrice();
                    MeterMoneyBean meterMoneyBean = new MeterMoneyBean();
                    meterMoneyBean.setMeterOrder(meterMap.get(t.getMeterId()).getMeterOrder());
                    if (t.getPriceTypeId() == Long.valueOf("101")) {
                        meterMoneyBean.setKhPower(t.getActiveWritePower());
                        meterMoneyBean.setTotalPower(BigDecimal.ZERO);
                    } else {
                        meterMoneyBean.setTotalPower(t.getTotalPower());
                        meterMoneyBean.setKhPower(BigDecimal.ZERO);
                    }
                    meterMoneyBean.setVolumeCharge(t.getVolumeCharge());
                    meterMoneyBean.setSurcharges(t.getSurcharges());
                    meterMoneyBean.setCosRate(t.getCosRate());
                    meterMoneyBean.setCos(t.getCos() == null ? BigDecimal.ZERO :
                            t.getCos().multiply(BigDecimal.valueOf(100)));
                    meterMoneyBean.setPowerRateMoney(t.getPowerRateMoney());
                    meterMoneyBean.setCalCapacity(t.getCalCapacity());
                    meterMoneyBean.setBasicMoney(t.getBasicMoney());
                    meterMoneyBean.setBasicPrice(basicPriceTypeId == null ?
                            BigDecimal.ZERO :
                            priceExecutionListMap.get(basicPriceTypeId).get(0).getPrice());
                    meterMoneyBean.setTotalMoney(t.getTotalAmount());
                    meterMoneyBean.setRefundMoney(t.getRefundMoney().negate());
                    meterMoneyBean.setShouldMoney(t.getAmount());
                    meterMoneyBeans.add(meterMoneyBean);
                }
                //获取退费
                List<Long> bulkSettlementIds = new ArrayList<>();
                bulkSettlementIds.add(settlementId);
                ChargeInfoDomain chargeInfoDomain = new ChargeInfoDomain();
                chargeInfoDomain.setMon(settlementDomain.getMon());
                chargeInfoDomain.setfChargeMode((short) 6);
                chargeInfoDomain.setSettlementIds(bulkSettlementIds);
                List<ChargeInfoDomain> chargeInfoDomains =
                        billingService.chargeBySettleIds(chargeInfoDomain);
                BigDecimal bulkChargeMoney = BigDecimal.ZERO;
                if (chargeInfoDomains != null && chargeInfoDomains.size() > 0) {
                    bulkChargeMoney =
                            chargeInfoDomains.stream().map(ChargeInfoDomain::getFactTotal).reduce(BigDecimal.ZERO, BigDecimal::add);
                }
                //排序
                meterMoneyBeans =
                        meterMoneyBeans.stream().sorted(Comparator.comparing(MeterMoneyBean::getMeterOrder, Comparator.nullsLast(Integer::compareTo))).collect(Collectors.toList());
                meterMoneyBeans.get(0).setRefundMoney(meterMoneyBeans.get(0).getRefundMoney().subtract(bulkChargeMoney));
                meterMoneyBeans.get(0).setShouldMoney(meterMoneyBeans.get(0).getShouldMoney().subtract(bulkChargeMoney));
                tableData.setTableData6(new JRBeanCollectionDataSource(meterMoneyBeans));
            }
            tableDataList.add(tableData);
        }

        return tableDataList;
    }
}
