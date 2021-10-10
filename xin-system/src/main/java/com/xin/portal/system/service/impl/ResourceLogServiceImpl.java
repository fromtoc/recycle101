package com.xin.portal.system.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import com.xin.portal.system.bean.PageModel;
import com.xin.portal.system.mapper.OrganizationMapper;
import com.xin.portal.system.mapper.ResourceLogMapper;
import com.xin.portal.system.mapper.ResourceMapper;
import com.xin.portal.system.model.Organization;
import com.xin.portal.system.model.Resource;
import com.xin.portal.system.model.ResourceLog;
import com.xin.portal.system.model.UserRecord;
import com.xin.portal.system.service.ResourceLogService;
import com.xin.portal.system.util.EchartData;
import com.xin.portal.system.util.Series;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.*;

/**
 * 资源操作记录表 服务实现类
 *
 * @author zhoujun
 * @since 2018-11-28
 */
@Service
public class ResourceLogServiceImpl extends ServiceImpl<ResourceLogMapper, ResourceLog> implements ResourceLogService {
	@Autowired
	private ResourceMapper resourceMapper;
	@Autowired
	private ResourceLogMapper mapper;
	@Autowired
	private OrganizationMapper organizationMapper;
	
	@Override
	public PageModel<ResourceLog> page(ResourceLog query, Integer pageNumber, Integer pageSize) {
		Page<ResourceLog> page = new Page<ResourceLog>(pageNumber, pageSize);
		EntityWrapper<ResourceLog> ew=new EntityWrapper<ResourceLog>();
		ew.orderBy("create_time", false);
		ew.eq("resource_id", query.getResourceId());
		if (query.getType()!=null) {
			if (query.getType()==ResourceLog.TYPE_COLLECT_ADD) {
				ew.in("type", Lists.newArrayList(ResourceLog.TYPE_COLLECT_ADD,ResourceLog.TYPE_COLLECT_DEL));
			} else if (query.getType()==ResourceLog.TYPE_COMMENT_ADD) {
				ew.in("type", Lists.newArrayList(ResourceLog.TYPE_COMMENT_ADD,ResourceLog.TYPE_COMMENT_DEL));
			} else {
				ew.eq("type", query.getType());
				
			}
		}
		page = selectPage(page, ew);
		return new PageModel<ResourceLog>(page);
	}

	@Transactional
	@Override
	public void save(ResourceLog resourceLog) {
		resourceLog.insert();
		String column = "";
		String method = "+";
		switch(resourceLog.getType()){
		case ResourceLog.TYPE_VIEW:
			column = "view_num";
			break;
		case ResourceLog.TYPE_DOWNLOAD:
			column = "download_num";
			break;
		case ResourceLog.TYPE_COLLECT_ADD:
			column = "collect_num";
			break;
		case ResourceLog.TYPE_COLLECT_DEL:
			column = "collect_num";
			method = "-";
			break;
		case ResourceLog.TYPE_COMMENT_ADD:
			column = "comment_num";
			break;
		case ResourceLog.TYPE_COMMENT_DEL:
			column = "comment_num";
			method = "-";
			break;
		}
		resourceMapper.updateNum(resourceLog.getResourceId(), column, method);
	}

	@Override
	public PageModel<ResourceLog> paramPage(ResourceLog query) {
		Page<ResourceLog> page = new Page<ResourceLog>(query.getPageNumber(), query.getPageSize());
		page.setRecords(mapper.findResourceLog(page,query));
		return new PageModel<ResourceLog>(page);
	}

	@Override
	public List<ResourceLog> selectCountByType(ResourceLog resourceLog) {
		// TODO Auto-generated method stub
		return mapper.selectCountByType(resourceLog);
	}

