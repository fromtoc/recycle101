package ${package.Controller};

<#if restControllerStyle>
import org.springframework.web.bind.annotation.RestController;
<#else>
import org.springframework.stereotype.Controller;
</#if>
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.beans.factory.annotation.Autowired;
<#if cfg.isApi>
import org.springframework.http.MediaType;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
</#if>
import com.baomidou.mybatisplus.mapper.EntityWrapper;

import com.xin.core.bean.BaseApi;
import ${package.Service}.${table.serviceName};
import ${package.Entity}.${entity};
<#if superControllerClassPackage??>
import ${superControllerClassPackage};
</#if>
<#if cfg.isApi>
import com.xin.core.util.Constants;
</#if>
import com.xin.core.log.SystemLog;

/**  
* @Title: ${package.Controller}.${table.controllerName} 
* @Description: ${cfg.name}
* @author zhoujun  
* @date ${date}
* @version V1.0  
*/ 
<#if restControllerStyle>
@RestController
<#else>
@Controller
</#if>
@RequestMapping("<#if package.ModuleName??>/${package.ModuleName}</#if>/<#if controllerMappingHyphenStyle??>${controllerMappingHyphen}<#else>${table.entityPath}</#if>")
<#if cfg.isApi>
@Api(tags = "${cfg.name}")
</#if>
<#if kotlin>
class ${table.controllerName}<#if superControllerClass??> : ${superControllerClass}()</#if>
<#elseif superControllerClass??>
public class ${table.controllerName} extends ${superControllerClass} {

<#else>
public class ${table.controllerName} {

	
</#if>
	@Autowired
	private ${table.serviceName} service;
	
<#if cfg.page>
	/**
	 * @Title: page 
	 * @Description:  分页查询
	 * @param query
	 * @param pageNumber 页数
	 * @param pageSize 每页条数
	 * @return BaseApi
	 * @author zhoujun
	 * @Date ${date} 
	 */
	@SystemLog(module="${cfg.name}",operation="分页查询")
<#if cfg.isApi>
	@ApiOperation(value="分页查询", notes="",code=Constants.HTTP_200,produces=MediaType.APPLICATION_JSON_UTF8_VALUE)
	@ApiImplicitParams({
    	@ApiImplicitParam(name="pageNumber",value="页码",required=true, paramType = "query",example="1"),
    	@ApiImplicitParam(name="pageSize",value="每页条数",required=true, paramType = "query" ,example="10")
    })
</#if>
	@GetMapping("/page")
	public BaseApi page(${entity} query, Integer pageNumber, Integer pageSize){
		return BaseApi.page(service.page(query,pageNumber,pageSize));
	}
</#if>	
	
	/**
	 * @Title: list 
	 * @Description:  列表查询
	 * @param ${entity}
	 * @return BaseApi
	 * @author zhoujun
	 * @Date ${date} 
	 */
	@SystemLog(module="${cfg.name}",operation="列表查询")
<#if cfg.isApi>
	@ApiOperation(value="列表查询", notes="",code=Constants.HTTP_200,produces=MediaType.APPLICATION_JSON_UTF8_VALUE)
</#if>
	@GetMapping("/list")
	public BaseApi list(${entity} query){
		EntityWrapper<${entity}> warpper = new EntityWrapper<${entity}>(query);
		return BaseApi.list(service.selectList(warpper));
	}
	
	/**
	 * @Title: info 
	 * @Description:  按id查询
	 * @param ${entity}
	 * @return BaseApi
	 * @author zhoujun
	 * @Date 2018-07-04 
	 */
	@SystemLog(module="${cfg.name}",operation="详情查询")
<#if cfg.isApi>
	@ApiOperation(value="按id查询", notes="",code=Constants.HTTP_200,produces=MediaType.APPLICATION_JSON_UTF8_VALUE)
	@ApiImplicitParam(name="id",value="id",required=true, paramType = "path")
</#if>
	@GetMapping("/info/{id}")
	public BaseApi info(@PathVariable String id){
		${entity} result = service.selectById(id);
		return result != null ? BaseApi.data(result) : BaseApi.error();
	}
	
	
	/**
	 * @Title: save 
	 * @Description:  保存
	 * @param query
	 * @return BaseApi
	 * @author zhoujun
	 * @Date ${date} 
	 */
	@SystemLog(module="${cfg.name}",operation="新增")
<#if cfg.isApi>
	@ApiOperation(value="新增保存", notes="",code=Constants.HTTP_200,produces=MediaType.APPLICATION_JSON_UTF8_VALUE)
</#if>
	@PostMapping("/save")
	public BaseApi save(${entity} record){
		return record.insert() ? BaseApi.success() : BaseApi.error();
	}
	
	/**
	 * @Title: update 
	 * @Description:  更新
	 * @param query
	 * @return BaseApi
	 * @author zhoujun
	 * @Date ${date} 
	 */
	@SystemLog(module="${cfg.name}",operation="更新")
<#if cfg.isApi>
	@ApiOperation(value="更新保存", notes="",code=Constants.HTTP_200,produces=MediaType.APPLICATION_JSON_UTF8_VALUE)
</#if>
	@RequestMapping(value="/update",method=RequestMethod.POST)
	public BaseApi update(${entity} record){
		return record.updateById() ? BaseApi.success() : BaseApi.error();
	}
	
	
	/**
	 * @Title: delete 
	 * @Description:  删除
	 * @param id
	 * @return BaseApi
	 * @author zhoujun
	 * @Date 2017-7-25 下午3:47:04
	 */
	@SystemLog(module="${cfg.name}",operation="删除")
<#if cfg.isApi>
	@ApiOperation(value="根据id删除", notes="",code=Constants.HTTP_200,produces=MediaType.APPLICATION_JSON_UTF8_VALUE)
	@ApiImplicitParam(name="id",value="id",required=true, paramType = "path")
</#if>
	@RequestMapping(value="/delete/{id}",method=RequestMethod.DELETE)
	public BaseApi delete(@PathVariable String id){
		return service.deleteById(id) ? BaseApi.success() : BaseApi.error();
	}
	
	
}

