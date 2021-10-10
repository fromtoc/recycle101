package com.xin.portal.system.controller;
import com.xin.portal.system.bean.PageModel;
import com.xin.portal.system.model.UserRecord;
import com.xin.portal.system.util.Series;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import com.xin.portal.system.model.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.xin.portal.system.bean.BaseApi;
import com.xin.portal.system.service.ResourceLogService;
import com.xin.portal.system.util.EchartData;
import com.xin.portal.system.util.LangTransform;
import com.xin.portal.system.util.SessionUtil;
import com.xin.portal.system.util.Constant.ConfigKeys;
import com.xin.portal.system.util.i18n.LanguageParam;
import com.xin.portal.system.model.ResourceLog;
import com.xin.portal.system.bean.BaseController;
import com.xin.portal.system.config.sysLog.LogType;
import com.xin.portal.system.config.sysLog.SystemLog;

/**  
* @Title: com.xin.portal.system.controller.ResourceLogController 
* @Description: 资源操作记录
* @author zhoujun  
* @date 2018-11-28
* @version V1.0  
*/ 
@Controller
@RequestMapping("/resourceLog")
public class ResourceLogController extends BaseController {

	private static final String PATH = "resourceLog/";

	private static final Integer NUM = 100;

	@Autowired
	private ResourceLogService service;

	@RequestMapping(value = "/index", method = RequestMethod.GET)
	public String index() {
		return PATH + "index";
	}

	@RequestMapping(value = "/paletteLog", method = RequestMethod.GET)
	public String paletteLog() {
		return PATH + "paletteLog";
	}

	@RequestMapping(value = "/add", method = RequestMethod.GET)
	public String add() {
		return PATH + "add";
	}

	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public String edit() {
		return PATH + "edit";
	}

	@RequestMapping(value = "/toIndex", method = RequestMethod.GET)
	public String toIndex() {
		return PATH + "index2";
	}

	@RequestMapping(value = "/resLogReport", method = RequestMethod.GET)
	public String resLogReport(Model model) {
		return PATH + "ResourceLogReport";
	}

	@RequestMapping(value = "/resLogReportInfo", method = RequestMethod.GET)
	public String resLogReportInfo(ResourceLog query, Model model) {
		model.addAttribute("record", query);
		return PATH + "resLogReportInfo";
	}

	@RequestMapping(value = "/resLogUserInfo", method = RequestMethod.GET)
	public String resLogUserInfo(ResourceLog query, Model model) {
		model.addAttribute("record", query);
		return PATH + "resLogUserInfo";
	}

	/**
	 * @param query
	 * @param pageNumber 页数
	 * @param pageSize   每页条数
	 * @return BaseApi
	 * @Title: page
	 * @Description: 分页查询
	 * @author zhoujun
	 * @Date 2018-11-28
	 */
	@SystemLog(module = LanguageParam.RESOURCE_OPERATION_RECORD , operation = LanguageParam.PAGING_QUERY , type = LogType.query)
	@GetMapping("/page")
	@ResponseBody
	public BaseApi page(ResourceLog query, Integer pageNumber, Integer pageSize) {
		return BaseApi.page(service.page(query, pageNumber, pageSize));
	}

	@SystemLog(module = LanguageParam.RESOURCE_OPERATION_RECORD , operation = LanguageParam.PAGING_QUERY , type = LogType.query)
	@RequestMapping("/paramPage")
	@ResponseBody
	public BaseApi paramPage(ResourceLog query) {
		return BaseApi.page(service.paramPage(query));
	}

	/**
	 * @param ResourceLog
	 * @return BaseApi
	 * @Title: list
	 * @Description: 列表查询
	 * @author zhoujun
	 * @Date 2018-11-28
	 */
	@SystemLog(module = LanguageParam.RESOURCE_OPERATION_RECORD , operation = LanguageParam.PAGING_QUERY ,type=LogType.query)
	@GetMapping("/list")
	@ResponseBody
	public BaseApi list(ResourceLog query) {
		EntityWrapper<ResourceLog> warpper = new EntityWrapper<ResourceLog>(query);
		return BaseApi.list(service.selectList(warpper));
	}

