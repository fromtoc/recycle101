package com.xin.portal.system.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.xin.portal.system.service.ConfigService;
import com.xin.portal.system.util.AdUtils.ADUtils;
import com.xin.portal.system.util.AdUtils.entity.AdDepartment;
import com.xin.portal.system.util.AdUtils.entity.AdEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.xin.portal.system.mapper.OrganizationMapper;
import com.xin.portal.system.model.Organization;
import com.xin.portal.system.service.OrganizationService;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class OrganizationServiceImpl  extends ServiceImpl<OrganizationMapper, Organization>  implements OrganizationService {

    @Autowired
    private OrganizationMapper mapper;

    @Autowired
    private ConfigService configService;


    public Organization selectByExtId(String extId,String tenantId){
        return mapper.selectByExtId(extId,tenantId);
    }

    /**
     * 导入/更新AD域内的组织架构
     * @param
     */
    @Transactional
    public void importOrgForAD(String tenantId){
        String DOMAIN_IP  = configService.findByCode("DOMAIN_IP",tenantId).getValue();
        String DOMAIN_PASSWORD  = configService.findByCode("DOMAIN_PASSWORD",tenantId).getValue();
        String DOMAIN_NAME  = configService.findByCode("DOMAIN_NAME",tenantId).getValue();
        String DOMAIN_ACCOUNT  = configService.findByCode("DOMAIN_ACCOUNT",tenantId).getValue();
        AdEntity adEntity = new AdEntity();
        adEntity.setUsername(DOMAIN_ACCOUNT);
        adEntity.setPassword(DOMAIN_PASSWORD);
        adEntity.setDomain(DOMAIN_NAME);
        adEntity.setLdapIP(DOMAIN_IP);
        adEntity.setLdapPORT("389");
        AdDepartment adDepartment = ADUtils.getAdDepartment(adEntity);
        Wrapper<Organization> wrapper = new EntityWrapper<>();
        wrapper.eq("status","2");
        Organization org = new Organization();
        List<Organization> orgList = org.selectList(wrapper);
        if(orgList.size()>0){
            //更新组织架构根节点
           /* org.setCode(adDepartment.getId());
            org.setExtId(adDepartment.getId());
            org.setExtCode(adDepartment.getId());
            org.setName(adDepartment.getName());
            org.setStatus(2);
            mapper.updateOrgForLdap(org);*/
            for(AdDepartment department:adDepartment.getChildren()){
                updateAdDepartment(department,adDepartment.getId());
            }
            //在数窗内删除在AD域内已经删掉的组织架构
            List<String> ids = ADUtils.getIdsByLdap(adEntity);//ad域内的所有组织id
            //删除不属于这些id的组织数据  not  in  ("ids");
            if(ids.size()>0){
                mapper.delByLdapIds(ids);
            }
        }else{
            org.setCode(adDepartment.getId());
            org.setExtId(adDepartment.getId());
            org.setExtCode(adDepartment.getId());
            org.setName(adDepartment.getName());
            org.setStatus(2);
            mapper.insertOrgForLdap(org);
            if(adDepartment.getChildren().size()>0){
                saveAdDepartmentList(adDepartment.getChildren(),org.getId());
            }
        }
    }

    /**
     * 此方法将AD域组织结构整体导入（用户初次开启“AD账户”的登录选项）
     * @param list
     * @param parentId
     */
    public void saveAdDepartmentList(List<AdDepartment> list, String parentId){
        for(AdDepartment department:list){
            Organization org=new Organization();
            org.setParentId(parentId);
            org.setCode(department.getId());
            org.setExtId(department.getId());
            org.setExtCode(department.getId());
            org.setName(department.getName());
            org.setStatus(2);
            mapper.insertOrgForLdap(org);
            if(department.getChildren().size()>0){
                saveAdDepartmentList(department.getChildren(),org.getId());
            }
        }
    }

    /**
     * 此方法根据当前AD用户的组织机构更新数窗的组织结构
     * @param adDepartment
     */
    public void updateAdDepartment(AdDepartment adDepartment,String parentExtId){
        Organization org = new Organization();
        Wrapper<Organization> parentEW = new EntityWrapper<>();
        parentEW.eq("ext_id",parentExtId);
        parentEW.eq("status","2");
        //通过父级的外部Id获取对应组织的数窗Id
        String parentId = org.selectOne(parentEW).getId();
        org.setName(adDepartment.getName());
        org.setParentId(parentId);
        org.setExtId(adDepartment.getId());
        //通过外部id获取对应的组织对象id
        Wrapper<Organization> childEW = new EntityWrapper<>();
        childEW.eq("ext_id",adDepartment.getId());
        childEW.eq("status","2");
        Organization orgSelf = org.selectOne(childEW);
        if(orgSelf == null){
            //更新AD域内新加入的组织
            saveAdDepartment(adDepartment,parentId);
        }else{
            mapper.updateOrgForLdap(org);
        }
        for(AdDepartment department:adDepartment.getChildren()){
            updateAdDepartment(department,adDepartment.getId());
        }
    }

    /**
     * 此方法添加AD域内单个组织架构的基础信息
     * @param adDepartment
     * @param parentId
     */
    public void saveAdDepartment(AdDepartment adDepartment, String parentId){
        Organization org=new Organization();
        org.setCode(adDepartment.getId());
        org.setExtId(adDepartment.getId());
        org.setExtCode(adDepartment.getId());
        org.setName(adDepartment.getName());
        org.setParentId(parentId);
        org.setStatus(2);
        mapper.insertOrgForLdap(org);
    }


}
