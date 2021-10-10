package com.xin.portal.system.mapper;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.xin.portal.system.model.BiUser;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author zhoujun123
 * @since 2018-01-22
 */

@Mapper
public interface BiUserMapper extends BaseMapper<BiUser> {
	
	BiUser find(BiUser mstrUser);

	List<BiUser> findList(BiUser query);

	BiUser findBiUser(@Param("serverId")String serverId,@Param("userId")String userId);

}
