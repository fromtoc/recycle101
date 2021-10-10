package com.xin.portal.system.service;

import com.baomidou.mybatisplus.service.IService;
import com.xin.portal.system.bean.PageModel;
import com.xin.portal.system.model.Resource;
import com.xin.portal.system.model.ResourceLog;
import com.xin.portal.system.util.EchartData;

import java.util.List;

/**
 * 资源操作记录表 服务类
 *
 * @author zhoujun
 * @since 2018-11-28
 */
public interface ResourceLogService extends IService<ResourceLog> {
	/**
	 * @Title: page 
	 * @Description:  分页查询
	 * @param query
	 * @param pageNumber 页数
	 * @param pageSize 每页条数
	 * @return PageModel
	 * @author zhoujun
	 * @Date 2018-11-28 
	 */
	PageModel<ResourceLog> page(ResourceLog query, Integer pageNumber, Integer pageSize);

	/**
	 * @Title: save 
	 * @Description:  TODO
	 * @param resourceLog void
	 * @author zhoujun
	 * @Date 2018年12月18日 下午4:37:54
	 */
	public void save(ResourceLog resourceLog);

	PageModel<ResourceLog> paramPage(ResourceLog query);

	List<ResourceLog> selectCountByType(ResourceLog resourceLog);
	
	EchartData queryResourceActivity(ResourceLog record);

	EchartData queryUserActivity(ResourceLog record);
	
	List<Resource> selecthistorylog(String id ,int num);

    List<ResourceLog> findList(ResourceLog query);


    List<ResourceLog> resourceClick(String createTimeStart, String createTimeEnd);

    List<ResourceLog> visitUser(String resourName, String createTimeStart, String createTimeEnd);


	PageModel<ResourceLog> clickTable1(ResourceLog resourceLog);

    PageModel<ResourceLog> InactiveResource(ResourceLog resourceLog);

    List<ResourceLog> activeUser(String createTimeStart, String createTimeEnd);

	PageModel<ResourceLog> clickTable(ResourceLog resourceLog);

	List<ResourceLog> clickRosource(String rdname, String createTimeStart, String createTimeEnd);

	PageModel<ResourceLog> dormancyUser(ResourceLog resourceLog);

	List<ResourceLog> dateRange(String createTimeStart, String createTimeEnd);

	List<ResourceLog> clickHour(String timeHour);

	List<ResourceLog> dateStatisticsUv(String createTimeStart, String createTimeEnd);

	List<ResourceLog> clickHourUv(String timeHour);


	List<ResourceLog> findEchartData(String userId);
	
	/**查询用户最近的访问记录*/
	List<ResourceLog> findRecentsResourceLog(String userId, Integer pageNumber, Integer pageSize);
	/**查询最热的资源访问，对于个人有权的内容*/
	List<ResourceLog> findHotAccessResourceLog(String userId, Integer pageNumber, Integer pageSize);

}
