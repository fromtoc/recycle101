package com.xin.portal.system.mapper;

import org.apache.ibatis.annotations.Mapper;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.xin.portal.system.model.WxPush;

/**
 * 微信推送表 Mapper 接口
 *
 * @author zhoujun
 * @since 2019-02-11
 */
@Mapper
public interface WxPushMapper extends BaseMapper<WxPush> {

}
