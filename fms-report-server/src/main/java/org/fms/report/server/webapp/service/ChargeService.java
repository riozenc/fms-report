package org.fms.report.server.webapp.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.fms.report.common.util.CustomCollectors;
import org.fms.report.common.util.FormatterUtil;
import org.fms.report.common.util.MonUtils;
import org.fms.report.common.util.NumToString;
import org.fms.report.common.webapp.bean.RecRateBean;
import org.fms.report.common.webapp.bean.TableDataBean;
import org.fms.report.common.webapp.domain.ArrearageDomain;
import org.fms.report.common.webapp.domain.ChargeInfoDetailEntity;
import org.fms.report.common.webapp.domain.ChargeInfoDomain;
import org.fms.report.common.webapp.domain.ChargeInfoEntity;
import org.fms.report.common.webapp.domain.DeptDomain;
import org.fms.report.common.webapp.domain.MeterDomain;
import org.fms.report.common.webapp.domain.MeterMoneyDomain;
import org.fms.report.common.webapp.domain.NoteInfoDomain;
import org.fms.report.common.webapp.domain.ParamDomain;
import org.fms.report.common.webapp.domain.PriceExecutionDomain;
import org.fms.report.common.webapp.domain.PriceLadderRelaDomain;
import org.fms.report.common.webapp.domain.PriceTypeDomain;
import org.fms.report.common.webapp.domain.SettlementDomain;
import org.fms.report.common.webapp.domain.SystemCommonConfigDomain;
import org.fms.report.common.webapp.domain.UserDomain;
import org.fms.report.common.webapp.domain.WriteFilesDomain;
import org.fms.report.common.webapp.domain.WriteSectDomain;
import org.fms.report.common.webapp.domain.WriteSectMongoDomain;
import org.fms.report.common.webapp.returnDomain.FeeRecStatisticsBean;
import org.fms.report.common.webapp.returnDomain.MapEntity;
import org.fms.report.server.webapp.dao.ArrearageDAO;
import org.fms.report.server.webapp.dao.ChargeInfoDAO;
import org.fms.report.server.webapp.dao.DeptDAO;
import org.fms.report.server.webapp.dao.SystemCommonConfigDAO;
import org.fms.report.server.webapp.dao.UserDAO;
import org.springframework.beans.factory.annotation.Autowired;

import com.riozenc.titanTool.annotation.TransactionDAO;
import com.riozenc.titanTool.annotation.TransactionService;
import com.riozenc.titanTool.properties.Global;
import com.riozenc.titanTool.spring.web.client.TitanTemplate;
import com.riozenc.titanTool.spring.web.http.HttpResultPagination;

import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

@TransactionService
public class ChargeService {
    @Autowired
    private TitanTemplate titanTemplate;

    @TransactionDAO
    private UserDAO userDAO;

    @TransactionDAO
    private SystemCommonConfigDAO systemCommonConfigDAO;

    @TransactionDAO
    private DeptDAO deptDAO;

    @TransactionDAO
    private ArrearageDAO arrearageDAO;

    @TransactionDAO
    private ChargeInfoDAO chargeInfoDAO;

    @Autowired
    private WriteSectService writeSectService;

    @Autowired
    private BillingService billingService;


    @Autowired
    private CimService cimService;

    @Autowired
    private ArrearageService arrearageService;

    @Autowired
    private MeterService meterService;

    @Autowired
    private UserService userService;

    @Autowired
    private DeptService deptService;

    public List<TableDataBean> recRate(ArrearageDomain arrearageDomain) {

        TableDataBean tableData = new TableDataBean();

        long yshs;
        BigDecimal ysje;
        long sshs;
        BigDecimal ssje;
        long qfhs;
        BigDecimal qfje;

        if ("year".equals(arrearageDomain.getGroupByDate())) {
            arrearageDomain.setStartMon(Integer.valueOf(arrearageDomain.getYear() + "01"));
            arrearageDomain.setEndMon(Integer.valueOf(arrearageDomain.getYear() + "12"));
        }
        List<String> mons = FormatterUtil.getMonthBetween(String.valueOf(arrearageDomain.getStartMon()), String.valueOf(arrearageDomain.getEndMon()));
        List<Integer> integerMons = mons.stream().map(x -> Integer.valueOf(x)).collect(Collectors.toList());
        arrearageDomain.setMons(integerMons);


        WriteSectMongoDomain writeSectMongoDomain = new WriteSectMongoDomain();
        writeSectMongoDomain.setMon(arrearageDomain.getStartMon());
        writeSectMongoDomain.setPageSize(-1);
        if ("writer".equals(arrearageDomain.getGroupBy())) {
            writeSectMongoDomain.setWritorIds(arrearageDomain.getWritorIds());
        } else if ("dept".equals(arrearageDomain.getGroupBy())) {
            writeSectMongoDomain.setBusinessPlaceCodes(arrearageDomain.getBusinessPlaceCodes());
        }

        List<WriteSectMongoDomain> writeSectMongoDomains =
                billingService.getMongoWriteSect(writeSectMongoDomain);

        if (writeSectMongoDomains == null || writeSectMongoDomains.size() < 1) {
            return new ArrayList<>();
        }
        //按条件获取抄表区段id集合
        List<Long> writeSectIds = new ArrayList<>();
        writeSectIds =
                writeSectMongoDomains.stream().map(WriteSectMongoDomain::getId).collect(Collectors.toList());

        Map<Long, WriteSectMongoDomain> writeSectMongoDomainMap =
                writeSectMongoDomains.stream().collect(Collectors.toMap(o -> o.getId(), a -> a, (k1, k2) -> k1));

        UserDomain userDomain = new UserDomain();
        userDomain.setMon(arrearageDomain.getStartMon());
        userDomain.setWriteSectIds(writeSectIds);
        userDomain.setUserType(arrearageDomain.getCustomerType());
        userDomain.setPageSize(-1);
        List<UserDomain> userDomains = userService.getUser(userDomain);
        List<Long> userIds =
                userDomains.stream().map(UserDomain::getId).distinct().collect(Collectors.toList());

        if (userIds == null || userIds.size() < 1) {
            return new ArrayList<>();
        }

        //根据用户id 汇总计量点id集合
        MeterDomain meterDomain = new MeterDomain();
        meterDomain.setMon(arrearageDomain.getStartMon());
        meterDomain.setUserIds(userIds);
        List<MeterDomain> meterDomains =
                meterService.getMeter(meterDomain);

        List<Long> meterIds =
                meterDomains.stream().map(MeterDomain::getId).distinct().collect(Collectors.toList());

        Map<Long, MeterDomain> meterDomainMap =
                meterDomains.stream().collect(Collectors.toMap(o -> o.getId(), a -> a, (k1, k2) -> k1));

        arrearageDomain.setMeterIds(meterIds);

        List<ArrearageDomain> arrearageBeanList = arrearageDAO.findByWhere(arrearageDomain);

        //收费记录
        ChargeInfoDomain chargeInfoDomain = new ChargeInfoDomain();
        chargeInfoDomain.setMons(integerMons);
        chargeInfoDomain.setPayDate(arrearageDomain.getCutDate() == null ? null :
                new Timestamp(arrearageDomain.getCutDate().getTime() + (long) 1000 * 3600 * 24));
        chargeInfoDomain.setMeterIds(meterIds);
        List<ChargeInfoDomain> chargeInfoDomainList = chargeInfoDAO.findByWhere(chargeInfoDomain);


        //向缴费记录List中插入抄表员信息
        for (ChargeInfoDomain infoDomain : chargeInfoDomainList) {
            infoDomain.setWritorId(meterDomainMap.get(infoDomain.getMeterId()).getWritorId());
        }


        List<RecRateBean> resultList = new ArrayList<RecRateBean>();


        if ("writer".equals(arrearageDomain.getGroupBy())) {
            //系统用户-获取抄表员名称
            List<Map<Long, Object>> listmap = userDAO.findMapByDomain(userDomain);
            Map<Long, String> UserMap = FormatterUtil.ListMapToMap(listmap);
            tableData.setvName("抄表员");

            for (Long key : arrearageDomain.getWritorIds()) {

                //应收户数
                yshs =
                        arrearageBeanList.stream().filter(a -> key.equals(a.getWritorId()) && a.getReceivable().compareTo(BigDecimal.ZERO) != 0).map(ArrearageDomain::getSettlementId).distinct().count();
                //应收金额
                ysje = arrearageBeanList.stream().filter(a -> key.equals(a.getWritorId())).map(x -> x.getReceivable()).reduce(BigDecimal.ZERO, BigDecimal::add);

                // 实收户数
                sshs =
                        arrearageBeanList.stream().filter(a -> key.equals(a.getWritorId()) && a.getReceivable().compareTo(BigDecimal.ZERO) != 0 && a.getOweMoney().compareTo(BigDecimal.ZERO) == 0).map(ArrearageDomain::getSettlementId).distinct().count();
                //实收金额
                ssje = chargeInfoDomainList.stream().filter(a -> key.equals(a.getWritorId())).map(x -> x.getFactMoney()).reduce(BigDecimal.ZERO, BigDecimal::add);
                //欠费户数
                qfhs = arrearageBeanList.stream().filter(a -> key.equals(a.getWritorId()) && Integer.valueOf(0).equals(a.getIsSettle())).map(ArrearageDomain::getSettlementId).distinct().count();
//            //欠费金额
                qfje = arrearageBeanList.stream().filter(a -> key.equals(a.getWritorId())).map(x -> x.getOweMoney() == null ? BigDecimal.ZERO : x.getOweMoney()).reduce(BigDecimal.ZERO, BigDecimal::add);
                RecRateBean recRateBean = new RecRateBean();
                recRateBean.setName(UserMap.get(key));
                recRateBean.setvName("抄表员");
                recRateBean.setQfhs(qfhs);
                recRateBean.setQfje(qfje);
                recRateBean.setSshs(sshs);
                recRateBean.setSsje(ssje);
                recRateBean.setYshs(yshs);
                recRateBean.setYsje(ysje);
                if (yshs != 0) {
                    recRateBean.setRecovery(ssje.divide(ysje, 4,
                            BigDecimal.ROUND_HALF_UP));
                } else {
                    recRateBean.setRecovery(BigDecimal.valueOf(0));
                }

                resultList.add(recRateBean);
            }
        } else if ("dept".equals(arrearageDomain.getGroupBy())) {
            DeptDomain deptDomain = new DeptDomain();
            //部门用户
            List<MapEntity> deptIdlistmap = deptDAO.findIdMapByDomain(deptDomain);
            Map<Long, String> deptIdMap = FormatterUtil.ListMapEntityToMap(deptIdlistmap);

            tableData.setvName("营业区域");
            for (Long key : arrearageDomain.getBusinessPlaceCodes()) {

                //应收户数
                yshs =
                        arrearageBeanList.stream().filter(a -> key.equals(a.getBusinessPlaceCode()) && a.getReceivable().compareTo(BigDecimal.ZERO) != 0).map(ArrearageDomain::getSettlementId).distinct().count();
                //应收金额
                ysje = arrearageBeanList.stream().filter(a -> key.equals(a.getBusinessPlaceCode())).map(x -> x.getReceivable()).reduce(BigDecimal.ZERO, BigDecimal::add);

                // 实收户数
                sshs =
                        arrearageBeanList.stream().filter(a -> key.equals(a.getBusinessPlaceCode()) && a.getReceivable().compareTo(BigDecimal.ZERO) != 0 && a.getOweMoney().compareTo(BigDecimal.ZERO) == 0).map(ArrearageDomain::getSettlementId).distinct().count();
                //实收金额
                ssje = chargeInfoDomainList.stream().filter(a -> key.equals(a.getBusinessPlaceCode())).map(x -> x.getFactMoney()).reduce(BigDecimal.ZERO, BigDecimal::add);
                //欠费户数
                qfhs = arrearageBeanList.stream().filter(a -> key.equals(a.getBusinessPlaceCode()) && Integer.valueOf(0).equals(a.getIsSettle())).map(ArrearageDomain::getSettlementId).distinct().count();
//            //欠费金额
                qfje = arrearageBeanList.stream().filter(a -> key.equals(a.getBusinessPlaceCode())).map(x -> x.getOweMoney() == null ? BigDecimal.ZERO : x.getOweMoney()).reduce(BigDecimal.ZERO, BigDecimal::add);
                RecRateBean recRateBean = new RecRateBean();
                recRateBean.setName(deptIdMap.get(key));
                recRateBean.setvName("营业区域");
                recRateBean.setQfhs(qfhs);
                recRateBean.setQfje(qfje);
                recRateBean.setSshs(sshs);
                recRateBean.setSsje(ssje);
                recRateBean.setYshs(yshs);
                recRateBean.setYsje(ysje);
                if (yshs != 0) {
                    recRateBean.setRecovery(ssje.divide(ysje, 4,
                            BigDecimal.ROUND_HALF_UP));
                } else {
                    recRateBean.setRecovery(BigDecimal.valueOf(0));
                }
                resultList.add(recRateBean);
            }
        } else if ("writeSect".equals(arrearageDomain.getGroupBy())) {

            tableData.setvName("抄表区段");
            for (Long key : arrearageDomain.getWriteSectIds()) {

                //应收户数
                yshs =
                        arrearageBeanList.stream().filter(a -> key.equals(a.getWriteSectId()) && a.getReceivable().compareTo(BigDecimal.ZERO) != 0).map(ArrearageDomain::getSettlementId).distinct().count();
                //应收金额
                ysje = arrearageBeanList.stream().filter(a -> key.equals(a.getWriteSectId())).map(x -> x.getReceivable()).reduce(BigDecimal.ZERO, BigDecimal::add);

                //抄表段信息---获取抄表员信息
                //根据抄表段
                // 实收户数
                sshs =
                        chargeInfoDomainList.stream().filter(a -> key.equals(a.getWriteSectId()) && a.getfChargeMode() != 4).map(ChargeInfoDomain::getSettlementId).distinct().count();
                //实收金额
                ssje = chargeInfoDomainList.stream().filter(a -> key.equals(a.getWriteSectId())).map(x -> x.getFactMoney()).reduce(BigDecimal.ZERO, BigDecimal::add);
                //欠费户数
                qfhs = arrearageBeanList.stream().filter(a -> key.equals(a.getWriteSectId()) && Integer.valueOf(0).equals(a.getIsSettle())).map(ArrearageDomain::getSettlementId).distinct().count();
//            //欠费金额
                qfje = arrearageBeanList.stream().filter(a -> key.equals(a.getWriteSectId())).map(x -> x.getOweMoney() == null ? BigDecimal.ZERO : x.getOweMoney()).reduce(BigDecimal.ZERO, BigDecimal::add);
                RecRateBean recRateBean = new RecRateBean();
                recRateBean.setName(writeSectMongoDomainMap.get(key).getWriteSectName());
                recRateBean.setvName("抄表区段");
                recRateBean.setQfhs(qfhs);
                recRateBean.setQfje(qfje);
                recRateBean.setSshs(sshs);
                recRateBean.setSsje(ssje);
                recRateBean.setYshs(yshs);
                recRateBean.setYsje(ysje);
                if (yshs != 0) {
                    recRateBean.setRecovery(ssje.divide(ysje, 4,
                            BigDecimal.ROUND_HALF_UP));
                } else {
                    recRateBean.setRecovery(BigDecimal.valueOf(0));
                }
                resultList.add(recRateBean);
            }
        }

        List<TableDataBean> tableDataList = new ArrayList<>();


        tableData.setTableData(new

                JRBeanCollectionDataSource(resultList));
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        tableData.setPrintDate(sdf.format(new

                Date()));
        tableDataList.add(tableData);
        return tableDataList;
    }

