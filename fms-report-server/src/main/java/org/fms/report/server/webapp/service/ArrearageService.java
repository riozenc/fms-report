package org.fms.report.server.webapp.service;

import static java.util.stream.Collectors.toList;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.fms.report.common.util.CustomCollectors;
import org.fms.report.common.util.MonUtils;
import org.fms.report.common.util.NumToString;
import org.fms.report.common.webapp.bean.ArrearageSumBean;
import org.fms.report.common.webapp.bean.FeeRecStatisticsBean;
import org.fms.report.common.webapp.bean.TableDataBean;
import org.fms.report.common.webapp.domain.ArrearageDomain;
import org.fms.report.common.webapp.domain.BankCollectionEntity;
import org.fms.report.common.webapp.domain.BankEntity;
import org.fms.report.common.webapp.domain.ChargeInfoDomain;
import org.fms.report.common.webapp.domain.DeptDomain;
import org.fms.report.common.webapp.domain.MeterMoneyDomain;
import org.fms.report.common.webapp.domain.SettlementDomain;
import org.fms.report.common.webapp.domain.SfPowerBankDomain;
import org.fms.report.common.webapp.domain.UserDomain;
import org.fms.report.common.webapp.domain.WriteSectDomain;
import org.fms.report.server.webapp.dao.ArrearageDAO;
import org.fms.report.server.webapp.dao.DeptDAO;
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
import com.riozenc.titanTool.spring.web.http.HttpResultPagination;

import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

@TransactionService
public class ArrearageService {

    @Autowired
    private TitanTemplate titanTemplate;

    @TransactionDAO
    private ArrearageDAO arrearageDAO;

    @TransactionDAO
    private DeptDAO deptDAO;

    @TransactionDAO
    private UserDAO userDAO;

    @Autowired
    private DeptService deptService;

    @Autowired
    private WriteSectService writeSectService;

    @Autowired
    private CimService cimService;

    @Autowired
    private MeterService meterService;

    @Autowired
    private BillingService billingService;

    @TransactionDAO
    private SystemCommonConfigDAO systemCommonConfigDAO;

