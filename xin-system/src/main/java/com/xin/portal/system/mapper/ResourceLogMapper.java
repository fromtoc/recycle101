package com.xin.portal.system.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.xin.portal.system.model.ResourceLog;
import com.xin.portal.system.model.UserRecord;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 资源操作记录表 Mapper 接口
 *
 * @author zhoujun
 * @since 2018-11-28
 */
@Mapper
public interface ResourceLogMapper extends BaseMapper<ResourceLog> {
	
	List<ResourceLog> findResourceLog(Page<ResourceLog> page, ResourceLog query);

	List<ResourceLog> selectCountByType(ResourceLog resourceLog);

	List<ResourceLog> selectAcitvityResLog(@Param("record")ResourceLog record, @Param("ew")Wrapper<ResourceLog> rw);

	List<ResourceLog> selectAcitvitySortResLog(@Param("record")ResourceLog record, @Param("ew")Wrapper<ResourceLog> rw);
	
	List<ResourceLog> selectUserActivity(@Param("record")ResourceLog record, @Param("ew")Wrapper<ResourceLog> rw);

	List<ResourceLog> findList(ResourceLog query);

    List<ResourceLog> resourceClick(@Param("createTimeStart")String createTimeStart,@Param("createTimeEnd") String createTimeEnd);

    List<ResourceLog> visitUser(@Param("resourName") String resourName,@Param("createTimeStart") String createTimeStart, @Param("createTimeEnd")String createTimeEnd);

	List<ResourceLog> clickTable1(Page<ResourceLog> page, ResourceLog resourceLog);


	List<ResourceLog> InactiveResource(Page<ResourceLog> page, ResourceLog resourceLog);

	List<ResourceLog> activeUser(@Param("createTimeStart")String createTimeStart,@Param("createTimeEnd") String createTimeEnd);

	List<ResourceLog> clickTable(Page<ResourceLog> page, ResourceLog resourceLog);

	List<ResourceLog> clickRosource(@Param("rdname") String rdname,@Param("createTimeStart")String createTimeStart, @Param("createTimeEnd") String createTimeEnd);

	List<ResourceLog> dormancyUser(Page<ResourceLog> page, ResourceLog resourceLog);

	List<ResourceLog> dateRange(@Param("createTimeStart") String createTimeStart, @Param("createTimeEnd") String createTimeEnd);

	List<ResourceLog> clickHour(String timeHour);

	List<ResourceLog> dateStatisticsUv(@Param("createTimeStart") String createTimeStart,@Param("createTimeEnd") String createTimeEnd);

	List<ResourceLog> clickHourUv(String timeHour);

	List<ResourceLog> findEchartData(ResourceLog query);
	
	/**查询用户最近的访问记录*/
	List<ResourceLog> findRecentsResourceLog(Page<ResourceLog> page, String userId);
	/**查询最热的资源访问，对于个人有权的内容*/
	List<ResourceLog> findHotAccessResourceLog(Page<ResourceLog> page, String userId);
	
}
