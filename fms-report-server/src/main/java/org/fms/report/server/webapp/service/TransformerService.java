package org.fms.report.server.webapp.service;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.fms.report.common.webapp.bean.FeeRecStatisticsBean;
import org.fms.report.common.webapp.bean.TableDataBean;
import org.fms.report.common.webapp.domain.ChargeInfoDomain;
import org.fms.report.common.webapp.domain.MeterDomain;
import org.fms.report.common.webapp.domain.MeterMoneyDomain;
import org.fms.report.common.webapp.domain.TransformerDomain;
import org.fms.report.common.webapp.domain.TransformerLineRelDomain;
import org.fms.report.common.webapp.domain.TransformerMeterRelDomain;
import org.fms.report.common.webapp.domain.WriteSectDomain;
import org.fms.report.common.webapp.domain.WriteSectMongoDomain;
import org.fms.report.server.webapp.dao.ArrearageDAO;
import org.fms.report.server.webapp.dao.ChargeInfoDAO;
import org.fms.report.server.webapp.dao.DeptDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import com.fasterxml.jackson.core.type.TypeReference;
import com.riozenc.titanTool.annotation.TransactionDAO;
import com.riozenc.titanTool.annotation.TransactionService;
import com.riozenc.titanTool.common.json.utils.GsonUtils;
import com.riozenc.titanTool.spring.web.client.TitanTemplate;

import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

@TransactionService
public class TransformerService {
	private Log logger = LogFactory.getLog(TransformerService.class);

	@Autowired
	private MeterService meterService;

	@Autowired
	private TitanTemplate titanTemplate;

	@TransactionDAO
	private ChargeInfoDAO chargeInfoDAO;

	@Autowired
	private MeterMoneyService meterMoneyService;

	@Autowired
	private UserService userService;

	@Autowired
	private DeptService deptService;

	@Autowired
	private WriteSectService writeSectService;

	@Autowired
	private ChargeService chargeService;

	@Autowired
	private BillingService billingService;

	@Autowired
	private TransformerMeterRelService transformerMeterRelService;

	@TransactionDAO
	private DeptDAO deptDAO;

	@TransactionDAO
	private ArrearageDAO arrearageDAO;

	@Autowired
	private CimService cimService;

