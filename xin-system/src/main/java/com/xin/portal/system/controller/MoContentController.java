package com.xin.portal.system.controller;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.xin.portal.system.bean.BaseApi;
import com.xin.portal.system.bean.BaseController;
import com.xin.portal.system.config.sysLog.LogType;
import com.xin.portal.system.config.sysLog.SystemLog;
import com.xin.portal.system.model.*;
import com.xin.portal.system.service.*;
import com.xin.portal.system.util.Constant.ConfigKeys;
import com.xin.portal.system.util.SessionUtil;
import com.xin.portal.system.util.i18n.LanguageParam;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.*;

/**  
* @Title: com.xin.portal.system.controller.MoContentController 
* @Description: 模板管理
* @author xue  
* @date 2018-11-08
* @version V1.0  
*/ 
@Controller
@RequestMapping("/moContent")
public class MoContentController extends BaseController {

	private static final String PATH = "moContent/";
	
	@Autowired
	private MoContentService service;
	@Autowired
	private BannerService bannerService;
	@Autowired
	private MenuService menuService;
	@Autowired
	private ConfigService configService;
	@Autowired
	private ResourceService resourceService;
	@Autowired
	private ResourceTypeService resourceTypeService;
	@Autowired
	private ResourceLogService resourceLogService;
	@Autowired
	private NoticeService noticeService;
	@Autowired
	private MessageCenterService messageCenterService;
	@RequestMapping(value="/index",method=RequestMethod.GET)
	public String index(){
		return PATH + "index";
	}
	@SystemLog(module=LanguageParam.TEMPLATE_MANAGEMENT ,operation=LanguageParam.ACTIONADD,type=LogType.add)
	@RequestMapping(value="/add/{moduleId}",method=RequestMethod.GET)
	public String add(@PathVariable String moduleId,Model model){
		model.addAttribute("moduleId", moduleId);
		Wrapper wrapper=new EntityWrapper<>();
		List resourceType=resourceTypeService.selectList(wrapper);
		model.addAttribute("resourceType", resourceType);
		return PATH + "add";
	}
	@SystemLog(module=LanguageParam.TEMPLATE_MANAGEMENT ,operation=LanguageParam.ACTIONUPDATE,type=LogType.update)
	@RequestMapping(value="/edit/{id}",method=RequestMethod.GET)
	public String edit(@PathVariable String id,Model model){
		MoContent moContent=new MoContent();
		moContent.setId(id);
		model.addAttribute("moContent", moContent.selectById());
		return PATH + "edit";
	}
	
	/**
	 * @Title: page 
	 * @Description:  分页查询
	 * @param query
	 * @param pageNumber 页数
	 * @param pageSize 每页条数
	 * @return BaseApi
	 * @author xue
	 * @Date 2018-11-08 
	 */
	@SystemLog(module=LanguageParam.TEMPLATE_MANAGEMENT ,operation=LanguageParam.PAGING_QUERY, type=LogType.query)
	@GetMapping("/page")
	@ResponseBody
	public BaseApi page(MoContent query, Integer pageNumber, Integer pageSize){
		return BaseApi.page(service.page(query,pageNumber,pageSize));
	}
	
	/**
	 * @Title: list 
	 * @Description:  列表查询
	 * @param MoContent
	 * @return BaseApi
	 * @author xue
	 * @Date 2018-11-08 
	 */
	@SystemLog(module=LanguageParam.TEMPLATE_MANAGEMENT ,operation=LanguageParam.ACTIONLISTSEE,type=LogType.query)
	@GetMapping("/list")
	@ResponseBody
	public BaseApi list(MoContent query){
		EntityWrapper<MoContent> warpper = new EntityWrapper<MoContent>(query);
		return BaseApi.list(service.selectList(warpper));
	}
	/**
	 * @Title: list 
	 * @Description:  列表查询
	 * @param MoContent
	 * @return BaseApi
	 * @author xue
	 * @Date 2018-11-08 
	 */
	@SystemLog(module=LanguageParam.TEMPLATE_MANAGEMENT ,operation=LanguageParam.ACTIONLISTSEE,type=LogType.query)
	@GetMapping("/listByModuleId/{moduleId}")
	@ResponseBody
	public BaseApi listByModuleId(@PathVariable String moduleId,MoContent query){
		return BaseApi.list(service.selectByModuleId(moduleId));
	}
	