	@Override
	public EchartData queryResourceActivity(ResourceLog record) {
		EchartData data = new EchartData();
		List<String> legend = new ArrayList<String>();//图例
		List<Series> series = new ArrayList<Series>();//数据
		List<String> xAxis = new ArrayList<String>();//x轴数据
		
		//图例 根据时间、条数、资源类型查出图例类型 记录类型：1查看2下载3添加收藏4取消收藏5添加评论6取消评论  
		legend.add("查看");
		legend.add("下载");
		legend.add("添加收藏");
		legend.add("取消收藏");
		legend.add("评论");
		legend.add("删除评论");
		//查询时间、资源类型中的数据
		
		//查询资源类型中的资源
		String resourceType = record.getResourceType();
		String resIdStr = "";
		if(resourceType!=null && resourceType!=""){
			//找到资源类型中的资源
			Wrapper<Resource> w = new EntityWrapper<>();
			w.in("resource_type1", resourceType);
			w.or().in("resource_type2", resourceType);
			List<Resource> resList = resourceMapper.selectList(w);
			for (Resource resource : resList) {
				resIdStr += resource.getId()+",";
			}
			resIdStr = resIdStr!=""?resIdStr.substring(0,resIdStr.length()-1):"";
			record.setResourceId(resIdStr);
		}else{
			record.setResourceId(null);
		}
		//查询条件下资源日志中的资源排行
		Wrapper<ResourceLog> rw = new EntityWrapper<ResourceLog>();
		if((record.getCreateTimeStart()!=null && record.getCreateTimeStart()!="") && (record.getCreateTimeEnd()!=null && record.getCreateTimeEnd()!="")){
			rw.between("create_time", record.getCreateTimeStart(), record.getCreateTimeEnd());
		}
		List<ResourceLog> resSortList = mapper.selectAcitvitySortResLog(record, rw);
		List<ResourceLog> resLogList = mapper.selectAcitvityResLog(record,rw);
		List<String> sortResId = new ArrayList<>();//按照访问数量排序后的资源
		if((resLogList!=null && resLogList.size()>0) && (resSortList!=null && resSortList.size()>0)){
			//设置logType的map
			Map<Integer, List<ResourceLog>> allTypeMap = new HashMap<>();
			List<ResourceLog> TYPE_VIEW_List = new ArrayList<>();
			List<ResourceLog> TYPE_DOWNLOAD_List = new ArrayList<>();
			List<ResourceLog> TYPE_COLLECT_ADD_List = new ArrayList<>();
			List<ResourceLog> TYPE_COLLECT_DEL = new ArrayList<>();
			List<ResourceLog> TYPE_COMMENT_ADD_List = new ArrayList<>();
			List<ResourceLog> TYPE_COMMENT_DEL_List = new ArrayList<>();
			for (ResourceLog resl : resLogList) {
				switch(resl.getType()){
	            case ResourceLog.TYPE_VIEW:
	            	TYPE_VIEW_List.add(resl);
	            	break;
	            case ResourceLog.TYPE_DOWNLOAD:
	            	TYPE_DOWNLOAD_List.add(resl);
	            	break;
	            case ResourceLog.TYPE_COLLECT_ADD:
	            	TYPE_COLLECT_ADD_List.add(resl);
	            	break;
	            case ResourceLog.TYPE_COLLECT_DEL:
	            	TYPE_COLLECT_DEL.add(resl);
	            	break;
	            case ResourceLog.TYPE_COMMENT_ADD:
	            	TYPE_COMMENT_ADD_List.add(resl);
	            	break;
	            case ResourceLog.TYPE_COMMENT_DEL:
	            	TYPE_COMMENT_DEL_List.add(resl);
	            	break;
	            }
			}
			allTypeMap.put(1, TYPE_VIEW_List);
			allTypeMap.put(2, TYPE_DOWNLOAD_List);
			allTypeMap.put(3, TYPE_COLLECT_ADD_List);
			allTypeMap.put(4, TYPE_COLLECT_DEL);
			allTypeMap.put(5, TYPE_COMMENT_ADD_List);
			allTypeMap.put(6, TYPE_COMMENT_DEL_List);
			//分别设置x轴和每个图例的数据  找到展示条数的数据
			Integer num = record.getTop()==null?10:record.getTop();
			int len = num >= resSortList.size()?resSortList.size():num;
			for (int i = 0; i < len ; i++) {
				xAxis.add(resSortList.get(i).getName());
				sortResId.add(resSortList.get(i).getResourceId());
			}
			//根据排序后的资源，设置日志类型的series
			//遍历map 按照type  遍历resSort 的排序 将ID相同的放入
			for (Map.Entry<Integer, List<ResourceLog>> entry : allTypeMap.entrySet()) { 
				System.out.println("Key = " + entry.getKey() + ", Value = " + entry.getValue());
				List<ResourceLog> resTList = entry.getValue();
//				List<Integer> serisData = new ArrayList<Integer>();//对应图例的每一个数据
				//定义内部类
				class resData implements Serializable{
					private static final long serialVersionUID = 1L;
					private Integer value;
					private String id;
					public Integer getValue() {
						return value;
					}
					public void setValue(Integer value) {
						this.value = value;
					}
					public String getId() {
						return id;
					}
					public void setId(String id) {
						this.id = id;
					}
					public resData(Integer value, String id) {
						super();
						this.value = value;
						this.id = id;
					}
				}
				List<resData> serisData = new ArrayList<>();
				switch(entry.getKey()){
	            case ResourceLog.TYPE_VIEW:
	            	for (int i = 0; i < len; i++) {
	            		String sortId = sortResId.get(i);
	            		for (ResourceLog resourceLog : resTList) {
	            			if(sortId.equals(resourceLog.getResourceId())){
//	            				serisData.add(resourceLog.getMyCount());
	            				serisData.add(new resData(resourceLog.getMyCount(), resourceLog.getResourceId()));
	            				break;
	            			}
						}
					}
	            	series.add(new Series("查看", serisData));
	            	break;
	            case ResourceLog.TYPE_DOWNLOAD:
	            	for (int i = 0; i < len; i++) {
	            		String sortId = sortResId.get(i);
	            		for (ResourceLog resourceLog : resTList) {
	            			if(sortId.equals(resourceLog.getResourceId())){
//	            				serisData.add(resourceLog.getMyCount());
	            				serisData.add(new resData(resourceLog.getMyCount(), resourceLog.getResourceId()));
	            				break;
	            			}
						}
					}
	            	series.add(new Series("下载", serisData));
	            	break;
	            case ResourceLog.TYPE_COLLECT_ADD:
	            	for (int i = 0; i < len; i++) {
	            		String sortId = sortResId.get(i);
	            		for (ResourceLog resourceLog : resTList) {
	            			if(sortId.equals(resourceLog.getResourceId())){
	            				//serisData.add(resourceLog.getMyCount());
	            				serisData.add(new resData(resourceLog.getMyCount(), resourceLog.getResourceId()));
	            				break;
	            			}
						}
					}
	            	series.add(new Series("添加收藏", serisData));
	            	break;
	            case ResourceLog.TYPE_COLLECT_DEL:
	            	for (int i = 0; i < len; i++) {
	            		String sortId = sortResId.get(i);
	            		for (ResourceLog resourceLog : resTList) {
	            			if(sortId.equals(resourceLog.getResourceId())){
//	            				serisData.add(resourceLog.getMyCount());
	            				serisData.add(new resData(resourceLog.getMyCount(), resourceLog.getResourceId()));
	            				break;
	            			}
						}
					}
	            	series.add(new Series("取消收藏", serisData));
	            	break;
	            case ResourceLog.TYPE_COMMENT_ADD:
	            	for (int i = 0; i < len; i++) {
	            		String sortId = sortResId.get(i);
	            		for (ResourceLog resourceLog : resTList) {
	            			if(sortId.equals(resourceLog.getResourceId())){
//	            				serisData.add(resourceLog.getMyCount());
	            				serisData.add(new resData(resourceLog.getMyCount(), resourceLog.getResourceId()));
	            				break;
	            			}
						}
					}
	            	series.add(new Series("评论", serisData));
	            	break;
	            case ResourceLog.TYPE_COMMENT_DEL:
	            	for (int i = 0; i < len; i++) {
	            		String sortId = sortResId.get(i);
	            		for (ResourceLog resourceLog : resTList) {
	            			if(sortId.equals(resourceLog.getResourceId())){
//	            				serisData.add(resourceLog.getMyCount());
	            				serisData.add(new resData(resourceLog.getMyCount(), resourceLog.getResourceId()));
	            				break;
	            			}
						}
					}
	            	series.add(new Series("删除评论", serisData));
	            	break;
	            }
			}
			data = new EchartData(legend, xAxis, series);
			
		}
		return data;
	}

