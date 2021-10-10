package com.xin.portal.system.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.xin.portal.system.model.ListManageResource;
import com.xin.portal.system.model.Resource;
import com.xin.portal.system.model.ResourcePerm;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface ResourceMapper extends BaseMapper<Resource> {
	
	public int updateIntroduce(Map map);
	
	public int updateNum(@Param("id")String id, @Param("column")String column, @Param("methodType")String methodType);

	Resource findSharResource(Resource query);

	List<Resource> findRoleResourceList(Page<Resource> page, Resource query);
	
	List<ResourcePerm> findResourcePermList(@Param("firstIndex")Integer firstIndex,@Param("pageSize")Integer pageSize,@Param("query")ResourcePerm resourcePerm);

	List<Resource> selectPageByListId(Page<Resource> page,ListManageResource query);

	List<Resource> selectListResource(@Param("relate")String relate,@Param("userId") String userId);

	List<Resource> selectOperationLog(@Param("num")int num);

	List<Resource> findReourceLog(@Param("userId")String userId);

	Resource selectResource( @Param("relate")String relate,@Param("userId") String userId);

	public List<Resource> selectByListId(@Param("id")String id,@Param("userId") String userId);
	
	List<Resource> selecthistorylog(@Param("id")String id , @Param("num")int num);
	
	boolean insertResourceAllColunmForNewTenant(Resource query);

    int resourceData();

	int resourceActive();

    List<Resource> chart();

	boolean updateEveryoneById(@Param("resourceId")String resourceId,@Param("value")String value);

	public List<Resource> selectMenuResourceByPermission(@Param("userId")String userId, @Param("menuParentId")String menuParentId);

	public int findResourcePermListCount(@Param("query")ResourcePerm query);
	
	public List<Resource> selectUserPermissionResource(@Param("userId")String userId, 
			@Param("resourceId")String resourceId, @Param("roleId")String roleId,
			@Param("isMobile")Integer isMobile, @Param("tenantId")String tenantId);

	public Resource selectByIdWithTenantId(@Param("resourceId")String resourceId,@Param("tenantId") String tenantId);
}