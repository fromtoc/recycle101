package ${package.ServiceImpl};

import ${package.Entity}.${entity};
import ${package.Mapper}.${table.mapperName};
import ${package.Service}.${table.serviceName};
import ${superServiceImplClassPackage};
import org.springframework.stereotype.Service;
<#if cfg.page>
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.xin.core.bean.PageModel;
</#if>

/**
 * ${table.comment} 服务实现类
 *
 * @author ${author}
 * @since ${date}
 */
@Service
<#if kotlin>
open class ${table.serviceImplName} : ${superServiceImplClass}<${table.mapperName}, ${entity}>(), ${table.serviceName} {

}
<#else>
public class ${table.serviceImplName} extends ${superServiceImplClass}<${table.mapperName}, ${entity}> implements ${table.serviceName} {
<#if cfg.page>
	@Override
	public PageModel<${entity}> page(${entity} query, Integer pageNumber, Integer pageSize) {
		Page<${entity}> page = new Page<${entity}>(pageNumber, pageSize);
		EntityWrapper<${entity}> ew=new EntityWrapper<${entity}>(query);
		page = selectPage(page, ew);
		return new PageModel<${entity}>(page);
	}
</#if>
}
</#if>
