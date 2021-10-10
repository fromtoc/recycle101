package com.xin.portal.system.mapper;

import com.baomidou.mybatisplus.plugins.Page;
import com.xin.portal.system.model.WxPushRecord;

import org.apache.ibatis.annotations.Mapper;
import com.baomidou.mybatisplus.mapper.BaseMapper;

import java.util.List;

/**
 * 微信推送记录 Mapper 接口
 *
 * @author zhoujun
 * @since 2019-02-27
 */
@Mapper
public interface WxPushRecordMapper extends BaseMapper<WxPushRecord> {

    public List<WxPushRecord> page(Page<WxPushRecord> page, WxPushRecord query);

    public List<WxPushRecord> findList(WxPushRecord query);
    
    public int insertWithTenantId(WxPushRecord record);

	public int insertBatchWithTenantId(List<WxPushRecord> wxPushRecordList);

}
