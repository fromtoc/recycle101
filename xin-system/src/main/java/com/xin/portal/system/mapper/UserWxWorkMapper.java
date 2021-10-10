package com.xin.portal.system.mapper;

import org.apache.ibatis.annotations.Mapper;
import com.xin.portal.system.model.UserWxWork;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.plugins.Page;

import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 在通讯录同步助手中此接口可以读取企业通讯录的所有成员信息，而自建应用可以读取该应用设置的可见范围内的成员信息。

请求方式：GET（HTTPS）
请求地址：https://qyapi.weixin.qq.com/cgi-bin/user/get?access_token=ACCESS_TOKEN&userid=USERID Mapper 接口
 *
 * @author zhoujun
 * @since 2019-01-07
 */
@Mapper
public interface UserWxWorkMapper extends BaseMapper<UserWxWork> {

    List<UserWxWork> findListForPush(@Param("ids") String ids, @Param("roleIds") String roleIds, @Param("orgIds") String orgIds, @Param("appId") String appId, @Param("agentId") String agentId);

    List<UserWxWork> findUserRelationApp(@Param("userId") String userId, @Param("tenantId") String tenantId);

    List<UserWxWork> selectPageByParam(Page<UserWxWork> page, UserWxWork userWxWork);
}
