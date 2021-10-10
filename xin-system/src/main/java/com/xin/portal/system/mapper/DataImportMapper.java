package com.xin.portal.system.mapper;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.xin.portal.system.model.DataImport;
import com.baomidou.mybatisplus.mapper.BaseMapper;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author zhoujun123
 * @since 2018-04-19
 */

@Mapper
public interface DataImportMapper extends BaseMapper<DataImport> {

	int saveBatch(@Param("tableName")String tableName, @Param("list")List<Map<Integer, String>> list);

}