    public List<ArrearageDomain> arrearageQuery(ArrearageDomain arrearage) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        List<ArrearageDomain> list = new ArrayList<>();
        try {
            list = titanTemplate.post("BILLING-SERVER",
                    "billingServer/arrearage/ArrearageQuery", httpHeaders,
                    GsonUtils.toJson(arrearage),
                    new TypeReference<List<ArrearageDomain>>() {
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<ArrearageDomain> arrearageQuerySum(ArrearageDomain arrearage) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        List<ArrearageDomain> list = new ArrayList<>();
        try {
            list = titanTemplate.post("BILLING-SERVER",
                    "billingServer/arrearage/ArrearageQuerySum", httpHeaders,
                    GsonUtils.toJson(arrearage),
                    new TypeReference<List<ArrearageDomain>>() {
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    //托收单
    public List<ArrearageDomain> findArrearageBySettleIds(List<Long> settlementIds) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        List<ArrearageDomain> list = new ArrayList<>();
        try {
            list = titanTemplate.post("BILLING-SERVER",
                    "billingServer/arrearage/findArrearageBySettleIds", httpHeaders,
                    GsonUtils.toJson(settlementIds),
                    new TypeReference<List<ArrearageDomain>>() {
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }


    public List<TableDataBean> summary(ArrearageDomain arrearage) {

        Map<Long, DeptDomain> deptDomainMap=new HashMap<>();
        if (arrearage.getBusinessPlaceCode() != null) {
            List<DeptDomain> deptList = deptService.getDeptList(arrearage.getBusinessPlaceCode());
            if (deptList.size() > 0) {
                arrearage.setBusinessPlaceCodes(deptList.stream().map(DeptDomain::getId).collect(Collectors.toList()));
                arrearage.getBusinessPlaceCodes().add(arrearage.getBusinessPlaceCode());
            }

            DeptDomain deptDomain=
                    deptService.getDept(arrearage.getBusinessPlaceCode());
            deptList.add(deptDomain);

            deptList.forEach(t->{
                if(t.getDeptName()==null || "".equals(t.getDeptName())){
                    t.setDeptName(t.getTitle());
                }
            });

            deptDomainMap= deptList.stream().filter(m -> m.getId() != null)
                    .collect(Collectors.toMap(DeptDomain::getId, a -> a, (k1, k2) -> k1));
        }
        //writesectionId转成writesectid 查询条件

        List<ArrearageDomain> list = arrearageQuerySum(arrearage);
        list=
                list.stream().filter(t->t.getOweMoney().compareTo(BigDecimal.ZERO)>0).collect(toList());

        List<ArrearageDomain> existsList = new ArrayList<>();
        Map<Object, List<ArrearageDomain>> listMap = new HashMap<>();
        if (arrearage.getGroupBy() != null) {
            listMap =
                list.stream().filter(t -> t.getGroupKey() != null).collect(Collectors.groupingBy(ArrearageDomain::getGroupKey));
            existsList =
                    list.stream().filter(t -> t.getGroupKey() == null).collect(Collectors.toList());
            if (existsList != null && existsList.size() > 0) {
                listMap.put("", existsList);
            }
        }

        Map<Long, UserDomain> writorMap=new HashMap<>();
        if ("writor".equals(arrearage.getGroupBy())){
            List<Long> writorIds = list.stream().filter(i -> i.getGroupKey() != null)
                    .map(ArrearageDomain::getGroupKey).distinct().collect(Collectors.toList());

            writorMap = userDAO.findByIds(writorIds)
                    .stream().collect(Collectors.toMap(UserDomain::getId, k -> k));
        }

        Map<Long, WriteSectDomain> writeSectDomainMap =new HashMap<>();

        if ("writeSect".equals(arrearage.getGroupBy())) {

            List<Long> writeSectIds =
                    list.stream().filter(i -> i.getGroupKey() != null)
                            .map(ArrearageDomain::getGroupKey).distinct().collect(Collectors.toList());

            WriteSectDomain writeSectDomain = new WriteSectDomain();
            writeSectDomain.setWriteSectionIds(writeSectIds);
            //writeSectDomain.setMon(arrearage.getStartMon());
            writeSectDomain.setPageSize(-1);
            List<WriteSectDomain> writeSectDomains = cimService.getWriteSectFindByWhere(writeSectDomain);

            writeSectDomainMap= writeSectDomains
                    .stream().collect(Collectors.toMap(WriteSectDomain::getId, k -> k));
        }


        TableDataBean tableData = new TableDataBean();

        List<ArrearageSumBean> beanList = new ArrayList<>();
        //赋值报表数据
        for (List<ArrearageDomain> item : listMap.values()) {
            if (item == null || item.size() < 1) {
                continue;
            }
            ArrearageSumBean bean = new ArrearageSumBean();
            if ("writeSect".equals(arrearage.getGroupBy())) {
                bean.setName("抄表区段");
                if (writeSectDomainMap.get(item.get(0).getGroupKey()) != null && !"".equals(writeSectDomainMap.get(item.get(0).getGroupKey()))) {
                    bean.setValue(writeSectDomainMap.get(item.get(0).getGroupKey()).getWriteSectName());
                    bean.setOrderBy(writeSectDomainMap.get(item.get(0).getGroupKey()).getWriteSectNo());
                } else {
                    bean.setValue("无抄表区段");
                    bean.setOrderBy("9999");
                }

            }
            if ("writor".equals(arrearage.getGroupBy())) {
                bean.setName("抄表员");
                if (writorMap.get(item.get(0).getGroupKey()) != null && !"".equals(writorMap.get(item.get(0).getGroupKey()))) {
                    bean.setValue(writorMap.get(item.get(0).getGroupKey()).getUserName());
                    bean.setOrderBy(item.get(0).getGroupKey().toString());
                } else {
                    bean.setOrderBy("9999");
                    bean.setValue("无抄表员");
                }
            }
            if ("mon".equals(arrearage.getGroupBy())) {
                bean.setName("电费月份");
                if (item.get(0).getGroupKey() != null && !"".equals(item.get(0).getGroupKey())) {
                    bean.setOrderBy(item.get(0).getGroupKey().toString());
                    bean.setValue(item.get(0).getGroupKey().toString());
                } else {
                    bean.setOrderBy("9999");
                    bean.setValue("无电费月份");
                }
            }

            if ("businessPlaceCode".equals(arrearage.getGroupBy())) {
                bean.setName("营业区域");
                if (item.get(0).getGroupKey() != null && !"".equals(item.get(0).getGroupKey())) {
                    bean.setValue(deptDomainMap.get(item.get(0).getGroupKey()).getDeptName());
                    bean.setOrderBy(item.get(0).getGroupKey().toString());
                } else {
                    bean.setOrderBy("9999");
                    bean.setValue("无营业区域");
                }
            }
            List<ArrearageDomain> oweList = item.stream().filter(a -> a.getOweMoney() != null && a.getOweMoney().compareTo(BigDecimal.ZERO) == 1).collect(Collectors.toList());
            bean.setArrearageNumber(item.get(0).getCount());
            bean.setOweMoneySum(oweList.stream().filter(a -> a.getOweMoney() != null).map(ArrearageDomain::getOweMoney).reduce(BigDecimal.ZERO, BigDecimal::add));
            beanList.add(bean);
        }
        //排序
        beanList =
                beanList.stream().sorted(Comparator.comparing(ArrearageSumBean::getOrderBy)).collect(Collectors.toList());
//        if (arrearage.getBusinessPlaceCode() != null) {
        if ("businessPlaceCode".equals(arrearage.getGroupBy())) {
            tableData.setvName("营业区域");
            DeptDomain d = deptService.getDept(arrearage.getBusinessPlaceCode());
            tableData.setvValue(d.getDeptName());
        }
//        if (arrearage.getWritorId() != null) {
        if ("writor".equals(arrearage.getGroupBy())) {
            tableData.setvName("抄表员");
            if (writorMap.get(arrearage.getWritorId()) != null) {
                tableData.setvValue(writorMap.get(arrearage.getWritorId()).getUserName());
            }
        }
//        if (arrearage.getWriteSectId() != null) {
        if("writeSect".equals(arrearage.getGroupBy())) {
            tableData.setvName("抄表区段");
            tableData.setvValue(writeSectDomainMap.get(arrearage.getWriteSectId()).getWriteSectName());
        }
        tableData.setPrintDate(MonUtils.getMon());
        tableData.setTableData(new JRBeanCollectionDataSource(beanList));
        List<TableDataBean> tableDataList = new ArrayList<>();
        tableDataList.add(tableData);

        return tableDataList;
    }

    public List<TableDataBean> settlementNotice(ArrearageDomain arrearage) {
        //获取通知单信息
        String operatorName = arrearage.getOperatorName();
        arrearage.setOperatorName(null);
        List<ArrearageDomain> list = arrearageQuery(arrearage);

        List<Long> meterMoneyIds=
                list.stream().map(ArrearageDomain::getMeterMoneyId).distinct().collect(toList());

        List<MeterMoneyDomain> meterMoneyDomains =
                billingService.findMeterMoneyByIds(meterMoneyIds);

        Map<String,MeterMoneyDomain> meterMoneyDomainMap=
                meterMoneyDomains.stream().collect(Collectors.toMap(o -> o.getMon()+"_"+o.getMeterId(), a -> a, (k1, k2) -> k1));;

        List<Long> settlementIds=
                list.stream().map(ArrearageDomain::getSettlementId).distinct().collect(toList());

        Map<Object, List<ArrearageDomain>> listMap;
        //按结算户进行结算单分组
        listMap = list.stream().filter(a -> a.getWriteSectId() != null).collect(Collectors.groupingBy(ArrearageDomain::getSettlementId));

        //抄表员
        List<Long> writorIds = list.stream().filter(i -> i.getWritorId() != null).map(ArrearageDomain::getWritorId).distinct().collect(Collectors.toList());
        //Map<Long, UserDomain> writorMap =
                //userDAO.findByIds(writorIds).stream().collect(Collectors
        // .toMap(UserDomain::getId, k -> k));



        //获取退费
        ChargeInfoDomain chargeInfoDomain=new ChargeInfoDomain();
        chargeInfoDomain.setMon(arrearage.getMon());
        chargeInfoDomain.setfChargeMode((short)6);
        chargeInfoDomain.setSettlementIds(settlementIds);
        List<ChargeInfoDomain> chargeInfoDomains=
                billingService.chargeBySettleIds(chargeInfoDomain);

        //系统用户
        List<Long> operatorIds=
                chargeInfoDomains.stream().map(ChargeInfoDomain::getOperator).distinct().collect(Collectors.toList());

        writorIds.forEach(t->{
            if (!operatorIds.contains(t)){
                operatorIds.add(t);
            }
        });

        Map<Long, String> userMap = userDAO.findByIds(operatorIds)
                .stream().collect(Collectors.toMap(UserDomain::getId, k -> k.getUserName()));


        Map<Long,List<ChargeInfoDomain>> chargeMap=
                chargeInfoDomains.stream().collect(Collectors.groupingBy(ChargeInfoDomain::getSettlementId));



        List<TableDataBean> tableDataList = new ArrayList<>();
        //创建结算单对象
        for (List<ArrearageDomain> item : listMap.values()) {
            List<MeterMoneyDomain> meterMoneyList=new ArrayList<>();

            Map<Long,ArrearageDomain> arrearageDomainMapById=
                    item.stream().filter(t->t.getFunctionCode()!=null).filter(t->t.getFunctionCode()!=2)
                    .collect(Collectors.toMap(ArrearageDomain::getMeterId, a -> a, (k1, k2) -> k1));

            BigDecimal oweSumMoney=BigDecimal.ZERO;
            BigDecimal deductionBalance=BigDecimal.ZERO;
            for (Long key:arrearageDomainMapById.keySet()){
                oweSumMoney=
                        oweSumMoney.add(arrearageDomainMapById.get(key).getOweMoney());
                deductionBalance=
                        deductionBalance.add(arrearageDomainMapById.get(key).getDeductionBalance());
            }

            List<Long> meterIds=
                    item.stream().map(ArrearageDomain::getMeterId).distinct().collect(toList());

            Map<Long,ArrearageDomain> arrearageDomainMap=
                    item.stream().collect(Collectors.toMap(ArrearageDomain::getMeterId, a -> a, (k1, k2) -> k1));

            for (int i=0;i<meterIds.size();i++){
                ArrearageDomain t = arrearageDomainMap.get(meterIds.get(i));
                String key=t.getMon()+"_"+t.getMeterId();
                meterMoneyList.add(meterMoneyDomainMap.get(key));
            }

            TableDataBean tableData = new TableDataBean();
            tableData.setMon(item.get(0).getMon());
            tableData.setTg(item.get(0).getTg());
            tableData.setLastWriteDate(item.get(0).getLastWriteDate());
            tableData.setWriteDate(item.get(0).getWriteDate());
            tableData.setSettlementNo(item.get(0).getSettlementNo());
            tableData.setSettlementName(item.get(0).getSettlementName());
            tableData.setWriteSectName(item.get(0).getWriteSectName());
            tableData.setSetAddress(item.get(0).getSetAddress());

            tableData.setTotalPowerSum(meterMoneyList.stream().filter(a -> a.getTotalPower() != null).map(MeterMoneyDomain::getTotalPower).reduce(BigDecimal.ZERO, BigDecimal::add));
            tableData.setQTotalPowerSum(meterMoneyList.stream().filter(a -> a.getqTotalPower()!= null).map(MeterMoneyDomain::getqTotalPower).reduce(BigDecimal.ZERO, BigDecimal::add));
            tableData.setCos(item.get(0).getCos());


            tableData.setCalCapacity(meterMoneyList.stream().filter(a -> a.getCalCapacity() != null)
                    .map(MeterMoneyDomain::getCalCapacity).reduce(BigDecimal.ZERO, BigDecimal::add));
            //退补电费,力调标准
            tableData.setCosStdCode(item.get(0).getCosStdCodeName());
            tableData.setCosRate(item.get(0).getCosRate());
            tableData.setCosFlagName(item.get(0).getCosFlagName());
            tableData.setCosStdCodeName(item.get(0).getCosStdCodeName());

            tableData.setPowerRateMoney(meterMoneyList.stream().filter(a -> a.getPowerRateMoney() != null).map(MeterMoneyDomain::getPowerRateMoney).reduce(BigDecimal.ZERO, BigDecimal::add));
            tableData.setAddMoney8(meterMoneyList.stream().filter(a -> a.getAddMoney8() != null).map(MeterMoneyDomain::getAddMoney8).reduce(BigDecimal.ZERO, BigDecimal::add));
            tableData.setSurcharges(meterMoneyList.stream().filter(a -> a.getSurcharges() != null).map(MeterMoneyDomain::getSurcharges).reduce(BigDecimal.ZERO, BigDecimal::add));

            tableData.setArrearsNum(item.get(0).getArrearsNum());


            tableData.setBasicMoneySum(meterMoneyList.stream().filter(a -> a.getBasicMoney() != null).map(MeterMoneyDomain::getBasicMoney).reduce(BigDecimal.ZERO, BigDecimal::add));
            tableData.setOweMoneySum(oweSumMoney);

            tableData.setRefundMoney(meterMoneyList.stream().filter(a -> a.getRefundMoney() != null).map(MeterMoneyDomain::getRefundMoney).reduce(BigDecimal.ZERO, BigDecimal::add));
            tableData.setReceivableSum(meterMoneyList.stream().filter(a -> a.getAmount() != null).map(MeterMoneyDomain::getAmount).reduce(BigDecimal.ZERO, BigDecimal::add).add(tableData.getRefundMoney()));


            BigDecimal recallMoney=BigDecimal.ZERO;
            List<ChargeInfoDomain> chargeInfos=
                    chargeMap.get(item.get(0).getSettlementId());
            if(chargeInfos!=null && chargeInfos.size()>0){
                recallMoney= chargeInfos.stream().map(ChargeInfoDomain::getFactTotal).reduce(BigDecimal.ZERO, BigDecimal::add);
            }

            tableData.setRefundMoney(recallMoney.add(tableData.getRefundMoney()).negate());
            tableData.setLastBalance(deductionBalance);
            tableData.setLastBalance(tableData.getLastBalance().subtract(recallMoney));
            tableData.setMoneyInWord(NumToString.number2CNMontrayUnit(tableData.getOweMoneySum()));
            tableData.setDept(item.get(0).getDeptName());
            tableData.setWriterName(userMap.get(item.get(0).getWritorId()));
            tableData.setOperatorName(operatorName);

            tableData.setPrintDate(MonUtils.getDay());
            List<FeeRecStatisticsBean> beanList = new ArrayList<>();

            List<String> keys=new ArrayList<>();
            BigDecimal deductionPowerSum=BigDecimal.ZERO;
            item=item.stream().sorted(Comparator.comparing(ArrearageDomain::getMeterId, Comparator.nullsLast(Long::compareTo))
                    .thenComparing(ArrearageDomain::getFunctionCode,Comparator.nullsLast(Integer::compareTo)))
                    .collect(Collectors.toList());
            for (ArrearageDomain arrearageDomain : item) {
                String key=
                        arrearageDomain.getMon()+"_"+arrearageDomain.getMeterId()+"_"+arrearageDomain.getFunctionCode();
                FeeRecStatisticsBean bean = new FeeRecStatisticsBean();
                bean.setName(arrearageDomain.getName() + "/" + arrearageDomain.getMeterName());
                bean.setStartNum(arrearageDomain.getStartNum());
                bean.setEndNum(arrearageDomain.getEndNum());
                bean.setChgPower(arrearageDomain.getChgPower());
                bean.setFactorNum(arrearageDomain.getFactorNum());
                if(keys.contains(key)){
                    bean.setActiveWritePower(BigDecimal.ZERO);
                    bean.setAddPower(BigDecimal.ZERO);
                    bean.setChgPower(BigDecimal.ZERO);
                    bean.setLineAndTransformerLossPower(BigDecimal.ZERO);
                    bean.setTotalPower(BigDecimal.ZERO);
                    bean.setPrice(BigDecimal.ZERO);
                    bean.setVolumeCharge(BigDecimal.ZERO);
                }else {
                    bean.setActiveWritePower(arrearageDomain.getActiveWritePower());
                    //退补换表
                    BigDecimal addAndDeductionPower = new BigDecimal(0);
                    if (arrearageDomain.getActiveDeductionPower() != null) {
                        addAndDeductionPower =
                                addAndDeductionPower.add(arrearageDomain.getActiveDeductionPower());
                    }
                    if (arrearageDomain.getAddPower() != null) {
                        addAndDeductionPower =
                                addAndDeductionPower.add(arrearageDomain.getAddPower());
                    }

                    bean.setAddPower(BigDecimal.ZERO);
                    if(bean.getChgPower()==null){
                        bean.setChgPower(addAndDeductionPower);
                    }else{
                        bean.setChgPower(bean.getChgPower().add(addAndDeductionPower));
                    }
                    //退补换表
                    BigDecimal lineAndTransformerLossPower = new BigDecimal(0);
                    if (arrearageDomain.getActiveLineLossPower() != null) {
                        lineAndTransformerLossPower =
                                lineAndTransformerLossPower.add(arrearageDomain.getActiveLineLossPower());
                    }
                    if (arrearageDomain.getActiveTransformerLossPower() != null) {
                        lineAndTransformerLossPower =
                                lineAndTransformerLossPower.add(arrearageDomain.getActiveTransformerLossPower());
                    }
                    bean.setLineAndTransformerLossPower(lineAndTransformerLossPower);
                    bean.setTotalPower(arrearageDomain.getTotalPower());
                    bean.setPrice(arrearageDomain.getPrice());
                    bean.setVolumeCharge(arrearageDomain.getVolumeCharge().add(arrearageDomain.getSurcharges()));
                    deductionPowerSum=
                            deductionPowerSum.add(arrearageDomain.getActiveDeductionPower());
                }
                keys.add(key);
                beanList.add(bean);
            }
            tableData.setDeductionPowerSum(deductionPowerSum);
            tableData.setTableData(new JRBeanCollectionDataSource(beanList));
            tableDataList.add(tableData);
        }

        //过滤掉有功无功总 电费=0的记录
        tableDataList=
                tableDataList.stream().filter(t-> t.getReceivableSum().compareTo(BigDecimal.ZERO)!=0).collect(toList());
        return tableDataList;
    }


    //银行托收单
    public List<TableDataBean> bankCollection(BankCollectionEntity bankCollectionEntity) {

        //托收结算户
        SettlementDomain paramSettlement = new SettlementDomain();
        paramSettlement.setChargeModeType((byte)5);
        if (bankCollectionEntity.getBusinessPlaceCode() != null) {
            paramSettlement.setBusinessPlaceCode(bankCollectionEntity.getBusinessPlaceCode());
        }
        if (bankCollectionEntity.getBankNo() != null && !"".equals(bankCollectionEntity.getBankNo().trim())){
            paramSettlement.setBankNo(bankCollectionEntity.getBankNo());
        }
        //符合要求的结算户记录
        List<SettlementDomain> settlementDomainList = cimService.findBankSettlement(paramSettlement);

        List<Long> settlementIds =
                settlementDomainList.stream().map(SettlementDomain::getId).distinct().collect(Collectors.toList());

        if (settlementDomainList == null || settlementDomainList.size() < 1) {
            return new ArrayList<>();
        }

        Map<Long, SettlementDomain> settementMap =
                settlementDomainList.stream().collect(Collectors.toMap(SettlementDomain::getId, k -> k));

        ArrearageDomain paramArrearageNoPage =new ArrearageDomain();
        //paramArrearageNoPage.setIsSettle(0);
        //paramArrearageNoPage.setIsLock((short) 0);
        paramArrearageNoPage.setMon(bankCollectionEntity.getMon());
        paramArrearageNoPage.setBusinessPlaceCode(null);
        paramArrearageNoPage.setSettlementIds(settlementIds);
        paramArrearageNoPage.setPageSize(-1);
        List<BankCollectionEntity> bankCollectionEntities =
                billingService.findArrearageGroupbySettleId(paramArrearageNoPage);


        if (bankCollectionEntities == null || bankCollectionEntities.size() < 1) {
            return new ArrayList<>();
        }

        //获取总行信息
        SfPowerBankDomain sfPowerBankDomain = new SfPowerBankDomain();
        sfPowerBankDomain.setPageSize(-1);
        List<SfPowerBankDomain> sfPowerBankDomains =
                cimService.getSfPowerBank(sfPowerBankDomain);

        List<Long> parentBusinessPlaceCode =
                sfPowerBankDomains.stream().map(SfPowerBankDomain::getBusinessPlaceCode).distinct().collect(toList());

        Map<Long, Long> businessPlaceCodeMap = new HashMap<>();

        for (Long parent : parentBusinessPlaceCode) {
            List<DeptDomain> child = deptService.getDeptList(parent);

            if(child==null || child.size()<1){
                continue;
            }

            for (DeptDomain deptDomain:child){
                businessPlaceCodeMap.put(deptDomain.getId(),parent);
            }
        }

        Map<String, SfPowerBankDomain> sfPowerBankDomainMap =
                sfPowerBankDomains.stream().collect(Collectors.toMap(o -> o.getBusinessPlaceCode() + "_" + o.getBankCode(), a -> a, (k1, k2) -> k1));

        Map<Long, String> connectBankMap =
                cimService.findSystemCommonConfigByType("CONNECT_BANK");

        //开户银行
        BankEntity bankEntity=new BankEntity();
        bankEntity.setPageSize(-1);
        List<BankEntity> bankArray =
                cimService.getBank(bankEntity);
        Map<Long,BankEntity> bankEntityMap=
                bankArray.stream().collect(Collectors.toMap(BankEntity::getId,a -> a, (k1, k2) -> k1));


        ChargeInfoDomain chargeInfoDomain = new ChargeInfoDomain();
        chargeInfoDomain.setfChargeMode((short) 4);
        chargeInfoDomain.setMon(bankCollectionEntity.getMon());
        chargeInfoDomain.setSettlementIds(settlementIds);
        List<ChargeInfoDomain> lastBalanceChargeInfoDomains =
                billingService.chargeBySettleIds(chargeInfoDomain);

        Map<Long, BigDecimal> lastBalanceChargeInfoMap =
                lastBalanceChargeInfoDomains.stream().filter(t -> t.getFactMoney() != null)
                        .collect(Collectors.groupingBy(ChargeInfoDomain::getSettlementId, CustomCollectors.summingBigDecimal(ChargeInfoDomain::getFactMoney)));



        //赋值数据项
        bankCollectionEntities.forEach(t->{
            BigDecimal oweMoney=t.getOweMoney();
            if(lastBalanceChargeInfoMap.get(t.getSettlementId())!=null){
                oweMoney=
                        oweMoney.subtract(lastBalanceChargeInfoMap.get(t.getSettlementId()));
            }
            t.setOweMoney(oweMoney);
            t.setOpendingBank(settementMap.get(t.getSettlementId()).getOpendingBank());
            t.setOpendingBankName(bankEntityMap.get(t.getOpendingBank()).getBankName());
            t.setConnectBank((long)settementMap.get(t.getSettlementId()).getConnectBank());
            t.setSettlementNo(settementMap.get(t.getSettlementId()).getSettlementNo());
            String key=
                    businessPlaceCodeMap.get(t.getBusinessPlaceCode()).toString()+"_"+t.getConnectBank();
            if(sfPowerBankDomainMap.get(key)==null){
                t.setDwBankNo("");
                t.setConnectBankName("");
            }else{
                t.setDwBankNo(sfPowerBankDomainMap.get(key).getAccount());
                t.setConnectBankName(sfPowerBankDomainMap.get(key).getBankName());
            }
            t.setSettlementName(settementMap.get(t.getSettlementId()).getAccountName());
            t.setBankNo(settementMap.get(t.getSettlementId()).getBankNo());
        });

        Map<String,List<BankCollectionEntity>> bankMapByBusinessAndDwBankNo=
                bankCollectionEntities.stream().collect(Collectors.groupingBy(o -> o.getBusinessPlaceCode() + "_" + o.getBankNo()));
        //组成返回数据体

        List<TableDataBean> bankCollectBeans = new ArrayList<>();
        for (String key : bankMapByBusinessAndDwBankNo.keySet()) {

            List<BankCollectionEntity> bankCollections=
                    bankMapByBusinessAndDwBankNo.get(key);

            if(bankCollections==null || bankCollections.size()<1){
                continue;
            }

            BankCollectionEntity bankCollection=bankCollections.get(0);

            TableDataBean bankCollectBean = new TableDataBean();
            bankCollectBean.setSettlementName(bankCollection.getSettlementName());
            bankCollectBean.setBankNo(bankCollection.getBankNo());
            if(bankCollectionEntity.getDate()==null ||"".equals(bankCollectionEntity.getDate())){
                Calendar calendar = Calendar.getInstance();
                bankCollectBean.setYear(calendar.get(Calendar.YEAR));
                bankCollectBean.setMon(calendar.get(Calendar.MONTH) + 1);
                bankCollectBean.setDay(calendar.get(Calendar.DATE));
            }else{
                bankCollectBean.setYear(Integer.valueOf(bankCollectionEntity.getDate().substring(0, 4)));
                bankCollectBean.setMon(Integer.valueOf(bankCollectionEntity.getDate().substring(4, 6)));
                bankCollectBean.setDay(Integer.valueOf(bankCollectionEntity.getDate().substring(6, 8)));

            }
            bankCollectBean.setSettlementNo(bankCollection.getSettlementNo());
            bankCollectBean.setOpendingName(bankCollection.getOpendingBankName());
            bankCollectBean.setDwBankNo(bankCollection.getDwBankNo());
            bankCollectBean.setConnectName(bankCollection.getConnectBankName());
            bankCollectBean.setOweNum(bankCollections.size());

            BigDecimal oweMoneySum =
                    bankCollections.stream().map(BankCollectionEntity::getOweMoney).reduce(BigDecimal.ZERO, BigDecimal::add);

            BigDecimal owePunishMoneySum =
                    bankCollections.stream().map(BankCollectionEntity::getPunishMoney).reduce(BigDecimal.ZERO, BigDecimal::add);

            bankCollectBean.setOweMoneySum(oweMoneySum.add(owePunishMoneySum));
            bankCollectBean.setMoneyInWord(NumToString.number2CNMontrayUnit(oweMoneySum.add(owePunishMoneySum)));
            bankCollectBeans.add(bankCollectBean);
        }


        return bankCollectBeans;
    }


    public List<TableDataBean> arrearageDetail(ArrearageDomain arrearage) {
        List<TableDataBean> tableDataList = new ArrayList<>();

        List<ArrearageDomain> arrearageDomains=arrearageDetailQuery(arrearage).getList();

        TableDataBean tableData = new TableDataBean();

        tableData.setTableData(new JRBeanCollectionDataSource(arrearageDomains));
        tableDataList.add(tableData);

        return tableDataList;
    }



    public List<ArrearageDomain> arrearageFindByWhere(ArrearageDomain arrearage) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        List<ArrearageDomain> list = new ArrayList<>();
        try {
            list = titanTemplate.post("BILLING-SERVER",
                    "billingServer/arrearage/select", httpHeaders,
                    GsonUtils.toJson(arrearage),
                    new TypeReference<List<ArrearageDomain>>() {
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public HttpResultPagination<ArrearageDomain> arrearageDetailQuery(ArrearageDomain arrearage) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        HttpResultPagination<ArrearageDomain> httpResultPagination =
                new HttpResultPagination<>();
        try {
            httpResultPagination = titanTemplate.post("BILLING-SERVER",
                    "billingServer/arrearage/arrearageDetailQuery", httpHeaders,
                    GsonUtils.toJson(arrearage),
                    new TypeReference<HttpResultPagination<ArrearageDomain>>() {
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
        return httpResultPagination;
    }
}