    public List<TableDataBean> feeRecStatistics(ChargeInfoDomain chargeInfoDomain) {
        List<TableDataBean> tableDataList = new ArrayList<>();

        TableDataBean tableData = new TableDataBean();


        List<DeptDomain> paramDeptList = deptService.findByWhere(chargeInfoDomain);
        List<Long> paramDeptIdList = new ArrayList<>();
        for (DeptDomain deptDomain : paramDeptList) {
            paramDeptIdList.add(deptDomain.getId());
        }
        chargeInfoDomain.setDeptParamList(paramDeptIdList);
        List<FeeRecStatisticsBean> chargeInfoDomainList = chargeInfoDAO.getFeeRecStatistics(chargeInfoDomain);



//系统用户
        UserDomain userDomain = new UserDomain();
        List<MapEntity> listmap = userService.findMapByDomain(userDomain);
        Map<Long, String> userMap = FormatterUtil.ListMapEntityToMap(listmap);
        //下拉列表
        SystemCommonConfigDomain systemCommonConfigDomain = new SystemCommonConfigDomain();
        systemCommonConfigDomain.setType("F_CHARGE_MODE");
        List<Map<Integer, Object>> sysListmap = systemCommonConfigDAO.findMapByDomain(systemCommonConfigDomain);
        Map<Integer, String> systemCommonConfigMap = FormatterUtil.LongListMapToMap(sysListmap);

        //部门用户
        DeptDomain deptDomain = new DeptDomain();
        List<MapEntity> deptIdlistmap = deptService.findIdMapByDomain(deptDomain);
        Map<Long, String> deptIdMap = FormatterUtil.ListMapEntityToMap(deptIdlistmap);
        //部门用户
        List<MapEntity> deptDeptIdlistmap = deptService.findDeptIdMapByDomain(deptDomain);
        Map<Long, String> deptDeptIdMap = FormatterUtil.ListMapEntityToMap(deptDeptIdlistmap);

        Map<Long, WriteSectDomain> writeSectDomainMap=new HashMap<>();
        if ("writeSectId".equals(chargeInfoDomain.getGroupBy())){
            WriteSectDomain writeSectDomain = new WriteSectDomain();
            writeSectDomain.setBusinessPlaceCodes(paramDeptIdList);
            //writeSectDomain.setMon(arrearage.getStartMon());
            writeSectDomain.setPageSize(-1);
            List<WriteSectDomain> writeSectDomains = cimService.getWriteSectFindByWhere(writeSectDomain);

            writeSectDomainMap = writeSectDomains
                    .stream().collect(Collectors.toMap(WriteSectDomain::getId, k -> k));
        }

        if ("operator".equals(chargeInfoDomain.getGroupBy())) {
            tableData.setvName("收费员");
        } else if ("dept".equals(chargeInfoDomain.getGroupBy())) {
            tableData.setvName("营业区域");
        } else if ("chargeMode".equals(chargeInfoDomain.getGroupBy())) {
            tableData.setvName("收费方式");
        } else if ("mon".equals(chargeInfoDomain.getGroupBy())) {
            tableData.setvName("年月");
        }else if ("writeSectId".equals(chargeInfoDomain.getGroupBy())) {
            tableData.setvName("抄表区段");
        }

        List<FeeRecStatisticsBean> resultList = new ArrayList<FeeRecStatisticsBean>();
        for (FeeRecStatisticsBean chargeInfoBean : chargeInfoDomainList) {
            if ("operator".equals(chargeInfoDomain.getGroupBy())) {
                chargeInfoBean.setName(userMap.get(Long.parseLong(chargeInfoBean.getName())));
            } else if ("dept".equals(chargeInfoDomain.getGroupBy())) {
                chargeInfoBean.setName(deptIdMap.get(Long.parseLong(chargeInfoBean.getName())));
            } else if ("chargeMode".equals(chargeInfoDomain.getGroupBy())) {
                chargeInfoBean.setName(systemCommonConfigMap.get(Integer.valueOf(chargeInfoBean.getName())));
            } else if ("mon".equals(chargeInfoDomain.getGroupBy())) {
                chargeInfoBean.setName(String.valueOf(chargeInfoBean.getMon()));
            }else if ("writeSectId".equals(chargeInfoDomain.getGroupBy())) {
                if(chargeInfoBean.getName()==null||
                        writeSectDomainMap.get(Long.parseLong(chargeInfoBean.getName()))==null){
                    chargeInfoBean.setName("无抄表区段");
                }else{
                    chargeInfoBean.setName(writeSectDomainMap.get(Long.parseLong(chargeInfoBean.getName())).getWriteSectName()+"/"+writeSectDomainMap.get(Long.parseLong(chargeInfoBean.getName())).getWriteSectName());
                }
            }

        }

        chargeInfoDomainList=
                chargeInfoDomainList.stream().sorted(Comparator.comparing(FeeRecStatisticsBean::getName,Comparator.nullsLast(String::compareTo))).collect(Collectors.toList());
        tableData.setTableData(new JRBeanCollectionDataSource(chargeInfoDomainList));
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        tableData.setPrintDate(sdf.format(new Date()));
        tableData.setDept(deptDeptIdMap.get(Long.valueOf(chargeInfoDomain.getDeptIdLike())));
        tableData.setStartDate(chargeInfoDomain.getStartDate() == null ? "" :
                chargeInfoDomain.getStartDate().toString());
        tableData.setEndDate(chargeInfoDomain.getEndDate() == null ? "" :
                chargeInfoDomain.getEndDate().toString());
        tableDataList.add(tableData);


        return tableDataList;
    }

    public List<TableDataBean> chargeDetails(ChargeInfoDomain chargeInfoDomain) {

        TableDataBean tableData = new TableDataBean();
        //传入实体转换
        ChargeInfoEntity chargeInfoEntity = new ChargeInfoEntity();
        chargeInfoEntity.setOperator(chargeInfoDomain.getUserParam() == null ? null : chargeInfoDomain.getUserParam().toString());
        chargeInfoEntity.setDept(chargeInfoDomain.getDeptParam() == null ? null : chargeInfoDomain.getDeptParam().toString());
        chargeInfoEntity.setWriteSect(chargeInfoDomain.getWriteSectIdParam() == null ? null : chargeInfoDomain.getWriteSectIdParam().toString());
        chargeInfoEntity.setStartPayDate(chargeInfoDomain.getStartDate());
        chargeInfoEntity.setEndPayDate(chargeInfoDomain.getEndDate());
        chargeInfoEntity.setfChargeMode(chargeInfoDomain.getfChargeMode() == null ? null : chargeInfoDomain.getfChargeMode().toString());
        chargeInfoEntity.setStartMon(chargeInfoDomain.getStartMon() == null ? null : chargeInfoDomain.getStartMon().toString());
        chargeInfoEntity.setEndMon(chargeInfoDomain.getEndMon() == null ? null : chargeInfoDomain.getEndMon().toString());
        chargeInfoEntity.setPageSize(chargeInfoDomain.getPageSize());

        HttpResultPagination<ChargeInfoDetailEntity> httpResultPagination =
                billingService.findChargeInfoDetails(chargeInfoEntity);

        List<TableDataBean> tableDataList = new ArrayList<>();
        //系统用户
        UserDomain userDomain = new UserDomain();
        List<MapEntity> listmap = userService.findMapByDomain(userDomain);
        Map<Long, String> userMap = FormatterUtil.ListMapEntityToMap(listmap);

        //下拉列表
        Map<Long, String> chargeModeMap =
                cimService.findSystemCommonConfigByType("F_CHARGE_MODE");
        Map<Long, String> ysTypeCodeMap =
                cimService.findSystemCommonConfigByType("YS_TYPE_CODE");

        //部门用户
        DeptDomain deptDomain = new DeptDomain();

        //部门用户
        List<MapEntity> deptDeptIdlistmap = deptService.findDeptIdMapByDomain(deptDomain);
        Map<Long, String> deptDeptIdMap = FormatterUtil.ListMapEntityToMap(deptDeptIdlistmap);

        List<ChargeInfoDetailEntity> chargeInfoDetailEntities =
                (List<ChargeInfoDetailEntity>) httpResultPagination.getList();

        if (chargeInfoDetailEntities == null || chargeInfoDetailEntities.size() < 1) {
            return new ArrayList<>();
        }

        List<Long> meterMoneyIds =
                chargeInfoDetailEntities.stream().map(ChargeInfoDetailEntity::getMeterMoneyId).distinct().collect(Collectors.toList());

        List<MeterMoneyDomain> meterMoneyDomains =
                billingService.findMeterMoneyByIds(meterMoneyIds);

        Map<Long, MeterMoneyDomain> moneyDomainMap =
                meterMoneyDomains.stream().collect(Collectors.toMap(MeterMoneyDomain::getId, a -> a, (k1, k2) -> k1));


        List<FeeRecStatisticsBean> feeRecStatisticsBeans = new ArrayList<FeeRecStatisticsBean>();

        chargeInfoDetailEntities.forEach(t -> {
            FeeRecStatisticsBean feeRecStatisticsBean =
                    new FeeRecStatisticsBean();
            feeRecStatisticsBean.setSettlementNo(Long.valueOf(t.getSettlementNo()));
            feeRecStatisticsBean.setSettlementName(t.getSettlementName());
            feeRecStatisticsBean.setMon(t.getMon());
            feeRecStatisticsBean.setYsTypeName(ysTypeCodeMap.get(Long.valueOf(t.getYsTypeCode())));
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            tableData.setPrintDate(sdf.format(new Date()));
            feeRecStatisticsBean.setPayDate(sdf.format(t.getPayDate()));
            feeRecStatisticsBean.setFactMoney(t.getFactMoney());
            feeRecStatisticsBean.setFactPunish(t.getFactPunish());
            feeRecStatisticsBean.setFactPre(t.getFactPre());
            feeRecStatisticsBean.setFactTotal(t.getFactTotal());
            feeRecStatisticsBean.setOperatorName(userMap.get(t.getOperator()));
            if (feeRecStatisticsBean.getOperatorName() == null) {
                feeRecStatisticsBean.setOperatorName("管理员");
            }
            feeRecStatisticsBean.setfChargeModeName(chargeModeMap.get(Long.valueOf(t.getfChargeMode())));
            feeRecStatisticsBean.setShhx(BigDecimal.ZERO);
            feeRecStatisticsBean.setNotePrintNo(t.getYfphm());
            feeRecStatisticsBean.setMeterId(t.getMeterId());
            if (t.getMeterMoneyId() != null) {
                if (moneyDomainMap.get(t.getMeterMoneyId()) == null) {
                    System.out.println("123");
                }
                feeRecStatisticsBean.setTotalPower(moneyDomainMap.get(t.getMeterMoneyId()).getTotalPower());
                feeRecStatisticsBean.setVolumeCharge(moneyDomainMap.get(t.getMeterMoneyId()).getVolumeCharge());
                feeRecStatisticsBean.setPowerRateMoney(moneyDomainMap.get(t.getMeterMoneyId()).getPowerRateMoney());
                feeRecStatisticsBean.setBasicMoney(moneyDomainMap.get(t.getMeterMoneyId()).getBasicMoney());
                feeRecStatisticsBean.setAddMoney2(moneyDomainMap.get(t.getMeterMoneyId()).getAddMoney1());
                feeRecStatisticsBean.setAddMoney3(moneyDomainMap.get(t.getMeterMoneyId()).getAddMoney2());
                feeRecStatisticsBean.setAddMoney4(moneyDomainMap.get(t.getMeterMoneyId()).getAddMoney3());
                feeRecStatisticsBean.setAddMoney5(moneyDomainMap.get(t.getMeterMoneyId()).getAddMoney4());
                feeRecStatisticsBean.setAddMoney6(moneyDomainMap.get(t.getMeterMoneyId()).getAddMoney5());
                feeRecStatisticsBean.setAddMoney7(moneyDomainMap.get(t.getMeterMoneyId()).getAddMoney6());
                feeRecStatisticsBean.setAddMoney8(moneyDomainMap.get(t.getMeterMoneyId()).getAddMoney7());
                feeRecStatisticsBean.setAddMoney9(moneyDomainMap.get(t.getMeterMoneyId()).getAddMoney8());
            }
            feeRecStatisticsBeans.add(feeRecStatisticsBean);
        });

        Map<Integer, Map<Long, List<PriceExecutionDomain>>> monsPriceMap = new HashMap<>();

        //计量点信息
        List<Long> meterIds =
                chargeInfoDetailEntities.stream().map(ChargeInfoDetailEntity::getMeterId).distinct().collect(Collectors.toList());

        Map<Integer, Map<Long, MeterDomain>> monMeterMap = new HashMap<>();

        List<Integer> mons =
                chargeInfoDetailEntities.stream().map(e -> e.getMon()).distinct().collect(Collectors.toList());

        if (!mons.contains(Integer.valueOf(Global.getConfig("mon")))) {
            mons.add(Integer.valueOf(Global.getConfig("mon")));
        }

        for (Integer mon : mons) {
            Map<Long, List<PriceExecutionDomain>> priceMap = billingService.findMongoPriceExecution(mon);
            monsPriceMap.put(mon, priceMap);

            MeterDomain meterDomain = new MeterDomain();
            meterDomain.setMon(mon);
            meterDomain.setIds(meterIds);
            List<MeterDomain> meterDomains = meterService.getMeter(meterDomain);
            Map<Long, MeterDomain> meterDomainMap =
                    meterDomains.stream().collect(Collectors.toMap(MeterDomain::getId, a -> a, (k1, k2) -> k1));
            monMeterMap.put(mon, meterDomainMap);
        }

        List<FeeRecStatisticsBean> resultList = new ArrayList<FeeRecStatisticsBean>();
        for (FeeRecStatisticsBean feeRecStatisticsBean : feeRecStatisticsBeans) {
            int mon = feeRecStatisticsBean.getMon();
            if (feeRecStatisticsBean.getMon() < (Integer.valueOf(Global.getConfig("mon")))) {
                mon = Integer.valueOf(Global.getConfig("mon"));
            }
            MeterDomain meterDomain =
                    monMeterMap.get(mon).get(feeRecStatisticsBean.getMeterId());
            if (meterDomain != null) {
                feeRecStatisticsBean.setPrice(monsPriceMap.get(mon).get(meterDomain.getPriceType())
                        .stream().filter(a -> a.getPrice() != null).map(PriceExecutionDomain::getPrice).reduce(BigDecimal.ZERO, BigDecimal::add));
                feeRecStatisticsBean.setMeterNo(meterDomain.getMeterNo());
            }
        }

        tableData.setTableData(new JRBeanCollectionDataSource(feeRecStatisticsBeans));
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        tableData.setPrintDate(sdf.format(new Date()));
        tableData.setDept(deptDeptIdMap.get(chargeInfoDomain.getBusinessPlaceCode()));
        tableData.setStartDate(chargeInfoDomain.getStartDate() == null ? "" : chargeInfoDomain.getStartDate().toString());
        tableData.setEndDate(chargeInfoDomain.getEndDate() == null ? "" : chargeInfoDomain.getEndDate().toString());
        tableDataList.add(tableData);
        return tableDataList;
    }


