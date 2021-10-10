package com.xin.portal.system.service;


import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.service.IService;
import com.xin.portal.system.bean.PageModel;
import com.xin.portal.system.model.ThirdAppParam;
import com.xin.portal.system.util.line.bean.LineBotProperties;

public interface ThirdAppParamService extends IService<ThirdAppParam> {

    PageModel<ThirdAppParam> page(ThirdAppParam record);

    boolean delete(ThirdAppParam record);

    List<ThirdAppParam> findeListByParam(ThirdAppParam record);

    boolean save(ThirdAppParam record);

    boolean update(ThirdAppParam record);

    Map<String, LineBotProperties> getAllLineProperties();

}
