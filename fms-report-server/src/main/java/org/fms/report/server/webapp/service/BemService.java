package org.fms.report.server.webapp.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.fms.report.common.webapp.bean.AppInfoQueryReturnBean;
import org.fms.report.common.webapp.bean.TableDataBean;
import org.fms.report.common.webapp.domain.AppInfoQueryEntity;
import org.fms.report.common.webapp.domain.DeptDomain;
import org.fms.report.common.webapp.domain.ParamDomain;
import org.fms.report.common.webapp.domain.UserDomain;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import com.fasterxml.jackson.core.type.TypeReference;
import com.riozenc.titanTool.annotation.TransactionService;
import com.riozenc.titanTool.common.json.utils.GsonUtils;
import com.riozenc.titanTool.spring.web.client.TitanTemplate;
import com.riozenc.titanTool.spring.web.http.HttpResult;

import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

@TransactionService
public class BemService {
    @Autowired
    private TitanTemplate titanTemplate;

    @Autowired
    private DeptService deptService;

    @Autowired
    private UserService userService;

    @Autowired
    private CimService cimService;


    //业扩信息查询报表
    public List<AppInfoQueryReturnBean> getAppInfoByParam(AppInfoQueryEntity appInfoQueryEntity) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        HttpResult<List<AppInfoQueryReturnBean>> httpResult=null;
        try {
            httpResult = titanTemplate.post("BEM-SERVER",
                    "bemServer/appBaseInfo/getAppInfoByParam",
                    httpHeaders,
                    GsonUtils.toJson(appInfoQueryEntity),
                    new TypeReference<HttpResult<List<AppInfoQueryReturnBean>>>() {
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
        return httpResult.getResultData();
    }



    //业扩信息查询报表
    public List<TableDataBean> appInfoQuery(AppInfoQueryEntity appInfoQueryEntity) {
        DeptDomain dept =
                deptService.getDept(appInfoQueryEntity.getBusinessPlaceCode());
        List<DeptDomain> deptList = new ArrayList<>();
        // 最上级
        if (dept.getParentId() != null && dept.getParentId() == Long.valueOf(0)) {
            ParamDomain paramDomain = new ParamDomain();
            // paramDomain.setPageSize(-1);
            deptList = deptService.findByWhere(paramDomain);
        } else {
            deptList = deptService.getDeptList(appInfoQueryEntity.getBusinessPlaceCode());
            deptList.add(dept);
        }
        List<Long> businessPlaceCodes = deptList.stream().map(DeptDomain::getId).collect(Collectors.toList());
        appInfoQueryEntity.setBusinessPlaceCodes(businessPlaceCodes);

        //获取营业区域
        if(appInfoQueryEntity.getBusinessPlaceCodes()!=null){
            appInfoQueryEntity.setBusinessPlaceCode(null);
        }
        //调用bem方法 查询基础数据
        List<AppInfoQueryReturnBean> appInfoQueryReturnEntities=
                getAppInfoByParam(appInfoQueryEntity);
        if(appInfoQueryReturnEntities==null || appInfoQueryReturnEntities.size()<1){
            return new ArrayList<>();
        }

        List<Long> finshMans=
                appInfoQueryReturnEntities.stream().filter(t->t.getFinishMan()!=null).map(AppInfoQueryReturnBean::getFinishMan).distinct().collect(Collectors.toList());

        Map<Long,String>finshManMap=new HashMap<>();

        if(finshMans!=null && finshMans.size()>0){

            finshManMap =
                    userService.findByIds(finshMans).stream().collect(Collectors.toMap(UserDomain::getId, k -> k.getUserName(),(k1,k2)->k1));
        }

        Map<Long, String> elecTypeMap =
                cimService.findSystemCommonConfigByType("ELEC_TYPE");

        Map<Long, String> templateMap =
                cimService.findSystemCommonConfigByType("TEMPLATE_TYPE");

        for(AppInfoQueryReturnBean t:appInfoQueryReturnEntities){
            t.setElecTypeName(elecTypeMap.get(t.getElecType()));
            t.setTemplateName(templateMap.get(t.getTemplateId()));
            if(finshManMap!=null){
                t.setFinishManName(finshManMap.get(t.getFinishMan()));
            }else{
                t.setFinishManName("");
            }

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            if(t.getApplyDate()!=null){
                t.setApplyDateString(sdf.format(t.getApplyDate()));
            }else{
                t.setApplyDateString("");
            }
            if(t.getFinishDate()!=null){
                t.setFinishDateString(sdf.format(t.getFinishDate()));
            }else{
                t.setFinishDateString("");
            }
        }


        List<TableDataBean> tableDataList = new ArrayList<>();
        TableDataBean tableData = new TableDataBean();
        tableData.setTableData(new JRBeanCollectionDataSource(appInfoQueryReturnEntities));
        tableDataList.add(tableData);
        return tableDataList;
    }
}
