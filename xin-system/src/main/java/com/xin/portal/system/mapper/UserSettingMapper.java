package com.xin.portal.system.mapper;

import org.apache.ibatis.annotations.Mapper;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.xin.portal.system.model.UserSetting;

/**
 *  Mapper 接口
 *
 * @author zhoujun
 * @since 2019-07-11
 */
@Mapper
public interface UserSettingMapper extends BaseMapper<UserSetting> {

	UserSetting selectByUseId();

	UserSetting selectByUseId(String userId);

}
