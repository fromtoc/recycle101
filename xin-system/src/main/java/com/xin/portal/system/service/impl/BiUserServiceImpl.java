package com.xin.portal.system.service.impl;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.xin.portal.bi.mstr.api.UserApi;
import com.xin.portal.system.bean.PageModel;
import com.xin.portal.system.mapper.BiMappingMapper;
import com.xin.portal.system.mapper.BiServerMapper;
import com.xin.portal.system.mapper.BiUserMapper;
import com.xin.portal.system.model.BiMapping;
import com.xin.portal.system.model.BiServer;
import com.xin.portal.system.model.BiUser;
import com.xin.portal.system.service.BiUserService;
/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author zhoujun123
 * @since 2018-01-22
 */
@Service
public class BiUserServiceImpl extends ServiceImpl<BiUserMapper, BiUser> implements BiUserService {
	@Autowired
	private BiServerMapper biServerMapper;
	@Autowired
	private BiUserMapper mapper;
	@Autowired
	private BiMappingMapper biMappingMapper;
	
	@Override
	public PageModel<BiUser> page(BiUser query, Integer pageNumber, Integer pageSize) {
		Page<BiUser> page = new Page<BiUser>(pageNumber,pageSize);
		EntityWrapper<BiUser> ew=new EntityWrapper<BiUser>();
		if(StringUtils.isNotEmpty(query.getBiServerId())){
			ew.eq("bi_server_id", query.getBiServerId());
		}
		if (StringUtils.isNotEmpty(query.getUsername())) {
			ew.like("username", query.getUsername());
		}
		if (StringUtils.isNotEmpty(query.getBiGroup())) {
			ew.like("bi_group", query.getBiGroup());
		}
		
		page = selectPage(page, ew);
		return new PageModel<BiUser>(page);
	}

	@Override
	@Transactional
	public boolean del(BiUser biUser) {
//// 展示去掉同步删除mstr的步骤。
//		BiServer biServer = biServerMapper.selectById(biUser.getBiServerId());
//		if(BiServer.TYPE_MSTR==biServer.getType().intValue()){//判断是否为mstr只有mstr才要同步删除
//			boolean result = UserApi.deleteUser(biServer,biUser);
//			if (result) {
//				EntityWrapper<BiMapping> mappingEw = new EntityWrapper<>();
//				mappingEw.eq("bi_user_id", biUser.getId());
//				biMappingMapper.delete(mappingEw);
//				return deleteById(biUser.getId());
//			}
//		}else{
			EntityWrapper<BiMapping> mappingEw = new EntityWrapper<>();
			mappingEw.eq("bi_user_id", biUser.getId());
			biMappingMapper.delete(mappingEw);
			return deleteById(biUser.getId());
//		}
//		return false;
	}

	@Override
	public boolean saveBatch(BiServer biServer, List<BiUser> biUserListUniqueDb) {
		//List<BiUser> biUserListMstrFolder = FolderApi.createFolderBatch(biServer,biUserListMstr);
		boolean result = insertBatch(biUserListUniqueDb);
		return result;
	}

	@Override
	public void check(BiUser query) {
		BiServer biServer = biServerMapper.selectById(query.getBiServerId());
		if(biServer.getType()!=null && biServer.getType()==BiServer.TYPE_MSTR){//这是mstr的检查方式，其他服务的检查等待开发
			EntityWrapper<BiUser> ew = new EntityWrapper<>(query);
			List<BiUser> list = selectList(ew);
			for (BiUser biUser : list) {
				JSONObject result = UserApi.sync(biServer,biUser);
				biUser.setUpdateTime(new Date());
				biUser.setState(result.getString("state"));
				if (result.get("biObjectId")!=null) {
					biUser.setBiObjectId(result.getString("biObjectId"));
					
				}
			}
			updateBatchById(list);
		}
	}

	@Override
	public List<BiUser> findList(BiUser query) {
		return mapper.findList(query);
	}


	public BiUser findBiUser(String serverId,String userId){
		return mapper.findBiUser(serverId,userId);
	}


}