	@Override
	public EchartData queryUserActivity(ResourceLog record) {
		List<String> legend = new ArrayList<String>();//图例
		List<Series> series = new ArrayList<Series>();//数据
		List<String> xAxis = new ArrayList<String>();//x轴数据
		//定义内部类
		class resData implements Serializable{
			private static final long serialVersionUID = 1L;
			private Integer value;
			private String id;
			public Integer getValue() {
				return value;
			}
			public void setValue(Integer value) {
				this.value = value;
			}
			public String getId() {
				return id;
			}
			public void setId(String id) {
				this.id = id;
			}
			public resData(Integer value, String id) {
				super();
				this.value = value;
				this.id = id;
			}
		}
		List<resData> serData = new ArrayList<>();//series 数据
		String orgId = record.getOrganization();
		String orgCode = "";
		if(orgId==null || orgId==""){//全部 orgCode = 001
			orgCode = "001";
		}else{//orgCode = 001...
			Organization org = organizationMapper.selectById(orgId);
			orgCode = org.getCode();
		}
		record.setOrganization(orgCode);
		Wrapper<ResourceLog> rw = new EntityWrapper<ResourceLog>();
		if((record.getCreateTimeStart()!=null && record.getCreateTimeStart()!="") && (record.getCreateTimeEnd()!=null && record.getCreateTimeEnd()!="")){
			rw.between("create_time",record.getCreateTimeStart() , record.getCreateTimeEnd());
		}
		List<ResourceLog> userList = mapper.selectUserActivity(record, rw);
		for (ResourceLog resourceLog : userList) {
			xAxis.add(resourceLog.getName());
			//serData.add(resourceLog.getMyCount());
			serData.add(new resData(resourceLog.getMyCount(), resourceLog.getCreater()));
		}
		series.add(new Series<>("用户访问量", "bar", serData));
		EchartData data = new EchartData(legend, xAxis, series);
		return data;
	}