    //结算单
    public List<TableDataBean> Statement(List<NoteInfoDomain> noteInfoDomainList, String operatorName) {

        Map<String, List<NoteInfoDomain>> noteInfoMapByMonAndSettle =
                noteInfoDomainList.stream().collect(Collectors.groupingBy(o -> o.getMon() + "_" + o.getSettlementId() + "_" + o.getYsTypeCode()));


        List<TableDataBean> tableDataList = new ArrayList<>();
        //系统用户
        /*UserDomain sysUserDomain = new UserDomain();
        List<Map<Long, Object>> listmap = userDAO.findMapByDomain(sysUserDomain);
        Map<Long, String> sysUserMap = FormatterUtil.ListMapToMap(listmap);*/

        //收费信息
        List<Long> chargeInfoIds =
                noteInfoDomainList.stream().map(NoteInfoDomain::getChargeInfoId).distinct().collect(Collectors.toList());

        List<ChargeInfoDomain> chargeInfoDomains =
                billingService.findChargeByIds(chargeInfoIds);

        List<Long> operatorIds=
                chargeInfoDomains.stream().map(ChargeInfoDomain::getOperator).distinct().collect(Collectors.toList());

        Map<Long, String> sysUserMap = userDAO.findByIds(operatorIds)
                .stream().collect(Collectors.toMap(UserDomain::getId, k -> k.getUserName()));

        Map<Long, ChargeInfoDomain> chargeInfoDomainMap =
                chargeInfoDomains.stream().collect(Collectors.toMap(ChargeInfoDomain::getId, a -> a, (k1, k2) -> k1));

        List<PriceTypeDomain> priceTypeDomains = billingService.findPriceType();

        Map<Long, PriceTypeDomain> priceTypeDomainMap =
                priceTypeDomains.stream().filter(m -> m.getId() != null).collect(Collectors.toMap(PriceTypeDomain::getId, a -> a, (k1, k2) -> k1));

        Map<Integer, Map<Long, List<PriceExecutionDomain>>> monsPriceMap = new HashMap<>();

        Map<Integer, List<PriceLadderRelaDomain>> priceLadderMap = new HashMap<>();

        List<Integer> mons =
                noteInfoDomainList.stream().map(e -> e.getMon()).distinct().collect(Collectors.toList());

        if (!mons.contains(Integer.valueOf(Global.getConfig("mon")))) {
            mons.add(Integer.valueOf(Global.getConfig("mon")));
        }

        for (Integer mon : mons) {
            Map<Long, List<PriceExecutionDomain>> priceMap = billingService.findMongoPriceExecution(mon);
            monsPriceMap.put(mon, priceMap);
            List<PriceLadderRelaDomain> monpriceLadderRelaDomains = billingService.findMongoPriceLadderRela(mon);
            priceLadderMap.put(mon, monpriceLadderRelaDomains);
        }

        //获取结算户信息
        List<Long> settlementIds =
                noteInfoDomainList.stream().map(NoteInfoDomain::getSettlementId).distinct().collect(Collectors.toList());

        List<SettlementDomain> settlementDomains =
                cimService.findSettlementByIds(settlementIds);

        Map<Long, SettlementDomain> settlementDomainMap =
                settlementDomains.stream().collect(Collectors.toMap(SettlementDomain::getId, a -> a, (k1, k2) -> k1));

        for (String k : noteInfoMapByMonAndSettle.keySet()) {

            List<NoteInfoDomain> v = noteInfoMapByMonAndSettle.get(k);

            TableDataBean tableDataBean = new TableDataBean();

            Integer mon = new Integer(Integer.valueOf(Global.getConfig("mon")));
            if (v.get(0).getMon() >= mon) {
                mon = v.get(0).getMon();
            }
            // Map<Long, List<PriceExecutionDomain>> priceMap =      billingService.findMongoPriceExecution(mon);

            List<PriceLadderRelaDomain> priceLadderRelaDomains = priceLadderMap.get(mon);

            List<Long> priceLadderPriceTypeIds =
                    priceLadderRelaDomains.stream().map(PriceLadderRelaDomain::getPriceExecutionId).collect(Collectors.toList());
            //按电价及sn分组
            Map<String, PriceLadderRelaDomain> priceLadderRelaMapByPriceAndSn =
                    priceLadderRelaDomains.stream().collect(Collectors.toMap(o -> o.getPriceExecutionId() + "_" + o.getLadderSn(), a -> a, (k1, k2) -> k1));
            //按电价分组
            Map<Long, List<PriceLadderRelaDomain>> priceLadderRelaMapByPrice =
                    priceLadderRelaDomains.stream().collect(Collectors.groupingBy(PriceLadderRelaDomain::getPriceExecutionId));

            List<NoteInfoDomain> noteInfoDomains = v;
            tableDataBean.setMon(v.get(0).getMon());
            tableDataBean.setSettlementNo(Long.valueOf(v.get(0).getSettlementNo()));
            tableDataBean.setSettlementName(v.get(0).getSettlementName());
            BigDecimal punishMoney =
                    noteInfoDomains.stream().filter(t -> t.getPunishMoney() != null)
                            .map(NoteInfoDomain::getPunishMoney).reduce(BigDecimal.ZERO, BigDecimal::add);
            tableDataBean.setFactPunish(punishMoney);
            BigDecimal factMoney =
                    noteInfoDomains.stream().filter(t -> t.getFactMoney() != null).filter(t->t.getfChargeMode()!=4).filter(t->t.getfChargeMode()!=5)
                            .map(NoteInfoDomain::getFactMoney).reduce(BigDecimal.ZERO, BigDecimal::add);

            BigDecimal factPre =
                    noteInfoDomains.stream().filter(t -> t.getFactPre() != null)
                            .map(NoteInfoDomain::getFactPre).reduce(BigDecimal.ZERO, BigDecimal::add);

            BigDecimal arrears =
                    noteInfoDomains.stream().filter(t -> t.getArrears() != null)
                            .map(NoteInfoDomain::getArrears).reduce(BigDecimal.ZERO,
                            BigDecimal::add);

            tableDataBean.setFactTotal(arrears);
            tableDataBean.setMoneyInWord(NumToString.number2CNMontrayUnit(arrears));
            if(factPre.compareTo(BigDecimal.ZERO)<0){
                factPre=BigDecimal.ZERO;
            }
            tableDataBean.setReceivableSum(punishMoney.add(factMoney).add(factPre));
            tableDataBean.setThisBalance(v.get(v.size() - 1).getThisBalance());

            //获取计算户号 取本月冲抵余额
            List<Long> monSettlementIds =
                    noteInfoDomains.stream().map(NoteInfoDomain::getSettlementId).distinct().collect(Collectors.toList());

            ChargeInfoDomain chargeInfoDomain = new ChargeInfoDomain();
            chargeInfoDomain.setfChargeMode((short) 4);
            chargeInfoDomain.setMon(Integer.valueOf(v.get(0).getMon().toString()));
            chargeInfoDomain.setSettlementIds(monSettlementIds);
            List<ChargeInfoDomain> lastBalanceChargeInfoDomains =
                    billingService.chargeBySettleIds(chargeInfoDomain);

            //获取计算户号 取本月冲抵余额


            BigDecimal factTotal = BigDecimal.ZERO;
            if (lastBalanceChargeInfoDomains != null && lastBalanceChargeInfoDomains.size() > 0) {
                factTotal =
                        lastBalanceChargeInfoDomains.stream().filter(t -> t.getFactMoney() != null).
                                map(ChargeInfoDomain::getFactMoney).reduce(BigDecimal.ZERO, BigDecimal::add);
            }

            //获取计算户号 取本月冲抵余额

            ChargeInfoDomain peoChargeInfoDomain = new ChargeInfoDomain();
            peoChargeInfoDomain.setfChargeMode((short) 5);
            peoChargeInfoDomain.setMon(Integer.valueOf(v.get(0).getMon().toString()));
            peoChargeInfoDomain.setSettlementIds(monSettlementIds);
            List<ChargeInfoDomain> peoChargeInfoDomains =
                    billingService.chargeBySettleIds(peoChargeInfoDomain);
            if (peoChargeInfoDomains != null && peoChargeInfoDomains.size() > 0) {
                factTotal =
                        factTotal.add(peoChargeInfoDomains.stream().filter(t -> t.getFactMoney() != null).
                                map(ChargeInfoDomain::getFactMoney).reduce(BigDecimal.ZERO, BigDecimal::add));
            }

            //退补电费
            ChargeInfoDomain recallChargeInfoDomain = new ChargeInfoDomain();
            recallChargeInfoDomain.setfChargeMode((short) 6);
            recallChargeInfoDomain.setMon(Integer.valueOf(v.get(0).getMon().toString()));
            recallChargeInfoDomain.setSettlementIds(monSettlementIds);
            List<ChargeInfoDomain> recallChargeInfoDomains =
                    billingService.chargeBySettleIds(recallChargeInfoDomain);
            BigDecimal recallMoney = BigDecimal.ZERO;
            if (recallChargeInfoDomains != null && recallChargeInfoDomains.size() > 0) {
                recallMoney =
                        recallChargeInfoDomains.stream().filter(t -> t.getFactMoney() != null).
                                map(ChargeInfoDomain::getFactMoney).reduce(BigDecimal.ZERO, BigDecimal::add);
            }


            tableDataBean.setLastBalance(v.get(0).getLastBalance().add(factTotal));

            ChargeInfoDomain returnChargeInfo = chargeInfoDomainMap.get(v.get(0).getChargeInfoId());

            tableDataBean.setOperatorName(operatorName);
            tableDataBean.setUserName(sysUserMap.get(new Long(returnChargeInfo.getOperator())));
            if (tableDataBean.getUserName() == null || "".equals(tableDataBean.getUserName())) {
                tableDataBean.setUserName("管理员");
            }
            tableDataBean.setPrintDate(MonUtils.getDay());

            tableDataBean.setSetAddress(settlementDomainMap.get(v.get(0).getSettlementId()).getAddress());

            //过滤预收
            List<Long> meterIds =
                    v.stream().filter(t->t.getMeterId()!=null).filter(t -> t.getYsTypeCode() != 2).map(NoteInfoDomain::getMeterId).distinct().collect(Collectors.toList());

            if(meterIds==null||meterIds.size()<1){
                tableDataBean.setLastBalance(v.get(0).getLastBalance());

            }

            //0电费不产生欠费的计量点
            ArrearageDomain arrearageDomain = new ArrearageDomain();
            arrearageDomain.setMon(mon);
            arrearageDomain.setSettlementId(v.get(0).getSettlementId());
            List<ArrearageDomain> arrearageDomains =
                    billingService.findArrearageBySettleIdMonAndSn(arrearageDomain);

            List<Long> zeroMoneyMeterIds = arrearageDomains.stream()
                    .filter(t -> t.getReceivable().compareTo(BigDecimal.ZERO) == 0 && t.getTotalPower().compareTo(BigDecimal.ZERO) != 0)
                    .distinct().map(ArrearageDomain::getMeterId).collect(Collectors.toList());

            if (zeroMoneyMeterIds != null && zeroMoneyMeterIds.size() > 0) {
                meterIds.addAll(zeroMoneyMeterIds);
            }
            List<FeeRecStatisticsBean> feeRecStatisticsBeans = new ArrayList<FeeRecStatisticsBean>();

            if (meterIds != null && meterIds.size() > 0) {

                MeterMoneyDomain meterMoneyDomain = new MeterMoneyDomain();
                meterMoneyDomain.setMon(v.get(0).getMon());
                meterMoneyDomain.setMeterIds(meterIds);
                List<MeterMoneyDomain> meterMoneyDomains =
                        billingService.findMeterMoneyByMonAndMeterIds(meterMoneyDomain);

                if (meterMoneyDomains == null || meterMoneyDomains.size() < 1) {
                    continue;
                }

                tableDataBean.setPowerRateMoney(meterMoneyDomains.stream().filter(a -> a.getPowerRateMoney() != null).map(MeterMoneyDomain::getPowerRateMoney).reduce(BigDecimal.ZERO, BigDecimal::add));
                tableDataBean.setBasicMoneySum(meterMoneyDomains.stream().filter(a -> a.getBasicMoney() != null).map(MeterMoneyDomain::getBasicMoney).reduce(BigDecimal.ZERO, BigDecimal::add));
                tableDataBean.setRefundMoney(meterMoneyDomains.stream().filter(a -> a.getRefundMoney() != null).map(MeterMoneyDomain::getRefundMoney).reduce(BigDecimal.ZERO, BigDecimal::add).negate());

                tableDataBean.setRefundMoney(recallMoney.add(tableDataBean.getRefundMoney()).negate());

                WriteFilesDomain conditionWriteFile = new WriteFilesDomain();
                conditionWriteFile.setMon(v.get(0).getMon());
                conditionWriteFile.setMeterIds(meterIds);
                List<WriteFilesDomain> writeFilesDomains =
                        billingService.findWriteFilesByMonAndMeterIds(conditionWriteFile);

                writeFilesDomains =
                        writeFilesDomains.stream().filter(t -> t.getTimeSeg() == 0).collect(Collectors.toList());

                Map<Long, List<WriteFilesDomain>> writeFilesDomainMap =
                        writeFilesDomains.stream()
                                .collect(Collectors.groupingBy(WriteFilesDomain::getMeterId));

                for (MeterMoneyDomain t : meterMoneyDomains) {

                    List<PriceExecutionDomain> priceExecutionDomains =
                            monsPriceMap.get(mon).get(t.getPriceTypeId());

                    BigDecimal price =
                            priceExecutionDomains.stream().filter(m -> m.getTimeSeg() == 0).map(PriceExecutionDomain::getPrice).reduce(BigDecimal.ZERO, BigDecimal::add);

                    //抄表记录
                    List<WriteFilesDomain> writeFilesByMeterId =
                            writeFilesDomainMap.get(t.getMeterId());

                    //无抄表记录的为虚拟表
                    if (writeFilesByMeterId == null || writeFilesByMeterId.size() < 1) {
                        FeeRecStatisticsBean feeRecStatisticsBean = new FeeRecStatisticsBean();
                        feeRecStatisticsBean.setStartNum(BigDecimal.ZERO);
                        feeRecStatisticsBean.setEndNum(BigDecimal.ZERO);
                        feeRecStatisticsBean.setFactorNum(BigDecimal.ZERO);
                        feeRecStatisticsBean.setAddPower(BigDecimal.ZERO);
                        feeRecStatisticsBean.setTotalPower(t.getTotalPower());
                        feeRecStatisticsBean.setReceivable(t.getVolumeCharge().add(t.getSurcharges()));
                        feeRecStatisticsBean.setPrice(price);
                        feeRecStatisticsBean.setName(priceTypeDomainMap.get(t.getPriceTypeId()).getPriceName());
                        feeRecStatisticsBeans.add(feeRecStatisticsBean);
                        continue;
                    }

                    for (WriteFilesDomain writeFilesDomain : writeFilesByMeterId) {

                        FeeRecStatisticsBean feeRecStatisticsBean = new FeeRecStatisticsBean();
                        feeRecStatisticsBean.setStartNum(writeFilesDomain.getStartNum());
                        feeRecStatisticsBean.setEndNum(writeFilesDomain.getEndNum());
                        feeRecStatisticsBean.setFactorNum(writeFilesDomain.getFactorNum());

                        feeRecStatisticsBean.setTotalPower(t.getTotalPower());
                        feeRecStatisticsBean.setReceivable(t.getVolumeCharge().add(t.getSurcharges()));
                        feeRecStatisticsBean.setPrice(price);
                        feeRecStatisticsBean.setName(priceTypeDomainMap.get(t.getPriceTypeId()).getPriceName());


                        BigDecimal addAndTransPower = BigDecimal.ZERO;

                        if (writeFilesDomain.getFunctionCode() == 2) {
                            feeRecStatisticsBean.setReceivable(BigDecimal.ZERO);
                            if (writeFilesDomain.getAddPower() != null) {
                                addAndTransPower =
                                        addAndTransPower.add(writeFilesDomain.getAddPower());
                            }
                            if (writeFilesDomain.getChgPower() != null) {
                                addAndTransPower =
                                        addAndTransPower.add(t.getChgPower());
                            }
                            feeRecStatisticsBean.setAddPower(addAndTransPower);
                            feeRecStatisticsBean.setReceivable(BigDecimal.ZERO);
                            feeRecStatisticsBeans.add(feeRecStatisticsBean);
                            continue;
                        }

                        if (t.getActiveLineLossPower() != null) {
                            addAndTransPower =
                                    addAndTransPower.add(t.getActiveLineLossPower());
                        }
                        if (t.getAddPower() != null) {
                            addAndTransPower =
                                    addAndTransPower.add(t.getAddPower());
                        }
                        if (t.getActiveTransformerLossPower() != null) {
                            addAndTransPower =
                                    addAndTransPower.add(t.getActiveTransformerLossPower());
                        }
                        if (t.getChgPower() != null) {
                            addAndTransPower =
                                    addAndTransPower.add(t.getChgPower());
                        }
                        if (t.getActiveDeductionPower() != null) {
                            addAndTransPower =
                                    addAndTransPower.add(t.getActiveDeductionPower());
                        }
                        feeRecStatisticsBean.setAddPower(addAndTransPower);
                        feeRecStatisticsBeans.add(feeRecStatisticsBean);

                        BigDecimal surPrice =
                                priceExecutionDomains.stream().filter(m -> m.getTimeSeg() == 0).filter(m -> m.getPriceItemId() != 1).map(PriceExecutionDomain::getPrice).reduce(BigDecimal.ZERO, BigDecimal::add);

                        //赋值阶梯信息
                        BigDecimal ladderSurcharges1 = BigDecimal.ZERO;
                        BigDecimal ladderSurcharges2 = BigDecimal.ZERO;
                        BigDecimal ladderSurcharges3 = BigDecimal.ZERO;
                        BigDecimal ladderSurcharges4 = BigDecimal.ZERO;

                        if (t.getLadder1Money() != null && t.getLadder1Money().compareTo(BigDecimal.ZERO) > 0) {
                            FeeRecStatisticsBean ladderbean = new FeeRecStatisticsBean();
                            ladderbean.setName("第一阶梯");
                            ladderbean.setPrice(priceLadderRelaMapByPriceAndSn.get(t.getPriceTypeId() + "_" + "1").getPrice().add(surPrice));
                            ladderbean.setTotalPower(t.getLadder1Power());
                            // 赋值附加费 考虑差值 有第二阶梯差值放在第二阶梯
                            if (t.getLadder2Money() != null && t.getLadder2Money().compareTo(BigDecimal.ZERO) > 0) {
                                ladderbean.setSurcharges(t.getLadder1Power().multiply(surPrice));
                                ladderSurcharges1 = ladderbean.getSurcharges();
                            } else {
                                ladderbean.setSurcharges(t.getSurcharges());
                            }
                            ladderbean.setReceivable(t.getLadder1Money().add(ladderbean.getSurcharges()));
                            feeRecStatisticsBeans.add(ladderbean);
                        }
                        if (t.getLadder2Money() != null && t.getLadder2Money().compareTo(BigDecimal.ZERO) > 0) {
                            FeeRecStatisticsBean ladderbean = new FeeRecStatisticsBean();
                            ladderbean.setName("第二阶梯");
                            ladderbean.setPrice(priceLadderRelaMapByPriceAndSn.get(t.getPriceTypeId() + "_" + "2").getPrice().add(surPrice));
                            ladderbean.setTotalPower(t.getLadder2Power());
                            // 赋值附加费 考虑差值 有第三阶梯差值放在第三阶梯
                            if (t.getLadder3Money() != null && t.getLadder3Money().compareTo(BigDecimal.ZERO) > 0) {
                                ladderbean.setSurcharges(t.getLadder2Power().multiply(surPrice));
                                ladderSurcharges2 =
                                        ladderSurcharges1.add(ladderbean.getSurcharges());
                            } else {
                                ladderbean.setSurcharges(t.getSurcharges().subtract(ladderSurcharges1));
                            }
                            ladderbean.setReceivable(t.getLadder2Money().add(ladderbean.getSurcharges()));
                            feeRecStatisticsBeans.add(ladderbean);
                        }
                        if (t.getLadder3Money() != null && t.getLadder3Money().compareTo(BigDecimal.ZERO) > 0) {
                            FeeRecStatisticsBean ladderbean = new FeeRecStatisticsBean();
                            ladderbean.setName("第三阶梯");
                            ladderbean.setPrice(priceLadderRelaMapByPriceAndSn.get(t.getPriceTypeId() + "_" + "3").getPrice().add(surPrice));
                            ladderbean.setTotalPower(t.getLadder3Power());
                            // 赋值附加费 考虑差值 有第四阶梯差值放在第四阶梯
                            if (t.getLadder4Money() != null && t.getLadder4Money().compareTo(BigDecimal.ZERO) > 0) {
                                ladderbean.setSurcharges(t.getLadder3Power().multiply(surPrice));
                                ladderSurcharges3 =
                                        ladderSurcharges2.add(ladderbean.getSurcharges());
                            } else {
                                ladderbean.setSurcharges(t.getSurcharges().subtract(ladderSurcharges2));
                            }
                            ladderbean.setReceivable(t.getLadder3Money().add(ladderbean.getSurcharges()));
                            feeRecStatisticsBeans.add(ladderbean);
                        }
                        if (t.getLadder4Money() != null && t.getLadder4Money().compareTo(BigDecimal.ZERO) > 0) {
                            FeeRecStatisticsBean ladderbean = new FeeRecStatisticsBean();
                            ladderbean.setName("第四阶梯");
                            ladderbean.setPrice(priceLadderRelaMapByPriceAndSn.get(t.getPriceTypeId() + "_" + "4").getPrice().add(surPrice));
                            ladderbean.setTotalPower(t.getLadder4Power());
                            // 赋值附加费 考虑差值
                            ladderbean.setSurcharges(t.getSurcharges().subtract(ladderSurcharges3));
                            ladderbean.setReceivable(t.getLadder4Money().add(ladderbean.getSurcharges()));
                            feeRecStatisticsBeans.add(ladderbean);
                        }
                    }
                }

                //有阶梯的计量点记录
                List<MeterMoneyDomain> ladderMeterMoneys =
                        meterMoneyDomains.stream().filter(t -> priceLadderPriceTypeIds.contains(t.getPriceTypeId()))
                                .collect(Collectors.toList());

                if (ladderMeterMoneys != null && ladderMeterMoneys.size() > 0) {

                    BigDecimal ladderSum =
                            ladderMeterMoneys.stream().map(MeterMoneyDomain::getLadderTotalPower)
                                    .reduce(BigDecimal.ZERO, BigDecimal::add).setScale(0, RoundingMode.HALF_UP);

                    BigDecimal ladderLimit1 = BigDecimal.ZERO;
                    BigDecimal ladderLimit2 = BigDecimal.ZERO;
                    BigDecimal ladderLimit3 = BigDecimal.ZERO;

                    for (int i = 0; i < ladderMeterMoneys.size(); i++) {
                        MeterMoneyDomain ladderMoneyDomain = ladderMeterMoneys.get(i);
                        List<PriceLadderRelaDomain> meterMoneyPriceLadders =
                                priceLadderRelaMapByPrice.get(ladderMoneyDomain.getPriceTypeId());

                        Map<Integer, PriceLadderRelaDomain> meterMoneyLadderMapBySn =
                                meterMoneyPriceLadders.stream()
                                        .collect(Collectors.toMap(PriceLadderRelaDomain::getLadderSn, a -> a, (k1, k2) -> k1));
                        ladderLimit1 =
                                ladderLimit1.add(new BigDecimal(meterMoneyLadderMapBySn.get(1).getLadderValue()));
                        ladderLimit2 =
                                ladderLimit2.add(new BigDecimal(meterMoneyLadderMapBySn.get(2).getLadderValue()));
                        ladderLimit3 =
                                ladderLimit3.add(new BigDecimal(meterMoneyLadderMapBySn.get(3).getLadderValue()));
                    }
                    //ladderLimit2是二阶梯总量包含一阶梯
                    tableDataBean.setLadder1Limit(ladderLimit1);
                    tableDataBean.setLadder2Limit(ladderLimit2.subtract(ladderLimit1));
                    tableDataBean.setLadder3Limit(ladderLimit3.subtract(ladderLimit2));
                    tableDataBean.setLadder4Limit("-");
                    //一阶梯电量
                    if (ladderSum.compareTo(ladderLimit1) == 1) {
                        tableDataBean.setLadder1Power(ladderLimit1);
                    } else {
                        tableDataBean.setLadder1Power(ladderSum);
                    }
                    //大约二阶梯
                    if (ladderSum.compareTo(ladderLimit2) == 1) {
                        tableDataBean.setLadder2Power(ladderLimit2.subtract(ladderLimit1));
                    } else if (ladderSum.compareTo(ladderLimit1) == 1) {
                        //大于一阶梯
                        tableDataBean.setLadder2Power(ladderSum.subtract(ladderLimit1));
                    }
                    if (ladderSum.compareTo(ladderLimit3) == 1) {
                        tableDataBean.setLadder3Power(ladderLimit3.subtract(ladderLimit2));
                        tableDataBean.setLadder4Power(ladderSum.subtract(ladderLimit3));
                    } else if (ladderSum.compareTo(ladderLimit1.add(ladderLimit2)) == 1) {
                        tableDataBean.setLadder3Power(ladderSum.subtract(ladderLimit2));
                    }

                }

            }
            tableDataBean.setTableData(new JRBeanCollectionDataSource(feeRecStatisticsBeans));
            tableDataList.add(tableDataBean);
        }
        ;

        List<TableDataBean> returnTableDataList = tableDataList;

        returnTableDataList =
                returnTableDataList.stream().sorted(Comparator.comparing(TableDataBean::getMon)).collect(Collectors.toList());

        return returnTableDataList;
    }

