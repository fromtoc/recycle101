package com.xin.portal.system.config.freemarker;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import com.google.common.collect.Lists;
import com.xin.portal.system.model.Menu;
import com.xin.portal.system.model.Permission;
import com.xin.portal.system.model.Resource;
import com.xin.portal.system.service.MenuService;
import com.xin.portal.system.service.PermissionService;
import com.xin.portal.system.service.ResourceService;
import com.xin.portal.system.util.Constant.SessionKeys;
import com.xin.portal.system.util.SessionUtil;

import freemarker.core.Environment;
import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapperBuilder;
import freemarker.template.SimpleScalar;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateDirectiveModel;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;

@Component
public class PathTag implements TemplateDirectiveModel {
	
	@Autowired
	private MenuService menuService;
	
	@Override
    public void execute(Environment environment, Map map, TemplateModel[] templateModels, TemplateDirectiveBody body) throws TemplateException, IOException {
        DefaultObjectWrapperBuilder builder = new DefaultObjectWrapperBuilder(Configuration.VERSION_2_3_28);
        String menuId = getParam(map,"id");
        List<Menu> menus = menuService.findUserMenus(SessionUtil.getUserId());
        List<String> list = Lists.newArrayList();
        Optional<Menu> opt = menus.stream().filter(item -> item.getId().equals(menuId)).findFirst();
        while(opt.isPresent()){
        	Menu menu = opt.get();
        	list.add(menu.getName());
        	opt = menus.stream().filter(item -> item.getId().equals(menu.getParentId())).findFirst();
		}
        Collections.reverse(list);
        environment.setVariable("pathList", builder.build().wrap(list));
        
        /*渲染模版*/
        if(body!=null)
            body.render(environment.getOut());
        
    }
	
	protected String getParam(Map params, String name) {
        Object value = params.get(name);

        if (value instanceof SimpleScalar) {
            return ((SimpleScalar)value).getAsString();
        }
        
        return null;
    }

}