	/**
	 * @param ResourceLog
	 * @return BaseApi
	 * @Title: info
	 * @Description: 按id查询
	 * @author zhoujun
	 * @Date 2018-07-04
	 */
	@SystemLog(module = LanguageParam.RESOURCE_OPERATION_RECORD , operation = LanguageParam.DETAILSQUERY , type=LogType.query)
	@GetMapping("/info/{id}")
	@ResponseBody
	public BaseApi info(@PathVariable String id) {
		ResourceLog result = service.selectById(id);
		return result != null ? BaseApi.data(result) : BaseApi.error();
	}


	/**
	 * @param query
	 * @return BaseApi
	 * @Title: save
	 * @Description: 保存
	 * @author zhoujun
	 * @Date 2018-11-28
	 */
	@SystemLog(module = LanguageParam.RESOURCE_OPERATION_RECORD , operation = LanguageParam.ACTIONADD, type = LogType.add)
	@PostMapping("/save")
	@ResponseBody
	public BaseApi save(ResourceLog record) {
		return record.insert() ? BaseApi.success() : BaseApi.error();
	}

	/**
	 * @param query
	 * @return BaseApi
	 * @Title: update
	 * @Description: 更新
	 * @author zhoujun
	 * @Date 2018-11-28
	 */
	@SystemLog(module = LanguageParam.RESOURCE_OPERATION_RECORD , operation = LanguageParam.ACTIONUPDATE, type = LogType.update)
	@RequestMapping(value = "/update", method = RequestMethod.POST)
	@ResponseBody
	public BaseApi update(ResourceLog record) {
		return record.updateById() ? BaseApi.success() : BaseApi.error();
	}


	/**
	 * @param id
	 * @return BaseApi
	 * @Title: delete
	 * @Description: 删除
	 * @author zhoujun
	 * @Date 2017-7-25 下午3:47:04
	 */
	@SystemLog(module = LanguageParam.RESOURCE_OPERATION_RECORD , operation = LanguageParam.ACTIONDELETE, type = LogType.delete)
	@RequestMapping(value = "/delete/{id}", method = RequestMethod.DELETE)
	@ResponseBody
	public BaseApi delete(@PathVariable String id) {
		return service.deleteById(id) ? BaseApi.success() : BaseApi.error();
	}

	@RequestMapping(value = "/analysisResourceActivity")
	@ResponseBody
	public EchartData analysisResourceActivity(ResourceLog record) {
		EchartData data = service.queryResourceActivity(record);
		return data;
	}

	@RequestMapping(value = "/analysisUserActivity")
	@ResponseBody
	public EchartData analysisUserActivity(ResourceLog record) {
		EchartData data = service.queryUserActivity(record);
		return data;
	}

	@RequestMapping(value = "/historylog", method = RequestMethod.GET)
	public String historylog(Model model) {
		String userinfoId = SessionUtil.getUserId();
		List<Resource> log = service.selecthistorylog(userinfoId, NUM);
		model.addAttribute("log", log);
		return PATH + "/historylog";
	}


	/**
	 * 根据日期查询各资源点击量
	 * */
	@RequestMapping("resourceClick")
	@ResponseBody
	public EchartData resourceClick(@RequestBody ResourceLog resourceLog) {
		List<String> legend = new ArrayList<>();
		List<String> xAxis = new ArrayList<>();
		List<Series> series = new ArrayList<>();
		List<Integer> serisData = new ArrayList<Integer>();
		String createTimeStart = resourceLog.getCreateTimeStart();
		String createTimeEnd = resourceLog.getCreateTimeEnd() + "23:59:59";
		//System.out.println(createTimeEnd);
		List<ResourceLog> resourceLogs = service.resourceClick(createTimeStart, createTimeEnd);
		Collections.reverse(resourceLogs);
		for (ResourceLog r : resourceLogs) {
			xAxis.add(r.getCreaterName());
			serisData.add(r.getHits());
		}
		series.add(new Series(LangTransform.getLocaleLang(((Locale)SessionUtil.getSession(ConfigKeys.SYS_LOCALE)).toString(),LanguageParam.CHART), "radar", serisData));
		EchartData data = new EchartData(legend, xAxis, series);
		return data;
	}