    /**
     * 封印
     * TableDataBean tableData = new TableDataBean();
     * <p>
     * <p>
     * List<Long> paramDeptList= deptDAO.getParamDeptList(chargeInfoDomain);
     * chargeInfoDomain.setDeptParamList(paramDeptList);
     * List<ArrearageDomain> arrearageDetailDomainList = arrearageService.arrearageDetailQuery(chargeInfoDomain);
     * <p>
     * List<TableDataBean> tableDataList = new ArrayList<>();
     * //系统用户
     * UserDomain userDomain=new UserDomain();
     * List<Map<Long,Object>> listmap=userDAO.findMapByDomain(userDomain);
     * Map<Long,String> userMap= FormatterUtil.ListMapToMap(listmap);
     * <p>
     * //下拉列表
     * SystemCommonConfigDomain systemCommonConfigDomain=new SystemCommonConfigDomain();
     * systemCommonConfigDomain.setType("F_CHARGE_MODE");
     * List<Map<Integer,Object>> chargeModeListmap=systemCommonConfigDAO.findMapByDomain(systemCommonConfigDomain);
     * Map<Integer,String> chargeModeMap= FormatterUtil.LongListMapToMap(chargeModeListmap);
     * <p>
     * systemCommonConfigDomain.setType("YS_TYPE_CODE");
     * List<Map<Integer,Object>> ysTypeCodeListmap=systemCommonConfigDAO.findMapByDomain(systemCommonConfigDomain);
     * Map<Integer,String> ysTypeCodeMap= FormatterUtil.LongListMapToMap(ysTypeCodeListmap);
     * <p>
     * //部门用户
     * DeptDomain deptDomain=new DeptDomain();
     * List<Map<Long,Object>> deptIdlistmap=deptDAO.findIdMapByDomain(deptDomain);
     * Map<Long,String> deptIdMap= FormatterUtil.ListMapToMap(deptIdlistmap);
     * //部门用户
     * List<Map<Long,Object>> deptDeptIdlistmap=deptDAO.findDeptIdMapByDomain(deptDomain);
     * Map<Long,String> deptDeptIdMap= FormatterUtil.ListMapToMap(deptDeptIdlistmap);
     * <p>
     * //计量点
     * List<Long> meterIds = arrearageDetailDomainList.stream().filter(a -> a.getMeterId() != null).map(ArrearageDomain::getMeterId)
     * .distinct().collect(Collectors.toList());
     * <p>
     * <p>
     * Map<Integer,Map<Long, List<PriceExecutionDomain>>> monsPriceMap=new HashMap<>();
     * Map<Integer,Map<Long, List<PriceLadderRelaDomain>>> monsPriceLadderMap=new HashMap<>();
     * Map<Integer,List<MeterDomain>> monsMeterMap=new HashMap<>();
     * <p>
     * List<Integer> mons=arrearageDetailDomainList.stream().map(e->e.getMon()).collect(Collectors.toList());
     * mons = new ArrayList<Integer>(new TreeSet<Integer>(mons));
     * for (Integer mon : mons) {
     * //价格
     * Map<Long, List<PriceExecutionDomain>> priceMap=billingService.findMongoPriceExecution(mon);
     * monsPriceMap.put(mon,priceMap);
     * Map<Long, List<PriceLadderRelaDomain>> priceLadderMap=billingService.findMongoPriceLadderRela(mon);
     * monsPriceLadderMap.put(mon,priceLadderMap);
     * <p>
     * //计量点
     * MeterDomain meter = new MeterDomain();
     * meter.setIds(meterIds);
     * meter.setMon(mon);
     * meter.setPageSize(-1);
     * List<MeterDomain> meterMap =meterService.getMeter(meter);
     * monsMeterMap.put(mon,meterMap);
     * }
     * <p>
     * <p>
     * <p>
     * NumToString n=new NumToString();
     * List<FeeRecStatisticsBean> resultList=new ArrayList<FeeRecStatisticsBean>();
     * for (ArrearageDomain arrearageDomainBean : arrearageDetailDomainList) {
     * FeeRecStatisticsBean bean = new FeeRecStatisticsBean();
     * bean.setPriceName(arrearageDomainBean.getPriceName());
     * bean.setStartNum(arrearageDomainBean.getStartNum());
     * bean.setEndNum(arrearageDomainBean.getEndNum());
     * bean.setFactorNum(arrearageDomainBean.getFactorNum());
     * bean.setAddPower(BigDecimal.ZERO);
     * if(arrearageDomainBean.getAddPower()!=null){
     * bean.setAddPower(arrearageDomainBean.getAddPower());
     * }
     * if(arrearageDomainBean.getActiveTransformerLossPower()!=null){
     * bean.setAddPower(bean.getAddPower().add(arrearageDomainBean.getActiveTransformerLossPower()));
     * }
     * List<PriceExecutionDomain> priceExecutionDomainList=monsPriceMap.get(arrearageDomainBean.getMon()).get(arrearageDomainBean.getPriceTypeId());
     * Map<Long, List<PriceExecutionDomain>> priceMap11=monsPriceMap.get(arrearageDomainBean.getMon());
     * List<PriceExecutionDomain>bb=priceMap11.get(arrearageDomainBean.getPriceTypeId());
     * PriceExecutionDomain priceExecutionDomain=bb.stream().filter(a->a.getPriceItemId()==1).findFirst().get();
     * <p>
     * Map<Long, List<PriceLadderRelaDomain>> priceLadderMap11=monsPriceLadderMap.get(arrearageDomainBean.getMon());
     * List<PriceLadderRelaDomain>priceLadderList=priceLadderMap11.get(arrearageDomainBean.getPriceTypeId());
     * bean.setPrice(priceExecutionDomain.getPrice());
     * bean.setTotalPower(arrearageDomainBean.getTotalPower());
     * bean.setReceivable(arrearageDomainBean.getReceivable());
     * resultList.add(bean);
     * if(bean.getLadder1Money()!=null){
     * FeeRecStatisticsBean ladder1bean = new FeeRecStatisticsBean();
     * ladder1bean.setPriceName("第一阶梯");
     * ladder1bean.setPrice(priceLadderList.stream().filter(a ->a.getPriceExecutionId()==priceExecutionDomain.getId()&&a.getLadderSn()==1&&a.getStatus()==1).findFirst().get().getPrice());
     * ladder1bean.setReceivable(bean.getLadder1Money());
     * ladder1bean.setTotalPower(bean.getLadder1Power());
     * resultList.add(ladder1bean);
     * }
     * if(bean.getLadder2Money()!=null){
     * FeeRecStatisticsBean ladder2bean = new FeeRecStatisticsBean();
     * ladder2bean.setPriceName("第二阶梯");
     * ladder2bean.setPrice(priceLadderList.stream().filter(a ->a.getPriceExecutionId()==priceExecutionDomain.getId()&&a.getLadderSn()==2&&a.getStatus()==1).findFirst().get().getPrice());
     * ladder2bean.setReceivable(bean.getLadder2Money());
     * ladder2bean.setTotalPower(bean.getLadder2Power());
     * resultList.add(ladder2bean);
     * }
     * if(bean.getLadder3Money()!=null){
     * FeeRecStatisticsBean ladder3bean = new FeeRecStatisticsBean();
     * ladder3bean.setPriceName("第三梯");
     * ladder3bean.setPrice(priceLadderList.stream().filter(a ->a.getPriceExecutionId()==priceExecutionDomain.getId()&&a.getLadderSn()==3&&a.getStatus()==1).findFirst().get().getPrice());
     * ladder3bean.setReceivable(bean.getLadder3Money());
     * ladder3bean.setTotalPower(bean.getLadder3Power());
     * resultList.add(ladder3bean);
     * }
     * if(bean.getLadder4Money()!=null){
     * FeeRecStatisticsBean ladder4bean = new FeeRecStatisticsBean();
     * ladder4bean.setPriceName("第四阶梯");
     * ladder4bean.setPrice(priceLadderList.stream().filter(a ->a.getPriceExecutionId()==priceExecutionDomain.getId()&&a.getLadderSn()==4&&a.getStatus()==1).findFirst().get().getPrice());
     * ladder4bean.setReceivable(bean.getLadder4Money());
     * ladder4bean.setTotalPower(bean.getLadder4Power());
     * resultList.add(ladder4bean);
     * }
     * }
     * <p>
     * tableData.setTableData(new JRBeanCollectionDataSource(arrearageDetailDomainList));
     * SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
     * tableData.setPrintDate(sdf.format(new Date()));
     * tableData.setSettlementName(arrearageDetailDomainList.get(0).getSettlementName());
     * tableData.setSettlementNo(arrearageDetailDomainList.get(0).getSettlementNo());
     * tableData.setMon(arrearageDetailDomainList.get(0).getMon());
     * tableData.setSetAddress(monsMeterMap.get(arrearageDetailDomainList.get(0).getMon()).get(0).getSetAddress());
     * tableData.setFactPunish(arrearageDetailDomainList.stream().filter(a ->a.getFactPunish()!=null).map(ArrearageDomain::getFactPunish).reduce(BigDecimal.ZERO,BigDecimal::add));
     * tableData.setReceivableSum(arrearageDetailDomainList.stream().filter(a ->a.getReceivable()!=null).map(ArrearageDomain::getReceivable).reduce(BigDecimal.ZERO,BigDecimal::add));
     * tableData.setMoneyInWord(n.cvt(tableData.getReceivableSum().toString(),true));
     * Optional<ArrearageDomain> largest=arrearageDetailDomainList.stream().max( Comparator.comparing(ArrearageDomain::getId));
     * //如果存在
     * if(largest.isPresent()) {
     * tableData.setLastBalance(largest.get().getLastBalance());
     * tableData.setThisBalance(largest.get().getThisBalance());
     * }
     * tableData.setFactTotal(arrearageDetailDomainList.stream().filter(a ->a.getFactTotal()!=null).map(ArrearageDomain::getFactTotal).reduce(BigDecimal.ZERO,BigDecimal::add));
     * tableData.setOperatorName(userMap.get(arrearageDetailDomainList.get(0).getOperator()));
     * tableData.setUserName(userMap.get(chargeInfoDomain.getOperator()));
     * <p>
     * <p>
     * tableData.setDept(deptDeptIdMap.get(chargeInfoDomain.getBusinessPlaceCode()));
     * tableData.setStartDate(chargeInfoDomain.getStartDate().toString());
     * tableData.setEndDate(chargeInfoDomain.getEndDate().toString());
     * tableDataList.add(tableData);
     */

