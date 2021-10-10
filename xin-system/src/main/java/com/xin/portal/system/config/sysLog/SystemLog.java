package com.xin.portal.system.config.sysLog;

import java.lang.annotation.Documented;  
import java.lang.annotation.ElementType;  
import java.lang.annotation.Retention;  
import java.lang.annotation.RetentionPolicy;  
import java.lang.annotation.Target;  
  
@Target({ElementType.PARAMETER,ElementType.METHOD})      
@Retention(RetentionPolicy.RUNTIME)      
@Documented      
public @interface SystemLog {
	public String operation() default "";  
  
	public String module() default "";
	/**
	 * 参数指定转换参数是否要在运行目标方法前执行，可以指定多个。(只针对tfToName，tfPToROut1，tfPToROut2，tfPToROut3)
	 * 格式（tfToName）可多个参数用逗号隔开，这样就会在执行目标方法前先将这些参数转换
	 * 例如：用户修改报表名称，由于目前系统的限制修改名称后无法获取到原来的名称，
	 * 需要在调用修改方法之前获取到原来的名称 ，所以设置 before=tfToName ；
	 * 这样系统或根据tfToName中的指定规则通过id获取到修改前的名称
	 * 
	 */
	public String before() default "";
	/**
	 * 对name、tfToName、tfPToROut1...等值进行排序（必须列举所有已设置的日志内容参数包括有：name，tfToName，tfPToROut1，tfPToROut2，tfPToROut3）
	 * 例如 ：设置参数内容为tfTName="..."则 sort=tfToName ；内容为：用户a 修改（type）报表   tfToName(旧报表名) 
	 * 例如：设置参数内容为tfPToROut2="..." tfPToROut1="..." 则sort=tfPToROut2，tfPToROut1：内容：用户a 添加  tfname2（用户名） tfname1(角色名)
	 * (最多两个参数排序)
	 */
	public String sort() default "";
	/**
	 * 获取对象中的参数（必须为实体对象）
	 * 内容格式为：实体对象参数位置,要获取的实体对象属性  例如方法中参数为(String Id,SysLog query)，希望获取到日志的type类型。
	 * 此时的格式为:name=2,type。默认实体对象位置是1，当参数位置为1时（如：(SysLog query,String Id)），可以设置为name=type。
	 * 
	 */
	public String name() default ""; //操作对象，例如：操作“系统管理” （对象中的参数）
	/**
	 * 参数使用说明：
	 * 用于操作[对象]内容，如：修改“报表资源”的名称（参数为对象，例如：Resource query），希望获得修改前的name（可以用id转换为name），
	 * 那么可以这样定义：tfToName=query,id,name,com.xin.portal.system.mapper.ResourceMapper,selectById
	 * 参数名称意义和格式定义：
	 * (对象中参数)如果操作对象为id 需要转化 可以指定 固定格式：
	 * tfparams(要转化参数名称),tfpro(要转换属性),tfResult(转化后的属性名称),tfMapper(转换参数的接口),methodname(转换使用的方法名)
	 * 
	 */
	public String tfToName1() default "";
	/**同上*/
	public String tfToName2() default "";
	/**
	 * 参数使用说明：
	 * 用于操作[非对象]内容，如：将a,b,c,d用户赋“E”角色（参数为非对象）例如：String roleId,String[] userIds，希望获得角色名称和 用户名称 （一个一个获取），
	 * 那么可以这样定义：tfPToROut1=roleId,id,name,com.xin.portal.system.mapper.RoleMapper,selectById
	 * 那么可以这样定义：tfPToROut2=userIds,id,name,com.xin.portal.system.mapper.UserMapper,selectById
	 * 参数名称意义和格式定义：
	 * (对象中参数)如果操作对象为id 需要转化 可以指定 固定格式：
	 * tfparams(要转化参数名称),tfpro(要转换属性),tfResult(转化后的属性名称),tfMapper(转换参数的接口),methodname(转换使用的方法名)
	 * 
	 */
	public String tfPToROut1() default "";
	/**同上*/
	public String tfPToROut2() default "";
	
	public LogType type() default LogType.query;
} 
