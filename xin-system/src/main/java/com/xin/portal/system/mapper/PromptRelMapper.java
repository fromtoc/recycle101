package com.xin.portal.system.mapper;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.xin.portal.system.model.PromptRel;
import com.xin.portal.system.model.Resource;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.plugins.Page;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author zhoujun123
 * @since 2018-01-09
 */

@Mapper
public interface PromptRelMapper extends BaseMapper<PromptRel> {

	/**  @Title: findList 
	 * @Description:  TODO
	 * @param query
	 * @return List<PromptRel>
	 * @author zhoujun
	 * @Date 2018年1月9日 下午1:34:58 
	 */
	List<PromptRel> findList(PromptRel query);

	/**  @Title: find 
	 * @Description:  TODO
	 * @param query
	 * @return Object
	 * @author zhoujun
	 * @Date 2018年1月9日 下午4:19:10 
	 */
	PromptRel find(PromptRel query);
	/**
	 * @Title: check 
	 * @Description:  检查是否已经绑定报表
	 * @param query
	 * @return PromptRel
	 * @author zhoujun
	 * @Date 2018年1月22日 上午11:33:36
	 */
	List<PromptRel> check(PromptRel query);

	int saveBatch(@Param("list")List<PromptRel> list);

	List<PromptRel> findByResourceId(String resourceId);
	
	List<Resource> findResourceList(Page<Resource> page, PromptRel query);

	List<PromptRel> findListWithTenantId(PromptRel query);
}
