package ${package.Service};

import ${package.Entity}.${entity};
import ${superServiceClassPackage};
<#if cfg.page>
import com.xin.portal.system.bean.PageModel;
</#if>

/**
 * ${table.comment} 服务类
 *
 * @author ${author}
 * @since ${date}
 */
<#if kotlin>
interface ${table.serviceName} : ${superServiceClass}<${entity}>
<#else>
public interface ${table.serviceName} extends ${superServiceClass}<${entity}> {
<#if cfg.page>
	/**
	 * @Title: page 
	 * @Description:  分页查询
	 * @param query
	 * @param pageNumber 页数
	 * @param pageSize 每页条数
	 * @return PageModel
	 * @author zhoujun
	 * @Date ${date} 
	 */
	PageModel<${entity}> page(${entity} query, Integer pageNumber, Integer pageSize);
</#if>
}
</#if>
