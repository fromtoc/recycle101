package com.xin.portal.system.service;

import java.util.List;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.IService;
import com.xin.portal.system.bean.BaseApi;
import com.xin.portal.system.bean.CollectTree;
import com.xin.portal.system.model.UserCollect;

public interface CollectService extends IService<UserCollect>{
	/**
	 * 每次通过父级id查到子类
	 * @param record
	 * @return
	 */
	List<UserCollect> findChildCollect(UserCollect record);
	/**
	 * 添加收藏
	 * @param record
	 * @return
	 */
	boolean save(UserCollect record);
	/**
	 * 查询用户所有收藏的文件
	 * @param userId
	 * @return
	 */
	List<UserCollect> findAllCollectOfUser(UserCollect record);
	/**
	 * 查询一个收藏的内容
	 * @param record
	 * @return
	 */
	UserCollect findCollectOne(UserCollect record);
	/**
	 * 查询所有的收藏转换为ztree
	 * @param record
	 * @param queryType
	 * @return
	 */
	List<CollectTree> findAllCollectForTree(UserCollect record, String queryType);
	/**
	 * 创建文件夹
	 * @param record
	 * @return
	 */
	BaseApi createCollectFloder(UserCollect record);
	/**
	 * 修改文件夹名称
	 * @param record
	 * @return
	 */
	BaseApi modifyCollectFolder(UserCollect record);
	/**
	 * 通过ID查询收藏
	 * @param record
	 * @return
	 */
	List<UserCollect> findCollectByParentId(UserCollect record);
	/**
	 * 获取新建（新添）文件的排序
	 * @param record
	 * @return
	 */
	int findSortForNew(UserCollect record);
	/**
	 * 同父级菜单下排序
	 * @param ownerId
	 * @param targetId
	 * @param targetPid
	 * @param moveType
	 * @return 
	 */
	boolean sameParentToSort(String ownerId, String targetId, String targetPid, String moveType);
	/**
	 * 不同父级下菜单排序
	 * @param ownerId
	 * @param ownerPid
	 * @param targetId
	 * @param targetPid
	 * @param moveType
	 */
	boolean diffParentToSort(String ownerId, String ownerPid, String targetId, String targetPid, String moveType);
	/**
	 * 插入到一个文件夹中
	 * @param ownerId
	 * @param ownerPid
	 * @param targetId
	 * @param targetPid
	 */
	boolean innerToSort(String ownerId, String ownerPid, String targetId, String targetPid);
	/**
	 * 获取个人收藏分页显示
	 * @param collect
	 * @return
	 */
	List<UserCollect> findCollectListPage(UserCollect collect);



}
