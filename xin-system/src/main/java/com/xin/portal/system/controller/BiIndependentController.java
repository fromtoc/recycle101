package com.xin.portal.system.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.xin.portal.system.bean.BaseApi;
import com.xin.portal.system.bean.BaseController;
import com.xin.portal.system.config.sysLog.LogType;
import com.xin.portal.system.config.sysLog.SystemLog;
import com.xin.portal.system.model.BiIndependent;
import com.xin.portal.system.service.BiIndependentService;
import com.xin.portal.system.service.BiProjectService;
import com.xin.portal.system.service.BiServerService;
import com.xin.portal.system.service.BiUserService;
import com.xin.portal.system.util.i18n.LanguageParam;

/**
 * @Title: com.xin.portal.system.controller.BiIndependentController
 * @Description:
 * @author zhoujun
 * @date 2018-01-22
 * @version V1.0
 */
@Controller
@RequestMapping("/biIndependent")
public class BiIndependentController extends BaseController {

	private static final String DIR = "biIndepend/";

	private static final Logger logger = LoggerFactory.getLogger(BiIndependentController.class);
	
	@Autowired
	private BiIndependentService service;
	@Autowired
	private BiUserService biUserservice;
	@Autowired
	private BiServerService biServerService;
	@Value("${system.default_password:123456}")
	private String defaultPassword;
	@Autowired
	private BiProjectService biProjectService;

	/**
	 * @Title: index
	 * @Description: 访问页
	 * @return String
	 * @author zhoujun
	 * @Date 2018-01-22
	 */
	@RequestMapping("/index")
	public String index() {
		return DIR + "index";
	}

	/**
	 * @Title: add
	 * @Description: 添加页面
	 * @return String
	 * @author zhoujun
	 * @Date 2018-01-22
	 */
	@RequestMapping("/add")
	public String add() {
		return DIR + "add";
	}
	
	@SystemLog(module = LanguageParam.INDEPENDENTANALYSIS, type=LogType.query ,operation=LanguageParam.ACTIVE_SEE)
	@RequestMapping(value="/viewIndependAnalyze/{id}" ,method=RequestMethod.GET)
	public String viewIndepend(@PathVariable String id,Model model) {
		String url = service.getMstrUrlByPrjectId(id);
		model.addAttribute("userFolder", url);
		return "userReport/index2";
	}
	
	/**
	 * @Title: page
	 * @Description: TODO
	 * @param query
	 * @return PageModel<MstrUser>
	 * @author zhoujun
	 * @Date 2018-01-22
	 */
	@SystemLog(module = LanguageParam.INDEPENDENTANALYSIS, type = LogType.query,operation=LanguageParam.ACTIONLISTSEE)
	@RequestMapping("/page")
	@ResponseBody
	public BaseApi page(BiIndependent query) {
		return BaseApi.page(service.page(query));
	}

	/**
	 * @Title: select
	 * @Description: TODO
	 * @param id
	 * @param model
	 * @return String
	 * @author zhoujun
	 * @Date 2018-01-22
	 */
	@RequestMapping("/findList")
	@ResponseBody
	public List<BiIndependent> findList(BiIndependent query, Model model) {
		EntityWrapper<BiIndependent> warpper = new EntityWrapper<BiIndependent>(query);
		return service.selectList(warpper);
	}

	/**
	 * @Title: edit
	 * @Description: 编辑页面
	 * @param query
	 * @param model
	 * @return String
	 * @author zhoujun
	 * @Date 2018-01-22
	 */
	@RequestMapping("/edit")
	public String edit(BiIndependent query, Model model) {
		EntityWrapper<BiIndependent> warpper = new EntityWrapper<BiIndependent>(query);
		BiIndependent record = service.selectOne(warpper);
		model.addAttribute("record", record);
		return DIR + "edit";
	}

	/**
	 * @Title: save
	 * @Description: 保存
	 * @param query
	 * @return BaseApi
	 * @author zhoujun
	 * @Date 2018-01-22
	 */
	@SystemLog(module = LanguageParam.INDEPENDENTANALYSIS ,operation=LanguageParam.ACTIONADD,sort="name", name="biOwnFolderName", type = LogType.add)
	@RequestMapping("/save")
	@ResponseBody
	public BaseApi save(BiIndependent record) {
		int result = service.save(record);
		if(result==1){
			return BaseApi.success();
		}else if (result==-1){
			return new BaseApi().put("code",1);
		}else if (result==-2){
			return new BaseApi().put("code",2);
		}else if (result==-9){
			return new BaseApi().put("code",3);
		}else{
			return new BaseApi().put("code",4);
		}
	}

	/**
	 * @Title: update
	 * @Description: 更新
	 * @param query
	 * @return BaseApi
	 * @author zhoujun
	 * @Date 2018-01-22
	 */
	@SystemLog(module = LanguageParam.INDEPENDENTANALYSIS ,operation=LanguageParam.ACTIONUPDATE , sort="name", name="biOwnFolderName", type = LogType.update)
	@RequestMapping("/update")
	@ResponseBody
	public BaseApi update(BiIndependent record) {
		return record.updateById() ? BaseApi.success() : BaseApi.error();
	}
	
	/**
	 * @Title: delete
	 * @Description: 删除
	 * @param id
	 * @return BaseApi
	 * @author zhoujun
	 * @Date 2017-7-25 下午3:47:04
	 */
	@SystemLog(module = LanguageParam.INDEPENDENTANALYSIS ,operation=LanguageParam.ACTIONDELETE , sort="tfToName1", before="tfToName1", tfToName1="query,id,biOwnFolderName,com.xin.portal.system.mapper.BiIndependentMapper,selectById", type = LogType.delete)
	@RequestMapping("/delete")
	@ResponseBody
	public BaseApi delete(BiIndependent query) {
		return service.deleteById(query.getId())? BaseApi.success() : BaseApi.error();
	}
	
	@RequestMapping("/checkOpen")
	@ResponseBody
	public BaseApi checkOpen(BiIndependent query) {
		return service.checkOpen(query);
	}
	

}
