package org.fms.report.server.webapp.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.fms.report.common.util.FormatterUtil;
import org.fms.report.common.util.MonUtils;
import org.fms.report.common.webapp.bean.RealWriteBean;
import org.fms.report.common.webapp.bean.TableDataBean;
import org.fms.report.common.webapp.bean.WriteFilesBean;
import org.fms.report.common.webapp.domain.CommonParamDomain;
import org.fms.report.common.webapp.domain.DeptDomain;
import org.fms.report.common.webapp.domain.LineDomain;
import org.fms.report.common.webapp.domain.MeterAssetsDomain;
import org.fms.report.common.webapp.domain.MeterDomain;
import org.fms.report.common.webapp.domain.MeterMeterAssetsRelDomain;
import org.fms.report.common.webapp.domain.MeterMoneyDomain;
import org.fms.report.common.webapp.domain.MeterMpedRelDomain;
import org.fms.report.common.webapp.domain.PMpedDomain;
import org.fms.report.common.webapp.domain.PriceExecutionDomain;
import org.fms.report.common.webapp.domain.SettlementDomain;
import org.fms.report.common.webapp.domain.SettlementMeterRelDomain;
import org.fms.report.common.webapp.domain.TransformerDomain;
import org.fms.report.common.webapp.domain.TransformerLineRelDomain;
import org.fms.report.common.webapp.domain.UserDomain;
import org.fms.report.common.webapp.domain.WriteFilesDomain;
import org.fms.report.common.webapp.domain.WriteSectDomain;
import org.fms.report.server.webapp.dao.UserDAO;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.druid.sql.visitor.functions.Nil;
import com.fasterxml.jackson.core.type.TypeReference;
import com.riozenc.titanTool.annotation.TransactionDAO;
import com.riozenc.titanTool.annotation.TransactionService;
import com.riozenc.titanTool.spring.web.client.TitanTemplate;

import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

@TransactionService
public class WriteFilesService {

    @TransactionDAO
    private UserDAO userDAO;

    @Autowired
    private TitanTemplate titanTemplate;

    @Autowired
    private WriteSectService writeSectService;

    @Autowired
    private DeptService deptService;

    @Autowired
    private MeterService meterService;

    @Autowired
    MeterMeterAssetsRelService relService;

    @Autowired
    private CimService cimService;

    @Autowired
    private BillingService billingService;


