package com.xin.portal.system.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.xin.portal.bi.mstr.api.FolderApi;
import com.xin.portal.system.bean.BaseApi;
import com.xin.portal.system.bean.PageModel;
import com.xin.portal.system.mapper.BiIndependentMapper;
import com.xin.portal.system.mapper.BiMappingMapper;
import com.xin.portal.system.mapper.BiProjectMapper;
import com.xin.portal.system.mapper.BiServerMapper;
import com.xin.portal.system.mapper.BiUserMapper;
import com.xin.portal.system.mapper.ConfigMapper;
import com.xin.portal.system.mapper.UserInfoMapper;
import com.xin.portal.system.model.BiIndependent;
import com.xin.portal.system.model.BiMapping;
import com.xin.portal.system.model.BiProject;
import com.xin.portal.system.model.BiServer;
import com.xin.portal.system.model.BiUser;
import com.xin.portal.system.model.Config;
import com.xin.portal.system.model.UserInfo;
import com.xin.portal.system.service.BiIndependentService;
import com.xin.portal.system.util.SessionUtil;
import com.xin.portal.system.util.Constant.ConfigKeys;
/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author zhoujun123
 * @since 2018-01-22
 */
@Service
public class BiIndependentServiceImpl extends ServiceImpl<BiIndependentMapper, BiIndependent> implements BiIndependentService {
	@Autowired
	private BiIndependentMapper mapper;
	@Autowired
	private BiServerMapper biServerMapper;
	@Autowired
	private BiProjectMapper biProjectMapper;
	@Autowired
	private BiUserMapper biUserMapper;
	@Autowired
	private BiMappingMapper biMappingMapper;
	@Autowired
	private ConfigMapper configMapper;
	@Autowired
	private UserInfoMapper userInfoMapper;

	@Override
	public PageModel<BiIndependent> page(BiIndependent query) {
		Page<BiIndependent> page = new Page<BiIndependent>(query.getPageNumber(),query.getPageSize());
		page.setRecords(mapper.find(page,query));
		return new PageModel<BiIndependent>(page);
	}

	@Override
	public List<BiIndependent> findList(BiIndependent query) {
		return mapper.find(query);
	}

	@Override
	public int save(BiIndependent record) {
		//查询server
		BiServer biServer = biServerMapper.selectById(record.getBiServerId());
		if(biServer!=null){//MSTR
			BiProject bp = (BiProject) biProjectMapper.selectById(record.getBiProjectId());
			if(bp!=null){
				//查询mstr用户
				BiUser bu = biUserMapper.selectById(record.getBiUserId());
				record.setBiOwnFolderName(record.getBiOwnFolderName()==null?bu.getUsername():record.getBiOwnFolderName());
				bu.setServerName(biServer.getServer());
				bu.setServerPort(biServer.getPort());
				bu.setProjectName(bp.getProject());
				//获取server下自主分析项目
				String folderId = FolderApi.createFolder(bu,record.getBiOwnFolderName());//创建文件夹
				if(folderId!=null && folderId!=""){//保存到数据库
					record.setBiOwnFolderId(folderId);
					record.setCreater(SessionUtil.getUserId());
					record.setCreateTime(new Date());
					return mapper.insert(record);
				}
				return -9;  //创建失败
			}
			return -2; //项目未找到
		}
		return -1;  //服务未找到
	}
	
	@Override
	public List<BiIndependent> findByQuery(BiIndependent query){
		return mapper.findByQuery(query);
	}

