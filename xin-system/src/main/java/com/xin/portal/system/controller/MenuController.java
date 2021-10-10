package com.xin.portal.system.controller;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.xin.portal.system.bean.BaseApi;
import com.xin.portal.system.bean.BaseController;
import com.xin.portal.system.config.sysLog.LogType;
import com.xin.portal.system.config.sysLog.SystemLog;
import com.xin.portal.system.model.Config;
import com.xin.portal.system.model.Menu;
import com.xin.portal.system.model.Resource;
import com.xin.portal.system.model.UserSetting;
import com.xin.portal.system.service.ConfigService;
import com.xin.portal.system.service.MenuService;
import com.xin.portal.system.service.UserSettingService;
import com.xin.portal.system.util.Constant.ConfigKeys;
import com.xin.portal.system.util.SessionUtil;
import com.xin.portal.system.util.i18n.LanguageParam;

/**  
* @Title: com.xin.portal.system.controller.MenuController 
* @Description: 菜单管理
* @author zhoujun  
* @date 2018-10-31
* @version V1.0  
*/ 
@Controller
@RequestMapping("/menu")public class MenuController extends BaseController {

	private static final String PATH = "menu/";
	
	@Autowired
	private MenuService service;
	@Autowired
	private ConfigService configService;
	@Autowired
	private UserSettingService userSettingService;
	
	
	@RequestMapping(value="/index",method=RequestMethod.GET)
	public String index(){
		return PATH + "index";
	}
	
	@RequestMapping(value="/add",method=RequestMethod.GET)
	public String add(){
		return PATH + "add";
	}
	
	@RequestMapping(value="/edit",method=RequestMethod.GET)
	public String edit(){
		return PATH + "edit";
	}
	
	@RequestMapping(value="/info",method=RequestMethod.GET)
	public String info(){
		return PATH + "info";
	}
	
	@RequestMapping(value="/infomobile",method=RequestMethod.GET)
	public String infomobile(){
		return PATH + "infomobile";
	}
	
	@RequestMapping(value="/cardShow/{id}",method=RequestMethod.GET)
	public String cardShow(@PathVariable("id")String id, Model model){
		//根据id查询出有权限菜单的子节点
		List<Resource> resource = service.selectMenuResourceByPermission(SessionUtil.getUserId(),id);
		Config config = configService.findByCode(ConfigKeys.SYS_THEME, getTenantId());
		String sysTheme = config.getValue();
		//获取用户设置的主题样式
		UserSetting userSetting = userSettingService.selectByUserId(SessionUtil.getUserId());
		if(userSetting != null && userSetting.getNavigaStyle() != null && userSetting.getNavigaStyle().trim().length() > 0){
			sysTheme = userSetting.getNavigaStyle();
		}
		model.addAttribute("sys_theme", sysTheme);
		model.addAttribute("file", resource);
		return PATH + "card_show";
	}
	
	@SystemLog(module=LanguageParam.MENU_MANAGEMENT , operation=LanguageParam.ACTIVE_SEE, sort="tfPToROut1", 
			tfPToROut1="id,id,name,com.xin.portal.system.mapper.MenuMapper,selectById", type=LogType.query)
	@RequestMapping("/view/{id:.+}")
	public String view(@PathVariable("id")String id,Model model){
		Menu menu = service.selectById(id);
		if (menu==null || StringUtils.isEmpty(menu.getResourceId())) {
			return "resource/missing";
		}
		model.addAttribute("menuId", id);
		return "forward:/resource/view/"+menu.getResourceId();
	}
	@RequestMapping("/mobile/view/{id:.+}/{userId:.+}")
	public String mobileView(@PathVariable("id")String id,@PathVariable("userId")String userId,Model model, RedirectAttributes redirectAttributes){
		Menu menu = service.selectById(id);
		if (menu==null || StringUtils.isEmpty(menu.getResourceId())) {
			return "resource/missing";
		}
		model.addAttribute("menuId", id);
		//model.addAttribute("userId", request.getParameter("userId"));
		redirectAttributes.addFlashAttribute("userId",userId);
		return "redirect:/resource/mobile/view/"+menu.getResourceId();
	}
	@RequestMapping("/preferView/{id}")
	public String preferView(@PathVariable("id")String id,Model model){
		String userId = SessionUtil.getUserId();
		Menu menu = service.selectById(id);
		//通过父节查询到所有的子节点 卡片方式展示
		Menu record = new Menu();
		record.setParentId(id);
		List<Menu> list = service.getChildByUserId(record,userId);
		List<String> resourceList = new ArrayList<String>();
		for(Menu m :list){
			resourceList.add(m.getResourceId());
		}
		//判断是否存在该目录，以及该目录的子目录下是否有资源
		if (menu==null || resourceList.size()==0) {
			return "resource/missing";
		}

		Config config = configService.findByCode(ConfigKeys.SYS_THEME, getTenantId());
		model.addAttribute("sys_theme", config.getValue());
		model.addAttribute("menu", list);
		return PATH + "catalogue";
	}

	@SystemLog(module = LanguageParam.USERINFO_6, operation=LanguageParam.MODIFY_DIRECTORY, type=LogType.update)
	@RequestMapping("/selctMenu/{type}")
	public String selectMenu(@PathVariable("type")String type,Model model){
		model.addAttribute("type", type);
		return PATH + "selctMenu";
	}
	
	/**
	 * @Title: page 
	 * @Description:  分页查询
	 * @param query
	 * @param pageNumber 页数
	 * @param pageSize 每页条数
	 * @return BaseApi
	 * @author zhoujun
	 * @Date 2018-10-31 
	 */
	@SystemLog(module=LanguageParam.MENU_MANAGEMENT ,operation=LanguageParam.ACTIONLISTSEE,type=LogType.query)
	@GetMapping("/page")
	@ResponseBody
	public BaseApi page(Menu query, Integer pageNumber, Integer pageSize){
		return BaseApi.page(service.page(query,pageNumber,pageSize));
	}
	
	/**
	 * @Title: list 
	 * @Description:  列表查询
	 * @param Menu
	 * @return BaseApi
	 * @author zhoujun
	 * @Date 2018-10-31 
	 */
	@SystemLog(module=LanguageParam.MENU_MANAGEMENT ,operation=LanguageParam.ACTIONLISTSEE,type=LogType.query)
	@GetMapping("/list")
	@ResponseBody
	public List<Menu> list(Menu query){
		return service.findList(query);
	}
	
	/**
	 * @Title: info 
	 * @Description:  按id查询
	 * @param Menu
	 * @return BaseApi
	 * @author zhoujun
	 * @Date 2018-07-04 
	 */
	@SystemLog(module=LanguageParam.MENU_MANAGEMENT ,operation=LanguageParam.DETAILSQUERY,type=LogType.query)
	@GetMapping("/info/{id}")
	@ResponseBody
	public BaseApi info(@PathVariable String id){
		Menu result = service.selectById(id);
		return result != null ? BaseApi.data(result) : BaseApi.error();
	}
	
	
	/**
	 * @Title: save 
	 * @Description:  保存
	 * @param query
	 * @return BaseApi
	 * @author zhoujun
	 * @Date 2018-10-31 
	 */
	@SystemLog(module=LanguageParam.MENU_MANAGEMENT ,sort="name",name="name",operation=LanguageParam.ACTIONADD,type=LogType.add)
	@PostMapping("/save")
	@ResponseBody
	public BaseApi save(Menu record){
		return record.insert() ? BaseApi.success() : BaseApi.error();
	}
	
	@SystemLog(module=LanguageParam.MENU_MANAGEMENT ,operation=LanguageParam.ACTION_SAVE,sort="name",name="name",type=LogType.add)
	@PostMapping("/saveOrUpdate")
	@ResponseBody
	public BaseApi saveOrUpdate(Menu record){
		if (StringUtils.isEmpty(record.getParentId())) {
			record.setParentId("0");
		}
		//如果增加、编辑的节点是菜单则要查询父节点（除了0）的showStyle是否为 card，如果是修改为list。
		//如果增加、编辑的节点是资源菜单直接添加或者编辑
		if(record.getResourceId() == null || record.getResourceId().trim().length() == 0){//菜单
			//判断父节点showStyle是否为card，如果是就修改为list
			if(!"0".equals(record.getParentId())){
				Menu pmenu = service.selectById(record.getParentId());
				if(Menu.SHOWTYPE_CARD.equals(pmenu.getShowStyle())){//card 修改为list
					pmenu.setShowStyle(Menu.SHOWTYPE_LIST);
					pmenu.updateById();
				}
			}
		}
		boolean result = record.insertOrUpdate();
		return result ? BaseApi.data(record.selectById()) : BaseApi.error();
	}
	
	
	
	/**
	 * @Title: update 
	 * @Description:  更新
	 * @param query
	 * @return BaseApi
	 * @author zhoujun
	 * @Date 2018-10-31 
	 */
	@SystemLog(module=LanguageParam.MENU_MANAGEMENT ,operation=LanguageParam.ACTIONUPDATE,type=LogType.update)
	@RequestMapping(value="/update",method=RequestMethod.POST)
	@ResponseBody
	public BaseApi update(Menu record){
		return record.updateById() ? BaseApi.success() : BaseApi.error();
	}
	
	
	/**
	 * @Title: delete 
	 * @Description:  删除
	 * @param id
	 * @return BaseApi
	 * @author zhoujun
	 * @Date 2017-7-25 下午3:47:04
	 */
	@SystemLog(module=LanguageParam.MENU_MANAGEMENT ,operation=LanguageParam.ACTIONDELETE,sort="tfPToROut1",before="tfPToROut1",
			tfPToROut1="id,id,name,com.xin.portal.system.mapper.MenuMapper,selectById", type=LogType.delete)
	@RequestMapping(value="/delete/{id}",method=RequestMethod.DELETE)
	@ResponseBody
	public BaseApi delete(@PathVariable String id){
		return service.deleteById(id) ? BaseApi.success() : BaseApi.error();
	}
	
	@RequestMapping(value="/findMenu")
	@ResponseBody
	public List<Menu> findMenu(Menu record){//首选项查询菜单
		return service.findMenusByType(record);
	}
	
	@RequestMapping(value="/checkParent")
	@ResponseBody
	public List<Menu> checkParent(Menu record){//首选项查询菜单
		String userId = SessionUtil.getUserId();
		return service.getChildByUserId(record,userId);
	}
	
	@RequestMapping(value="/findList")
	@ResponseBody
	public List<Menu> findList(Menu menu){//获取有权限的menus
		List<Menu> list = service.findUserResourceMenus(SessionUtil.getUserId());
		return list;
	}
	
}

