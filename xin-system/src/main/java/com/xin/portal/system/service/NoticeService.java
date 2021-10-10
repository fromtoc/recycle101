package com.xin.portal.system.service;

import com.xin.portal.system.model.Notice;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.service.IService;
import com.xin.portal.system.bean.PageModel;
/**
 * <p>
 *  服务类
 * </p>
 *
 * @author zhoujun123
 * @since 2018-03-23
 */
public interface NoticeService extends IService<Notice> {
	PageModel<Notice> page(Notice query);


	boolean publish(Notice record);

	Map findList();

	Map findListForTheme();
	
	List<Notice> findAllList(Notice record);

	boolean updateReadNum(String id);

	public Notice findOneNotice(String id);

	boolean update(Notice record);


	List<Notice> findListByNum(Notice notice);


	boolean setAllNoticeReadByUserId(String userId);


	boolean deleteAlreadyReadUser(Notice record);
}
