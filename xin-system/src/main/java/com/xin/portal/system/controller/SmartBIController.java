/**  
* @Title: com.xin.portal.system.controller.MSTRController  
* @Description: TODO 
* @author dod123456  
* @date 2018年1月8日  
* @version V1.0  
*/  
package com.xin.portal.system.controller;


import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.microstrategy.webapi.EnumDSSXMLObjectTypes;
import com.xin.portal.system.bean.BaseController;
import com.xin.portal.system.model.*;
import com.xin.portal.system.service.*;
import com.xin.portal.system.util.Constant;
import com.xin.portal.system.util.DateUtil;
import com.xin.portal.system.util.SessionUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;


/**
 * @ClassPath: com.xin.portal.system.controller.MSTRController 
 * @Description: TODO
 * @author zhoujun
 * @date 2018年1月8日 下午2:10:06 
 */
@Controller
@RequestMapping("/smartBI")
public class SmartBIController extends BaseController{


	private static final Logger logger = LoggerFactory.getLogger(SmartBIController.class);

	private static final String DIR = "smartbi/";

	@Autowired
	private BiProjectService biProjectService;
	@Autowired
	private ConfigService configService;
	@Autowired
	private ResourceService resourceService;
	@Autowired
	private BiServerService biServerService;
	@Autowired
	private BiUserService biUserService;
	@Autowired
	private BiMappingService biMappingService;
	@Autowired
	private PromptRelService promptRelService;
    @Autowired
    private DictService dictService;
    @Autowired
    private OrganizationService organizationService;

	@RequestMapping("/view/{id}")
	public String view(@PathVariable("id")String resourceId,Model model){

        Resource resource = resourceService.selectById(resourceId);
        UserInfo userInfo = SessionUtil.getUserInfo();
        PromptRel query = new PromptRel();
        query.setResourceId(resource.getId());
        List<PromptRel> promptRelList = promptRelService.findList(query);


        //关于水印的部分
        BiServer  server  = biServerService.selectById(resource.getServerId());
        if(server!=null&&server.getWaterMark()==1){
            model.addAttribute("waterMarkText", server.getWatermarkText());
            model.addAttribute("waterMark", server.getWaterMark());
        }



        model.addAttribute("resource", resource);
        model.addAttribute("resourceId", resource.getId());
        promptRelList = DateUtil.timeFunction(promptRelList);
        promptRelList = setOrgValue(promptRelList);
        model.addAttribute("promptRelList",promptRelList);
        model.addAttribute("hasPrompt",promptRelList.size()>0?true:false);



        boolean showPrompt = false;
        StringBuilder promptContent = new StringBuilder();
        if (promptRelList!=null && promptRelList.size()>0) {
            for (PromptRel rec : promptRelList) {
                if (StringUtils.isNotEmpty(rec.getDefaultValue1())) {
                    promptContent.append(rec.getName()).append(":").append(rec.getDefaultValue1());
                }

                if (rec.getHidden().equals(0)) {
                    showPrompt = true;
                }

                if (rec.getType().equals(4)||rec.getType().equals(5)||rec.getType().equals(6)) {//下拉列表/单选框/复选框
                    if (StringUtils.isNotEmpty(rec.getDictCode())) {
                        List<Dict> items = dictService.listItem(new Dict(rec.getDictCode()));
                        rec.setItems(items);
                    } else if ("#branch".equals(rec.getSpecial())) {//分公司
                        Organization orgQuery = new Organization();
                        orgQuery.setCodeLike(userInfo.getOrgCode());
                        orgQuery.setLevel(userInfo.getOrgCode().length()+3);

                        EntityWrapper<Organization> ew = new EntityWrapper<Organization>(orgQuery);
                        List<Organization> list = organizationService.selectList(ew);
                        rec.setItems(list);
                        model.addAttribute("branchList", JSONArray.toJSONString(list));
                        model.addAttribute("branchCode", rec.getCode());
                    }  else if ("#business".equals(rec.getSpecial())) {//营业部
                        Organization orgQuery = new Organization();
                        orgQuery.setCodeLike(userInfo.getOrgCode());
                        orgQuery.setLevel(userInfo.getOrgCode().length()+6);
                        EntityWrapper<Organization> ew = new EntityWrapper<Organization>(orgQuery);
                        List<Organization> list = organizationService.selectList(ew);
                        for(Organization org : list) {
                            org.setExtCode(org.getExtCode().substring(1));
                        }
                        rec.setItems(list);
                        model.addAttribute("businessList", JSONArray.toJSONString(list));
                        model.addAttribute("businessCode", rec.getCode());
                    }
                }

            }
        }
        //提示是否显示
        model.addAttribute("showPrompt", showPrompt);
        model.addAttribute("promptContent",promptContent);
        //有默认值的第一次访问地址

        try {
			EntityWrapper<Resource> ew = new EntityWrapper<Resource>();
			ew.eq("id",resourceId);
			Resource resources = resourceService.selectOne(ew);
			String reportId = resources.getReportId();
			String serverId = resources.getServerId();
			String username = "";
			String password = "";
			if(serverId!=null && !"".equals(serverId)){
				EntityWrapper<BiServer> ewBiServer = new EntityWrapper<BiServer>();
				ewBiServer.eq("id",serverId);
				BiServer biServer = biServerService.selectOne(ewBiServer);
				BiUser biUser = biUserService.findBiUser(serverId,SessionUtil.getUserId());
				if(biUser == null){
					username = biServer.getDefaultUid();
					password = biServer.getDefaultPwd();
				}else{
					username = biUser.getUsername();
					password = biUser.getPassword();
				}


				/*String url  = biServer.getUrl()+"/vision/openresource.jsp?resid="+reportId+"&user="+username+"&password="+password+"&hidetoolbaritems=MYFAVORITE ";
				model.addAttribute("url",url);*/
				String url  = biServer.getUrl()+"/vision/openresource.jsp";
				model.addAttribute("url",url);
				String url2="resid="+reportId+"&user="+username+"&password="+password+"&hidetoolbaritems=MYFAVORITE ";
				model.addAttribute("url2",url2);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return DIR+"container";
	}
    private List<PromptRel> setOrgValue(List<PromptRel> list){
        for (PromptRel promptRel : list) {
            if(promptRel.getPromptType()!=null && "4".equals(promptRel.getPromptType())){//组织筛选
                //判断是否为隐藏，如果隐藏则通过查询code设置默认值，
                if(promptRel.getHidden()!=null && promptRel.getHidden()==1){//隐藏
                    //查询出用户code
                    Organization org = organizationService.selectById(SessionUtil.getUserInfo().getOrgId());
                    promptRel.setDefaultValue1(org.getExtCode());
                }else{//如果不是隐藏则使用用户设置的默认//如果没有默认值，判断是否必须，如过不必须则通过查询获得code
                    if(promptRel.getDefaultValue1() ==null || promptRel.getDefaultValue1().length()<=0){
                        if(promptRel.getRequired()==null || promptRel.getRequired()==0){
                            String orgCode = SessionUtil.getUserInfo().getExtCode();
                            promptRel.setDefaultValue1(orgCode);
                        }
                    }
                }
            }
        }
        return list;
    }


}
