package com.xin.portal.system.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.xin.portal.system.model.UserCollect;

@Mapper
public interface UserCollectMapper extends BaseMapper<UserCollect>{
	/** 查询子类 **/
	List<UserCollect> findCollectList(UserCollect record);
	/**查询单个收藏**/
	UserCollect selectCollectOne(UserCollect record);
	/**创建文件夹**/
	Integer createCollectFloder(UserCollect record);
	/**编辑文件夹名称**/
	Integer modifyCollectFolder(UserCollect record);
	/**获取用户的收藏列表（只有收藏内容不包含文件夹。时间逆序）*/
	List<UserCollect> findCollectListPage(Page<UserCollect> page, UserCollect collect);

}
