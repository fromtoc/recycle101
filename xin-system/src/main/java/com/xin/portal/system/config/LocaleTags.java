package com.xin.portal.system.config;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.xin.portal.system.model.Dict;
import com.xin.portal.system.service.DictService;

import freemarker.core.Environment;
import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapperBuilder;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateDirectiveModel;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;

@Component
public class LocaleTags implements TemplateDirectiveModel {
	
	@Autowired
	private DictService dictService;
	
	@Override
    public void execute(Environment environment, Map map, TemplateModel[] templateModels, TemplateDirectiveBody body) throws TemplateException, IOException {
        DefaultObjectWrapperBuilder builder = new DefaultObjectWrapperBuilder(Configuration.VERSION_2_3_25);
        EntityWrapper<Dict> ew = new EntityWrapper<>();
        List<Dict> list = dictService.selectList(ew);
        environment.setVariable("listParentBanks", builder.build().wrap("afsdfsfd"));
        /*渲染模版*/
        if(body!=null)
            body.render(environment.getOut());
        
    }

}