    public List<TableDataBean> monBalance(ChargeInfoDomain chargeInfoDomain) {
        TableDataBean tableData = new TableDataBean();

        //queryCriteria 1：本月预收 2：本月冲减 3：时点预收余额

        //获取相关的结算户
        List<Long> paramSettList = new ArrayList<>();
        List<SettlementDomain> settlementDomains = new ArrayList<>();
        List<ChargeInfoDomain> chargeInfoDomains = new ArrayList<>();

        if ("writeSect".equals(chargeInfoDomain.getGroupBy())) {
            tableData.setvName("抄表段");
            tableData.setvValue(chargeInfoDomain.getWriteSectionName());
            List<Long> writeSectionIds = new ArrayList<>();
            writeSectionIds.add(chargeInfoDomain.getWriteSectId());
            paramSettList =
                    cimService.getSettlementIdsByWriteSectionId(writeSectionIds);

            settlementDomains = cimService.findSettlementByIds(paramSettList);

        } else if ("dept".equals(chargeInfoDomain.getGroupBy())) {
            tableData.setvName("营业区域");
            tableData.setvValue(chargeInfoDomain.getBusinessPlaceName());
            SettlementDomain settlementDomain = new SettlementDomain();
            settlementDomain.setPageSize(-1);
            settlementDomain.setBusinessPlaceCode(chargeInfoDomain.getBusinessPlaceCode());
            settlementDomains = cimService.getSettlement(settlementDomain);
            paramSettList =
                    settlementDomains.stream().map(SettlementDomain::getId).collect(Collectors.toList());
        }

        //结算户map
        Map<Long, SettlementDomain> settlementDomainMap =
                settlementDomains.stream().collect(Collectors.toMap(SettlementDomain::getId, a -> a, (k1, k2) -> k1));

        ChargeInfoDomain paramChargeInfoDomain = new ChargeInfoDomain();
        paramChargeInfoDomain.setMon(chargeInfoDomain.getMon());
        paramChargeInfoDomain.setSettlementIds(paramSettList);
        if ("3".equals(chargeInfoDomain.getQueryCriteria())) {
            ChargeInfoEntity paramChargeInfoEntity = new ChargeInfoEntity();
            paramChargeInfoEntity.setEndPayDate(chargeInfoDomain.getEndDate());
            paramChargeInfoEntity.setSettlementIds(paramSettList);
            chargeInfoDomains =
                    billingService.findMaxIdBySettlementIds(paramChargeInfoEntity);
        } else {
            if ("2".equals(chargeInfoDomain.getQueryCriteria())) {
                paramChargeInfoDomain.setfChargeMode((short) 4);
            }
            chargeInfoDomains =
                    billingService.chargeBySettleIds(paramChargeInfoDomain);
        }
        //分组求和 预收余额
        Map<Long, BigDecimal> chargeInfoDomainMap = new HashMap<>();
        if ("1".equals(chargeInfoDomain.getQueryCriteria())) {
            chargeInfoDomainMap =
                    chargeInfoDomains.stream().filter(t -> t.getFactPre() != null && t.getFactPre().compareTo(BigDecimal.ZERO) > 0)
                            .collect(Collectors.groupingBy(ChargeInfoDomain::getSettlementId, CustomCollectors.summingBigDecimal(ChargeInfoDomain::getFactPre)));
        } else if ("2".equals(chargeInfoDomain.getQueryCriteria())) {
            chargeInfoDomainMap =
                    chargeInfoDomains.stream().filter(t -> t.getFactMoney() != null && t.getFactTotal().compareTo(BigDecimal.ZERO) > 0)
                            .collect(Collectors.groupingBy(ChargeInfoDomain::getSettlementId, CustomCollectors.summingBigDecimal(ChargeInfoDomain::getFactMoney)));
        } else {
            List<Long> chargeInfoIds =
                    chargeInfoDomains.stream().map(ChargeInfoDomain::getId).distinct().collect(Collectors.toList());
            NoteInfoDomain paramNoteInfoDomain = new NoteInfoDomain();
            paramNoteInfoDomain.setChargeInfoIds(chargeInfoIds);
            List<NoteInfoDomain> middleNoteInfoDomains =
                    billingService.findNoteInfoByWhere(paramNoteInfoDomain);
            chargeInfoDomainMap =
                    middleNoteInfoDomains.stream().filter(t -> t.getThisBalance() != null && t.getThisBalance().compareTo(BigDecimal.ZERO) > 0)
                            .collect(Collectors.toMap(NoteInfoDomain::getSettlementId, a -> a.getThisBalance(), (k1, k2) -> k1));
        }
        List<NoteInfoDomain> noteInfoDomains = new ArrayList<>();
        for (Long k : chargeInfoDomainMap.keySet()) {
            SettlementDomain settlementDomain = settlementDomainMap.get(k);
            NoteInfoDomain noteInfoDomain = new NoteInfoDomain();
            noteInfoDomain.setSettlementNo(settlementDomain.getSettlementNo());
            noteInfoDomain.setSettlementName(settlementDomain.getSettlementName());
            noteInfoDomain.setBankNo(settlementDomain.getBankNo());
            noteInfoDomain.setLastBalance(chargeInfoDomainMap.get(k));
            if ("2".equals(chargeInfoDomain.getQueryCriteria())) {
                noteInfoDomain.setLastBalance(chargeInfoDomainMap.get(k).negate());
            }
            noteInfoDomains.add(noteInfoDomain);
        }

        List<TableDataBean> tableDataList = new ArrayList<>();

        //排序 根据结算户号排序
        noteInfoDomains =
                noteInfoDomains.stream().sorted(Comparator.comparing(NoteInfoDomain::getSettlementNo)).collect(Collectors.toList());

        tableData.setTableData(new JRBeanCollectionDataSource(noteInfoDomains));
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        tableData.setPrintDate(sdf.format(new Date()));
        tableData.setMon(chargeInfoDomain.getMon());
        tableDataList.add(tableData);
        return tableDataList;
    }