	@Override
	public List<Resource> selecthistorylog(String id, int num) {
		return resourceMapper.selecthistorylog(id, num);
	}

	@Override
	public List<ResourceLog> findList(ResourceLog query) {
		return mapper.findList(query);
	}


	@Override
	public List<ResourceLog> resourceClick(String createTimeStart, String createTimeEnd) {
		return mapper.resourceClick(createTimeStart, createTimeEnd);
	}


	@Override
	public List<ResourceLog> findEchartData(String userId){
		ResourceLog query = new ResourceLog();
		Calendar cal = Calendar.getInstance();//获取一个Calendar对象
		cal.setTime(new Date() );
		cal.add(Calendar.MONTH, -1);//获取当前时间上一个月
		query.setCreateTime(cal.getTime());
		query.setCreater(userId);
		return mapper.findEchartData(query);
	}

	@Override
	public List<ResourceLog> visitUser(String resourName, String createTimeStart, String createTimeEnd) {
		return mapper.visitUser(resourName,createTimeStart,createTimeEnd);
	}

	/*@Override
	public PageModel<ResourceLog> clickTable1(ResourceLog resourceLog) {
		Page<ResourceLog> page = new Page<>(resourceLog.getPageNumber(), resourceLog.getPageSize());
		page.setRecords(mapper.clickTable1(page,resourceLog));
		return *//*new PageModel<ResourceLog>(page)*//*mapper.clickTable1(page,resourceLog);
	}*/


	@Override
	public PageModel<ResourceLog> clickTable1(ResourceLog resourceLog) {
		Page<ResourceLog> page = new Page<>(resourceLog.getPageNumber(), resourceLog.getPageSize());
		page.setRecords(mapper.clickTable1(page,resourceLog));
		return new PageModel<ResourceLog>(page);
	}

	@Override
	public PageModel<ResourceLog> InactiveResource(ResourceLog resourceLog) {
		Page<ResourceLog> page = new Page<>(resourceLog.getPageNumber(),resourceLog.getPageSize());
		page.setRecords(mapper.InactiveResource(page,resourceLog));
		return new PageModel<ResourceLog>(page);
	}

	@Override
	public List<ResourceLog> activeUser(String createTimeStart, String createTimeEnd) {
		return mapper.activeUser(createTimeStart,createTimeEnd);
	}

	@Override
	public PageModel<ResourceLog> clickTable(ResourceLog resourceLog) {
		Page<ResourceLog> page = new Page<>(resourceLog.getPageNumber(), resourceLog.getPageSize());
		page.setRecords(mapper.clickTable(page,resourceLog));
		return new PageModel<ResourceLog>(page);
	}

	@Override
	public List<ResourceLog> clickRosource(String rdname, String createTimeStart, String createTimeEnd) {
		return mapper.clickRosource(rdname,createTimeStart,createTimeEnd);
	}

	@Override
	public PageModel<ResourceLog> dormancyUser(ResourceLog resourceLog) {
		Page<ResourceLog> page = new Page<>(resourceLog.getPageNumber(),resourceLog.getPageSize());
		page.setRecords(mapper.dormancyUser(page,resourceLog));
		return new PageModel<ResourceLog>(page);
	}

	@Override
	public List<ResourceLog> dateRange(String createTimeStart, String createTimeEnd) {
		return mapper.dateRange(createTimeStart,createTimeEnd);
	}

	@Override
	public List<ResourceLog> clickHour(String timeHour) {
		return mapper.clickHour(timeHour);
	}

	@Override
	public List<ResourceLog> dateStatisticsUv(String createTimeStart, String createTimeEnd) {
		return mapper.dateStatisticsUv(createTimeStart,createTimeEnd);
	}

	@Override
	public List<ResourceLog> clickHourUv(String timeHour) {
		return  mapper.clickHourUv(timeHour);
	}
	
	@Override
	public List<ResourceLog> findRecentsResourceLog(String userId, Integer pageNumber, Integer pageSize) {
		Page<ResourceLog> page = new Page<ResourceLog>(pageNumber, pageSize);
		return mapper.findRecentsResourceLog(page,userId);
	}
	
	@Override
	public List<ResourceLog> findHotAccessResourceLog(String userId, Integer pageNumber, Integer pageSize) {
		Page<ResourceLog> page = new Page<ResourceLog>(pageNumber, pageSize);
		return mapper.findHotAccessResourceLog(page,userId);
	}
}