	@Override
	public BaseApi checkOpen(BiIndependent query) {
		String url = null;
		String biUserId = query.getBiUserId();
		//获取server
		BiServer bs = biServerMapper.selectById(query.getBiServerId());
		//获取project
		BiProject bp = biProjectMapper.selectById(query.getBiProjectId());
		if(biUserId!=null && biUserId!=""){//打开用户，使用传入的bi用户
			//获取bi用户的
			BiUser bu = biUserMapper.selectById(query.getBiUserId());
			//获取bi用户的自主分析地址
			Wrapper<BiIndependent> w = new EntityWrapper<>();
			w.eq("bi_user_id", query.getBiUserId());
			w.eq("bi_project_id", query.getBiProjectId());
			BiIndependent bid = mapper.selectList(w).get(0);
			url = getMstrIndependReportCommonUrl(bu,bp,bs,bid);
		}else{//打开项目，使用的当前用户  映射的bi账户
			//获取当前用户映射的bi账户的自主分析地址
			String userId = SessionUtil.getUserId();
			Wrapper<BiMapping> wm = new EntityWrapper<>();
			wm.eq("user_id", userId);
			List<BiMapping> bml = biMappingMapper.selectList(wm);
			if(bml!=null && bml.size()>0){
				List<String> bidList = new ArrayList<>();
				for (BiMapping biMapping : bml) {
					bidList.add(biMapping.getBiUserId());
				}
				Wrapper<BiIndependent> wb = new EntityWrapper<>();
				wb.eq("bi_project_id", query.getBiProjectId());
				wb.eq("bi_server_id", query.getBiServerId());
				wb.in("bi_user_id", bidList);
				List<BiIndependent> bil = mapper.selectList(wb);
				if(bil!=null && bil.size()>0){
					BiIndependent bii = bil.get(0);
					BiUser bu = biUserMapper.selectById(bii.getBiUserId());
					url = getMstrIndependReportCommonUrl(bu,bp,bs,bii);
				}else{
					url = "-1";
				}
			}else{
				url = "-9";
			}
		}
		return BaseApi.data(url);
	}
	
	private String getMstrIndependReportCommonUrl(BiUser bu,BiProject bp,BiServer bs,BiIndependent bid){
		StringBuilder sb = new StringBuilder();
		sb.append(bs.getUrl()).append("?");
		sb.append("Server=").append(bs.getServer());
		sb.append("&Project=").append(bp.getProject());
		sb.append("&Port=").append(bs.getPort());
		sb.append("&uid=").append(bu.getUsername());
		sb.append("&pwd=").append(bu.getPassword());
		sb.append("&hiddensections=header,path&evt=2001&src=mstrWeb.shared.fbb.fb.2001&folderID=").append(bid.getBiOwnFolderId());
		return sb.toString();
	}

	@Override
	public String getMstrUrlByPrjectId(String id) {
		String url = "";
		String userId = SessionUtil.getUserId();
		UserInfo userInfo = userInfoMapper.selectById(userId);
		Wrapper<BiMapping> wm = new EntityWrapper<>();
		wm.eq("user_id", userId);
		wm.eq("type", BiServer.getTypeName(BiServer.TYPE_MSTR));
		List<BiMapping> bml = biMappingMapper.selectList(wm);//包含了所有mstr系统的映射的账户
		Config config = new Config();
		config.setCode(ConfigKeys.SYS_SYNC_CREATE_MSTR_USER);
		Config syncCreateMstrUser = configMapper.selectOne(config);
		if("1".equals(syncCreateMstrUser.getValue()) && "1".equals(String.valueOf(userInfo.getMstrUser()))){//同步创建的用户，默认是一对一的（一个人对应一个项目）
			//通过mstr用户获取到independent
			if(bml!=null && bml.size()>0){
				EntityWrapper<BiIndependent> ewb = new EntityWrapper<>();
				ewb.eq("bi_user_id", bml.get(0).getBiUserId());
				List<BiIndependent> bii1 = mapper.selectList(ewb);
				//获取projectID  serverId
				if(bii1!=null && bii1.size()>0){
					BiProject bp1 = biProjectMapper.selectById(bii1.get(0).getBiProjectId());
					BiServer bs1 = biServerMapper.selectById(bii1.get(0).getBiServerId());
					BiUser bu1 = biUserMapper.selectById(bii1.get(0).getBiUserId());
					url = getMstrIndependReportCommonUrl(bu1,bp1,bs1,bii1.get(0));
				}
			}
		}else{
			//获取biProject
			BiProject bp = biProjectMapper.selectById(id);
			//获取biServer
			BiServer bs = biServerMapper.selectById(bp.getBiServerId());
			if(bml!=null && bml.size()>0){
				List<String> bidList = new ArrayList<>();
				for (BiMapping biMapping : bml) {
					bidList.add(biMapping.getBiUserId());
				}
				Wrapper<BiIndependent> wb = new EntityWrapper<>();
				wb.eq("bi_project_id", bp.getId());
				wb.eq("bi_server_id", bs.getId());
				wb.in("bi_user_id", bidList);
				List<BiIndependent> bil = mapper.selectList(wb);
				if(bil!=null && bil.size()>0){
					BiIndependent bii = bil.get(0);
					BiUser bu = biUserMapper.selectById(bii.getBiUserId());
					url = getMstrIndependReportCommonUrl(bu,bp,bs,bii);
				}
			}
		}
		return url;
	}
	
}
