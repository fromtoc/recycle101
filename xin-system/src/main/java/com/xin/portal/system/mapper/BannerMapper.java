package com.xin.portal.system.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.xin.portal.system.model.Banner;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface BannerMapper  extends BaseMapper<Banner>{

    /**
     * @Title: find 
     * @Description:  查询记录
     * @param query
     * @return Banner
     * @author zhoujun
     * @Date 2018年1月15日 下午4:38:51
     */
    Banner find(Banner query);


    /**
     * @Title: findList 
     * @Description:  查询列表
     * @param query
     * @return List<Banner>
     * @author zhoujun
     * @param page 
     * @Date 2018年1月15日 下午4:39:29
     */
	List<Banner> findList(Page<Banner> page, Banner query);

	/**
	 * @Title: findForHome 
	 * @Description:  使用时查询列表
	 * @param bannerSize
	 * @return List<Banner>
	 * @author zhoujun
	 * @Date 2018年1月15日 下午4:39:40
	 */
	List<Banner> findForHome(@Param("bannerSize")int bannerSize);


	List<Banner> findAll(Banner banner);
}