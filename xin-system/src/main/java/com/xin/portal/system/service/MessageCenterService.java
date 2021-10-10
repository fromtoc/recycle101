package com.xin.portal.system.service;

import java.util.Map;

import com.baomidou.mybatisplus.service.IService;
import com.xin.portal.system.bean.PageModel;
import com.xin.portal.system.model.MessageCenter;

/**
 *  服务类
 *
 * @author zhoujun
 * @since 2019-06-21
 */
public interface MessageCenterService extends IService<MessageCenter> {
	/**
	 * @Title: page 
	 * @Description:  分页查询
	 * @param query
	 * @param pageNumber 页数
	 * @param pageSize 每页条数
	 * @return PageModel
	 * @author zhoujun
	 * @Date 2019-06-21 
	 */
	PageModel<MessageCenter> page(MessageCenter query, Integer pageNumber, Integer pageSize);

	Map<String, Object> findUnReadMessage();

	Map<String, Object> findAllUnReadMessageMap();

	boolean updateAllMessageIsRead(String userId);

	boolean deleteMessageBySourceId(String sourceId);

    boolean clearAllMessage(String userId);
}