	/**
	 * 根据资源名称查询访问该资源的用户数量
	 * */
	@RequestMapping("visitUser")
	@ResponseBody
	public EchartData visitUser(@RequestParam("resourName") String resourName, @RequestBody ResourceLog resourceLog) {
		List<String> legend = new ArrayList<>();
		List<String> xAxis = new ArrayList<>();
		List<Series> series = new ArrayList<>();
		List<Integer> serisData = new ArrayList<Integer>();
		String createTimeStart = resourceLog.getCreateTimeStart();
		String createTimeEnd = resourceLog.getCreateTimeEnd() + "23:59:59";
		List<ResourceLog> resourceLogs = service.visitUser(resourName, createTimeStart, createTimeEnd);
		for (ResourceLog u : resourceLogs) {
			xAxis.add(u.getCreaterName());
			serisData.add(u.getHits());
		}
		series.add(new Series(LangTransform.getLocaleLang(((Locale)SessionUtil.getSession(ConfigKeys.SYS_LOCALE)).toString(),LanguageParam.CHART), "radar", serisData));
		EchartData data = new EchartData(legend, xAxis, series);
		return data;
	}

	/**
	 * 根据资源名称查询访问该资源的日志记录
	 * */
     	@RequestMapping(value = "clickTable1")
        @ResponseBody
        public BaseApi clickTable1( ResourceLog resourceLog){
            String createTimeEnd=resourceLog.getCreateTimeEnd()+"23:59:59";
            resourceLog.setCreateTimeEnd(createTimeEnd);
            return BaseApi.page(service.clickTable1(resourceLog));

        }


        /**
		 * 根据时间和访问量查询非活跃资源的信息
		 * */
	@RequestMapping("InactiveResource")
	@ResponseBody
	public PageModel<ResourceLog> InactiveResource(ResourceLog resourceLog){
		return service.InactiveResource(resourceLog);
	}

  /**
   *活跃用户TO20
   * */
	@RequestMapping("activeUser")
	@ResponseBody
	public EchartData activeUser(@RequestBody ResourceLog resourceLog){
		List<String> legend = new ArrayList<>();
		List<String> yAxis =new ArrayList<>();
		List<Series> series=new ArrayList<>();
		List<Integer> serisData=new ArrayList<Integer>();
		String createTimeStart = resourceLog.getCreateTimeStart();
		String createTimeEnd = resourceLog.getCreateTimeEnd()+"23:59:59";
		List<ResourceLog>  resourceLogs =service.activeUser(createTimeStart,createTimeEnd);
		Collections.reverse(resourceLogs);
		for(ResourceLog r:resourceLogs){
			yAxis.add(r.getRdname());
			serisData.add(r.getHits());
		}
		series.add(new Series(LangTransform.getLocaleLang(((Locale)SessionUtil.getSession(ConfigKeys.SYS_LOCALE)).toString(),LanguageParam.CHART), "radar", serisData));
		EchartData data = new EchartData(legend, yAxis, series);
		return data;

	}

	/**
	 *点击用户名查询用户访问信息
	 * */
	@RequestMapping(value = "clickRosource")
	@ResponseBody
	public EchartData clickRosource(@RequestParam("rdname") String rdname,@RequestBody ResourceLog resourceLog){
		List<String> legend = new ArrayList<>();
		List<String> xAxis =new ArrayList<>();
		List<Series> series=new ArrayList<>();
		List<Integer> serisData=new ArrayList<Integer>();
		List<ResourceLog>  resourceLogs=service.clickRosource(rdname,resourceLog.getCreateTimeStart(),resourceLog.getCreateTimeEnd()+"23:59:59");
		for(ResourceLog r:resourceLogs){
			xAxis.add(r.getRdname());
			serisData.add(r.getHits());
		}
		series.add(new Series(LangTransform.getLocaleLang(((Locale)SessionUtil.getSession(ConfigKeys.SYS_LOCALE)).toString(),LanguageParam.CHART),"radar",serisData));
		EchartData data=new EchartData(legend,xAxis,series);
		return data;
	}
/**
 *根据用户名查询用户访问日志
 * */
	@RequestMapping(value = "clickTable")
	@ResponseBody
	public BaseApi clickTable( ResourceLog resourceLog){
		resourceLog.setCreateTimeEnd(resourceLog.getCreateTimeEnd()+"23:59:59");
		return BaseApi.page(service.clickTable(resourceLog));


	}

	/**
	 *根据时间与访问量查询休眠用户的信息
	 * */
	@RequestMapping("dormancyUser")
	@ResponseBody
	public PageModel<ResourceLog> dormancyUser(ResourceLog resourceLog){
		return service.dormancyUser(resourceLog);
	}