	public List<TransformerDomain> getTransformer(TransformerDomain transformer) {
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.setContentType(MediaType.APPLICATION_JSON);
		List<TransformerDomain> list = new ArrayList<>();
		try {
			list = titanTemplate.post("BILLING-SERVER", "billingServer/transformer/getTransformer", httpHeaders,
					GsonUtils.toJson(transformer), new TypeReference<List<TransformerDomain>>() {
					});
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	public List<TransformerMeterRelDomain> getTransMeterRel(TransformerMeterRelDomain transMeterRel) {
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.setContentType(MediaType.APPLICATION_JSON);
		List<TransformerMeterRelDomain> transMeterRelList = new ArrayList<>();
		try {
			transMeterRelList = titanTemplate.post("BILLING-SERVER", "billingServer/transMeterRel/getTransMeterRel",
					httpHeaders, GsonUtils.toJson(transMeterRel), new TypeReference<List<TransformerMeterRelDomain>>() {
					});
		} catch (Exception e) {
			e.printStackTrace();
		}
		return transMeterRelList;
	}

//    public List<TableDataBean> writeCharge(TransformerDomain transformer) throws IOException {
//        DeptDomain deptDomain = null;
//        List<MeterDomain> mList = new ArrayList<>();
//        if (transformer.getWritorId() != null) {
//            // 根据抄表员获取所有计量点
//            MeterDomain m = new MeterDomain();
//            m.setMon(transformer.getMon());
//            m.setWritorId(transformer.getWritorId());
//            m.setPageSize(-1);
//            mList = meterService.getMeter(m);
//            // 根据计量点、计量点变压器关系表获取所有变压器id
//            TransformerMeterRelDomain rel = new TransformerMeterRelDomain();
//            rel.setMon(transformer.getMon());
//            rel.setMeterIds(mList.stream().map(MeterDomain::getId).distinct().collect(Collectors.toList()));
//            transformer.setIds(getTransMeterRel(rel).stream().map(TransformerMeterRelDomain::getTransformerId).distinct().collect(Collectors.toList()));
//        }
//
//        if (transformer.getBusinessPlaceCode() != null) {
//
//            List<DeptDomain> list = deptService.getDeptList(transformer.getBusinessPlaceCode());
//            deptDomain = deptService.getDept(transformer.getBusinessPlaceCode());
//            list.add(deptDomain);
//
//            MeterDomain m = new MeterDomain();
//            m.setMon(transformer.getMon());
//            m.setBusinessPlaceCode(transformer.getBusinessPlaceCode());
//
//            if (list.size() > 1) {
//                List<Long> businessPlaceCodes = list.stream().map(DeptDomain::getId).collect(Collectors.toList());
//                transformer.setBusinessPlaceCodes(list.stream().map(DeptDomain::getId).distinct().collect(Collectors.toList()));
//                m.setBusinessPlaceCodes(businessPlaceCodes);
//            }
//
//            mList = meterService.getMeter(m);
//        }
//
//        // 查询变压器
//        List<TransformerDomain> transformerList = getTransformer(transformer);
//        List<TransWriteChargeBean> beanList = new ArrayList<>();
//        if (transformerList.size() > 0) {
//            TransformerMeterRelDomain tmr = new TransformerMeterRelDomain();
//            tmr.setMon(transformer.getMon());
//            tmr.setTransformerIds(transformerList.stream().map(TransformerDomain::getId).distinct().collect(Collectors.toList()));
//            // 变压器计量点关系表
//            List<TransformerMeterRelDomain> rel = getTransMeterRel(tmr);
//            Map<Long, List<TransformerMeterRelDomain>> relMap = rel.stream().collect(Collectors.groupingBy(TransformerMeterRelDomain::getTransformerId));
//            transformerList.forEach(a -> {
//                if (relMap.get(a.getId()) != null) {
//                    a.setMeterIds(relMap.get(a.getId()).stream().map(TransformerMeterRelDomain::getMeterId).distinct().collect(Collectors.toList()));
//                }
//            });
//
//            List<Long> meters = rel.stream().map(TransformerMeterRelDomain::getMeterId).distinct().collect(Collectors.toList());
//
//            MeterDomain m = new MeterDomain();
//            m.setMon(transformer.getMon());
//            m.setIds(meters);
//            m.setPageSize(-1);
//            // 与变压器相关计量点
//            List<MeterDomain> meterList = meterService.getMeter(m);
//
//            // 无台区计量点
//            if (mList.size() > 0) {
//                mList = mList.stream().filter(item ->
//                        !meterList.stream().map(MeterDomain::getId).collect(Collectors.toList()).contains(item.getId())
//                ).collect(Collectors.toList());
//            }
//
//
//            List<Long> userIds = meterList.stream().filter(a -> a.getUserId() != null).map(MeterDomain::getUserId).distinct().collect(Collectors.toList());
//            UserDomain user = new UserDomain();
//            user.setMon(transformer.getMon());
//            user.setUserType((byte) 60);
//            user.setIds(userIds);
//            user.setPageSize(-1);
//            // 考核户
//            List<UserDomain> userList = userService.getUser(user);
//
//            List<Long> filterUserIds = userList.stream().map(UserDomain::getId).distinct().collect(Collectors.toList());
//            // 考核户相关计量点
//            List<MeterDomain> filtermeterList = meterList.stream().filter(item ->
//                    filterUserIds.contains(item.getUserId())).collect(Collectors.toList());
//
//            List<Long> filterMeterIds = filtermeterList.stream().map(MeterDomain::getId).distinct().collect(Collectors.toList());
//
//            // 变压器相关电费
//            MeterMoneyDomain meterMoney = new MeterMoneyDomain();
//            meterMoney.setMon(transformer.getMon());
//            meterMoney.setMeterIds(meters);
//            meterMoney.setPageSize(-1);
//            List<MeterMoneyDomain> meterMoneyList = meterMoneyService.mongoFind(meterMoney);
//
//            // 变压器相关欠费记录
//            ArrearageDomain arrearage = new ArrearageDomain();
//            arrearage.setMon(transformer.getMon());
//            arrearage.setMeterIds(meters);
//            arrearage.setPageSize(-1);
//            List<ArrearageDomain> arrearageList = arrearageDAO.findByWhere(arrearage);
//
//            // 变压器相关收费记录
//            ChargeInfoDomain chargeInfo = new ChargeInfoDomain();
//            chargeInfo.setMon(transformer.getMon());
//            chargeInfo.setMeterIds(meters);
//            chargeInfo.setPageSize(-1);
//            List<ChargeInfoDomain> chargeInfoList = chargeInfoDAO.findByWhere(chargeInfo);
//
//            transformerList.forEach(trans -> {
//                TransWriteChargeBean bean = new TransWriteChargeBean();
//                try {
//                    bean = JSONUtil.readValue(JSONUtil.toJsonString(trans), new TypeReference<TransWriteChargeBean>() {});
//                    if (bean.getDeskName() == null) {
//                        bean.setDeskName(bean.getTransformerNo());
//                    }
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//                if (trans.getMeterIds() != null) {
//                    // 变压器相关所有电费信息
//                    List<MeterMoneyDomain> mmList = meterMoneyList.stream().filter(mm -> trans.getMeterIds().contains(mm.getMeterId())).collect(Collectors.toList());
//
//                    // 附加费
//                    List<Map<String, BigDecimal>> mapList = mmList.stream().filter(a -> a.getSurchargeDetail() != null).map(MeterMoneyDomain::getSurchargeDetail).collect(Collectors.toList());
//                    List<MeterMoneyDomain> l = new ArrayList<>();
//                    mapList.forEach(map -> {
//                        MeterMoneyDomain mm = new MeterMoneyDomain();
//                        for (Map.Entry<String,BigDecimal> entry : map.entrySet()) {
//                            if ("95#2#0".equals(entry.getKey())) {
//                                mm.setTwo(entry.getValue());
//                            }
//                            if ("95#3#0".equals(entry.getKey())) {
//                                mm.setThree(entry.getValue());
//                            }
//                            if ("95#4#0".equals(entry.getKey())) {
//                                mm.setFour(entry.getValue());
//                            }
//                            if ("95#5#0".equals(entry.getKey())) {
//                                mm.setFive(entry.getValue());
//                            }
//                            if ("95#6#0".equals(entry.getKey())) {
//                                mm.setSix(entry.getValue());
//                            }
//                            if ("95#7#0".equals(entry.getKey())) {
//                                mm.setSeven(entry.getValue());
//                            }
//                            if ("95#8#0".equals(entry.getKey())) {
//                                mm.setEight(entry.getValue());
//                            }
//                            if ("95#9#0".equals(entry.getKey())) {
//                                mm.setNine(entry.getValue());
//                            }
//                        }
//                        l.add(mm);
//                    });
//                    bean.setTwo(l.stream().filter(a -> a.getTwo() != null).map(MeterMoneyDomain::getTwo).reduce(BigDecimal.ZERO,BigDecimal::add));
//                    bean.setThree(l.stream().filter(a -> a.getThree() != null).map(MeterMoneyDomain::getThree).reduce(BigDecimal.ZERO,BigDecimal::add));
//                    bean.setFour(l.stream().filter(a -> a.getFour() != null).map(MeterMoneyDomain::getFour).reduce(BigDecimal.ZERO,BigDecimal::add));
//                    bean.setFive(l.stream().filter(a -> a.getFive() != null).map(MeterMoneyDomain::getFive).reduce(BigDecimal.ZERO,BigDecimal::add));
//                    bean.setSix(l.stream().filter(a -> a.getSix() != null).map(MeterMoneyDomain::getSix).reduce(BigDecimal.ZERO,BigDecimal::add));
//                    bean.setSeven(l.stream().filter(a -> a.getSeven() != null).map(MeterMoneyDomain::getSeven).reduce(BigDecimal.ZERO,BigDecimal::add));
//                    bean.setEight(l.stream().filter(a -> a.getEight() != null).map(MeterMoneyDomain::getEight).reduce(BigDecimal.ZERO,BigDecimal::add));
//                    bean.setNine(l.stream().filter(a -> a.getNine() != null).map(MeterMoneyDomain::getNine).reduce(BigDecimal.ZERO,BigDecimal::add));
//
//                    // 电度电费
//                    bean.setVolumeCharge(mmList.stream().filter(a -> a.getVolumeCharge() != null).map(MeterMoneyDomain::getVolumeCharge).reduce(BigDecimal.ZERO, BigDecimal::add));
//
//                    // 线损电量
//                    BigDecimal activeLineLossPower = mmList.stream().filter(a -> a.getActiveLineLossPower() != null).map(MeterMoneyDomain::getActiveLineLossPower).reduce(BigDecimal.ZERO, BigDecimal::add);
//                    // 总电量
//                    BigDecimal totalPower = mmList.stream().filter(a -> a.getTotalPower() != null).map(MeterMoneyDomain::getTotalPower).reduce(BigDecimal.ZERO, BigDecimal::add);
//                    // 变压器相关考核户电费信息
//                    List<MeterMoneyDomain> filterMM = mmList.stream().filter(fmm -> filterMeterIds.contains(fmm.getMeterId())).collect(Collectors.toList());
//                    // 考核户电量即供电量
//                    bean.setSupplyPower(filterMM.stream().filter(a -> a.getTotalPower() != null).map(MeterMoneyDomain::getTotalPower).reduce(BigDecimal.ZERO, BigDecimal::add));
//                    // 线损率
//                    if (bean.getSupplyPower().compareTo(BigDecimal.ZERO) == 0) {
//                        bean.setLineLossRate(BigDecimal.ZERO);
//                    } else {
//                        bean.setLineLossRate(activeLineLossPower.divide(bean.getSupplyPower(), 2, BigDecimal.ROUND_HALF_UP));
//                    }
//
//                    // 售电量 = 总电量 - 供电量
//                    bean.setSalePower(totalPower.subtract(bean.getSupplyPower()));
//
//                    // 变压器相关欠费记录
//                    List<ArrearageDomain> aList = arrearageList.stream().filter(a -> trans.getMeterIds().contains(a.getMeterId())).collect(Collectors.toList());
//                    // 应收电费
//                    bean.setReceivable(aList.stream().filter(a -> a.getReceivable() != null).map(ArrearageDomain::getReceivable).reduce(BigDecimal.ZERO, BigDecimal::add));
//
//                    // 变压器相关收费记录
//                    List<ChargeInfoDomain> cList = chargeInfoList.stream().filter(c -> trans.getMeterIds().contains(c.getMeterId())).collect(Collectors.toList());
//                    // 违约金
//                    bean.setPunishMoney(cList.stream().filter(c -> c.getFactPunish() != null).map(ChargeInfoDomain::getFactPunish).reduce(BigDecimal.ZERO, BigDecimal::add));
//                    // 实收电费
//                    bean.setFactMoney(cList.stream().filter(c -> c.getFactMoney() != null).map(ChargeInfoDomain::getFactMoney).reduce(BigDecimal.ZERO, BigDecimal::add));
//
//                    // 电费回收率
//                    if (bean.getReceivable().compareTo(BigDecimal.ZERO) == 0) {
//                        bean.setMoneyReceiveRate(BigDecimal.ZERO);
//                    } else {
//                        bean.setMoneyReceiveRate(bean.getFactMoney().divide(bean.getReceivable(), 2, BigDecimal.ROUND_HALF_UP));
//                    }
//
//                }
//                beanList.add(bean);
//
//            });
//        }
//
//        // 公变合计
//        List<TransWriteChargeBean> gbList = beanList.stream().filter(a -> a.getIsPubType() != null && a.getIsPubType() == 1).collect(Collectors.toList());
//        TransWriteChargeBean gbBean = new TransWriteChargeBean();
//        gbBean.setDeskName("公变合计");
//        gbBean.setCapacity(gbList.stream().filter(a -> a.getCapacity() != null).map(TransWriteChargeBean::getCapacity).reduce(BigDecimal.ZERO, BigDecimal::add));
//        gbBean.setSupplyPower(gbList.stream().filter(a -> a.getSupplyPower() != null).map(TransWriteChargeBean::getSupplyPower).reduce(BigDecimal.ZERO, BigDecimal::add));
//        gbBean.setSalePower(gbList.stream().filter(a -> a.getSalePower() != null).map(TransWriteChargeBean::getSalePower).reduce(BigDecimal.ZERO, BigDecimal::add));
//        gbBean.setReceivable(gbList.stream().filter(a -> a.getReceivable() != null).map(TransWriteChargeBean::getReceivable).reduce(BigDecimal.ZERO, BigDecimal::add));
//        gbBean.setFactMoney(gbList.stream().filter(a -> a.getFactMoney() != null).map(TransWriteChargeBean::getFactMoney).reduce(BigDecimal.ZERO, BigDecimal::add));
//        gbBean.setTwo(gbList.stream().filter(a -> a.getTwo() != null).map(TransWriteChargeBean::getTwo).reduce(BigDecimal.ZERO, BigDecimal::add));
//        gbBean.setThree(gbList.stream().filter(a -> a.getThree() != null).map(TransWriteChargeBean::getThree).reduce(BigDecimal.ZERO, BigDecimal::add));
//        gbBean.setFour(gbList.stream().filter(a -> a.getFour() != null).map(TransWriteChargeBean::getFour).reduce(BigDecimal.ZERO, BigDecimal::add));
//        gbBean.setFive(gbList.stream().filter(a -> a.getFive() != null).map(TransWriteChargeBean::getFive).reduce(BigDecimal.ZERO, BigDecimal::add));
//        gbBean.setSix(gbList.stream().filter(a -> a.getSix() != null).map(TransWriteChargeBean::getSix).reduce(BigDecimal.ZERO, BigDecimal::add));
//        gbBean.setSeven(gbList.stream().filter(a -> a.getSeven() != null).map(TransWriteChargeBean::getSeven).reduce(BigDecimal.ZERO, BigDecimal::add));
//        gbBean.setEight(gbList.stream().filter(a -> a.getEight() != null).map(TransWriteChargeBean::getEight).reduce(BigDecimal.ZERO, BigDecimal::add));
//        gbBean.setNine(gbList.stream().filter(a -> a.getNine() != null).map(TransWriteChargeBean::getNine).reduce(BigDecimal.ZERO, BigDecimal::add));
//        gbBean.setVolumeCharge(gbList.stream().filter(a -> a.getVolumeCharge() != null).map(TransWriteChargeBean::getVolumeCharge).reduce(BigDecimal.ZERO, BigDecimal::add));
//        gbBean.setPunishMoney(gbList.stream().filter(a -> a.getPunishMoney() != null).map(TransWriteChargeBean::getPunishMoney).reduce(BigDecimal.ZERO, BigDecimal::add));
//        gbBean.setLineLossRate(gbList.stream().filter(a -> a.getLineLossRate() != null).map(TransWriteChargeBean::getLineLossRate).reduce(BigDecimal.ZERO, BigDecimal::add));
//        gbBean.setMoneyReceiveRate(gbList.stream().filter(a -> a.getMoneyReceiveRate() != null).map(TransWriteChargeBean::getMoneyReceiveRate).reduce(BigDecimal.ZERO, BigDecimal::add));
//        beanList.add(gbBean);
//        // 专变合计
//        List<TransWriteChargeBean> zbList = beanList.stream().filter(a -> a.getIsPubType() != null && a.getIsPubType() == 2).collect(Collectors.toList());
//        TransWriteChargeBean zbBean = new TransWriteChargeBean();
//        zbBean.setDeskName("专变合计");
//        zbBean.setCapacity(zbList.stream().filter(a -> a.getCapacity() != null).map(TransWriteChargeBean::getCapacity).reduce(BigDecimal.ZERO, BigDecimal::add));
//        zbBean.setSupplyPower(zbList.stream().filter(a -> a.getSupplyPower() != null).map(TransWriteChargeBean::getSupplyPower).reduce(BigDecimal.ZERO, BigDecimal::add));
//        zbBean.setSalePower(zbList.stream().filter(a -> a.getSalePower() != null).map(TransWriteChargeBean::getSalePower).reduce(BigDecimal.ZERO, BigDecimal::add));
//        zbBean.setReceivable(zbList.stream().filter(a -> a.getReceivable() != null).map(TransWriteChargeBean::getReceivable).reduce(BigDecimal.ZERO, BigDecimal::add));
//        zbBean.setFactMoney(zbList.stream().filter(a -> a.getFactMoney() != null).map(TransWriteChargeBean::getFactMoney).reduce(BigDecimal.ZERO, BigDecimal::add));
//        zbBean.setTwo(zbList.stream().filter(a -> a.getTwo() != null).map(TransWriteChargeBean::getTwo).reduce(BigDecimal.ZERO, BigDecimal::add));
//        zbBean.setThree(zbList.stream().filter(a -> a.getThree() != null).map(TransWriteChargeBean::getThree).reduce(BigDecimal.ZERO, BigDecimal::add));
//        zbBean.setFour(zbList.stream().filter(a -> a.getFour() != null).map(TransWriteChargeBean::getFour).reduce(BigDecimal.ZERO, BigDecimal::add));
//        zbBean.setFive(zbList.stream().filter(a -> a.getFive() != null).map(TransWriteChargeBean::getFive).reduce(BigDecimal.ZERO, BigDecimal::add));
//        zbBean.setSix(zbList.stream().filter(a -> a.getSix() != null).map(TransWriteChargeBean::getSix).reduce(BigDecimal.ZERO, BigDecimal::add));
//        zbBean.setSeven(zbList.stream().filter(a -> a.getSeven() != null).map(TransWriteChargeBean::getSeven).reduce(BigDecimal.ZERO, BigDecimal::add));
//        zbBean.setEight(zbList.stream().filter(a -> a.getEight() != null).map(TransWriteChargeBean::getEight).reduce(BigDecimal.ZERO, BigDecimal::add));
//        zbBean.setNine(zbList.stream().filter(a -> a.getNine() != null).map(TransWriteChargeBean::getNine).reduce(BigDecimal.ZERO, BigDecimal::add));
//        zbBean.setVolumeCharge(zbList.stream().filter(a -> a.getVolumeCharge() != null).map(TransWriteChargeBean::getVolumeCharge).reduce(BigDecimal.ZERO, BigDecimal::add));
//        zbBean.setPunishMoney(zbList.stream().filter(a -> a.getPunishMoney() != null).map(TransWriteChargeBean::getPunishMoney).reduce(BigDecimal.ZERO, BigDecimal::add));
//        zbBean.setLineLossRate(zbList.stream().filter(a -> a.getLineLossRate() != null).map(TransWriteChargeBean::getLineLossRate).reduce(BigDecimal.ZERO, BigDecimal::add));
//        zbBean.setMoneyReceiveRate(zbList.stream().filter(a -> a.getMoneyReceiveRate() != null).map(TransWriteChargeBean::getMoneyReceiveRate).reduce(BigDecimal.ZERO, BigDecimal::add));
//        beanList.add(zbBean);
//        // 无台区客户
//        TransWriteChargeBean nonRelBean = new TransWriteChargeBean();
//        if (mList.size() > 0) {
//
//            nonRelBean.setDeskName("无台区客户");
//            nonRelBean.setCapacity(BigDecimal.ZERO);
//            // 无台区客户相关计量点id
//            List<Long> nonRelMeterIds = mList.stream().map(MeterDomain::getId).collect(Collectors.toList());
//            MeterMoneyDomain mm = new MeterMoneyDomain();
//            mm.setMon(transformer.getMon());
//            mm.setMeterIds(nonRelMeterIds);
//            // 无台区客户电费记录
//            List<MeterMoneyDomain> nonRelMM = meterMoneyService.mongoFind(mm);
//
//            // 无台区客户线损电量
//            BigDecimal activeLineLossPower = nonRelMM.stream().filter(a -> a.getActiveLineLossPower() != null).map(MeterMoneyDomain::getActiveLineLossPower).reduce(BigDecimal.ZERO, BigDecimal::add);
//            // 无台区客户总电量
//            BigDecimal nonRelTotalPower = nonRelMM.stream().filter(i -> i.getTotalPower() != null).map(MeterMoneyDomain::getTotalPower).reduce(BigDecimal.ZERO, BigDecimal::add);
//            nonRelBean.setVolumeCharge(nonRelMM.stream().filter(i -> i.getVolumeCharge() != null).map(MeterMoneyDomain::getVolumeCharge).reduce(BigDecimal.ZERO, BigDecimal::add));
//
//            // 附加费
//            List<Map<String, BigDecimal>> nonRelMapList = nonRelMM.stream().filter(i -> i.getSurchargeDetail() != null).map(MeterMoneyDomain::getSurchargeDetail).collect(Collectors.toList());
//            List<MeterMoneyDomain> l = new ArrayList<>();
//            nonRelMapList.forEach(map -> {
//                MeterMoneyDomain nonMM = new MeterMoneyDomain();
//                for (Map.Entry<String,BigDecimal> entry : map.entrySet()) {
//                    if ("95#2#0".equals(entry.getKey())) {
//                        nonMM.setTwo(entry.getValue());
//                    }
//                    if ("95#3#0".equals(entry.getKey())) {
//                        nonMM.setThree(entry.getValue());
//                    }
//                    if ("95#4#0".equals(entry.getKey())) {
//                        nonMM.setFour(entry.getValue());
//                    }
//                    if ("95#5#0".equals(entry.getKey())) {
//                        nonMM.setFive(entry.getValue());
//                    }
//                    if ("95#6#0".equals(entry.getKey())) {
//                        nonMM.setSix(entry.getValue());
//                    }
//                    if ("95#7#0".equals(entry.getKey())) {
//                        nonMM.setSeven(entry.getValue());
//                    }
//                    if ("95#8#0".equals(entry.getKey())) {
//                        nonMM.setEight(entry.getValue());
//                    }
//                    if ("95#9#0".equals(entry.getKey())) {
//                        nonMM.setNine(entry.getValue());
//                    }
//                }
//                l.add(nonMM);
//            });
//            nonRelBean.setTwo(l.stream().filter(i -> i.getTwo() != null).map(MeterMoneyDomain::getTwo).reduce(BigDecimal.ZERO,BigDecimal::add));
//            nonRelBean.setThree(l.stream().filter(i -> i.getThree() != null).map(MeterMoneyDomain::getThree).reduce(BigDecimal.ZERO,BigDecimal::add));
//            nonRelBean.setFour(l.stream().filter(i -> i.getFour() != null).map(MeterMoneyDomain::getFour).reduce(BigDecimal.ZERO,BigDecimal::add));
//            nonRelBean.setFive(l.stream().filter(i -> i.getFive() != null).map(MeterMoneyDomain::getFive).reduce(BigDecimal.ZERO,BigDecimal::add));
//            nonRelBean.setSix(l.stream().filter(i -> i.getSix() != null).map(MeterMoneyDomain::getSix).reduce(BigDecimal.ZERO,BigDecimal::add));
//            nonRelBean.setSeven(l.stream().filter(i -> i.getSeven() != null).map(MeterMoneyDomain::getSeven).reduce(BigDecimal.ZERO,BigDecimal::add));
//            nonRelBean.setEight(l.stream().filter(i -> i.getEight() != null).map(MeterMoneyDomain::getEight).reduce(BigDecimal.ZERO,BigDecimal::add));
//            nonRelBean.setNine(l.stream().filter(i -> i.getNine() != null).map(MeterMoneyDomain::getNine).reduce(BigDecimal.ZERO,BigDecimal::add));
//
//            ArrearageDomain a = new ArrearageDomain();
//            a.setMon(transformer.getMon());
//            a.setMeterIds(nonRelMeterIds);
//            // 无台区客户欠费记录
//            List<ArrearageDomain> nonRelA = arrearageDAO.findByWhere(a);
//            nonRelBean.setReceivable(nonRelA.stream().filter(i -> i.getReceivable() != null).map(ArrearageDomain::getReceivable).reduce(BigDecimal.ZERO, BigDecimal::add));
//
//            ChargeInfoDomain c = new ChargeInfoDomain();
//            c.setMon(transformer.getMon());
//            c.setMeterIds(nonRelMeterIds);
//            // 无台区客户收费记录
//            List<ChargeInfoDomain> nonRelC = chargeInfoDAO.findByWhere(c);
//            nonRelBean.setFactMoney(nonRelC.stream().filter(i -> i.getFactMoney() != null).map(ChargeInfoDomain::getFactMoney).reduce(BigDecimal.ZERO, BigDecimal::add));
//            nonRelBean.setPunishMoney(nonRelC.stream().filter(i -> i.getFactPunish() != null).map(ChargeInfoDomain::getFactPunish).reduce(BigDecimal.ZERO, BigDecimal::add));
//
//            List<Long> nonRelUserIds = mList.stream().filter(i -> i.getUserId() != null).map(MeterDomain::getUserId).collect(Collectors.toList());
//            UserDomain u = new UserDomain();
//            u.setIds(nonRelUserIds);
//            u.setMon(transformer.getMon());
//            u.setUserType((byte) 60);
//            // 无台区考核客户信息记录
//            List<UserDomain> nonRelKHU = userService.getUser(u);
//            // 无台区考核客户id
//            List<Long> nonRelKHUserIds = nonRelKHU.stream().map(UserDomain::getId).collect(Collectors.toList());
//            // 无台区考核客户绑定计量点记录
//            List<MeterDomain> nonRelKHM = mList.stream().filter(i -> nonRelKHUserIds.contains(i.getUserId())).collect(Collectors.toList());
//            List<Long> nonRelKHMeterIds = nonRelKHM.stream().map(MeterDomain::getId).collect(Collectors.toList());
//            List<MeterMoneyDomain> nonRelKHMM = nonRelMM.stream().filter(i -> nonRelKHMeterIds.contains(i.getMeterId())).collect(Collectors.toList());
//
//            // 无台区考核表客户电量即无台区客户供电量
//            nonRelBean.setSupplyPower(nonRelKHMM.stream().filter(i -> i.getTotalPower() != null).map(MeterMoneyDomain::getTotalPower).reduce(BigDecimal.ZERO, BigDecimal::add));
//
//            nonRelBean.setSalePower(nonRelTotalPower.subtract(nonRelBean.getSupplyPower()));
//            // 无台区客户线损率
//            if (nonRelBean.getSupplyPower().compareTo(BigDecimal.ZERO) == 0) {
//                nonRelBean.setLineLossRate(BigDecimal.ZERO);
//            } else {
//                nonRelBean.setLineLossRate(activeLineLossPower.divide(nonRelBean.getSupplyPower(), 2, BigDecimal.ROUND_HALF_UP));
//            }
//            // 无台区客户电费回收率
//            if (nonRelBean.getReceivable().compareTo(BigDecimal.ZERO) == 0) {
//                nonRelBean.setMoneyReceiveRate(BigDecimal.ZERO);
//            } else {
//                nonRelBean.setMoneyReceiveRate(nonRelBean.getFactMoney().divide(nonRelBean.getReceivable(), 2, BigDecimal.ROUND_HALF_UP));
//            }
//
//
//            beanList.add(nonRelBean);
//        }
//
//        // 总合计
//        TransWriteChargeBean beanSum = new TransWriteChargeBean();
//        beanSum.setDeskName("总合计");
//        beanSum.setCapacity(gbBean.getCapacity().add(zbBean.getCapacity()));
//        if (nonRelBean.getCapacity() != null) {
//            beanSum.setCapacity(beanSum.getCapacity().add(nonRelBean.getCapacity()));
//        }
//        beanSum.setSupplyPower(gbBean.getSupplyPower().add(zbBean.getSupplyPower()));
//        if (nonRelBean.getSupplyPower() != null) {
//            beanSum.setSupplyPower(beanSum.getSupplyPower().add(nonRelBean.getSupplyPower()));
//        }
//        beanSum.setSalePower(gbBean.getSalePower().add(zbBean.getSalePower()));
//        if (nonRelBean.getSalePower() != null) {
//            beanSum.setSalePower(beanSum.getSalePower().add(nonRelBean.getSalePower()));
//        }
//        beanSum.setReceivable(gbBean.getReceivable().add(zbBean.getReceivable()));
//        if (nonRelBean.getReceivable() != null) {
//            beanSum.setReceivable(beanSum.getReceivable().add(nonRelBean.getReceivable()));
//        }
//        beanSum.setFactMoney(gbBean.getFactMoney().add(zbBean.getFactMoney()));
//        if (nonRelBean.getFactMoney() != null) {
//            beanSum.setFactMoney(beanSum.getFactMoney().add(nonRelBean.getFactMoney()));
//        }
//        beanSum.setTwo(gbBean.getTwo().add(zbBean.getTwo()));
//        if (nonRelBean.getTwo() != null) {
//            beanSum.setTwo(beanSum.getTwo().add(nonRelBean.getTwo()));
//        }
//        beanSum.setThree(gbBean.getThree().add(zbBean.getThree()));
//        if (nonRelBean.getThree() != null) {
//            beanSum.setThree(beanSum.getThree().add(nonRelBean.getThree()));
//        }
//        beanSum.setFour(gbBean.getFour().add(zbBean.getFour()));
//        if (nonRelBean.getFour() != null) {
//            beanSum.setFour(beanSum.getFour().add(nonRelBean.getFour()));
//        }
//        beanSum.setFive(gbBean.getFive().add(zbBean.getFive()));
//        if (nonRelBean.getFive() != null) {
//            beanSum.setFive(beanSum.getFive().add(nonRelBean.getFive()));
//        }
//        beanSum.setSix(gbBean.getSix().add(zbBean.getSix()));
//        if (nonRelBean.getSix() != null) {
//            beanSum.setSix(beanSum.getSix().add(nonRelBean.getSix()));
//        }
//        beanSum.setSeven(gbBean.getSeven().add(zbBean.getSeven()));
//        if (nonRelBean.getSeven() != null) {
//            beanSum.setSeven(beanSum.getSeven().add(nonRelBean.getSeven()));
//        }
//        beanSum.setEight(gbBean.getEight().add(zbBean.getEight()));
//        if (nonRelBean.getEight() != null) {
//            beanSum.setEight(beanSum.getEight().add(nonRelBean.getEight()));
//        }
//        beanSum.setNine(gbBean.getNine().add(zbBean.getNine()));
//        if (nonRelBean.getNine() != null) {
//            beanSum.setNine(beanSum.getNine().add(nonRelBean.getNine()));
//        }
//        beanSum.setVolumeCharge(gbBean.getVolumeCharge().add(zbBean.getVolumeCharge()));
//        if (nonRelBean.getVolumeCharge() != null) {
//            beanSum.setVolumeCharge(beanSum.getVolumeCharge().add(nonRelBean.getVolumeCharge()));
//        }
//        beanSum.setPunishMoney(gbBean.getPunishMoney().add(zbBean.getPunishMoney()));
//        if (nonRelBean.getPunishMoney() != null) {
//            beanSum.setPunishMoney(beanSum.getPunishMoney().add(nonRelBean.getPunishMoney()));
//        }
//        beanSum.setLineLossRate(gbBean.getLineLossRate().add(zbBean.getLineLossRate()));
//        if (nonRelBean.getLineLossRate() != null) {
//            beanSum.setLineLossRate(beanSum.getLineLossRate().add(nonRelBean.getLineLossRate()));
//        }
//        beanSum.setMoneyReceiveRate(gbBean.getMoneyReceiveRate().add(zbBean.getMoneyReceiveRate()));
//        if (nonRelBean.getMoneyReceiveRate() != null) {
//            beanSum.setMoneyReceiveRate(beanSum.getMoneyReceiveRate().add(nonRelBean.getMoneyReceiveRate()));
//        }
//        beanList.add(beanSum);
//
//        TableDataBean tableData = new TableDataBean();
//        tableData.setvValue(deptDomain.getDeptName());
//        tableData.setMon(transformer.getMon());
//        tableData.setTableData(new JRBeanCollectionDataSource(beanList));
//        List<TableDataBean> tableDataList = new ArrayList<>();
//        tableDataList.add(tableData);
//        return tableDataList;
//
//    }
	public List<TableDataBean> writeCharge(TransformerDomain transformer) throws IOException {
		TableDataBean tableData = new TableDataBean();
		MeterMoneyDomain meterMoneyDomain = new MeterMoneyDomain();
		meterMoneyDomain.setMon(transformer.getMon());
		if (transformer.getBusinessPlaceCode() != null && transformer.getBusinessPlaceCode() != 0) {
			WriteSectMongoDomain writeSectMongoDomain = new WriteSectMongoDomain();
			writeSectMongoDomain.setMon(transformer.getMon());
			writeSectMongoDomain.setBusinessPlaceCode(transformer.getBusinessPlaceCode());
			writeSectMongoDomain.setPageSize(-1);
			List<WriteSectMongoDomain> writeSectMongoDomains = billingService.getMongoWriteSect(writeSectMongoDomain);
			List<Long> writeSectionIds = writeSectMongoDomains.stream().map(WriteSectMongoDomain::getId).distinct()
					.collect(Collectors.toList());
			meterMoneyDomain.setWriteSectIds(writeSectionIds);
			tableData.setvName("营业区域");
			tableData.setvValue(transformer.getBusinessPlaceName());
		} else if (transformer.getWritorId() != null && transformer.getWritorId() != 0) {
			// 根据抄表员获取抄表段
			WriteSectDomain writeSectDomain = new WriteSectDomain();
			writeSectDomain.setMon(transformer.getMon());
			writeSectDomain.setWritorId(transformer.getWritorId());
			List<WriteSectDomain> writeSectDomainList = writeSectService.getWriteSect(writeSectDomain);
			List<Long> writeSectIdList = writeSectDomainList.stream().map(WriteSectDomain::getId)
					.collect(Collectors.toList());
			meterMoneyDomain.setWriteSectIds(writeSectIdList);
			tableData.setvName("抄表员");
			tableData.setvValue(transformer.getWritorName());
		}

		// METER_MONEY列表
		meterMoneyDomain.setPageSize(-1);
		List<MeterMoneyDomain> meterMoneyDomainList = billingService.mongoFind(meterMoneyDomain);
		List<Long> meterIdList = meterMoneyDomainList.stream().map(MeterMoneyDomain::getMeterId).distinct()
				.collect(Collectors.toList());

		ChargeInfoDomain chargeInfoDomain = new ChargeInfoDomain();
		chargeInfoDomain.setMon(transformer.getMon());
		chargeInfoDomain.setMeterIds(meterIdList);
		List<ChargeInfoDomain> chargeInfoDomainList = billingService.chargeFindByWhere(chargeInfoDomain);

		// 收费记录MAP
		Map<Long, List<ChargeInfoDomain>> chargeInfoDomainMap = chargeInfoDomainList.stream()
				.collect(Collectors.groupingBy(ChargeInfoDomain::getMeterId));

		// 变压器计量点关系
		List<TransformerMeterRelDomain> transformerMeterRelDomainList = new ArrayList<>();

		// 获取计量点与线路关系
		int len = meterIdList.size();

		// 无电费记录 预收之类
		if (len != 0) {
			for (int m = 0; m < len / 4999 + 1; m++) {// 遍历次数

				List<Long> tl = meterIdList.subList(m * 4999, (m + 1) * 4999 > len ? len : (m + 1) * 4999);

				TransformerMeterRelDomain transformerMeterRelDomain = new TransformerMeterRelDomain();
				transformerMeterRelDomain.setMon(transformer.getMon());
				transformerMeterRelDomain.setMeterIds(tl);
				transformerMeterRelDomainList
						.addAll(transformerMeterRelService.getTransMeterRel(transformerMeterRelDomain));
			}
		}

		// 变压器计量点关系MAP
		Map<Long, TransformerMeterRelDomain> transformerMeterRelDomainMap = transformerMeterRelDomainList.stream()
				.collect(Collectors.toMap(TransformerMeterRelDomain::getMeterId, k -> k, (k1, k2) -> k1));

		// 变压器信息
		List<Long> transformerIds = transformerMeterRelDomainList.stream()
				.map(TransformerMeterRelDomain::getTransformerId).distinct().collect(Collectors.toList());
		TransformerDomain transformerDomain = new TransformerDomain();
		transformerDomain.setMon(transformer.getMon());
		transformerDomain.setIds(transformerIds);
		List<TransformerDomain> transformerDomainList = getTransformer(transformerDomain);
		// 变压器Map
		Map<Long, TransformerDomain> transformerDomainMap = transformerDomainList.stream()
				.collect(Collectors.toMap(TransformerDomain::getId, k -> k));

		for (MeterMoneyDomain metermoneyDomain : meterMoneyDomainList) {

			if (transformerMeterRelDomainMap.get(metermoneyDomain.getMeterId()) == null) {
				metermoneyDomain.setTransFormerId((long) 0);
				metermoneyDomain.setTransformerName(null);
				metermoneyDomain.setCapacity(null);
				metermoneyDomain.setIsPubType(null);

			} else {
				metermoneyDomain.setTransFormerId(transformerDomainMap
						.get(transformerMeterRelDomainMap.get(metermoneyDomain.getMeterId()).getTransformerId())
						.getId());
				metermoneyDomain.setTransformerName(transformerDomainMap
						.get(transformerMeterRelDomainMap.get(metermoneyDomain.getMeterId()).getTransformerId())
						.getDeskName());
				metermoneyDomain.setCapacity(transformerDomainMap
						.get(transformerMeterRelDomainMap.get(metermoneyDomain.getMeterId()).getTransformerId())
						.getCapacity());
				metermoneyDomain.setIsPubType(transformerDomainMap
						.get(transformerMeterRelDomainMap.get(metermoneyDomain.getMeterId()).getTransformerId())
						.getIsPubType());

			}

			if (chargeInfoDomainMap.get(metermoneyDomain.getMeterId()) == null) {
				metermoneyDomain.setFactMoney(BigDecimal.ZERO);
				metermoneyDomain.setFactPunish(BigDecimal.ZERO);

			} else {
				metermoneyDomain.setFactMoney(chargeInfoDomainMap.get(metermoneyDomain.getMeterId()).stream()
						.map(ChargeInfoDomain::getFactMoney).reduce(BigDecimal.ZERO, BigDecimal::add));
				metermoneyDomain.setFactPunish(chargeInfoDomainMap.get(metermoneyDomain.getMeterId()).stream()
						.map(ChargeInfoDomain::getFactPunish).reduce(BigDecimal.ZERO, BigDecimal::add));

			}

		}

		List<Long> notransformerNameMeterIds = meterMoneyDomainList.stream().filter(t -> t.getMeterId() != null)
				.filter(t -> t.getTransformerName() == null).map(MeterMoneyDomain::getMeterId).distinct()
				.collect(Collectors.toList());
		// 专变无变压器名称用户名
		if (notransformerNameMeterIds != null && notransformerNameMeterIds.size() > 0) {
			MeterDomain m = new MeterDomain();
			m.setMon(transformer.getMon());
			m.setIds(notransformerNameMeterIds);
			List<MeterDomain> meterDomains = meterService.getMeter(m);

			String nolinString = meterDomains.stream().map(MeterDomain::getId).map(String::valueOf)
					.collect(Collectors.joining(","));
			logger.info((transformer.getBusinessPlaceCode() == null ? "" : transformer.getBusinessPlaceCode())
					+ "无台区用户计量点======" + nolinString);

			Map<Long, String> meterMap = meterDomains.stream()
					.collect(Collectors.toMap(MeterDomain::getId, k -> k.getUserName(), (k1, k2) -> k1));
			meterMoneyDomainList.stream().filter(t -> notransformerNameMeterIds.contains(t.getMeterId())).forEach(t -> {
				t.setTransformerName(meterMap.get(t.getMeterId()));
			});

		}

		Map<Long, List<MeterMoneyDomain>> transformerListMap = meterMoneyDomainList.stream()
				.collect(Collectors.groupingBy(MeterMoneyDomain::getTransFormerId));

		// 变压器线路关系
		TransformerLineRelDomain transformerLineRelDomain = new TransformerLineRelDomain();
		transformerLineRelDomain.setTransformIds(transformerIds);
		List<TransformerLineRelDomain> transformerLineRelDomains = cimService
				.findRelByTranformIds(transformerLineRelDomain);

		Map<Long, TransformerLineRelDomain> transformerLineRelDomainMap = transformerLineRelDomains.stream()
				.collect(Collectors.toMap(TransformerLineRelDomain::getTransformerId, a -> a, (k1, k2) -> k1));

		List<FeeRecStatisticsBean> resultList = new ArrayList();
		FeeRecStatisticsBean noTransBean = new FeeRecStatisticsBean();
		noTransBean.initFeeRecStatisticsBean(noTransBean);
		FeeRecStatisticsBean gbBean = new FeeRecStatisticsBean();
		gbBean.initFeeRecStatisticsBean(gbBean);
		FeeRecStatisticsBean zbBean = new FeeRecStatisticsBean();
		zbBean.initFeeRecStatisticsBean(zbBean);
		FeeRecStatisticsBean totalBean = new FeeRecStatisticsBean();
		totalBean.initFeeRecStatisticsBean(totalBean);
		// 按变压器遍历
		for (Map.Entry<Long, List<MeterMoneyDomain>> entryDomain : transformerListMap.entrySet()) {
			Long key = entryDomain.getKey();
			List<MeterMoneyDomain> entryDomainList = entryDomain.getValue();
			entryDomainList = entryDomainList.stream().sorted(
					Comparator.comparing(MeterMoneyDomain::getCapacity, Comparator.nullsLast(BigDecimal::compareTo)))
					.collect(Collectors.toList());
			int count = 1;
			FeeRecStatisticsBean feeRecStatisticsBean = new FeeRecStatisticsBean();
			feeRecStatisticsBean.initFeeRecStatisticsBean(feeRecStatisticsBean);
			// 遍历某变压器下所有计量点
			for (MeterMoneyDomain moneyDomain : entryDomainList) {
				if (moneyDomain.getTransFormerId() != 0) {
					assignment(transformerLineRelDomainMap, feeRecStatisticsBean, moneyDomain, count);
					//专用变标志为null的暂定为公变合计
					if (null != moneyDomain.getIsPubType() && 1 != moneyDomain.getIsPubType()) {
						assignment(transformerLineRelDomainMap, zbBean, moneyDomain, count);
					} else {
						assignment(transformerLineRelDomainMap, gbBean, moneyDomain, count);

					}
				} else {
					// System.out.println("meterId============"+moneyDomain
					// .getMeterId());
					assignment(transformerLineRelDomainMap, noTransBean, moneyDomain, count);
				}
				assignment(transformerLineRelDomainMap, totalBean, moneyDomain, count);
				count++;
			}

			// 列表中不显示空台区
			if (feeRecStatisticsBean.getTransFormerId() != null && feeRecStatisticsBean.getTransFormerId() != 0) {
				// 没维护考核电价时供电量=售电量
				if (feeRecStatisticsBean.getPowerSupply().compareTo(BigDecimal.ZERO) == 0) {
					feeRecStatisticsBean.setPowerSupply(feeRecStatisticsBean.getElectricitySales());
				}
				resultList.add(feeRecStatisticsBean);
			}

		}

		// 上一步才完成所有台区的供电量数据，所以在这里进行供电量汇总
		gbBean.setPowerSupply(BigDecimal.ZERO);
		zbBean.setPowerSupply(BigDecimal.ZERO);
		totalBean.setPowerSupply(BigDecimal.ZERO);
		resultList.forEach(r ->

		{

			if (1 == r.getIsPubType()) {
				gbBean.setPowerSupply(gbBean.getPowerSupply().add(r.getPowerSupply()));
			} else {
				zbBean.setPowerSupply(zbBean.getPowerSupply().add(r.getPowerSupply()));
			}
			totalBean.setPowerSupply(totalBean.getPowerSupply().add(r.getPowerSupply()));
		});

		resultList = resultList.stream()
				.sorted(Comparator.comparing(FeeRecStatisticsBean::getLineId, Comparator.nullsFirst(Long::compareTo))
						.thenComparing(FeeRecStatisticsBean::getIsPubType, Comparator.nullsFirst(Byte::compareTo)))
				.collect(Collectors.toList());
		// 无台区客户供电量直接取售电量
		noTransBean.setPowerSupply(noTransBean.getElectricitySales());
		noTransBean.setTransformerName("无台区客户");
		if (noTransBean.getPowerSupply().intValue() != 0) {
			noTransBean.setLineLossRate((noTransBean.getPowerSupply().subtract(noTransBean.getElectricitySales()))
					.divide(noTransBean.getPowerSupply(), 4, BigDecimal.ROUND_HALF_UP));
		}
		if (noTransBean.getReceivable().intValue() != 0) {

			noTransBean.setRecoveryRate(
					noTransBean.getFactMoney().divide(noTransBean.getReceivable(), 4, BigDecimal.ROUND_HALF_UP));
		}
		resultList.add(noTransBean);
		gbBean.setTransformerName("公变合计");
		if (gbBean.getPowerSupply().intValue() != 0) {
			gbBean.setLineLossRate((gbBean.getPowerSupply().subtract(gbBean.getElectricitySales()))
					.divide(gbBean.getPowerSupply(), 4, BigDecimal.ROUND_HALF_UP));
		}
		if (gbBean.getReceivable().intValue() != 0) {

			gbBean.setRecoveryRate(gbBean.getFactMoney().divide(gbBean.getReceivable(), 4, BigDecimal.ROUND_HALF_UP));
		}
		resultList.add(gbBean);
		zbBean.setTransformerName("专变合计");
		if (zbBean.getPowerSupply().intValue() != 0) {
			zbBean.setLineLossRate((zbBean.getPowerSupply().subtract(zbBean.getElectricitySales()))
					.divide(zbBean.getPowerSupply(), 4, BigDecimal.ROUND_HALF_UP));
		}
		if (zbBean.getReceivable().intValue() != 0) {

			zbBean.setRecoveryRate(zbBean.getFactMoney().divide(zbBean.getReceivable(), 4, BigDecimal.ROUND_HALF_UP));
		}
		resultList.add(zbBean);
		totalBean.setTransformerName("总合计");
		if (totalBean.getPowerSupply().intValue() != 0) {
			totalBean.setLineLossRate((totalBean.getPowerSupply().subtract(totalBean.getElectricitySales()))
					.divide(totalBean.getPowerSupply(), 4, BigDecimal.ROUND_HALF_UP));
		}
		if (totalBean.getReceivable().intValue() != 0) {

			totalBean.setRecoveryRate(
					totalBean.getFactMoney().divide(totalBean.getReceivable(), 4, BigDecimal.ROUND_HALF_UP));
		}
		resultList.add(totalBean);

		List<TableDataBean> tableDataList = new ArrayList<>();
		tableData.setTableData(new JRBeanCollectionDataSource(resultList));
		tableData.setMon(transformer.getMon());

		tableDataList.add(tableData);
		return tableDataList;
	}

	public void assignment(Map<Long, TransformerLineRelDomain> transformerLineRelDomainMap,
			FeeRecStatisticsBean feeRecStatisticsBean, MeterMoneyDomain moneyDomain, int count) {
		// 展示
		TransformerLineRelDomain transformerLineRelDomain = transformerLineRelDomainMap
				.get(moneyDomain.getTransFormerId());
		if (transformerLineRelDomain != null) {
			feeRecStatisticsBean.setLineId(transformerLineRelDomain.getLineId());
			if (moneyDomain.getTransformerName() == null) {
				logger.info("无变压器名称计量点========" + moneyDomain.getMeterId());
			}
			feeRecStatisticsBean.setTransformerName(
					moneyDomain.getTransformerName() == null ? "无变压器名称/" + transformerLineRelDomain.getLineName()
							: moneyDomain.getTransformerName() + "/" + transformerLineRelDomain.getLineName());
		} else {
			feeRecStatisticsBean.setTransformerName(
					moneyDomain.getTransformerName() == null ? "" : moneyDomain.getTransformerName());
		}
		if (count == 1) {
			feeRecStatisticsBean.setCapacity(moneyDomain.getCapacity() == null ? BigDecimal.ZERO
					: feeRecStatisticsBean.getCapacity().add(moneyDomain.getCapacity()));
		}
		if (moneyDomain.getPriceTypeId() == 101) {
			feeRecStatisticsBean.setPowerSupply(moneyDomain.getTransformerName() == null ? BigDecimal.ZERO
					: feeRecStatisticsBean.getPowerSupply().add(moneyDomain.getTotalPower()));
		} else {
			feeRecStatisticsBean
					.setElectricitySales(feeRecStatisticsBean.getElectricitySales() == null ? BigDecimal.ZERO
							: feeRecStatisticsBean.getElectricitySales().add(moneyDomain.getTotalPower()));
		}
		feeRecStatisticsBean.setReceivable(feeRecStatisticsBean.getReceivable() == null ? BigDecimal.ZERO
				: feeRecStatisticsBean.getReceivable().add(moneyDomain.getAmount()));
		feeRecStatisticsBean.setFactMoney(feeRecStatisticsBean.getFactMoney() == null ? BigDecimal.ZERO
				: feeRecStatisticsBean.getFactMoney().add(moneyDomain.getFactMoney()));
		feeRecStatisticsBean.setAddMoney1(feeRecStatisticsBean.getAddMoney1() == null ? BigDecimal.ZERO
				: feeRecStatisticsBean.getAddMoney1()
						.add(moneyDomain.getSurchargeDetail()
								.get(moneyDomain.getPriceTypeId() + "#" + "2" + "#" + "0") == null ? BigDecimal.ZERO
										: moneyDomain.getSurchargeDetail()
												.get(moneyDomain.getPriceTypeId() + "#" + "2" + "#" + "0")));
		feeRecStatisticsBean.setAddMoney2(feeRecStatisticsBean.getAddMoney2() == null ? BigDecimal.ZERO
				: feeRecStatisticsBean.getAddMoney2()
						.add(moneyDomain.getSurchargeDetail()
								.get(moneyDomain.getPriceTypeId() + "#" + "3" + "#" + "0") == null ? BigDecimal.ZERO
										: moneyDomain.getSurchargeDetail()
												.get(moneyDomain.getPriceTypeId() + "#" + "3" + "#" + "0")));
		feeRecStatisticsBean.setAddMoney3(feeRecStatisticsBean.getAddMoney3() == null ? BigDecimal.ZERO
				: feeRecStatisticsBean.getAddMoney3()
						.add(moneyDomain.getSurchargeDetail()
								.get(moneyDomain.getPriceTypeId() + "#" + "4" + "#" + "0") == null ? BigDecimal.ZERO
										: moneyDomain.getSurchargeDetail()
												.get(moneyDomain.getPriceTypeId() + "#" + "4" + "#" + "0")));
		feeRecStatisticsBean.setAddMoney4(feeRecStatisticsBean.getAddMoney4() == null ? BigDecimal.ZERO
				: feeRecStatisticsBean.getAddMoney4()
						.add(moneyDomain.getSurchargeDetail()
								.get(moneyDomain.getPriceTypeId() + "#" + "5" + "#" + "0") == null ? BigDecimal.ZERO
										: moneyDomain.getSurchargeDetail()
												.get(moneyDomain.getPriceTypeId() + "#" + "5" + "#" + "0")));
		feeRecStatisticsBean.setAddMoney5(feeRecStatisticsBean.getAddMoney5() == null ? BigDecimal.ZERO
				: feeRecStatisticsBean.getAddMoney5()
						.add(moneyDomain.getSurchargeDetail()
								.get(moneyDomain.getPriceTypeId() + "#" + "6" + "#" + "0") == null ? BigDecimal.ZERO
										: moneyDomain.getSurchargeDetail()
												.get(moneyDomain.getPriceTypeId() + "#" + "6" + "#" + "0")));
		feeRecStatisticsBean.setAddMoney6(feeRecStatisticsBean.getAddMoney6() == null ? BigDecimal.ZERO
				: feeRecStatisticsBean.getAddMoney6()
						.add(moneyDomain.getSurchargeDetail()
								.get(moneyDomain.getPriceTypeId() + "#" + "7" + "#" + "0") == null ? BigDecimal.ZERO
										: moneyDomain.getSurchargeDetail()
												.get(moneyDomain.getPriceTypeId() + "#" + "7" + "#" + "0")));
		feeRecStatisticsBean.setAddMoney7(feeRecStatisticsBean.getAddMoney7() == null ? BigDecimal.ZERO
				: feeRecStatisticsBean.getAddMoney7()
						.add(moneyDomain.getSurchargeDetail()
								.get(moneyDomain.getPriceTypeId() + "#" + "8" + "#" + "0") == null ? BigDecimal.ZERO
										: moneyDomain.getSurchargeDetail()
												.get(moneyDomain.getPriceTypeId() + "#" + "8" + "#" + "0")));
		feeRecStatisticsBean.setAddMoney8(feeRecStatisticsBean.getAddMoney8() == null ? BigDecimal.ZERO
				: feeRecStatisticsBean.getAddMoney8()
						.add(moneyDomain.getSurchargeDetail()
								.get(moneyDomain.getPriceTypeId() + "#" + "9" + "#" + "0") == null ? BigDecimal.ZERO
										: moneyDomain.getSurchargeDetail()
												.get(moneyDomain.getPriceTypeId() + "#" + "9" + "#" + "0")));

		feeRecStatisticsBean.setVolumeCharge(feeRecStatisticsBean.getVolumeCharge() == null ? BigDecimal.ZERO
				: feeRecStatisticsBean.getVolumeCharge().add(moneyDomain.getVolumeCharge()));
		feeRecStatisticsBean.setFactPunish(feeRecStatisticsBean.getFactPunish() == null ? BigDecimal.ZERO
				: feeRecStatisticsBean.getFactPunish().add(moneyDomain.getFactPunish()));
		if (feeRecStatisticsBean.getPowerSupply().intValue() != 0) {
			feeRecStatisticsBean.setLineLossRate(
					(feeRecStatisticsBean.getPowerSupply().subtract(feeRecStatisticsBean.getElectricitySales()))
							.divide(feeRecStatisticsBean.getPowerSupply(), 4, BigDecimal.ROUND_HALF_UP));
		}
		if (feeRecStatisticsBean.getReceivable().intValue() != 0) {

			feeRecStatisticsBean.setRecoveryRate(feeRecStatisticsBean.getFactMoney()
					.divide(feeRecStatisticsBean.getReceivable(), 4, BigDecimal.ROUND_HALF_UP));
		}

		// 不展示
		feeRecStatisticsBean.setIsPubType(moneyDomain.getIsPubType());
		feeRecStatisticsBean.setTransFormerId(moneyDomain.getTransFormerId());
	}

}
