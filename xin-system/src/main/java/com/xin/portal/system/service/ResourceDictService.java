package com.xin.portal.system.service;

import com.baomidou.mybatisplus.service.IService;
import com.xin.portal.system.bean.PageModel;
import com.xin.portal.system.model.ResourceDict;

import java.util.List;

public interface ResourceDictService extends IService<ResourceDict> {

    PageModel<ResourceDict> page(String resourceId, Integer pageNumber, Integer pageSize,String name);

    List<ResourceDict> findList(String resourceId);

    boolean saveBatch(List<ResourceDict> list,String resourceId);

}
