package com.xin.portal.system.service;

import java.util.List;

import com.baomidou.mybatisplus.service.IService;
import com.xin.portal.system.bean.PageModel;
import com.xin.portal.system.model.UserInfo;
/**
 * <p>
 *  服务类
 * </p>
 *
 * @author zhoujun123
 * @since 2018-01-22
 */
public interface UserInfoService extends IService<UserInfo> {
	PageModel<UserInfo> page(UserInfo query,String status);

	UserInfo findUserInfo(UserInfo userInfo);

	boolean save(UserInfo record,String configLocale,String status,String tenantId,String roleCode) throws Exception;

	boolean saveAdministrator(UserInfo record,String configLocale,String status,String tenantId,String roleCode) throws Exception;

	void updateADUser(String username,String password,String tenantId);

	List<UserInfo> selectUser(UserInfo b);

	int insertList(List<UserInfo> userInfoList);

	PageModel<UserInfo> pageRoleUser(UserInfo query);

	boolean updatePersonnalRecordById(UserInfo userInfo);


}