	/**
	 * 用户点击量按日PV统计
	 * */
	@RequestMapping(value = "dateStatistics")
	@ResponseBody
	public EchartData dateStatistics(@RequestBody ResourceLog resourceLog){
		List<String> legend = new ArrayList<>();
		List<String> xAxis =new ArrayList<>();
		List<Series> series=new ArrayList<>();
		List<Integer> serisData=new ArrayList<Integer>();
		String createTimeStart = resourceLog.getCreateTimeStart();
		String createTimeEnd = resourceLog.getCreateTimeEnd()+"23:59:59";
		List<ResourceLog> resourceLogs = service.dateRange(createTimeStart, createTimeEnd);

		for(ResourceLog u:resourceLogs){
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			String createdate = sdf.format(u.getCreateTime());
			xAxis.add(createdate);
			serisData.add(u.getHits());
		}
		series.add(new Series(LangTransform.getLocaleLang(((Locale)SessionUtil.getSession(ConfigKeys.SYS_LOCALE)).toString(),LanguageParam.CHART), "radar", serisData));
		EchartData data = new EchartData(legend, xAxis, series);
		return data;
	}

	/**
	 * 用户点击量按小时PV统计
	 * */
	@RequestMapping(value = "clickHour")
	@ResponseBody
	public EchartData clickHour(@RequestParam("timeHour") String timeHour){
		List<String> legend = new ArrayList<>();
		List<String> xAxis =new ArrayList<>();
		List<Series> series=new ArrayList<>();
		List<Integer> serisData=new ArrayList<Integer>();
		List<ResourceLog> resourceLogs= service.clickHour(timeHour);
		for(ResourceLog u:resourceLogs){
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH");
			String createdates = sdf.format(u.getCreateTime());
			String[] sp = createdates.split(" ");
			String createdate = sp[1];
			xAxis.add(createdate);
			serisData.add(u.getHits());
		}
		series.add(new Series(LangTransform.getLocaleLang(((Locale)SessionUtil.getSession(ConfigKeys.SYS_LOCALE)).toString(),LanguageParam.CHART), "radar", serisData));
		EchartData data = new EchartData(legend, xAxis, series);
		return data;

	}
/**
 *用户点击量按日UV统计
 * */
	@RequestMapping(value = "dateStatisticsUv")
	@ResponseBody
	public EchartData dateStatisticsUv(@RequestBody ResourceLog resourceLog){

		List<String> legend = new ArrayList<>();
		List<String> xAxis =new ArrayList<>();
		List<Series> series=new ArrayList<>();
		List<Integer> serisData=new ArrayList<Integer>();
		String createTimeStart = resourceLog.getCreateTimeStart();
		String createTimeEnd = resourceLog.getCreateTimeEnd()+"23:59:59";
		List<ResourceLog> resourceLogs = service.dateStatisticsUv(createTimeStart, createTimeEnd);
		for(ResourceLog u:resourceLogs){
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			String createdate = sdf.format(u.getCreateTime());
			xAxis.add(createdate);
			serisData.add(u.getHits());
		}
		series.add(new Series(LangTransform.getLocaleLang(((Locale)SessionUtil.getSession(ConfigKeys.SYS_LOCALE)).toString(),LanguageParam.CHART), "radar", serisData));
		EchartData data = new EchartData(legend, xAxis, series);
		return data;
	}
	/**
	 *用户点击量按小时UV统计
	 * */
	@RequestMapping(value = "clickHourUv")
	@ResponseBody
	public EchartData clickHourUv(@RequestParam("timeHour") String timeHour){
		List<String> legend = new ArrayList<>();
		List<String> xAxis =new ArrayList<>();
		List<Series> series=new ArrayList<>();
		List<Integer> serisData=new ArrayList<Integer>();
		List<ResourceLog> resourceLogs= service.clickHourUv(timeHour);
		for(ResourceLog u:resourceLogs){
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH");
			String createdates = sdf.format(u.getCreateTime());
			String[] sp = createdates.split(" ");
			String createdate = sp[1];
			xAxis.add(createdate);
			serisData.add(u.getHits());
		}
		series.add(new Series(LangTransform.getLocaleLang(((Locale)SessionUtil.getSession(ConfigKeys.SYS_LOCALE)).toString(),LanguageParam.CHART), "radar", serisData));
		EchartData data = new EchartData(legend, xAxis, series);
		return data;
	}

}