    //合打结算单
    public List<TableDataBean> jointStatement(List<NoteInfoDomain> noteInfoDomainList, String operatorName) {

        Map<String, List<NoteInfoDomain>> noteInfoMapByMonAndSettle =
                noteInfoDomainList.stream().collect(Collectors.groupingBy(o -> o.getSettlementId() + "_" + o.getYsTypeCode()));


        List<TableDataBean> tableDataList = new ArrayList<>();

        //收费信息
        List<Long> chargeInfoIds =
                noteInfoDomainList.stream().map(NoteInfoDomain::getChargeInfoId).distinct().collect(Collectors.toList());

        List<ChargeInfoDomain> chargeInfoDomains =
                billingService.findChargeByIds(chargeInfoIds);

        List<Long> operatorIds=
                chargeInfoDomains.stream().map(ChargeInfoDomain::getOperator).distinct().collect(Collectors.toList());

        Map<Long, String> sysUserMap = userDAO.findByIds(operatorIds)
                .stream().collect(Collectors.toMap(UserDomain::getId, k -> k.getUserName()));

        Map<Long, ChargeInfoDomain> chargeInfoDomainMap =
                chargeInfoDomains.stream().collect(Collectors.toMap(ChargeInfoDomain::getId, a -> a, (k1, k2) -> k1));

        List<PriceTypeDomain> priceTypeDomains = billingService.findPriceType();

        Map<Long, PriceTypeDomain> priceTypeDomainMap =
                priceTypeDomains.stream().filter(m -> m.getId() != null).collect(Collectors.toMap(PriceTypeDomain::getId, a -> a, (k1, k2) -> k1));

        Map<Integer, Map<Long, List<PriceExecutionDomain>>> monsPriceMap = new HashMap<>();

        Map<Integer, List<PriceLadderRelaDomain>> priceLadderMap = new HashMap<>();

        //合打票据 历史欠费月份
        List<Integer> jointMons =
                noteInfoDomainList.stream().sorted(Comparator.comparing(NoteInfoDomain::getMon)).map(e -> e.getMon()).distinct().collect(Collectors.toList());

        List<Integer> mons =
                noteInfoDomainList.stream().map(e -> e.getMon()).distinct().collect(Collectors.toList());

        if (!mons.contains(Integer.valueOf(Global.getConfig("mon")))) {
            mons.add(Integer.valueOf(Global.getConfig("mon")));
        }
        Integer maxMon = mons.stream().mapToInt(Integer::valueOf).max().getAsInt();

        Map<Long, List<PriceExecutionDomain>> priceMap = billingService.findMongoPriceExecution(maxMon);
        monsPriceMap.put(maxMon, priceMap);
        List<PriceLadderRelaDomain> monpriceLadderRelaDomains = billingService.findMongoPriceLadderRela(maxMon);
        priceLadderMap.put(maxMon, monpriceLadderRelaDomains);

        //获取结算户信息
        List<Long> settlementIds =
                noteInfoDomainList.stream().map(NoteInfoDomain::getSettlementId).distinct().collect(Collectors.toList());

        List<SettlementDomain> settlementDomains =
                cimService.findSettlementByIds(settlementIds);

        Map<Long, SettlementDomain> settlementDomainMap =
                settlementDomains.stream().collect(Collectors.toMap(SettlementDomain::getId, a -> a, (k1, k2) -> k1));

        for (String k : noteInfoMapByMonAndSettle.keySet()) {

            List<NoteInfoDomain> v = noteInfoMapByMonAndSettle.get(k);

            v = v.stream().sorted(Comparator.comparing(NoteInfoDomain::getMon).reversed()).collect(Collectors.toList());

            TableDataBean tableDataBean = new TableDataBean();

            Integer mon = maxMon;

            List<PriceLadderRelaDomain> priceLadderRelaDomains = priceLadderMap.get(mon);

            List<Long> priceLadderPriceTypeIds =
                    priceLadderRelaDomains.stream().map(PriceLadderRelaDomain::getPriceExecutionId).collect(Collectors.toList());

            //按电价及sn分组
            Map<String, PriceLadderRelaDomain> priceLadderRelaMapByPriceAndSn =
                    priceLadderRelaDomains.stream().collect(Collectors.toMap(o -> o.getPriceExecutionId() + "_" + o.getLadderSn(), a -> a, (k1, k2) -> k1));

            //按电价分组
            Map<Long, List<PriceLadderRelaDomain>> priceLadderRelaMapByPrice =
                    priceLadderRelaDomains.stream().collect(Collectors.groupingBy(PriceLadderRelaDomain::getPriceExecutionId));

            List<NoteInfoDomain> noteInfoDomains = v;
            tableDataBean.setMon(v.get(0).getMon());
            tableDataBean.setSettlementNo(Long.valueOf(v.get(0).getSettlementNo()));
            tableDataBean.setSettlementName(v.get(0).getSettlementName());
            BigDecimal punishMoney =
                    noteInfoDomains.stream().filter(t -> t.getPunishMoney() != null)
                            .map(NoteInfoDomain::getPunishMoney).reduce(BigDecimal.ZERO, BigDecimal::add);
            tableDataBean.setFactPunish(punishMoney);
            BigDecimal factMoney =
                    noteInfoDomains.stream().filter(t -> t.getFactMoney() != null).filter(t->t.getfChargeMode()!=4).filter(t->t.getfChargeMode()!=5)
                            .map(NoteInfoDomain::getFactMoney).reduce(BigDecimal.ZERO, BigDecimal::add);

            BigDecimal factPre =
                    noteInfoDomains.stream().filter(t -> t.getFactPre() != null)
                            .map(NoteInfoDomain::getFactPre).reduce(BigDecimal.ZERO, BigDecimal::add);

            BigDecimal arrears =
                    noteInfoDomains.stream().filter(t -> t.getArrears() != null)
                            .map(NoteInfoDomain::getArrears).reduce(BigDecimal.ZERO,
                            BigDecimal::add);

            tableDataBean.setFactTotal(arrears);
            tableDataBean.setMoneyInWord(NumToString.number2CNMontrayUnit(arrears));
            tableDataBean.setReceivableSum(punishMoney.add(factMoney).add(factPre));

            List<NoteInfoDomain> balanceNoteInfoDomains=
                    noteInfoDomains.stream().sorted(Comparator.comparing(NoteInfoDomain::getId)).collect(Collectors.toList());


            tableDataBean.setThisBalance(balanceNoteInfoDomains.get(balanceNoteInfoDomains.size()-1).getThisBalance());

            //获取计算户号 取本月冲抵余额
            List<Long> monSettlementIds =
                    noteInfoDomains.stream().map(NoteInfoDomain::getSettlementId).distinct().collect(Collectors.toList());

            ChargeInfoDomain chargeInfoDomain = new ChargeInfoDomain();
            chargeInfoDomain.setfChargeMode((short) 4);
            chargeInfoDomain.setMon(Integer.valueOf(v.get(v.size()-1).getMon().toString()));
            chargeInfoDomain.setSettlementIds(monSettlementIds);
            List<ChargeInfoDomain> lastBalanceChargeInfoDomains =
                    billingService.chargeBySettleIds(chargeInfoDomain);

            BigDecimal factTotal = BigDecimal.ZERO;
            if (lastBalanceChargeInfoDomains != null && lastBalanceChargeInfoDomains.size() > 0) {
                factTotal =
                        lastBalanceChargeInfoDomains.stream().filter(t -> t.getFactMoney() != null).
                                map(ChargeInfoDomain::getFactMoney).reduce(BigDecimal.ZERO, BigDecimal::add);
            }

            //退补电费
            ChargeInfoDomain recallChargeInfoDomain = new ChargeInfoDomain();
            recallChargeInfoDomain.setfChargeMode((short) 6);
            recallChargeInfoDomain.setEndMon(Integer.valueOf(v.get(0).getMon().toString()));
            recallChargeInfoDomain.setStartMon(Integer.valueOf(v.get(v.size() - 1).getMon().toString()));
            recallChargeInfoDomain.setSettlementIds(monSettlementIds);
            List<ChargeInfoDomain> recallChargeInfoDomains =
                    billingService.chargeBySettleIds(recallChargeInfoDomain);
            BigDecimal recallMoney = BigDecimal.ZERO;
            if (recallChargeInfoDomains != null && recallChargeInfoDomains.size() > 0) {
                recallMoney =
                        recallChargeInfoDomains.stream().filter(t -> t.getFactTotal() != null).
                                map(ChargeInfoDomain::getFactTotal).reduce(BigDecimal.ZERO, BigDecimal::add);
            }

            tableDataBean.setLastBalance(balanceNoteInfoDomains.get(0).getLastBalance().add(factTotal));

            ChargeInfoDomain returnChargeInfo = chargeInfoDomainMap.get(v.get(0).getChargeInfoId());

            tableDataBean.setOperatorName(operatorName);
            tableDataBean.setUserName(sysUserMap.get(new Long(returnChargeInfo.getOperator())));
            if (tableDataBean.getUserName() == null || "".equals(tableDataBean.getUserName())) {
                tableDataBean.setUserName("管理员");
            }
            tableDataBean.setPrintDate(MonUtils.getDay());

            tableDataBean.setSetAddress(settlementDomainMap.get(v.get(0).getSettlementId()).getAddress());

            //赋值历史欠费
            if (jointMons.size() > 1) {
                String historyOweInfo = "欠费月份 :" + jointMons.get(0).toString() +
                        "月 到" + jointMons.get(jointMons.size() - 2).toString() + "月份  电费:  ";

                BigDecimal historyAarrears =
                        noteInfoDomains.stream().filter(t -> t.getArrears() != null).filter(t -> t.getMon().intValue() != jointMons.get(jointMons.size() - 1).intValue())
                                .map(NoteInfoDomain::getArrears).reduce(BigDecimal.ZERO,
                                BigDecimal::add);

                tableDataBean.setHistoryOweInfo(historyOweInfo + historyAarrears.toString());
            } else {
                tableDataBean.setHistoryOweInfo("");
            }


            //过滤预收
            List<Long> meterIds =
                    v.stream().filter(t->t.getMeterId()!=null).filter(t -> t.getYsTypeCode() != 2).map(NoteInfoDomain::getMeterId).distinct().collect(Collectors.toList());

            if(meterIds==null || meterIds.size()<1){
                tableDataBean.setLastBalance(v.get(0).getLastBalance());
            }
            //0电费不产生欠费的计量点
            ArrearageDomain arrearageDomain = new ArrearageDomain();
            arrearageDomain.setMon(mon);
            arrearageDomain.setSettlementId(v.get(0).getSettlementId());
            List<ArrearageDomain> arrearageDomains =
                    billingService.findArrearageBySettleIdMonAndSn(arrearageDomain);

            List<Long> zeroMoneyMeterIds = arrearageDomains.stream()
                    .filter(t->t.getMeterId()!=null).filter(t -> t.getReceivable().compareTo(BigDecimal.ZERO) == 0 && t.getTotalPower().compareTo(BigDecimal.ZERO) != 0)
                    .distinct().map(ArrearageDomain::getMeterId).collect(Collectors.toList());

            if (zeroMoneyMeterIds != null && zeroMoneyMeterIds.size() > 0) {
                meterIds.addAll(zeroMoneyMeterIds);
            }
            List<FeeRecStatisticsBean> feeRecStatisticsBeans = new ArrayList<FeeRecStatisticsBean>();

            if (meterIds != null && meterIds.size() > 0) {

                MeterMoneyDomain meterMoneyDomain = new MeterMoneyDomain();
                meterMoneyDomain.setMon(v.get(0).getMon());
                meterMoneyDomain.setMeterIds(meterIds);
                List<MeterMoneyDomain> meterMoneyDomains =
                        billingService.findMeterMoneyByMonAndMeterIds(meterMoneyDomain);

                if (meterMoneyDomains == null || meterMoneyDomains.size() < 1) {
                    return new ArrayList<>();
                }

                tableDataBean.setPowerRateMoney(meterMoneyDomains.stream().filter(a -> a.getPowerRateMoney() != null).map(MeterMoneyDomain::getPowerRateMoney).reduce(BigDecimal.ZERO, BigDecimal::add));
                tableDataBean.setBasicMoneySum(meterMoneyDomains.stream().filter(a -> a.getBasicMoney() != null).map(MeterMoneyDomain::getBasicMoney).reduce(BigDecimal.ZERO, BigDecimal::add));
                tableDataBean.setRefundMoney(meterMoneyDomains.stream().filter(a -> a.getRefundMoney() != null).map(MeterMoneyDomain::getRefundMoney).reduce(BigDecimal.ZERO, BigDecimal::add).negate());
                tableDataBean.setRefundMoney(recallMoney.add(tableDataBean.getRefundMoney()).negate());
                WriteFilesDomain conditionWriteFile = new WriteFilesDomain();
                conditionWriteFile.setMon(v.get(0).getMon());
                conditionWriteFile.setMeterIds(meterIds);
                List<WriteFilesDomain> writeFilesDomains =
                        billingService.findWriteFilesByMonAndMeterIds(conditionWriteFile);

                writeFilesDomains =
                        writeFilesDomains.stream().filter(t -> t.getTimeSeg() == 0).collect(Collectors.toList());

                Map<Long, List<WriteFilesDomain>> writeFilesDomainMap =
                        writeFilesDomains.stream()
                                .collect(Collectors.groupingBy(WriteFilesDomain::getMeterId));

                for (MeterMoneyDomain t : meterMoneyDomains) {

                    List<PriceExecutionDomain> priceExecutionDomains =
                            monsPriceMap.get(mon).get(t.getPriceTypeId());

                    BigDecimal price =
                            priceExecutionDomains.stream().filter(m -> m.getTimeSeg() == 0).map(PriceExecutionDomain::getPrice).reduce(BigDecimal.ZERO, BigDecimal::add);

                    //抄表记录
                    List<WriteFilesDomain> writeFilesByMeterId =
                            writeFilesDomainMap.get(t.getMeterId());

                    //无抄表记录的为虚拟表
                    if (writeFilesByMeterId == null || writeFilesByMeterId.size() < 1) {
                        FeeRecStatisticsBean feeRecStatisticsBean = new FeeRecStatisticsBean();
                        feeRecStatisticsBean.setStartNum(BigDecimal.ZERO);
                        feeRecStatisticsBean.setEndNum(BigDecimal.ZERO);
                        feeRecStatisticsBean.setFactorNum(BigDecimal.ZERO);
                        feeRecStatisticsBean.setAddPower(BigDecimal.ZERO);
                        feeRecStatisticsBean.setTotalPower(t.getTotalPower());
                        feeRecStatisticsBean.setReceivable(t.getVolumeCharge().add(t.getSurcharges()));
                        feeRecStatisticsBean.setPrice(price);
                        feeRecStatisticsBean.setName(priceTypeDomainMap.get(t.getPriceTypeId()).getPriceName());
                        feeRecStatisticsBeans.add(feeRecStatisticsBean);
                        continue;
                    }

                    for (WriteFilesDomain writeFilesDomain : writeFilesByMeterId) {

                        FeeRecStatisticsBean feeRecStatisticsBean = new FeeRecStatisticsBean();
                        feeRecStatisticsBean.setStartNum(writeFilesDomain.getStartNum());
                        feeRecStatisticsBean.setEndNum(writeFilesDomain.getEndNum());
                        feeRecStatisticsBean.setFactorNum(writeFilesDomain.getFactorNum());

                        feeRecStatisticsBean.setTotalPower(t.getTotalPower());
                        feeRecStatisticsBean.setReceivable(t.getVolumeCharge().add(t.getSurcharges()));
                        feeRecStatisticsBean.setPrice(price);
                        feeRecStatisticsBean.setName(priceTypeDomainMap.get(t.getPriceTypeId()).getPriceName());


                        BigDecimal addAndTransPower = BigDecimal.ZERO;

                        if (writeFilesDomain.getFunctionCode() == 2) {
                            feeRecStatisticsBean.setReceivable(BigDecimal.ZERO);
                            if (writeFilesDomain.getAddPower() != null) {
                                addAndTransPower =
                                        addAndTransPower.add(writeFilesDomain.getAddPower());
                            }
                            if (writeFilesDomain.getChgPower() != null) {
                                addAndTransPower =
                                        addAndTransPower.add(t.getChgPower());
                            }
                            feeRecStatisticsBean.setAddPower(addAndTransPower);
                            feeRecStatisticsBean.setReceivable(BigDecimal.ZERO);
                            feeRecStatisticsBeans.add(feeRecStatisticsBean);
                            continue;
                        }

                        if (t.getActiveLineLossPower() != null) {
                            addAndTransPower =
                                    addAndTransPower.add(t.getActiveLineLossPower());
                        }
                        if (t.getAddPower() != null) {
                            addAndTransPower =
                                    addAndTransPower.add(t.getAddPower());
                        }
                        if (t.getActiveTransformerLossPower() != null) {
                            addAndTransPower =
                                    addAndTransPower.add(t.getActiveTransformerLossPower());
                        }
                        if (t.getChgPower() != null) {
                            addAndTransPower =
                                    addAndTransPower.add(t.getChgPower());
                        }
                        if (t.getActiveDeductionPower() != null) {
                            addAndTransPower =
                                    addAndTransPower.add(t.getActiveDeductionPower());
                        }
                        feeRecStatisticsBean.setAddPower(addAndTransPower);
                        feeRecStatisticsBeans.add(feeRecStatisticsBean);

                        BigDecimal surPrice =
                                priceExecutionDomains.stream().filter(m -> m.getTimeSeg() == 0).filter(m -> m.getPriceItemId() != 1).map(PriceExecutionDomain::getPrice).reduce(BigDecimal.ZERO, BigDecimal::add);

                        //赋值阶梯信息
                        BigDecimal ladderSurcharges1 = BigDecimal.ZERO;
                        BigDecimal ladderSurcharges2 = BigDecimal.ZERO;
                        BigDecimal ladderSurcharges3 = BigDecimal.ZERO;
                        BigDecimal ladderSurcharges4 = BigDecimal.ZERO;

                        if (t.getLadder1Money() != null && t.getLadder1Money().compareTo(BigDecimal.ZERO) > 0) {
                            FeeRecStatisticsBean ladderbean = new FeeRecStatisticsBean();
                            ladderbean.setName("第一阶梯");
                            ladderbean.setPrice(priceLadderRelaMapByPriceAndSn.get(t.getPriceTypeId() + "_" + "1").getPrice().add(surPrice));
                            ladderbean.setTotalPower(t.getLadder1Power());
                            // 赋值附加费 考虑差值 有第二阶梯差值放在第二阶梯
                            if (t.getLadder2Money() != null && t.getLadder2Money().compareTo(BigDecimal.ZERO) > 0) {
                                ladderbean.setSurcharges(t.getLadder1Power().multiply(surPrice));
                                ladderSurcharges1 = ladderbean.getSurcharges();
                            } else {
                                ladderbean.setSurcharges(t.getSurcharges());
                            }
                            ladderbean.setReceivable(t.getLadder1Money().add(ladderbean.getSurcharges()));
                            feeRecStatisticsBeans.add(ladderbean);
                        }
                        if (t.getLadder2Money() != null && t.getLadder2Money().compareTo(BigDecimal.ZERO) > 0) {
                            FeeRecStatisticsBean ladderbean = new FeeRecStatisticsBean();
                            ladderbean.setName("第二阶梯");
                            ladderbean.setPrice(priceLadderRelaMapByPriceAndSn.get(t.getPriceTypeId() + "_" + "2").getPrice().add(surPrice));
                            ladderbean.setTotalPower(t.getLadder2Power());
                            // 赋值附加费 考虑差值 有第三阶梯差值放在第三阶梯
                            if (t.getLadder3Money() != null && t.getLadder3Money().compareTo(BigDecimal.ZERO) > 0) {
                                ladderbean.setSurcharges(t.getLadder2Power().multiply(surPrice));
                                ladderSurcharges2 =
                                        ladderSurcharges1.add(ladderbean.getSurcharges());
                            } else {
                                ladderbean.setSurcharges(t.getSurcharges().subtract(ladderSurcharges1));
                            }
                            ladderbean.setReceivable(t.getLadder2Money().add(ladderbean.getSurcharges()));
                            feeRecStatisticsBeans.add(ladderbean);
                        }
                        if (t.getLadder3Money() != null && t.getLadder3Money().compareTo(BigDecimal.ZERO) > 0) {
                            FeeRecStatisticsBean ladderbean = new FeeRecStatisticsBean();
                            ladderbean.setName("第三阶梯");
                            ladderbean.setPrice(priceLadderRelaMapByPriceAndSn.get(t.getPriceTypeId() + "_" + "3").getPrice().add(surPrice));
                            ladderbean.setTotalPower(t.getLadder3Power());
                            // 赋值附加费 考虑差值 有第四阶梯差值放在第四阶梯
                            if (t.getLadder4Money() != null && t.getLadder4Money().compareTo(BigDecimal.ZERO) > 0) {
                                ladderbean.setSurcharges(t.getLadder3Power().multiply(surPrice));
                                ladderSurcharges3 =
                                        ladderSurcharges2.add(ladderbean.getSurcharges());
                            } else {
                                ladderbean.setSurcharges(t.getSurcharges().subtract(ladderSurcharges2));
                            }
                            ladderbean.setReceivable(t.getLadder3Money().add(ladderbean.getSurcharges()));
                            feeRecStatisticsBeans.add(ladderbean);
                        }
                        if (t.getLadder4Money() != null && t.getLadder4Money().compareTo(BigDecimal.ZERO) > 0) {
                            FeeRecStatisticsBean ladderbean = new FeeRecStatisticsBean();
                            ladderbean.setName("第四阶梯");
                            ladderbean.setPrice(priceLadderRelaMapByPriceAndSn.get(t.getPriceTypeId() + "_" + "4").getPrice().add(surPrice));
                            ladderbean.setTotalPower(t.getLadder4Power());
                            // 赋值附加费 考虑差值
                            ladderbean.setSurcharges(t.getSurcharges().subtract(ladderSurcharges3));
                            ladderbean.setReceivable(t.getLadder4Money().add(ladderbean.getSurcharges()));
                            feeRecStatisticsBeans.add(ladderbean);
                        }

                    }
                }

                //有阶梯的计量点记录
                List<MeterMoneyDomain> ladderMeterMoneys =
                        meterMoneyDomains.stream().filter(t -> priceLadderPriceTypeIds.contains(t.getPriceTypeId()))
                                .collect(Collectors.toList());

                if (ladderMeterMoneys != null && ladderMeterMoneys.size() > 0) {

                    BigDecimal ladderSum =
                            ladderMeterMoneys.stream().map(MeterMoneyDomain::getLadderTotalPower)
                                    .reduce(BigDecimal.ZERO, BigDecimal::add).setScale(0, RoundingMode.HALF_UP);

                    BigDecimal ladderLimit1 = BigDecimal.ZERO;
                    BigDecimal ladderLimit2 = BigDecimal.ZERO;
                    BigDecimal ladderLimit3 = BigDecimal.ZERO;

                    for (int i = 0; i < ladderMeterMoneys.size(); i++) {
                        MeterMoneyDomain ladderMoneyDomain = ladderMeterMoneys.get(i);
                        List<PriceLadderRelaDomain> meterMoneyPriceLadders =
                                priceLadderRelaMapByPrice.get(ladderMoneyDomain.getPriceTypeId());

                        Map<Integer, PriceLadderRelaDomain> meterMoneyLadderMapBySn =
                                meterMoneyPriceLadders.stream()
                                        .collect(Collectors.toMap(PriceLadderRelaDomain::getLadderSn, a -> a, (k1, k2) -> k1));
                        ladderLimit1 =
                                ladderLimit1.add(new BigDecimal(meterMoneyLadderMapBySn.get(1).getLadderValue()));
                        ladderLimit2 =
                                ladderLimit2.add(new BigDecimal(meterMoneyLadderMapBySn.get(2).getLadderValue()));
                        ladderLimit3 =
                                ladderLimit3.add(new BigDecimal(meterMoneyLadderMapBySn.get(3).getLadderValue()));
                    }
                    //ladderLimit2是二阶梯总量包含一阶梯
                    tableDataBean.setLadder1Limit(ladderLimit1);
                    tableDataBean.setLadder2Limit(ladderLimit2.subtract(ladderLimit1));
                    tableDataBean.setLadder3Limit(ladderLimit3.subtract(ladderLimit2));
                    tableDataBean.setLadder4Limit("-");
                    //一阶梯电量
                    if (ladderSum.compareTo(ladderLimit1) == 1) {
                        tableDataBean.setLadder1Power(ladderLimit1);
                    } else {
                        tableDataBean.setLadder1Power(ladderSum);
                    }
                    //大约二阶梯
                    if (ladderSum.compareTo(ladderLimit2) == 1) {
                        tableDataBean.setLadder2Power(ladderLimit2.subtract(ladderLimit1));
                    } else if (ladderSum.compareTo(ladderLimit1) == 1) {
                        //大于一阶梯
                        tableDataBean.setLadder2Power(ladderSum.subtract(ladderLimit1));
                    }
                    if (ladderSum.compareTo(ladderLimit3) == 1) {
                        tableDataBean.setLadder3Power(ladderLimit3.subtract(ladderLimit2));
                        tableDataBean.setLadder4Power(ladderSum.subtract(ladderLimit3));
                    } else if (ladderSum.compareTo(ladderLimit1.add(ladderLimit2)) == 1) {
                        tableDataBean.setLadder3Power(ladderSum.subtract(ladderLimit2));
                    }

                }

            }
            tableDataBean.setTableData(new JRBeanCollectionDataSource(feeRecStatisticsBeans));
            tableDataList.add(tableDataBean);
        }
        ;

        List<TableDataBean> returnTableDataList = tableDataList;

        returnTableDataList =
                returnTableDataList.stream().sorted(Comparator.comparing(TableDataBean::getMon)).collect(Collectors.toList());

        return returnTableDataList;
    }


