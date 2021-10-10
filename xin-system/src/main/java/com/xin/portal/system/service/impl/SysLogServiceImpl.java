package com.xin.portal.system.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.xin.portal.system.bean.PageModel;
import com.xin.portal.system.mapper.SysLogMapper;
import com.xin.portal.system.model.SysLog;
import com.xin.portal.system.model.UserCollect;
import com.xin.portal.system.service.SysLogService;
import com.xin.portal.system.util.SessionUtil;
/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author zhoujun123
 * @since 2018-01-09
 */
@Service
public class SysLogServiceImpl extends ServiceImpl<SysLogMapper, SysLog> implements SysLogService {
	
	@Autowired
	private SysLogMapper mapper;

	
	@Override
	public PageModel<SysLog> page(SysLog query) {
		Page<SysLog> page = new Page<SysLog>(query.getPageNumber(),query.getPageSize());
//		EntityWrapper<SysLog> ew=new EntityWrapper<SysLog>();
//		if (query.getCreateTimeStart()!=null && query.getCreateTimeEnd()!=null) {
//			ew.between("create_time", query.getCreateTimeStart(), query.getCreateTimeEnd());
//		} else if (query.getCreateTimeStart()!=null) {
//			ew.ge("create_time", query.getCreateTimeStart());
//		} else if (query.getCreateTimeEnd()!=null) {
//			ew.le("create_time", query.getCreateTimeEnd());
//		}
		page.setRecords(mapper.findPageList(page,query));
		return new PageModel<SysLog>(page);
	}
	
	@Override
	public Integer addLog(SysLog myLog) {
		return mapper.insert(myLog);
	}

	@Override
	public List<SysLog> getDistinctOp() {
		// TODO Auto-generated method stub
		return mapper.getDistinctOp();
	}

	@Override
	public Integer addCollectLog(UserCollect userCollect) {
		// TODO Auto-generated method stub
		SysLog log = new SysLog();
		String user = SessionUtil.getUserId();
        String  orgz=SessionUtil.getUserInfo().getOrgCode();
		log.setCreater(user);
		log.setOrganization(orgz);
		log.setOperation("用户收藏课件");
		log.setContent("用户收藏课件"+"["+userCollect.getResourceId()+"]");
		log.setType(10);
		log.setCreateTime(new Date());
		log.setNormal(1);
		return addLog(log);
	}
}
