package com.xin.portal.system.service.impl;

import java.util.*;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.xin.portal.system.mapper.NoticeAlreadyReadMapper;
import com.xin.portal.system.model.NoticeAlreadyRead;
import com.xin.portal.system.util.SessionUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.xin.portal.system.bean.PageModel;
import com.xin.portal.system.mapper.NoticeMapper;
import com.xin.portal.system.model.Notice;
import com.xin.portal.system.service.NoticeService;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author zhoujun123
 * @since 2018-03-23
 */
@Service
public class NoticeServiceImpl extends ServiceImpl<NoticeMapper, Notice> implements NoticeService {
	
	@Autowired
	private NoticeMapper mapper;
	@Autowired
	private NoticeAlreadyReadMapper alreadyReadmapper;
	
	@Override
	public PageModel<Notice> page(Notice query) {
		Page<Notice> page = new Page<Notice>(query.getPageNumber(),query.getPageSize());
//		EntityWrapper<Notice> ew=new EntityWrapper<Notice>(query);
//		ew.orderBy("publish_time", false);
//		page = selectPage(page, ew);
		page.setRecords(mapper.findAllList(page,query));
//		List<Notice> list = selectList(ew);
		return new PageModel<Notice>(page);
	}


	@Override
	public boolean publish(Notice record) {
		int num = mapper.publish(record);
		if(num>0){//如果是取消发布则删除已读用户
			if(record.getState()!=null && record.getState()==0){
				this.deleteAlreadyReadUser(record);
			}
		}
		return mapper.publish(record) > 0 ? true : false;
	}


	@Override
	public Map findList() {
		/*Page<Notice> page = new Page<Notice>(record.getPageNumber(),record.getPageSize());
		return mapper.findList(page,record);*/

		Page<Notice> page = new Page<Notice>(0,999);
		Map map = new HashMap();
		map.put("state","1");//查询已发布的公告
		map.put("userId",SessionUtil.getUserId());
		map.put("tenantId",SessionUtil.getUserInfo().getTenantId());
		map.put("nowDate",new Date());
		List<Notice> readList = mapper.findListRead(page,map);
		List<Notice> unreadList = mapper.findListUnread(page,map);
		map.put("realSize",unreadList.size());
		map.put("readList",readList);
		map.put("unreadList",unreadList);





		return map;
	}
	@Override
	public Map findListForTheme() {
		Page<Notice> page = new Page<Notice>(0,999);
		Map map = new HashMap();
		map.put("state","1");//查询已发布的公告
		map.put("userId",SessionUtil.getUserId());
		map.put("tenantId",SessionUtil.getUserInfo().getTenantId());
		map.put("nowDate",new Date());
		List<Notice> readList = mapper.findListRead(page,map);
		List<Notice> unreadList = mapper.findListUnread(page,map);
		map.put("realSize",unreadList.size());
		if(unreadList.size()==10){
			map.put("readList",null);
			map.put("unreadList",unreadList);
		}else if(unreadList.size()>10){
			map.put("readList",null);
			List<Notice> list = new ArrayList<>();
			for(int i=0;i<(10);i++){
				list.add(unreadList.get(i));
			}
			map.put("unreadList",list);
		}else if(unreadList.size()<10){
			List<Notice> notices = new ArrayList<>();
			if(readList.size()>(10-unreadList.size())){
				for(int i=0;i<10-unreadList.size();i++){
					notices.add(readList.get(i));
				}
				map.put("readList",notices);
			}else{
				map.put("readList",readList);
			}
			map.put("unreadList",unreadList);
		}
		return map;
	}
	
	@Override
	public List<Notice> findAllList(Notice record) {
		Page<Notice> page = new Page<Notice>(record.getPageNumber(),record.getPageSize());
		return mapper.findAllList(page,record);
	}
	
	@Override
	public boolean updateReadNum(String id){
		return mapper.updateNum(id)>0?true:false;
	}
	
	@Override
	@Transactional
	public Notice findOneNotice(String id){
		NoticeAlreadyRead nar = new NoticeAlreadyRead();
		nar.setNoticeId(id);
		nar.setUserId(SessionUtil.getUserId());
		NoticeAlreadyRead result = alreadyReadmapper.selectOne(nar);
		if(result==null){
			alreadyReadmapper.insert(nar);
		}
		return mapper.findOneNotice(id);
	}


	@Override
	public boolean update(Notice record) {
		int num = mapper.update(record);
		//修改成功后删除原来已读 表数据
		if(num>0){
			this.deleteAlreadyReadUser(record);
		}
		return num > 0 ? true : false;
	}


	@Override
	public List<Notice> findListByNum(Notice notice) {
		return mapper.findListByNum(notice);
	}


	@Override
	public boolean setAllNoticeReadByUserId(String userId) {
		try {
			//mapper.setAllNoticeReadByUserId(userId,new Date());//该用户的未读公告（不包括过期公告，过期公告不展示）的  浏览数加一
			List<Notice> NotReadlist = mapper.selectAllNoticeNotReadAndValidByUserId(userId,new Date());//获取所有未读的已发布公告（不包括未读过期公告）
			//设置未读的公告为已读,浏览数加一
			if(NotReadlist!=null && NotReadlist.size()>0){
				for (Notice notice : NotReadlist) {
					NoticeAlreadyRead read = new NoticeAlreadyRead();
					read.setUserId(userId);
					read.setNoticeId(notice.getId());
					alreadyReadmapper.insert(read);
					mapper.updateNum(notice.getId());
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}


	@Override
	public boolean deleteAlreadyReadUser(Notice record) {
		EntityWrapper<NoticeAlreadyRead> ew = new EntityWrapper<>();
		ew.eq("notice_id", record.getId());
		return alreadyReadmapper.delete(ew)>0?true:false;
	}
	
	
	
}
