package com.xin.portal.system.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.xin.portal.system.model.SysLogo;

/**
 * 系统logo设置 Mapper 接口
 *
 * @author zhoujun
 * @since 2019-01-04
 */
@Mapper
public interface SysLogoMapper extends BaseMapper<SysLogo> {

	List<SysLogo> findLogoPage(Page<SysLogo> page, SysLogo query);

}