    // 实抄率
    public List<TableDataBean> realShow(WriteFilesDomain writeFiles) {

        Map<Long, DeptDomain> deptMap = null;
        DeptDomain dept = null;
        WriteSectDomain writeSectDomain = new WriteSectDomain();
        writeSectDomain.setMon(writeFiles.getMon());
        // 查询营业区域下所有子营业区域
        if (writeFiles.getBusinessPlaceCode() != null) {
            List<DeptDomain> deptList = deptService.getDeptList(writeFiles.getBusinessPlaceCode());
            dept = deptService.getDept(writeFiles.getBusinessPlaceCode());
            deptList.add(dept);
            deptMap = deptList.stream().collect(Collectors.toMap(DeptDomain::getId, k -> k));

            if (deptList.size() > 1) {
                List<Long> businessPlaceCodes = deptList.stream().filter(d -> d.getParentId().equals(writeFiles.getBusinessPlaceCode())).map(DeptDomain::getId).collect(Collectors.toList());
                businessPlaceCodes.add(writeFiles.getBusinessPlaceCode());
                writeFiles.setBusinessPlaceCodes(businessPlaceCodes);
                writeSectDomain.setBusinessPlaceCodes(businessPlaceCodes);
            } else {
                writeSectDomain.setBusinessPlaceCode(writeFiles.getBusinessPlaceCode());
            }

        }

        if (writeFiles.getWritorId() != null) {
            writeSectDomain.setWritorId(writeFiles.getWritorId());
        }
        if (writeFiles.getWriteSectionId() != null) {
            writeSectDomain.setId(writeFiles.getWriteSectionId());
        }
        //所有的符合要求的抄表区段
        List<WriteSectDomain> writeSec = writeSectService.getWriteSect(writeSectDomain);
        if (writeSec==null || writeSec.size() <1)  {
            return new ArrayList<>(0);
        }
        //查询所有的抄表记录
        List<Long> writeSectionIds = writeSec.stream().map(WriteSectDomain::getId).distinct().collect(Collectors.toList());
        WriteFilesDomain paramwriteFileDomain=new WriteFilesDomain();
        paramwriteFileDomain.setWriteSectionIds(writeSectionIds);
        paramwriteFileDomain.setMon(writeFiles.getMon());
        List<WriteFilesDomain> writeFilesDomain =
                billingService.getWriteFiles(paramwriteFileDomain);
        //抄表员
        List<Long> writorIds = writeSec.stream().filter(i -> i.getWritorId() != null).map(WriteSectDomain::getWritorId).distinct().collect(Collectors.toList());
        List<UserDomain> userDomains=userDAO.findByIds(writorIds);
        Map<Long, UserDomain> writorsMap =
                userDomains.stream().collect(Collectors.toMap(UserDomain::getId, k -> k,(k1, k2) -> k1));

        Map<Long, WriteSectDomain> writeSectMap = writeSec.stream().collect(Collectors.toMap(WriteSectDomain::getId, k -> k));

        writeFilesDomain.forEach(a -> {
            if (writeSectMap.get(a.getWriteSectionId()) != null) {
                a.setWriteSectName(writeSectMap.get(a.getWriteSectionId()).getWriteSectName());
            }
            if (writeSectMap.get(a.getWriteSectionId()).getWritorId() != null) {
                a.setWritorId(writeSectMap.get(a.getWriteSectionId()).getWritorId());
            }
            if (writorsMap.get(a.getWritorId()) != null) {
                a.setWritorName(writorsMap.get(a.getWritorId()).getUserName());
            }
        });

        Map<Long, List<WriteFilesDomain>> listMap = new HashMap<>();
        if (writeFiles.getBusinessPlaceCode() != null) {
            // 如果当前营业区域下子营业区域不为空，则按营业区域分组；为空，则按抄表员分组
            if (writeFiles.getBusinessPlaceCodes() != null) {
                listMap = writeFilesDomain.stream().filter(a -> a.getBusinessPlaceCode() != null).collect(Collectors.groupingBy(WriteFilesDomain::getBusinessPlaceCode));
            } else {
                listMap = writeFilesDomain.stream().filter(a -> a.getWritorId() != null).collect(Collectors.groupingBy(WriteFilesDomain::getWritorId));
            }
        }
        if (writeFiles.getWritorId() != null) {
            listMap = writeFilesDomain.stream().collect(Collectors.groupingBy(WriteFilesDomain::getWriteSectionId));
        }
        if (writeFiles.getWriteSectionId() != null) {
            listMap = writeFilesDomain.stream().collect(Collectors.groupingBy(WriteFilesDomain::getWriteSectionId));
        }

        TableDataBean tableData = new TableDataBean();
        List<RealWriteBean> writeFilesBeanList = new ArrayList<>();
        for (List<WriteFilesDomain> list : listMap.values()) {
            BigDecimal sYCount = BigDecimal.ZERO;//应抄有功数
            BigDecimal fYCount = BigDecimal.ZERO;//实抄有功数
            BigDecimal fTCount = BigDecimal.ZERO;//实抄数
            BigDecimal sTCount = BigDecimal.ZERO;//应抄数
            BigDecimal zYCount = BigDecimal.ZERO;//划零有功数
            BigDecimal zCount = BigDecimal.ZERO; //划零数
            BigDecimal abnormalCount = BigDecimal.ZERO; //异常数
            if (list.size() > 0) {
                sTCount =
                         new BigDecimal(list.stream().filter(i -> i.getFunctionCode() != null && i.getTimeSeg() == 0).count());//应抄有功

                sYCount =
                        new BigDecimal(list.stream().filter(i -> i.getFunctionCode() != null && i.getFunctionCode() == 1&& i.getTimeSeg() == 0).count());//应抄有功

                fYCount =
                        new BigDecimal(list.stream().filter(i -> i.getWriteFlag() != null && i.getWriteFlag() == 1 && i.getFunctionCode() != null && i.getFunctionCode() == 1&& i.getTimeSeg() == 0).count());//实抄有功
                fTCount =
                        new BigDecimal(list.stream().filter(i -> i.getWriteFlag() != null && i.getWriteFlag() == 1&& i.getTimeSeg() == 0&& i.getFunctionCode() != null).count());//实抄
                zCount =
                        new BigDecimal(list.stream().filter(i -> i.getDiffNum() != null && i.getDiffNum().compareTo(BigDecimal.ZERO) == 0&& i.getTimeSeg() == 0).count());//划零
                zYCount =
                        new BigDecimal(list.stream().filter(i -> i.getDiffNum() != null && i.getDiffNum().compareTo(BigDecimal.ZERO) == 0 && i.getFunctionCode() == 1&& i.getTimeSeg() == 0).count());//划零有功
                abnormalCount =
                        new BigDecimal(list.stream().filter(i -> i.getDiffNum() != null && i.getDiffNum().compareTo(BigDecimal.ZERO) < 0&& i.getTimeSeg() == 0).count());//异常

            }
            RealWriteBean writeFilesBean = new RealWriteBean();
            writeFilesBean.setsYNumber(sYCount.intValue()); //应抄有功
            writeFilesBean.setsWNumber(sTCount.subtract(sYCount).intValue()); //应抄无功
            writeFilesBean.setsTNumber(sTCount.intValue());//应抄数
            writeFilesBean.setfYNumber(fYCount.intValue()); //实抄有功
            writeFilesBean.setfWNumber(fTCount.subtract(fYCount).intValue()); //实抄无功
            writeFilesBean.setfTNumber(fTCount.intValue()); //实抄
            writeFilesBean.setzYNumber(zYCount.intValue()); //划零有功
            writeFilesBean.setzWNumber(zCount.subtract(zYCount).intValue()); //划零无功
            writeFilesBean.setAbnormalNumber(abnormalCount.intValue());

            if (writeFiles.getBusinessPlaceCode() != null) {
                tableData.setvName("营业区域:");
                if (dept != null) {
                    tableData.setvValue(dept.getDeptName());
                }

                if (writeFiles.getBusinessPlaceCodes() != null && writeFiles.getBusinessPlaceCodes().size() > 0) {
                    writeFilesBean.setFirstName("营业区域");
                    if (deptMap != null && deptMap.get(list.get(0).getBusinessPlaceCode()) != null) {
                        writeFilesBean.setFirstValue(deptMap.get(list.get(0).getBusinessPlaceCode()).getTitle());
                    }
                } else {
                    writeFilesBean.setFirstName("抄表员");
                    writeFilesBean.setFirstValue(list.get(0).getWritorName());
                }
            }
            if (writeFiles.getWritorId() != null) {
                tableData.setvName("抄表员");
                tableData.setvValue(list.get(0).getWritorName());
                writeFilesBean.setFirstName("抄表区段");
                writeFilesBean.setFirstValue(list.get(0).getWriteSectName());
            }
            if (writeFiles.getWriteSectionId() != null) {
                tableData.setvName("抄表区段");
                tableData.setvValue(list.get(0).getWriteSectName());
                writeFilesBean.setFirstName("抄表区段");
                writeFilesBean.setFirstValue(list.get(0).getWriteSectName());
            }
            if(sTCount==null || sTCount.compareTo(BigDecimal.ZERO)==0){
                writeFilesBean.setfPercent("0");
            }else{
                writeFilesBean.setfPercent(fTCount.divide(sTCount).multiply(new BigDecimal("100")).setScale(0,RoundingMode.HALF_UP).toString());
            }
            writeFilesBeanList.add(writeFilesBean);
        }

        tableData.setMon(writeFiles.getMon());
        tableData.setDate(new Date());
        tableData.setTableData(new JRBeanCollectionDataSource(writeFilesBeanList));
        List<TableDataBean> tableDataList = new ArrayList<>();
        tableDataList.add(tableData);
        return tableDataList;
    }