	/**
	 * @Title: info 
	 * @Description:  按id查询
	 * @param MoContent
	 * @return BaseApi
	 * @author xue
	 * @Date 2018-07-04 
	 */
	@SystemLog(module=LanguageParam.TEMPLATE_MANAGEMENT ,operation=LanguageParam.DETAILSQUERY,type=LogType.query)
	@GetMapping("/info/{id}")
	@ResponseBody
	public BaseApi info(@PathVariable String id){
		MoContent result = service.selectById(id);
		return result != null ? BaseApi.data(result) : BaseApi.error();
	}
	
	
	/**
	 * @Title: save 
	 * @Description:  保存
	 * @param query
	 * @return BaseApi
	 * @author xue
	 * @Date 2018-11-08 
	 */
	@SystemLog(module=LanguageParam.TEMPLATE_MANAGEMENT ,operation=LanguageParam.ADD_AND_SAVE,type=LogType.add)
	@PostMapping("/save")
	@ResponseBody
	public BaseApi save(MoContent record){
		//sort排序，从1开始，每次去查，如果有，取max+1，如果没有，赋值1，如果模板被删除一个，不影响，不做处理
		int count=0;
		ModuleContent a=new ModuleContent();
		Wrapper<ModuleContent> wrapper=new EntityWrapper<ModuleContent>();
		wrapper.eq("module_id", record.getModuleId());
		count=a.selectCount(wrapper);
		if(count==0){
			record.setSort(1);
		}else{
			int b=service.selectMaxSort(record.getModuleId())+1;
			record.setSort(b);
		}
		/*MoContent query=new MoContent();
		query=query.sel*/
		boolean result=record.insert();
		ModuleContent moduleContent=new ModuleContent();
		moduleContent.setContentId(record.getId());
		moduleContent.setModuleId(record.getModuleId());
		result=moduleContent.insert();
		return result ? BaseApi.success() : BaseApi.error();
	}
	
	/**
	 * @Title: update 
	 * @Description:  更新
	 * @param query
	 * @return BaseApi
	 * @author xue
	 * @Date 2018-11-08 
	 */
	@SystemLog(module=LanguageParam.TEMPLATE_MANAGEMENT ,operation=LanguageParam.ACTION_RENEWAL,type=LogType.update)
	@RequestMapping(value="/update",method=RequestMethod.POST)
	@ResponseBody
	public BaseApi update(MoContent record){

		return service.update(record) ? BaseApi.success() : BaseApi.error();
	}
	
	
	/**
	 * @Title: delete 
	 * @Description:  删除
	 * @param id
	 * @return BaseApi
	 * @author xue
	 * @Date 2017-7-25 下午3:47:04
	 */
	@SystemLog(module=LanguageParam.TEMPLATE_MANAGEMENT ,operation=LanguageParam.ACTIONDELETE, type=LogType.delete)
	@RequestMapping(value="/delete/{id}",method=RequestMethod.DELETE)
	@ResponseBody
	public BaseApi delete(@PathVariable String id){
		ModuleContent moduleContent=new ModuleContent();
		moduleContent.setContentId(id);
		EntityWrapper<ModuleContent> warpper = new EntityWrapper<ModuleContent>(moduleContent);
		moduleContent.delete(warpper);
		return service.deleteById(id) ? BaseApi.success() : BaseApi.error();
	}
	@SystemLog(module=LanguageParam.TEMPLATE_MANAGEMENT ,operation=LanguageParam.NOTICE_TEMPLATE ,type=LogType.query)
	@RequestMapping(value="/content_notice/{id}",method=RequestMethod.GET)
	public String content_notice(@PathVariable String id,Model model){
		MoContent query=new MoContent();
		query.setId(id);
		MoContent moContent=query.selectById(id);
		Notice notice=new Notice();
    	notice.setState(1);
    	notice.setNowDate(new Date());
    	notice.setTopNum(moContent.getTopNum()==null?10:moContent.getTopNum());
		model.addAttribute("notice", noticeService.findListByNum(notice));
		return PATH + "content_notice";
	}
	@SystemLog(module=LanguageParam.TEMPLATE_MANAGEMENT ,operation=LanguageParam.HTML_TEMPLATE , type=LogType.query)
	@RequestMapping(value="/content_html/{id}",method=RequestMethod.GET)
	public String content_html(@PathVariable String id,Model model){
		MoContent moContent=new MoContent();
		model.addAttribute("content", moContent.selectById(id));
		return PATH + "content_html";
	}
	@SystemLog(module=LanguageParam.TEMPLATE_MANAGEMENT ,operation=LanguageParam.MORE_NOTICE , type=LogType.query)
	@RequestMapping(value="/content_notice_more/{id}",method=RequestMethod.GET)
	public String content_notice_more(@PathVariable String id,Model model){
		/*Notice notice=new Notice();
    	notice.setState(1);
    	notice.setPageNumber(0);
    	notice.setPageSize(999);
    	notice.setNowDate(new Date());*/
		Map map = noticeService.findList();
		List<Notice> readNotices= (List<Notice>)map.get("readList");
		List<Notice> unreadNotices = (List<Notice>)map.get("unreadList");
		model.addAttribute("notice", readNotices);
		model.addAttribute("unnotice", unreadNotices);


		return PATH + "content_notice_more";
	}
	@SystemLog(module=LanguageParam.TEMPLATE_MANAGEMENT ,operation=LanguageParam.MORE_MESSAGE, type=LogType.query)
	@RequestMapping(value="/content_message_more",method=RequestMethod.GET)
	public String content_message_more(Model model){
		/*EntityWrapper<MessageCenter> ewc = new EntityWrapper<>();
		ewc.eq("receive_user", SessionUtil.getUserId());
		ewc.orderBy("is_read", true);//按照是否已读字段排序是为了让未读消息显示在前面
		ewc.orderBy("create_time", false);
		List<MessageCenter> list = messageCenterService.selectList(ewc);
		model.addAttribute("messageCenter", list);*/
		Map<String, Object> unReadMessage = messageCenterService.findAllUnReadMessageMap();
		model.addAttribute("messageList",(List<MessageCenter>)unReadMessage.get("messageList"));
		return PATH + "content_message_more";
	}
	
