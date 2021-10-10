package com.xin.portal.system.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.xin.portal.system.model.Notice;
import com.xin.portal.system.model.NoticeAlreadyRead;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;



@Mapper
public interface NoticeAlreadyReadMapper extends BaseMapper<NoticeAlreadyRead> {


	
}