    public List<TableDataBean> monBalanceByDay(ChargeInfoDomain chargeInfoDomain) {
        //获取营业区域
        List<DeptDomain> deptList =
                deptService.getDeptListByUserId(chargeInfoDomain.getOperator());
        deptList =
                deptList.stream().sorted(Comparator.comparing(DeptDomain::getDepeType)).collect(Collectors.toList());
        //查询参数
        String groupBy = chargeInfoDomain.getGroupBy();
        TableDataBean tableData = new TableDataBean();
        //查询参数
        Calendar   calendar = new GregorianCalendar();
        calendar.setTime(chargeInfoDomain.getEndDate());
        calendar.add(calendar.DATE,1); //把日期往后增加一天,整数  往后推,负数往前移动
        //传入实体转换
        ChargeInfoEntity chargeInfoEntity = new ChargeInfoEntity();
        chargeInfoEntity.setOperator(chargeInfoDomain.getOperator().toString());
        chargeInfoEntity.setStartPayDate(chargeInfoDomain.getStartDate());
        chargeInfoEntity.setEndPayDate(calendar.getTime());
        chargeInfoEntity.setGroupBy(groupBy);

        List<ChargeInfoDetailEntity> chargeInfoDetailEntities =
                billingService.findChargeInfoDetailsGroupByDay(chargeInfoEntity);

        if (chargeInfoDetailEntities == null || chargeInfoDetailEntities.size() < 1) {
            return new ArrayList<>();
        }
        List<TableDataBean> tableDataList = new ArrayList<>();
        //下拉列表
        Map<Long, String> chargeModeMap =
                cimService.findSystemCommonConfigByType("F_CHARGE_MODE");

        if (("fChargeMode").equals(groupBy)) {
            chargeInfoDetailEntities.forEach(t -> {
                t.setColumnName(chargeModeMap.get(Long.valueOf(t.getColumnName())));
            });
        }



        List<FeeRecStatisticsBean> feeRecStatisticsBeans=new ArrayList<>();
        chargeInfoDetailEntities.forEach(t->{
            FeeRecStatisticsBean feeRecStatisticsBean= new FeeRecStatisticsBean();
            feeRecStatisticsBean.setRow1name(t.getColumnName());
            feeRecStatisticsBean.setFactMoney(t.getFactMoney());
            feeRecStatisticsBean.setFactTotal(t.getFactTotal());
            feeRecStatisticsBean.setFactPunish(t.getFactPunish());
            feeRecStatisticsBean.setFactAdvance(t.getFactAdvance());
            feeRecStatisticsBean.setFactPre(t.getFactPre());
            feeRecStatisticsBean.setSumFlow(t.getSumFlow());
            feeRecStatisticsBean.setSumSettlement(t.getSumSettlement());
            feeRecStatisticsBeans.add(feeRecStatisticsBean);
        });
        tableData.setTableData(new JRBeanCollectionDataSource(feeRecStatisticsBeans));
        //tableData.setDept(deptDeptIdMap.get(chargeInfoDomain
        // .getBusinessPlaceCode()));
        //格式化时间
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        tableData.setUserName(chargeInfoDomain.getOperatorName());
        tableData.setBusinessPlaceCodeName(deptList.get(0).getTitle());
        tableData.setStartDate(chargeInfoDomain.getStartDate() == null ? "" :
                simpleDateFormat.format(new Date(chargeInfoDomain.getStartDate().getTime())));
        tableData.setEndDate(chargeInfoDomain.getEndDate() == null ? "" :
                simpleDateFormat.format(new Date(chargeInfoDomain.getEndDate().getTime())));
        tableDataList.add(tableData);
        return tableDataList;
    }