	@SystemLog(module=LanguageParam.TEMPLATE_MANAGEMENT ,operation=LanguageParam.HTML_MORE_TEMPLATES,type=LogType.query)
	@RequestMapping(value="/content_html_more/{id}",method=RequestMethod.GET)
	public String content_html_more(@PathVariable String id,Model model){
		MoContent moContent=new MoContent();
		model.addAttribute("content", moContent.selectById(id));
		return PATH + "content_html_more";
	}
	@SystemLog(module=LanguageParam.TEMPLATE_MANAGEMENT ,operation=LanguageParam.SLIDESHOW_TEMPLATE,type=LogType.query)
	@RequestMapping(value="/content_banner/{id}",method=RequestMethod.GET)
	public String content_banner(@PathVariable String id,Model model){
		Banner banner=new Banner();
		List<Banner> bannerList=bannerService.findAll(banner);
		model.addAttribute("banner", bannerList);
		Config config = configService.findByCode(ConfigKeys.SYS_THEME, getTenantId());
		model.addAttribute("sys_theme", config.getValue());
		return PATH + "content_banner";
	}
	@SystemLog(module=LanguageParam.TEMPLATE_MANAGEMENT ,operation=LanguageParam.OBJECT_TEMPLATE,type=LogType.query)
	@RequestMapping(value="/content_resource/{id}",method=RequestMethod.GET)
	public String content_resource(@PathVariable String id,Model model){
		MoContent query=new MoContent();
		query.setId(id);
		MoContent moContent=query.selectById(id);
		Resource resource=resourceService.selectResource(moContent.getRelate(),SessionUtil.getUserId());
		model.addAttribute("resource", resource);
		model.addAttribute("showType", moContent.getShowType());
		model.addAttribute("panelId", id);
		return PATH + "content_resource";
	}
	@SystemLog(module=LanguageParam.TEMPLATE_MANAGEMENT ,operation=LanguageParam.DIRECTORY_TEMPLATE,type=LogType.query)
	@RequestMapping(value="/content_file/{id}",method=RequestMethod.GET)
	public String content_file(@PathVariable String id,Model model){
		String userId = SessionUtil.getUserId();
		MoContent query=new MoContent();
		query.setId(id);
		MoContent moContent=query.selectById(id);
		List<Menu> menulist=menuService.selectChildById(moContent.getRelate(),userId);
		int count;
		if(moContent.getxNum()!=null&&moContent.getyNum()!=null&&menulist.size()>0){
			count=moContent.getxNum()*moContent.getyNum();
			List<Menu> list=new ArrayList<Menu>();
			if(count<menulist.size()){
				//设置hiddenMore=0,并且拿出前count位数据，多余的暂时不显示，点击更多显示出来
				model.addAttribute("hiddenMore", 0);
				List<Menu> newList=menulist.subList(0, count);
				for(Menu list1:newList){
					list1.setxNum(12/moContent.getxNum());
					list.add(list1);
				}
				//设置moContent的HiddenMore字段
				query.setHiddenMore(0);
				query.updateById();
				model.addAttribute("showType", moContent.getShowType());
				model.addAttribute("file", list);
				
			}else{
				//设置hiddenMore=1
				model.addAttribute("hiddenMore", 1);
				for(Menu list2:menulist){
					list2.setxNum(12/moContent.getxNum());
					list.add(list2);
				}
				//设置moContent的HiddenMore字段
				query.setHiddenMore(1);
				query.updateById();
				model.addAttribute("showType", moContent.getShowType());
				model.addAttribute("file", list);
			}
		}
		model.addAttribute("panelId", id);
		return PATH + "content_file";
	}
	@SystemLog(module=LanguageParam.TEMPLATE_MANAGEMENT ,operation=LanguageParam.LIST_TEMPLATE,type=LogType.query)
	@RequestMapping(value="/content_list/{id}",method=RequestMethod.GET)
	public String content_list(@PathVariable String id,Model model){
		MoContent query=new MoContent();
		query.setId(id);
		MoContent moContent=query.selectById(id);
		System.out.println(moContent.getRelate());
		List<Resource> listResource=resourceService.selectListResource(moContent.getRelate(),SessionUtil.getUserId());
		int count;
		if(moContent.getxNum()!=null&&moContent.getyNum()!=null&&listResource.size()>0){
			count=moContent.getxNum()*moContent.getyNum();
			List<Resource> list=new ArrayList<Resource>();
			if(count<listResource.size()){
				//设置hiddenMore=0,并且拿出前count位数据，多余的暂时不显示，点击更多显示出来
				model.addAttribute("hiddenMore", 0);
				List<Resource> newList=listResource.subList(0, count);
				for(Resource list1:newList){
					list1.setxNum(12/moContent.getxNum());
					list.add(list1);
				}
				//设置moContent的HiddenMore字段
				query.setHiddenMore(0);
				model.addAttribute("hiddenMore", 0);
				query.updateById();
				model.addAttribute("showType", moContent.getShowType());
				model.addAttribute("file", list);
				
			}else{
				//设置hiddenMore=1
				model.addAttribute("hiddenMore", 1);
				for(Resource list2:listResource){
					list2.setxNum(12/moContent.getxNum());
					list.add(list2);
				}
				//设置moContent的HiddenMore字段
				query.setHiddenMore(1);
				query.updateById();
				model.addAttribute("showType", moContent.getShowType());
				model.addAttribute("file", list);
			}
		}
		model.addAttribute("panelId", id);
		return PATH + "content_list";
	}
	//@SystemLog(module=LanguageParam.TEMPLATE_MANAGEMENT ,operation="热点资源模板")
	@RequestMapping(value="/content_hot/{id}",method=RequestMethod.GET)
	public String content_hot(@PathVariable String id,Model model){
		MoContent query=new MoContent();
		query.setId(id);
		MoContent moContent=query.selectById(id);
		ResourceLog resourceLog=new ResourceLog();
		resourceLog.setResourceType(moContent.getResourceType());
		resourceLog.setTop(moContent.getTopNum());
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Calendar c = Calendar.getInstance();
		switch (moContent.getDateType()) {
		case 0://全部
			break;
		case 1://日
			resourceLog.setCreateTime(c.getTime());
			break;
		case 2://周
			resourceLog.setCreateTimeEnd(format.format(c.getTime()));
			c.add(Calendar.DATE, - 7);
			resourceLog.setCreateTimeStart(format.format(c.getTime()));
			break;
		case 3://月
			resourceLog.setCreateTimeEnd(format.format(c.getTime()));
			c.add(Calendar.DATE, - 30);
			resourceLog.setCreateTimeStart(format.format(c.getTime()));
			break;
		case 4://年
			resourceLog.setCreateTimeEnd(format.format(c.getTime()));
			c.add(Calendar.YEAR, - 1);
			resourceLog.setCreateTimeStart(format.format(c.getTime()));
			break;
		default:
			break;
		} 
		List<ResourceLog> a=resourceLogService.selectCountByType(resourceLog);
		model.addAttribute("resourceLog", a);
		//model.addAttribute("showType", moContent.getShowType());
		model.addAttribute("panelId", id);
		return PATH + "content_hot";
	}
	@RequestMapping(value="/content_timeline/{id}",method=RequestMethod.GET)
	public String content_timeline(@PathVariable String id,Model model){
		MoContent query=new MoContent();
		query.setId(id);
		MoContent moContent=query.selectById(id);
		//暂取前10条
		List<Resource> log=resourceService.selectOperationLog(moContent.getTopNum()==null?10:moContent.getTopNum());
		model.addAttribute("log", log);
		moContent.setxNum(12/moContent.getxNum());
		model.addAttribute("moContent", moContent);
		model.addAttribute("panelId", id);
		return PATH+"/content_timeline";
	}
	@SystemLog(module=LanguageParam.TEMPLATE_MANAGEMENT ,operation=LanguageParam.MORE_OBJECT_TEMPLATES,type=LogType.query)
	@RequestMapping(value="/content_file_more/{id}",method=RequestMethod.GET)
	public String content_file_more(@PathVariable String id,Model model){
		String userId = SessionUtil.getUserId();
		MoContent query=new MoContent();
		query.setId(id);
		MoContent moContent=query.selectById(id);
		List<Menu> menulist=menuService.selectChildById(moContent.getRelate(),userId);
		model.addAttribute("file", menulist);
		moContent.setxNum(12/moContent.getxNum());
		model.addAttribute("showType", moContent.getShowType());
		model.addAttribute("moContent", moContent);
		Config config = configService.findByCode(ConfigKeys.SYS_THEME, getTenantId());
		model.addAttribute("sys_theme", config.getValue());
		model.addAttribute("panelId", id);
		return PATH+"/content_file_more";
	}
	@SystemLog(module=LanguageParam.TEMPLATE_MANAGEMENT ,operation=LanguageParam.MORE_LIST_TEMPLATES,type=LogType.query)
	@RequestMapping(value="/content_list_more/{id}",method=RequestMethod.GET)
	public String content_list_more(@PathVariable String id,Model model){
		MoContent query=new MoContent();
		query.setId(id);
		MoContent moContent=query.selectById(id);
		List<Resource> listResource=resourceService.selectListResource(moContent.getRelate(),SessionUtil.getUserId());
		model.addAttribute("file", listResource);
		moContent.setxNum(12/moContent.getxNum());
		model.addAttribute("showType", moContent.getShowType());
		model.addAttribute("moContent", moContent);
		Config config = configService.findByCode(ConfigKeys.SYS_THEME, getTenantId());
		model.addAttribute("sys_theme", config.getValue());
		model.addAttribute("panelId", id);
		return PATH+"/content_list_more";
	}
	@SystemLog(module=LanguageParam.TEMPLATE_MANAGEMENT ,operation=LanguageParam.MORE_RECENT_NEWS_TEMPLATES,type=LogType.query)
	@RequestMapping(value="/content_timeline_more/{id}",method=RequestMethod.GET)
	public String content_timeline_more(@PathVariable String id,Model model){
		MoContent query=new MoContent();
		query.setId(id);
		MoContent moContent=query.selectById(id);
		//暂取前10条
		List<Resource> log=resourceService.selectOperationLog(100);
		model.addAttribute("log", log);
		moContent.setxNum(12/moContent.getxNum());
		model.addAttribute("moContent", moContent);
		model.addAttribute("panelId", id);
		return PATH+"/content_timeline_more";
	}
}