    public List<WriteFilesDomain> writeFilesQuery(WriteFilesDomain writeFiles) {
        List<WriteSectDomain> wsList = new ArrayList<>();
        if (writeFiles.getBusinessPlaceCode() != null) {
            List<DeptDomain> deptList = deptService.getDeptList(writeFiles.getBusinessPlaceCode());
            deptList.add(deptService.getDept(writeFiles.getBusinessPlaceCode()));
            List<Long> businessPlaceCodes = deptList.stream().map(DeptDomain::getId).distinct().collect(Collectors.toList());
            if (businessPlaceCodes.size() > 1) {
                writeFiles.setBusinessPlaceCodes(businessPlaceCodes);
            }

        }
        if (writeFiles.getWritorId() != null) {
            WriteSectDomain ws = new WriteSectDomain();
            ws.setMon(writeFiles.getMon());
            ws.setWritorId(writeFiles.getWritorId());
            wsList = writeSectService.getWriteSect(ws);
            List<Long> writeSectionIds = wsList.stream().map(WriteSectDomain::getId).distinct().collect(Collectors.toList());
            writeFiles.setWriteSectionIds(writeSectionIds);
        }

        List<WriteFilesDomain> list = billingService.getWriteFiles(writeFiles);
        if (writeFiles.getBackToLine() != null && writeFiles.getBackToLine()) {
            list.stream().filter(a -> a.getBackToLine()).collect(Collectors.toList());
        }
        if (wsList.isEmpty()) {
            List<Long> writeSectionIdList = list.stream().filter(i -> i.getWriteSectionId() != null).map(WriteFilesDomain::getWriteSectionId).collect(Collectors.toList());
            WriteSectDomain ws = new WriteSectDomain();
            ws.setMon(writeFiles.getMon());
            ws.setWriteSectionIds(writeSectionIdList);
            wsList = writeSectService.getWriteSect(ws);
        }
        Map<Long, WriteSectDomain> writeSectMap = wsList.stream().collect(Collectors.toMap(WriteSectDomain::getId, k -> k));
        MeterDomain m = new MeterDomain();
        m.setMon(writeFiles.getMon());
        List<Long> meterIds = list.stream().filter(i -> i.getMeterId() != null).map(WriteFilesDomain::getMeterId).distinct().collect(Collectors.toList());
        m.setIds(meterIds);
        Map<Long, MeterDomain> meterMap = meterService.getMeter(m).stream().collect(Collectors.toMap(MeterDomain::getId, k -> k));

        MeterMeterAssetsRelDomain rel = new MeterMeterAssetsRelDomain();
        rel.setMon(writeFiles.getMon());
        rel.setMeterIds(meterIds);
        List<MeterMeterAssetsRelDomain> relList = relService.getRel(rel);

        MeterMoneyDomain meterMoney = new MeterMoneyDomain();
        meterMoney.setMon(writeFiles.getMon());
        meterMoney.setMeterIds(meterIds);
        List<MeterMoneyDomain> meterMoneyList = billingService.mongoFind(meterMoney);

        WriteFilesDomain last = new WriteFilesDomain();
        last.setMon(Integer.valueOf(MonUtils.getLastMon(writeFiles.getMon().toString())));
        List<WriteFilesDomain> lastWriteFiles =
                billingService.getWriteFiles(last);

        //获取下拉
        Map<String, Object> commonParams = new HashMap<>();
        Map<String, List<CommonParamDomain>> commonResult = null;
        try {
            commonResult = titanTemplate.postJson("TITAN-CONFIG", "titan-config/sysCommConfig/getAllSysCommConfig", null, commonParams,
                    new TypeReference<Map<String, List<CommonParamDomain>>>() {
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
        List<CommonParamDomain> functionCodeAssetsList = commonResult.get("FUNCTION_CODE_ASSETS");
        Map<Integer, String> functionCodeAssetsMap = functionCodeAssetsList.stream().collect(Collectors.toMap(CommonParamDomain::getParamKey, CommonParamDomain::getParamValue));


        // 填充抄表区段、分时表标志、电价、用电类别、表序号、上月止码、上月度差、波动率、换表电量
        list.forEach(a -> {
            if (a.getFunctionCode() != null) {
                a.setFunctionCodeName(functionCodeAssetsMap.get((Integer) a.getFunctionCode().intValue()));
            }
            if (a.getTimeSeg() != null && a.getTimeSeg().equals((byte) 0)) {
                a.setTimeSegName("总");
            }
            if (writeSectMap.get(a.getWriteSectionId()) != null) {
                // 填充抄表区段
                a.setWriteSectNo(writeSectMap.get(a.getWriteSectionId()).getWriteSectNo());
                a.setWriteSectName(writeSectMap.get(a.getWriteSectionId()).getWriteSectName());
            }
            if (meterMap.get(a.getMeterId()) != null) {
                // 分时表标志
                a.setTsType(meterMap.get(a.getMeterId()).getTsType());
                // 电价
                a.setPriceType(meterMap.get(a.getMeterId()).getPriceType());
                // 用电类别
                a.setElecTypeCode(meterMap.get(a.getMeterId()).getElecTypeCode());
            }
            relList.forEach(r -> {
                if (a.getMeterId().equals(r.getMeterId()) && a.getMeterAssetsId().equals(r.getMeterAssetsId())) {
                    // 表序号
                    a.setMeterOrder(r.getMeterOrder());
                    a.setWriteSn(r.getWriteSn());
//                    a.setMeterAssetsNo(r.getm);
                }
            });
            lastWriteFiles.forEach(l -> {
                if (a.getFunctionCode().equals(l.getFunctionCode()) && a.getMeterId().equals(l.getMeterId())
                        && a.getPhaseSeq().equals(l.getPhaseSeq()) && a.getPowerDirection().equals(l.getPowerDirection())
                        && a.getTimeSeg().equals(l.getTimeSeg())) {
                    // 上月止码
                    a.setLastEndNum(l.getEndNum());

                    // 上月度差
                    a.setLastDiffNum(l.getDiffNum());

                    if(a.getDiffNum()==null) {
                        a.setDiffNum(FormatterUtil.calcDiff(a.getStartNum(), a.getEndNum()));
                    }

                    if(a.getLastDiffNum()==null) {
                        a.setLastDiffNum(FormatterUtil.calcDiff(l.getStartNum(),l.getEndNum()));
                    }

                    if (a.getLastDiffNum() != null && a.getLastDiffNum().compareTo(BigDecimal.ZERO)==1) {
                        // 波动率
                        a.setPowerRate(a.getDiffNum().divide(a.getLastDiffNum(), 4, BigDecimal.ROUND_HALF_UP).multiply(BigDecimal.valueOf(100)));
                    }else{
                        a.setPowerRate(BigDecimal.ONE);
                    }
                }
            });
            meterMoneyList.forEach(o -> {
                        if (o.getMeterId() != null && o.getMeterId().equals(a.getMeterId())) {
                            a.setChgPower(o.getChgPower());
                        }
                    }
            );

        });
        // 电量波动异常查询 电量波动率条件筛选
        if (writeFiles.getPowerRate() != null) {
            list = list.stream().filter(i -> i.getPowerRate() != null && i.getPowerRate().compareTo(writeFiles.getPowerRate()) > -1).collect(Collectors.toList());
        }
        list = list.stream().sorted(Comparator.comparing(WriteFilesDomain::getUserNo)).collect(Collectors.toList());

        return list;
    }

    public List<TableDataBean> findWriteFiles(WriteFilesDomain writeFiles) {
        List<WriteFilesDomain> result = writeFilesQuery(writeFiles);
        TableDataBean tableData = new TableDataBean();
        tableData.setDate(new Date());
        tableData.setTableData(new JRBeanCollectionDataSource(result));
        List<TableDataBean> tableDataList = new ArrayList<>();
        tableDataList.add(tableData);
        return tableDataList;
    }


    //表码查询
    public List<TableDataBean> bmQueryShow(WriteFilesDomain writeFilesDomain) {

        //营业区域
        if (writeFilesDomain.getBusinessPlaceCode() != null) {
            List<DeptDomain> deptList = deptService.getDeptList(writeFilesDomain.getBusinessPlaceCode());
            deptList.add(deptService.getDept(writeFilesDomain.getBusinessPlaceCode()));
            List<Long> businessPlaceCodes = deptList.stream().map(DeptDomain::getId).distinct().collect(Collectors.toList());
            if (businessPlaceCodes.size() > 1) {
                writeFilesDomain.setBusinessPlaceCodes(businessPlaceCodes);
            }

        }
        // 抄表记录
        List<WriteFilesDomain> writeFilesDomains =
                billingService.getWriteFiles(writeFilesDomain);
        List<WriteFilesBean> writeFilesBeans=new ArrayList<>();

        if(writeFilesDomains==null || writeFilesDomains.size()<1){
            return  new ArrayList<>();
        }
        //转圈
        if (writeFilesDomain.getBackToLine() != null && writeFilesDomain.getBackToLine()) {

            writeFilesDomains=
                    writeFilesDomains.stream().filter(t->t.getStartNum()!=null)
                            .filter(t->t.getEndNum()!=null)
                            .filter(a -> a.getStartNum().compareTo(a.getEndNum())==1)
                            .collect(Collectors.toList());
        }
        //已抄
        if (writeFilesDomain.getIsWrite() != null && writeFilesDomain.getIsWrite()) {

            writeFilesDomains=
                    writeFilesDomains.stream().filter(t->t.getWriteFlag()!=null)
                            .filter(t->t.getWriteFlag()==(byte)1)
                            .collect(Collectors.toList());
        }
        //止码为0
        if (writeFilesDomain.getEndNumZero() != null && writeFilesDomain.getEndNumZero()) {

            writeFilesDomains=
                    writeFilesDomains.stream().filter(t->t.getEndNum()!=null)
                            .filter(a -> a.getEndNum().compareTo(BigDecimal.ZERO)==0)
                            .collect(Collectors.toList());
        }
        //止码为null
        if (writeFilesDomain.getEndNumNull() != null && writeFilesDomain.getEndNumNull()) {

            writeFilesDomains=
                    writeFilesDomains.stream().filter(t->t.getEndNum()==null).collect(Collectors.toList());
        }

        if(writeFilesDomains==null || writeFilesDomains.size()<1){
            return new ArrayList<>();
        }

        List<Long> writeSectionIdList=
                writeFilesDomains.stream().filter(t->t.getWriteSectionId()!=null)
                        .map(t->t.getWriteSectionId()).distinct().collect(Collectors.toList());

        //获取抄表区段
        WriteSectDomain ws = new WriteSectDomain();
        ws.setMon(writeFilesDomain.getMon());
        ws.setWriteSectionIds(writeSectionIdList);
        List<WriteSectDomain> writeSectDomains = writeSectService.getWriteSect(ws);
        Map<Long,WriteSectDomain> writeSectDomainMap=
                writeSectDomains.stream().collect(Collectors.toMap(WriteSectDomain::getId, a -> a, (k1, k2) -> k1));

        //获取计费点
        List<Long> meterIds=
                writeFilesDomains.stream().filter(t->t.getMeterId()!=null)
                        .map(WriteFilesDomain::getMeterId).distinct().collect(Collectors.toList());

        //获取资产号
//        List<Long> meterAssetsIds=
//                writeFilesDomains.stream().filter(t->t.getMeterAssetsId()!=null)
//                .map(WriteFilesDomain::getMeterAssetsId).distinct().collect(Collectors.toList());
        
        //获取计量点
//        List<Long> mpedIds = 
//        		writeFilesDomains.stream().filter(t->t.getMpedId()!=null)
//        		.map(WriteFilesDomain::getMpedId).distinct().collect(Collectors.toList());

        MeterMpedRelDomain meterMpedRelParamDomain = new MeterMpedRelDomain();
        meterMpedRelParamDomain.setPageSize(-1);
        meterMpedRelParamDomain.setMeterIds(meterIds);
        //
        List<MeterMpedRelDomain> meterMpedRelDomains = cimService.findMeterMpedRelByWhere(meterMpedRelParamDomain);
        
        Map<String,MeterMpedRelDomain> meterMpedRelDomainMap=
        		meterMpedRelDomains.stream().collect(Collectors.toMap(o -> o.getMeterId() + "_" + o.getMpedId()+"_"+o.getFunctionCode(), a -> a, (k1, k2) -> k1));
        
        PMpedDomain pMpedParamDomain = new PMpedDomain();
        pMpedParamDomain.setMpedIds(meterMpedRelDomains.stream().map(MeterMpedRelDomain::getMpedId).collect(Collectors.toList()));
        List<PMpedDomain> pMpedDomains = cimService.findMpedByWhere(pMpedParamDomain);
        Map<Long, PMpedDomain> pmpedMap = pMpedDomains.stream().collect(Collectors.toMap(o -> o.getId(), a -> a, (k1, k2) -> k1));
        
//        List<MeterAssetsDomain> meterAssetsDomains=
//                cimService.getMeterAssetsByAssetsIds(paramMap);
//        Map<Long,MeterAssetsDomain> meterAssetsDomainMap=meterAssetsDomains.stream()
//                .collect(Collectors.toMap(o -> o.getId(), a -> a,(k1, k2) -> k1));
        //获取计量点和表计关系
        //表序号
//        MeterMeterAssetsRelDomain parammeterMeterAssetsRelDomain =
//                new MeterMeterAssetsRelDomain();
//        parammeterMeterAssetsRelDomain.setMeterAssetsIds(meterAssetsIds);
//        parammeterMeterAssetsRelDomain.setPageSize(-1);
//        List<MeterMeterAssetsRelDomain> meterMeterAssetsRelDomains =
//                cimService.findMeterMeterAssetsRelByWhere(parammeterMeterAssetsRelDomain);

//        Map<String,MeterMeterAssetsRelDomain> meterMeterAssetsRelDomainMap=
//                meterMeterAssetsRelDomains.stream().collect(Collectors.toMap(o -> o.getMeterId() + "_" + o.getMeterAssetsId()+"_"+o.getFunctionCode(), a -> a, (k1, k2) -> k1));

        MeterMoneyDomain meterMoney = new MeterMoneyDomain();
        meterMoney.setMon(writeFilesDomain.getMon());
        meterMoney.setMeterIds(meterIds);
        List<MeterMoneyDomain> meterMoneyList = billingService.mongoFind(meterMoney);
        Map<Long,MeterMoneyDomain> meterMoneyDomainMap=meterMoneyList.stream()
                .collect(Collectors.toMap(o -> o.getMeterId(), a -> a, (k1, k2) -> k1));

        Map<Long, String> functionCodeAssetsMap =
                cimService.findSystemCommonConfigByType("FUNCTION_CODE_ASSETS");

        Map<Long, String> timeSegMap =
                cimService.findSystemCommonConfigByType("TIME_SEG");


        // 填充抄表区段、分时表标志、电价、用电类别、表序号、上月止码、上月度差、波动率、换表电量
        writeFilesDomains.forEach(a -> {
            if (a.getFunctionCode() != null) {
                a.setFunctionCodeName(functionCodeAssetsMap.get(Long.valueOf(a.getFunctionCode())));
            }
            if (a.getTimeSeg() != null) {
                a.setTimeSegName(timeSegMap.get(Long.valueOf(a.getTimeSeg())));
            }

            if (writeSectDomainMap.get(a.getWriteSectionId()) != null) {
                // 填充抄表区段
                a.setWriteSectNo(writeSectDomainMap.get(a.getWriteSectionId()).getWriteSectNo());
                a.setWriteSectName(writeSectDomainMap.get(a.getWriteSectionId()).getWriteSectName());
            }
            //
            String key =
                    a.getMeterId()+"_"+a.getMpedId()+"_"+a.getFunctionCode();
            
            MeterMpedRelDomain meterMpedRelDomain = meterMpedRelDomainMap.get(key);
            
            if (meterMpedRelDomain !=null) {
            	a.setWriteSn(meterMpedRelDomain.getWriteSn());
            	a.setMpedId(meterMpedRelDomain.getMpedId());
            	PMpedDomain pMpedDomain = pmpedMap.get(meterMpedRelDomain.getMpedId());
            	a.setMpedNo(pMpedDomain==null?null:pMpedDomain.getCode());
			}
            
            
            //表序号
//            String key =
//                    a.getMeterId()+"_"+a.getMeterAssetsId()+"_"+a.getFunctionCode();

//            MeterMeterAssetsRelDomain meterMeterAssetsRelDomain=
//                                  meterMeterAssetsRelDomainMap.get(key);


//            if(meterMeterAssetsRelDomain!=null){
//                a.setMeterOrder(meterMeterAssetsRelDomain.getMeterOrder());
//                a.setWriteSn(meterMeterAssetsRelDomain.getWriteSn());
//            }

            MeterMoneyDomain meterMoneyDomain=
                    meterMoneyDomainMap.get(a.getMeterId());

            if(meterMoneyDomain!=null){
                a.setChgPower(meterMoneyDomain.getChgPower());
            }

//            MeterAssetsDomain meterAssetsDomain=
//                    meterAssetsDomainMap.get(a.getMeterAssetsId());
//            if(meterAssetsDomain!=null){
//                a.setMeterAssetsNo(meterAssetsDomain.getMeterAssetsNo());
//            }

        });

        for (WriteFilesDomain t:writeFilesDomains){
            WriteFilesBean writeFilesBean=new WriteFilesBean();
            writeFilesBean.setUserNo(t.getUserNo());
            writeFilesBean.setUserName(t.getUserName());
            writeFilesBean.setAddress(t.getAddress());
            writeFilesBean.setWriteSn(t.getWriteSn());
            writeFilesBean.setWriteSn(t.getWriteSn());
            writeFilesBean.setMeterAssetsNo(t.getMeterAssetsNo());
            writeFilesBean.setMeterOrder(t.getMeterOrder());
            writeFilesBean.setFunctionCodeName(t.getFunctionCodeName());
            writeFilesBean.setTimeSegName(t.getTimeSegName());
            writeFilesBean.setEndNum(t.getEndNum());
            writeFilesBean.setStartNum(t.getStartNum());
            writeFilesBean.setChgPower(t.getChgPower());
            writeFilesBean.setWriteSectNo(t.getWriteSectNo());
            writeFilesBean.setWriteDate(t.getLastWriteDate());
            writeFilesBean.setMpedId(t.getMpedId());
            writeFilesBean.setMpedNo(t.getMpedNo());
            writeFilesBeans.add(writeFilesBean);
        };

        //排序
        writeFilesBeans =
                writeFilesBeans.stream().sorted(Comparator.comparing(WriteFilesBean::getWriteSn, Comparator.nullsLast(Long::compareTo)).thenComparing(Comparator.comparing(WriteFilesBean::getUserNo, Comparator.nullsLast(String::compareTo))).thenComparing(WriteFilesBean::getMeterOrder, Comparator.nullsLast(Long::compareTo))).collect(Collectors.toList());

        TableDataBean tableData = new TableDataBean();
        tableData.setDate(new Date());
        tableData.setTableData(new JRBeanCollectionDataSource(writeFilesBeans));
        List<TableDataBean> tableDataList = new ArrayList<>();
        tableDataList.add(tableData);
        return tableDataList;
    }



    //打印抄表卡
    public List<TableDataBean> printMeterReadingCard(MeterDomain meterDomain) {

        MeterDomain paramMeterDomian=new MeterDomain();
        if(meterDomain.getUserNo()!=null && !"".equals(meterDomain.getUserNo())){
            List<UserDomain> userDomains=
                    cimService.findUserByNo(meterDomain.getUserNo());
            if(userDomains==null || userDomains.size()!=1){
                return new ArrayList<>();
            }
            paramMeterDomian.setUserId(userDomains.get(0).getId());
        }

        if(meterDomain.getWriteSectionId()!=null){
            paramMeterDomian.setWriteSectionId(meterDomain.getWriteSectionId());
        }
        paramMeterDomian.setPageSize(-1);

        //获取计量点
        List<MeterDomain> meterDomains=
                cimService.findClearMeterDoaminByWhere(paramMeterDomian);

        if(meterDomains==null ||meterDomains.size()<1){
            return new ArrayList<>();
        }

        Map<Long,MeterDomain> meterDomainMap=
                meterDomains.stream().collect(Collectors.toMap(MeterDomain::getId, a -> a, (k1, k2) -> k1));

        //用户信息
        List<Long> userIds=
                meterDomains.stream().map(MeterDomain::getUserId).distinct().collect(Collectors.toList());

        List<UserDomain> userDomains=cimService.getUserByIds(userIds);

        Map<Long,UserDomain> userDomainMap=
                userDomains.stream().collect(Collectors.toMap(UserDomain::getId, a -> a, (k1, k2) -> k1));

        List<Long> meterIds=
                meterDomains.stream().map(MeterDomain::getId).distinct().collect(Collectors.toList());

        //获取计量点表计关系
        MeterMeterAssetsRelDomain parammeterMeterAssetsRelDomain =
                new MeterMeterAssetsRelDomain();
        parammeterMeterAssetsRelDomain.setMeterIds(meterIds);
        parammeterMeterAssetsRelDomain.setPageSize(-1);
        List<MeterMeterAssetsRelDomain> meterMeterAssetsRelDomains =
                cimService.findMeterMeterAssetsRelByWhere(parammeterMeterAssetsRelDomain);

        //获取表计id
        List<Long> meterAssetsIds=
                meterMeterAssetsRelDomains.stream().map(MeterMeterAssetsRelDomain::getMeterAssetsId).distinct().collect(Collectors.toList());
        if(meterAssetsIds==null ||meterAssetsIds.size()<1){
            return new ArrayList<>();
        }
        //获取表计信息
        Map<String,List<Long>> paramMap=new HashMap<>();
        paramMap.put("ids",meterAssetsIds);
        List<MeterAssetsDomain> meterAssetsDomains=
                cimService.getMeterAssetsByAssetsIds(paramMap);
        Map<Long,MeterAssetsDomain> meterAssetsDomainMap= meterAssetsDomains.stream().collect(Collectors.toMap(MeterAssetsDomain::getId, a -> a, (k1, k2) -> k1));

        //电价
        List<PriceExecutionDomain> priceExecutionDomains = billingService.findTimeLadderPrice();
        Map<Long, PriceExecutionDomain> priceExecutionDomainMap =
                priceExecutionDomains.stream().filter(m -> m.getPriceTypeId() != null).collect(Collectors.toMap(PriceExecutionDomain::getPriceTypeId, a -> a, (k1, k2) -> k1));


        //根据计量点找变压器
        List<TransformerLineRelDomain>transformerLineRelDomains= cimService.findTransformerLineByMeterIds(meterIds);
        Map<Long,TransformerLineRelDomain> transformerLineRelDomainMap=
                transformerLineRelDomains.stream().collect(Collectors.toMap(TransformerLineRelDomain::getMeterId, a -> a, (k1, k2) -> k1));
        Map<Long, LineDomain> lineDomainMap=null;
        Map<Long, TransformerDomain> transformerDomainMap=null;
        if(transformerLineRelDomains!=null && transformerLineRelDomains.size()>0){

            List<Long> lineIds =
                    transformerLineRelDomains.stream().map(TransformerLineRelDomain::getLineId).distinct().collect(Collectors.toList());

            List<Long> transformerIds =
                    transformerLineRelDomains.stream().map(TransformerLineRelDomain::getTransformerId).distinct().collect(Collectors.toList());

            //获取变压器详细信息
            if(transformerIds!=null && transformerIds.size()>0){
                TransformerDomain paramTransformerDomain = new TransformerDomain();
                paramTransformerDomain.setTransformerIds(transformerIds);
                paramTransformerDomain.setPageSize(-1);
                List<TransformerDomain> transformerEntities =
                        cimService.getAvaTransformerByWhere(paramTransformerDomain);
                transformerDomainMap =
                        transformerEntities.stream().collect(Collectors.toMap(TransformerDomain::getId, a -> a, (k1, k2) -> k1));
            }

            //获取线路详细信息
            if(lineIds!=null && lineIds.size()>0){
                LineDomain lineDomain = new LineDomain();
                lineDomain.setLineIds(lineIds);
                lineDomain.setPageSize(-1);
                List<LineDomain> lineDomains = cimService.getLine(lineDomain);

                lineDomainMap= lineDomains.stream().collect(Collectors.toMap(LineDomain::getId, a -> a, (k1, k2) -> k1));
            }

        }

        List<SettlementMeterRelDomain> settlementMeterRelDomains =
                                    cimService.getSettlementByBillingMeterIds(meterIds);
        Map<Long,Long>settlementMeterRelMap=
                settlementMeterRelDomains.stream().collect(Collectors.toMap(SettlementMeterRelDomain::getMeterId, a -> a.getSettlementId(), (k1, k2) -> k1));

        List<Long> settlementIds=
                settlementMeterRelDomains.stream().map(SettlementMeterRelDomain::getSettlementId).distinct().collect(Collectors.toList());

        List<SettlementDomain>settlementDomains =
                cimService.findSettlementByIds(settlementIds);

        Map<Long,SettlementDomain>settlementDomainMap=
                settlementDomains.stream().collect(Collectors.toMap(SettlementDomain::getId, a -> a, (k1, k2) -> k1));

        //下拉
        Map<Long, String> voltLevelCodeMap =
                cimService.findSystemCommonConfigByType("VOLT_LEVEL_CODE");

        Map<Long, String> msModeMap =
                cimService.findSystemCommonConfigByType("MS_MODE");

        Map<Long, String> transLostFlagMap =
                cimService.findSystemCommonConfigByType("TRANS_SHARE_FLAG");

        Map<Long, String> lineLostFlagMap =
                cimService.findSystemCommonConfigByType("LINE_SHARE_FLAG");

        Map<Long, String> cosStdCodeMap =
                cimService.findSystemCommonConfigByType("COS_STD_CODE");

        Map<Long, String> modelCodeMap =
                cimService.findSystemCommonConfigByType("MODEL_CODE");

        Map<Long, String> functionCodeMap =
                cimService.findSystemCommonConfigByType("FUNCTION_CODE");

        Map<Long, String> phaseLine =
                cimService.findSystemCommonConfigByType("PHASE_LINE");

        Map<Long, String> ratedCurntCode =
                cimService.findSystemCommonConfigByType("RATED_CURNT_CODE");


        //组装返回实体
        //去掉虚拟表
        meterMeterAssetsRelDomains=
                meterMeterAssetsRelDomains.stream().filter(t->t.getFunctionCode()!=Byte.valueOf("3")).collect(Collectors.toList());

        Map<Long,Long> meterMeterAssetsRelMap=
                meterMeterAssetsRelDomains.stream().filter(t->t.getFunctionCode()==Byte.valueOf("1")).filter(t->t.getWriteSn()!=null).collect(Collectors.toMap(o->o.getMeterId(), a -> a.getWriteSn(), (k1, k2) -> k1));

        meterMeterAssetsRelDomains.stream().filter(t->t.getFunctionCode()==Byte.valueOf("2")).forEach(t->{
            t.setWriteSn(meterMeterAssetsRelMap.get(t.getMeterId()));
        });

        //显示表码
        List<Long> meterMeterAssetsIds=
                meterMeterAssetsRelDomains.stream().filter(t->t.getMeterAssetsId()!=null).map(MeterMeterAssetsRelDomain::getMeterAssetsId).distinct().collect(Collectors.toList());

        Integer mon=billingService.getCurrentMon();
        if(mon==null){
            mon=Integer.valueOf(MonUtils.getMon());
        }
        WriteFilesDomain paramWriteFile=new WriteFilesDomain();
        paramWriteFile.setMeterAssetsIds(meterMeterAssetsIds);
        paramWriteFile.setMon(mon);
        paramWriteFile.setTimeSeg((byte)0);
        paramWriteFile.setPageSize(-1);
        List<WriteFilesDomain> writeFilesDomains=
                billingService.getWriteFiles(paramWriteFile);

        Map<String,String> writeFilesMap=
                Optional.ofNullable(writeFilesDomains).orElse(Collections.emptyList()).stream().filter(m -> m.getMeterAssetsId() != null)
                        .collect(Collectors.toMap(t->t.getMeterAssetsId().toString()+"_"+t.getFunctionCode().toString(), a -> Optional.ofNullable(a.getEndNum()).orElse(BigDecimal.ZERO).stripTrailingZeros().toPlainString(), (k1, k2) -> k1));

        //按抄表序号 计量点 功能代码
        meterMeterAssetsRelDomains=
                meterMeterAssetsRelDomains.stream().sorted(Comparator.comparing(MeterMeterAssetsRelDomain::getWriteSn, Comparator.nullsLast(Long::compareTo))
                        .thenComparing(MeterMeterAssetsRelDomain::getMeterId,  Comparator.nullsLast(Long::compareTo))
                        .thenComparing(MeterMeterAssetsRelDomain::getFunctionCode,Comparator.nullsLast(Byte::compareTo))).collect(Collectors.toList());

        List<TableDataBean> tableDataBeans = new ArrayList<>();

        for (int i=0;i<meterMeterAssetsRelDomains.size();i++){
            MeterMeterAssetsRelDomain meterAssetsRelDomain=
                    meterMeterAssetsRelDomains.get(i);
            MeterDomain midMeterDomain=
                    meterDomainMap.get(meterMeterAssetsRelDomains.get(i).getMeterId());
            TableDataBean tableDataBean=new TableDataBean();

            if(midMeterDomain==null){
                continue;
            }
            UserDomain userDomain=
                    userDomainMap.get(midMeterDomain.getUserId());

            if (userDomain==null){
                continue;
            }

            tableDataBean.setUserName(userDomain.getUserName());
            tableDataBean.setSetAddress(userDomain.getAddress());
            tableDataBean.setCalCapacity(userDomain.getAllCapacity());
            tableDataBean.setUserNo(userDomain.getUserNo());
            tableDataBean.setVoltLevelTypeName(midMeterDomain.getVoltLevelType()==null?"":voltLevelCodeMap.get(Long.valueOf(midMeterDomain.getVoltLevelType())));

            Long settlementId=settlementMeterRelMap.get(midMeterDomain.getId());
            SettlementDomain settlementDomain=
                    settlementDomainMap.get(settlementId);
            if(settlementDomain!=null){
                tableDataBean.setPhoneNum(settlementDomain.getSettlementPhone());
            }
            tableDataBean.setLadderSn(midMeterDomain.getLadderNum()==null?"":midMeterDomain.getLadderNum().toString());
            TransformerLineRelDomain transformerLineRelDomain=
                    transformerLineRelDomainMap.get(midMeterDomain.getId());

            if (transformerLineRelDomain!=null){
                TransformerDomain transformerDomain=
                        transformerDomainMap.get(transformerLineRelDomain.getTransformerId());

                tableDataBean.setTg(transformerDomain==null?"":transformerDomain.getDeskName());
                tableDataBean.setMsTypeName(transformerLineRelDomain.getMsType()==null?"":
                        msModeMap.get(Long.valueOf(transformerLineRelDomain.getMsType())));

                LineDomain lineDomain=
                        lineDomainMap.get(transformerLineRelDomain.getLineId());

                tableDataBean.setLineName(lineDomain==null?"":
                        lineDomain.getLineName());

                tableDataBean.setStationName(lineDomain==null?"":
                        lineDomain.getBeginSubsName());
                tableDataBean.setTransLostName(transformerLineRelDomain.getTransLostType()==null?"":transLostFlagMap.get(Long.valueOf(transformerLineRelDomain.getTransLostType())));
            }
            tableDataBean.setLineLostName(midMeterDomain.getLineLostType()==null?"":lineLostFlagMap.get(Long.valueOf(midMeterDomain.getLineLostType())));
            tableDataBean.setCosStdCodeName(midMeterDomain.getCosType()==null?"":
                    cosStdCodeMap.get(Long.valueOf(midMeterDomain.getCosType())));
            MeterAssetsDomain meterAssetsDomain=
                    meterAssetsDomainMap.get(meterAssetsRelDomain.getMeterAssetsId());

            tableDataBean.setFunctionCodeName(meterAssetsRelDomain.getFunctionCode()==null?"":functionCodeMap.get(Long.valueOf(meterAssetsRelDomain.getFunctionCode())));
            tableDataBean.setFactorNum(meterAssetsRelDomain.getFactorNum()==null?BigDecimal.ONE:meterAssetsRelDomain.getFactorNum());
            if(meterAssetsDomain!=null){
                tableDataBean.setModelName(meterAssetsDomain.getModelCode()==null?"":modelCodeMap.get(Long.valueOf(meterAssetsDomain.getModelCode())));
                tableDataBean.setPhaseName(meterAssetsDomain.getPhaseLine()==null?"":phaseLine.get(Long.valueOf(meterAssetsDomain.getPhaseLine())));
                tableDataBean.setMadeNo(meterAssetsDomain.getMadeNo());
                tableDataBean.setRatedCurntCodeName(meterAssetsDomain.getRatedCurntCode()==null?"":ratedCurntCode.get(Long.valueOf(meterAssetsDomain.getRatedCurntCode())));
                tableDataBean.setAssetsNo(meterAssetsDomain.getMeterAssetsNo());
            }
            tableDataBean.setRatedPtCodeName("");
            tableDataBean.setRatedCtCodeName("");
            tableDataBean.setPrice(midMeterDomain.getPriceType()==null
                    ?"":
                    priceExecutionDomainMap.get(midMeterDomain.getPriceType()).getPrice().stripTrailingZeros().toPlainString());
            if(meterAssetsRelDomain.getMeterAssetsId()!=null && meterAssetsRelDomain.getFunctionCode()!=null){
                tableDataBean.setEndNum(writeFilesMap.get(meterAssetsRelDomain.getMeterAssetsId().toString()+"_"+meterAssetsRelDomain.getFunctionCode().toString()));

            }else{
                tableDataBean.setEndNum("");
            }

            tableDataBeans.add(tableDataBean);
        }
        if(tableDataBeans==null || tableDataBeans.size()<1){
            return  new ArrayList<>();
        }
        return tableDataBeans;
    }
}