    //跨区收费
    public List<TableDataBean> findCrossChargeInfoDetails(ChargeInfoDomain chargeInfoDomain) {
        //获取下级营业区域
        DeptDomain dept =
                deptService.getDept(chargeInfoDomain.getBusinessPlaceCode());
        List<DeptDomain> deptList = new ArrayList<>();
        // 最上级
        if (dept.getParentId() != null && dept.getParentId() == Long.valueOf(0)) {
            ParamDomain paramDomain = new ParamDomain();
            // paramDomain.setPageSize(-1);
            deptList = deptService.findByWhere(paramDomain);
        } else {
            deptList = deptService.getDeptList(chargeInfoDomain.getBusinessPlaceCode());
            deptList.add(dept);
        }

        deptList.forEach(t->{
            if(t.getDeptName()==null || "".equals(t.getDeptName())){
                t.setDeptName(t.getTitle());
            }
        });
        Map<Long, String> businessPlaceCodeMap = deptList.stream().filter(m -> m.getId() != null)
                .collect(Collectors.toMap(DeptDomain::getId, a -> a.getDeptName(), (k1, k2) -> k1));

        List<Long> businessPlaceCodes =
                deptList.stream().filter(m -> m.getId() != null).map(DeptDomain::getId).distinct().collect(Collectors.toList());

        //查询参数
        Calendar   calendar = new GregorianCalendar();
        calendar.setTime(chargeInfoDomain.getEndDate());
        calendar.add(calendar.DATE,1); //把日期往后增加一天,整数  往后推,负数往前移动
        String groupBy = chargeInfoDomain.getGroupBy();
        TableDataBean tableData = new TableDataBean();
        //传入实体转换
        ChargeInfoEntity chargeInfoEntity = new ChargeInfoEntity();
        chargeInfoEntity.setMon(chargeInfoDomain.getMon());
        chargeInfoEntity.setStartPayDate(chargeInfoDomain.getStartDate());
        chargeInfoEntity.setEndPayDate(calendar.getTime());
        chargeInfoEntity.setBusinessPlaceCodes(businessPlaceCodes);

        List<ChargeInfoDetailEntity> chargeInfoDetailEntities =
                billingService.findCrossChargeInfoDetails(chargeInfoEntity);

        if (chargeInfoDetailEntities == null || chargeInfoDetailEntities.size() < 1) {
            return new ArrayList<>();
        }
        List<Long> operatorIds=
                chargeInfoDetailEntities.stream().map(ChargeInfoDetailEntity::getOperator).distinct().collect(Collectors.toList());

        Map<Long, String> sysUserMap = userDAO.findByIds(operatorIds)
                .stream().collect(Collectors.toMap(UserDomain::getId, k -> k.getUserName()));

        //统计方式
        String statisticalMethod = chargeInfoDomain.getStatisticalMethod();

        List<FeeRecStatisticsBean> feeRecStatisticsBeans = new ArrayList<>();
        for (ChargeInfoDetailEntity chargeInfoDetailEntity : chargeInfoDetailEntities) {
            FeeRecStatisticsBean feeRecStatisticsBean = new FeeRecStatisticsBean();
            if ("customer".equals(statisticalMethod)) {
                feeRecStatisticsBean.setRow1name(businessPlaceCodeMap.get(chargeInfoDetailEntity.getBusinessPlaceCode()));
                feeRecStatisticsBean.setRow2name(sysUserMap.get(chargeInfoDetailEntity.getOperator()));
            }
            if ("collection".equals(statisticalMethod)) {
                feeRecStatisticsBean.setRow1name(sysUserMap.get(chargeInfoDetailEntity.getOperator()));
                feeRecStatisticsBean.setRow2name(businessPlaceCodeMap.get(chargeInfoDetailEntity.getBusinessPlaceCode()));
            }
            feeRecStatisticsBean.setBusinessPlaceCode(chargeInfoDetailEntity.getBusinessPlaceCode());
            feeRecStatisticsBean.setBusinessPlaceCodeName(businessPlaceCodeMap.get(chargeInfoDetailEntity.getBusinessPlaceCode()));
            feeRecStatisticsBean.setOperatorName(sysUserMap.get(chargeInfoDetailEntity.getOperator()));
            feeRecStatisticsBean.setSumSettlement(chargeInfoDetailEntity.getSumSettlement());
            feeRecStatisticsBean.setSumFlow(chargeInfoDetailEntity.getSumFlow());
            feeRecStatisticsBean.setFactTotal(chargeInfoDetailEntity.getFactTotal());
            feeRecStatisticsBean.setFactMoney(chargeInfoDetailEntity.getFactMoney());
            feeRecStatisticsBean.setVolumeCharge(chargeInfoDetailEntity.getVolumeCharge());
            feeRecStatisticsBean.setOperator(chargeInfoDetailEntity.getOperator());
            feeRecStatisticsBean.setBasicMoney(chargeInfoDetailEntity.getBasicMoney());
            feeRecStatisticsBean.setPowerRateMoney(chargeInfoDetailEntity.getPowerRateMoney());
            feeRecStatisticsBean.setAddMoney1(chargeInfoDetailEntity.getAddMoney1());
            feeRecStatisticsBean.setAddMoney2(chargeInfoDetailEntity.getAddMoney2());
            feeRecStatisticsBean.setAddMoney3(chargeInfoDetailEntity.getAddMoney3());
            feeRecStatisticsBean.setAddMoney4(chargeInfoDetailEntity.getAddMoney4());
            feeRecStatisticsBean.setAddMoney5(chargeInfoDetailEntity.getAddMoney5());
            feeRecStatisticsBean.setAddMoney6(chargeInfoDetailEntity.getAddMoney6());
            feeRecStatisticsBean.setAddMoney7(chargeInfoDetailEntity.getAddMoney7());
            feeRecStatisticsBean.setAddMoney8(chargeInfoDetailEntity.getAddMoney8());
            feeRecStatisticsBean.setFactPre(chargeInfoDetailEntity.getFactPre());
            feeRecStatisticsBean.setFactPunish(chargeInfoDetailEntity.getFactPunish());
            feeRecStatisticsBean.setFactAdvance(chargeInfoDetailEntity.getFactAdvance());
            feeRecStatisticsBeans.add(feeRecStatisticsBean);
        }


        Map<Long, List<FeeRecStatisticsBean>> feeRecStatisticsBeanMap = new HashMap<>();

        if ("customer".equals(statisticalMethod)) {
            feeRecStatisticsBeanMap =
                    feeRecStatisticsBeans.stream().collect(Collectors.groupingBy(FeeRecStatisticsBean::getBusinessPlaceCode));
        }
        if ("collection".equals(statisticalMethod)) {
            feeRecStatisticsBeanMap =
                    feeRecStatisticsBeans.stream().collect(Collectors.groupingBy(FeeRecStatisticsBean::getOperator));
        }

        for (Long key : feeRecStatisticsBeanMap.keySet()) {
            List<FeeRecStatisticsBean> feeRecStatisticsBeanList =
                    feeRecStatisticsBeanMap.get(key);
            FeeRecStatisticsBean feeRecStatisticsBean = new FeeRecStatisticsBean();
            feeRecStatisticsBean.setSortColumn(key);
            if ("customer".equals(statisticalMethod)) {
                feeRecStatisticsBean.setRow1name(businessPlaceCodeMap.get(key));
            }
            if ("collection".equals(statisticalMethod)) {
                feeRecStatisticsBean.setRow1name(sysUserMap.get(key));
            }
            feeRecStatisticsBean.setRow2name("小计");
            feeRecStatisticsBean.setSumSettlement(feeRecStatisticsBeanList.stream().filter(a -> a.getSumSettlement() != null).map(FeeRecStatisticsBean::getSumSettlement).reduce(BigDecimal.ZERO, BigDecimal::add));
            feeRecStatisticsBean.setSumFlow(feeRecStatisticsBeanList.stream().filter(a -> a.getSumFlow() != null).map(FeeRecStatisticsBean::getSumFlow).reduce(BigDecimal.ZERO, BigDecimal::add));
            feeRecStatisticsBean.setFactTotal(feeRecStatisticsBeanList.stream().filter(a -> a.getFactTotal() != null).map(FeeRecStatisticsBean::getFactTotal).reduce(BigDecimal.ZERO, BigDecimal::add));
            feeRecStatisticsBean.setFactMoney(feeRecStatisticsBeanList.stream().filter(a -> a.getFactMoney() != null).map(FeeRecStatisticsBean::getFactMoney).reduce(BigDecimal.ZERO, BigDecimal::add));
            feeRecStatisticsBean.setVolumeCharge(feeRecStatisticsBeanList.stream().filter(a -> a.getVolumeCharge() != null).map(FeeRecStatisticsBean::getVolumeCharge).reduce(BigDecimal.ZERO, BigDecimal::add));
            feeRecStatisticsBean.setBasicMoney(feeRecStatisticsBeanList.stream().filter(a -> a.getBasicMoney() != null).map(FeeRecStatisticsBean::getBasicMoney).reduce(BigDecimal.ZERO, BigDecimal::add));
            feeRecStatisticsBean.setPowerRateMoney(feeRecStatisticsBeanList.stream().filter(a -> a.getPowerRateMoney() != null).map(FeeRecStatisticsBean::getPowerRateMoney).reduce(BigDecimal.ZERO, BigDecimal::add));
            feeRecStatisticsBean.setAddMoney1(feeRecStatisticsBeanList.stream().filter(a -> a.getAddMoney1() != null).map(FeeRecStatisticsBean::getAddMoney1).reduce(BigDecimal.ZERO, BigDecimal::add));
            feeRecStatisticsBean.setAddMoney2(feeRecStatisticsBeanList.stream().filter(a -> a.getAddMoney2() != null).map(FeeRecStatisticsBean::getAddMoney2).reduce(BigDecimal.ZERO, BigDecimal::add));
            feeRecStatisticsBean.setAddMoney3(feeRecStatisticsBeanList.stream().filter(a -> a.getAddMoney3() != null).map(FeeRecStatisticsBean::getAddMoney3).reduce(BigDecimal.ZERO, BigDecimal::add));
            feeRecStatisticsBean.setAddMoney4(feeRecStatisticsBeanList.stream().filter(a -> a.getAddMoney4() != null).map(FeeRecStatisticsBean::getAddMoney4).reduce(BigDecimal.ZERO, BigDecimal::add));
            feeRecStatisticsBean.setAddMoney5(feeRecStatisticsBeanList.stream().filter(a -> a.getAddMoney5() != null).map(FeeRecStatisticsBean::getAddMoney5).reduce(BigDecimal.ZERO, BigDecimal::add));
            feeRecStatisticsBean.setAddMoney6(feeRecStatisticsBeanList.stream().filter(a -> a.getAddMoney6() != null).map(FeeRecStatisticsBean::getAddMoney6).reduce(BigDecimal.ZERO, BigDecimal::add));
            feeRecStatisticsBean.setAddMoney7(feeRecStatisticsBeanList.stream().filter(a -> a.getAddMoney7() != null).map(FeeRecStatisticsBean::getAddMoney7).reduce(BigDecimal.ZERO, BigDecimal::add));
            feeRecStatisticsBean.setAddMoney8(feeRecStatisticsBeanList.stream().filter(a -> a.getAddMoney8() != null).map(FeeRecStatisticsBean::getAddMoney8).reduce(BigDecimal.ZERO, BigDecimal::add));
            feeRecStatisticsBean.setFactPre(feeRecStatisticsBeanList.stream().filter(a -> a.getFactPre() != null).map(FeeRecStatisticsBean::getFactPre).reduce(BigDecimal.ZERO, BigDecimal::add));
            feeRecStatisticsBean.setFactPunish(feeRecStatisticsBeanList.stream().filter(a -> a.getFactPunish() != null).map(FeeRecStatisticsBean::getFactPunish).reduce(BigDecimal.ZERO, BigDecimal::add));
            feeRecStatisticsBean.setFactAdvance(feeRecStatisticsBeanList.stream().filter(a -> a.getFactAdvance() != null).map(FeeRecStatisticsBean::getFactAdvance).reduce(BigDecimal.ZERO, BigDecimal::add));
            feeRecStatisticsBeanList.add(feeRecStatisticsBean);
            feeRecStatisticsBeanMap.put(key, feeRecStatisticsBeanList);
        }

        //排序完的数据
        Map<Long, List<FeeRecStatisticsBean>> resultFeeRecStatisticsBeanMap =
                new LinkedHashMap<>();
        feeRecStatisticsBeanMap.entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .forEachOrdered(x -> resultFeeRecStatisticsBeanMap.put(x.getKey(), x.getValue()));

        List<FeeRecStatisticsBean> feeRecStatisticsBeanList = new ArrayList<>();

        resultFeeRecStatisticsBeanMap.forEach((k, v) -> {
            feeRecStatisticsBeanList.addAll(v);
        });


        //赋值合计
        List<FeeRecStatisticsBean> sumFeeRecStatisticsBeans =
                feeRecStatisticsBeanList.stream().filter(t -> !("小计").equals(t.getRow2name())).collect(Collectors.toList());

        FeeRecStatisticsBean feeRecStatisticsBean = new FeeRecStatisticsBean();
        feeRecStatisticsBean.setRow1name("合计");
        feeRecStatisticsBean.setRow2name("");
        feeRecStatisticsBean.setSumSettlement(sumFeeRecStatisticsBeans.stream().filter(a -> a.getSumSettlement() != null).map(FeeRecStatisticsBean::getSumSettlement).reduce(BigDecimal.ZERO, BigDecimal::add));
        feeRecStatisticsBean.setSumFlow(sumFeeRecStatisticsBeans.stream().filter(a -> a.getSumFlow() != null).map(FeeRecStatisticsBean::getSumFlow).reduce(BigDecimal.ZERO, BigDecimal::add));
        feeRecStatisticsBean.setFactTotal(sumFeeRecStatisticsBeans.stream().filter(a -> a.getFactTotal() != null).map(FeeRecStatisticsBean::getFactTotal).reduce(BigDecimal.ZERO, BigDecimal::add));
        feeRecStatisticsBean.setFactMoney(sumFeeRecStatisticsBeans.stream().filter(a -> a.getFactMoney() != null).map(FeeRecStatisticsBean::getFactMoney).reduce(BigDecimal.ZERO, BigDecimal::add));
        feeRecStatisticsBean.setVolumeCharge(sumFeeRecStatisticsBeans.stream().filter(a -> a.getVolumeCharge() != null).map(FeeRecStatisticsBean::getVolumeCharge).reduce(BigDecimal.ZERO, BigDecimal::add));
        feeRecStatisticsBean.setBasicMoney(sumFeeRecStatisticsBeans.stream().filter(a -> a.getBasicMoney() != null).map(FeeRecStatisticsBean::getBasicMoney).reduce(BigDecimal.ZERO, BigDecimal::add));
        feeRecStatisticsBean.setPowerRateMoney(sumFeeRecStatisticsBeans.stream().filter(a -> a.getPowerRateMoney() != null).map(FeeRecStatisticsBean::getPowerRateMoney).reduce(BigDecimal.ZERO, BigDecimal::add));
        feeRecStatisticsBean.setAddMoney1(sumFeeRecStatisticsBeans.stream().filter(a -> a.getAddMoney1() != null).map(FeeRecStatisticsBean::getAddMoney1).reduce(BigDecimal.ZERO, BigDecimal::add));
        feeRecStatisticsBean.setAddMoney2(sumFeeRecStatisticsBeans.stream().filter(a -> a.getAddMoney2() != null).map(FeeRecStatisticsBean::getAddMoney2).reduce(BigDecimal.ZERO, BigDecimal::add));
        feeRecStatisticsBean.setAddMoney3(sumFeeRecStatisticsBeans.stream().filter(a -> a.getAddMoney3() != null).map(FeeRecStatisticsBean::getAddMoney3).reduce(BigDecimal.ZERO, BigDecimal::add));
        feeRecStatisticsBean.setAddMoney4(sumFeeRecStatisticsBeans.stream().filter(a -> a.getAddMoney4() != null).map(FeeRecStatisticsBean::getAddMoney4).reduce(BigDecimal.ZERO, BigDecimal::add));
        feeRecStatisticsBean.setAddMoney5(sumFeeRecStatisticsBeans.stream().filter(a -> a.getAddMoney5() != null).map(FeeRecStatisticsBean::getAddMoney5).reduce(BigDecimal.ZERO, BigDecimal::add));
        feeRecStatisticsBean.setAddMoney6(sumFeeRecStatisticsBeans.stream().filter(a -> a.getAddMoney6() != null).map(FeeRecStatisticsBean::getAddMoney6).reduce(BigDecimal.ZERO, BigDecimal::add));
        feeRecStatisticsBean.setAddMoney7(sumFeeRecStatisticsBeans.stream().filter(a -> a.getAddMoney7() != null).map(FeeRecStatisticsBean::getAddMoney7).reduce(BigDecimal.ZERO, BigDecimal::add));
        feeRecStatisticsBean.setAddMoney8(sumFeeRecStatisticsBeans.stream().filter(a -> a.getAddMoney8() != null).map(FeeRecStatisticsBean::getAddMoney8).reduce(BigDecimal.ZERO, BigDecimal::add));
        feeRecStatisticsBean.setFactPre(sumFeeRecStatisticsBeans.stream().filter(a -> a.getFactPre() != null).map(FeeRecStatisticsBean::getFactPre).reduce(BigDecimal.ZERO, BigDecimal::add));
        feeRecStatisticsBean.setFactPunish(sumFeeRecStatisticsBeans.stream().filter(a -> a.getFactPunish() != null).map(FeeRecStatisticsBean::getFactPunish).reduce(BigDecimal.ZERO, BigDecimal::add));
        feeRecStatisticsBean.setFactAdvance(sumFeeRecStatisticsBeans.stream().filter(a -> a.getFactAdvance() != null).map(FeeRecStatisticsBean::getFactAdvance).reduce(BigDecimal.ZERO, BigDecimal::add));
        feeRecStatisticsBeanList.add(feeRecStatisticsBean);

        List<TableDataBean> tableDataList = new ArrayList<>();
        String statisticalMethodName = "";
        if ("customer".equals(statisticalMethod)) {
            statisticalMethodName = "按客户所属单位";
        }
        if ("collection".equals(statisticalMethod)) {
            statisticalMethodName = "按收费单位";
        }
        tableData.setTableData(new JRBeanCollectionDataSource(feeRecStatisticsBeanList));
        tableData.setBusinessPlaceCodeName(dept.getDeptName());
        tableData.setStatisticalMethod(statisticalMethodName);
        tableData.setStartDate(chargeInfoDomain.getStartDate() == null ? "" : chargeInfoDomain.getStartDate().toString());
        tableData.setEndDate(chargeInfoDomain.getEndDate() == null ? "" : chargeInfoDomain.getEndDate().toString());
        tableData.setPrintDate(MonUtils.getDay());
        tableDataList.add(tableData);
        return tableDataList;
    }
}
