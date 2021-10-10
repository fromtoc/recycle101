package com.xin.portal.system.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.xin.portal.system.model.Dict;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface DictMapper extends BaseMapper<Dict>{

    Dict selectByPrimaryKey(Integer id);

    List<Dict> findPageList(Page<Dict> page,Dict query);
    
    List<Dict> findList(Dict query);
    
    List<Dict> findListByName(Dict query);
    
    List<Dict> findItemList(Dict query);

    int update(Dict dict);

	int delByCode(String dictCode);

	int delById(String id);

	Integer save(Dict dict);

	Dict find(Dict query);
	
	boolean insertDictAllColunmForNewTenant(Dict query);

    List<Dict> findBytype();

	Dict findWithTenantId(Dict query);

	List<Dict> findItemListWithTenantId(Dict query);
}