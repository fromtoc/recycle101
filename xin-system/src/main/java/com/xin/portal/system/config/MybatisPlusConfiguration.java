/**   
* @Title: MybatisPlusConfiguration.java 
* @Package com.xin.portal.system.config 
* @Description: TODO
* @author zhoujun 
* @date 2018年12月24日 上午10:13:54 
* @version V1.0   
*/
package com.xin.portal.system.config;

import com.alibaba.druid.pool.DruidDataSource;
import com.baomidou.mybatisplus.MybatisConfiguration;
import com.baomidou.mybatisplus.MybatisXMLLanguageDriver;
import com.baomidou.mybatisplus.mapper.ISqlInjector;
import com.baomidou.mybatisplus.mapper.LogicSqlInjector;
import com.baomidou.mybatisplus.plugins.PaginationInterceptor;
import com.baomidou.mybatisplus.plugins.parser.ISqlParser;
import com.baomidou.mybatisplus.plugins.parser.ISqlParserFilter;
import com.baomidou.mybatisplus.plugins.parser.tenant.TenantHandler;
import com.baomidou.mybatisplus.plugins.parser.tenant.TenantSqlParser;
import com.baomidou.mybatisplus.spring.MybatisSqlSessionFactoryBean;
import com.baomidou.mybatisplus.spring.boot.starter.MybatisPlusProperties;
import com.baomidou.mybatisplus.spring.boot.starter.SpringBootVFS;
import com.baomidou.mybatisplus.toolkit.PluginUtils;
import com.xin.portal.system.mapper.ResourceTypeMapper;
import com.xin.portal.system.util.Constant.SessionKeys;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.StringValue;
import org.apache.ibatis.mapping.DatabaseIdProvider;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.VendorDatabaseIdProvider;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * @ClassPath: com.xin.portal.system.config.MybatisPlusConfiguration 
 * @Description: TODO
 * @author zhoujun
 * @date 2018年12月24日 上午10:18:32
 */
@Configuration
@EnableConfigurationProperties(MybatisPlusProperties.class)
public class MybatisPlusConfiguration {
	
	private final Logger logger = LoggerFactory.getLogger(MybatisPlusConfiguration.class);
	
	@Autowired
    @Qualifier("druidDataSource")
    private DruidDataSource dataSource;
	@Autowired
    private MybatisPlusProperties properties;
	@Autowired(required = false)
    private Interceptor[] interceptors;

    @Bean
    public ISqlInjector sqlInjector() {
        return new LogicSqlInjector();
    }
	
	@Bean
	public PaginationInterceptor paginationInterceptor() {
        PaginationInterceptor paginationInterceptor = new PaginationInterceptor();
        logger.info("======dbType:{}======",dataSource.getDbType());
        
        paginationInterceptor.setLocalPage(true);// 开启 PageHelper 的支持
        
        List<ISqlParser> sqlParserList = new ArrayList<>();
        TenantSqlParser tenantSqlParser = new TenantSqlParser();
        tenantSqlParser.setTenantHandler(new TenantHandler() {
            @Override
            public Expression getTenantId() {
            	Subject subject = SecurityUtils.getSubject();
            	if (subject!=null) {
            		Session session = subject.getSession();
            		String tenantId = (String) session.getAttribute(SessionKeys.TENANT_ID);
            		tenantId = StringUtils.isEmpty(tenantId) ? "1" : tenantId;
            		return new StringValue(tenantId);
            		
            	}
                return new StringValue("1");
            	
            }

            @Override
            public String getTenantIdColumn() {
                return "tenant_id";
            }

            @Override
            public boolean doTableFilter(String tableName) {
                // 这里可以判断是否过滤表后不会自动加tenant_id;
                /*
                 * t_integration_log:集成日志表
                 */
                if ("t_tenant".equals(tableName) ||"t_role_resource".equals(tableName)
                		||"t_sys_release".equals(tableName)/*||"t_resource_type".equals(tableName)*/
                		||"t_role_permission".equals(tableName)
                		|| "t_integration_log".equals(tableName)
                		|| "t_user_wx_work".equals(tableName)
                		|| "t_third_app_param".equals(tableName)
                		//|| "t_user_line_work".equals(tableName)//Line功能注释
                        || "t_wx_push".equals(tableName)
                        || "t_wx_push_record".equals(tableName)
                        || "t_wx_push_file".equals(tableName)
                        || tableName.startsWith("qrtz_")) {
                    return true;
                }
                return false;
            }
        });
        sqlParserList.add(tenantSqlParser);
        paginationInterceptor.setSqlParserList(sqlParserList);
        paginationInterceptor.setSqlParserFilter(new ISqlParserFilter() {
            @Override
            public boolean doFilter(MetaObject metaObject) {
                MappedStatement ms = PluginUtils.getMappedStatement(metaObject);
//                // 过滤自定义查询此时无租户信息约束出现
                if ("com.xin.portal.system.mapper.UserMapper.findByName".equals(ms.getId()) || 
                		"com.xin.portal.system.mapper.ResourceMapper.findResourcePermList".equals(ms.getId())||
                		//当添加了新的租户后，这些方法被调用后会出错
                		//"com.xin.portal.system.mapper.RoleMapper.insert".equals(ms.getId())||
                		//"com.xin.portal.system.mapper.ConfigMapper.insert".equals(ms.getId())||
                		//"com.xin.portal.system.mapper.OrganizationMapper.insert".equals(ms.getId())||
                		//"com.xin.portal.system.mapper.RoleUserMapper.insert".equals(ms.getId())||
                		//"com.xin.portal.system.mapper.RoleResourceMapper.insert".equals(ms.getId())||
                        "com.xin.portal.system.mapper.ConfigMapper.findByCode".equals(ms.getId())||
                        "com.xin.portal.system.mapper.ConfigMapper.findListByCode".equals(ms.getId())||
                        "com.xin.portal.system.mapper.UserMapper.findByUsernameAndTenantIds".equals(ms.getId())||
                        //用于在调度中使用（由于调度新开线程可能无法使用sqlsession，如果不加这个会报no session）
                        "com.xin.portal.system.mapper.MessageCenterMapper.insertWithTenantId".equals(ms.getId())||
                        "com.xin.portal.system.mapper.WxPushRecordMapper.insertWithTenantId".equals(ms.getId())||
                        "com.xin.portal.system.mapper.UserInfoMapper.findListWithTenantId".equals(ms.getId())||
                		"com.xin.portal.system.mapper.UserInfoMapper.selectBatchIdsWithTenantId".equals(ms.getId())||
                		"com.xin.portal.system.mapper.ConfigMapper.selectEmailParamWithTenant".equals(ms.getId())||
                		"com.xin.portal.system.mapper.ResourceMapper.selectUserPermissionResource".equals(ms.getId())||
                		"com.xin.portal.system.mapper.PromptRelMapper.findListWithTenantId".equals(ms.getId())||
                		"com.xin.portal.system.mapper.BiProjectMapper.findByIdWithTenantId".equals(ms.getId())||
                		"com.xin.portal.system.mapper.BiMappingMapper.selectBiUserBySysUserAndServerIdWithTenantId".equals(ms.getId())||
                		"com.xin.portal.system.mapper.FileModelMapper.insertWithTenantId".equals(ms.getId())||
                		"com.xin.portal.system.mapper.DictMapper.findWithTenantId".equals(ms.getId())||
                		"com.xin.portal.system.mapper.DatasourceMapper.selectWithTenantId".equals(ms.getId())||
                		"com.xin.portal.system.mapper.DataPermissionMapper.selectDPByUserIdWithTenantId".equals(ms.getId())||
                		"com.xin.portal.system.mapper.DictMapper.findItemListWithTenantId".equals(ms.getId())||
                		"com.xin.portal.system.mapper.ResourceMapper.selectByIdWithTenantId".equals(ms.getId())||
                		//用于在初始化license.lic文件中读取数据使用
                		"com.xin.portal.system.mapper.FileModelMapper.selectByCreateTime".equals(ms.getId())||
                		//新增租户的添加方法，没有租户约束可以自己指定租户id
                        "com.xin.portal.system.mapper.UserMapper.updateIsDeleteForTenantDelete".equals(ms.getId())||
                        "com.xin.portal.system.mapper.RolePermissionMapper.insertRolePermission".equals(ms.getId())||
                		"com.xin.portal.system.mapper.RoleMapper.insertAllRoleColunmForNewTenant".equals(ms.getId())||
                		"com.xin.portal.system.mapper.ConfigMapper.insert".equals(ms.getId())|| //目前没有新增"系统配置"的功能,暂时不补充新的插入方法
                		"com.xin.portal.system.mapper.OrganizationMapper.insertOrganizationAllColunmForNewTenant".equals(ms.getId())||
                		"com.xin.portal.system.mapper.RoleUserMapper.insertRoleUserAllColunmForNewTenant".equals(ms.getId())||//目前没有其他地方调用这个方法.
                		"com.xin.portal.system.mapper.UserMapper.insertUserAllColunmForNewTenant".equals(ms.getId())||
                		"com.xin.portal.system.mapper.UserInfoMapper.insertUserInfoAllColunmForNewTenant".equals(ms.getId())||
                		"com.xin.portal.system.mapper.ResourceMapper.insertResourceAllColunmForNewTenant".equals(ms.getId())||
                		"com.xin.portal.system.mapper.PermissionMapper.insertPermissionAllColunmForNewTenant".equals(ms.getId())||
                		"com.xin.portal.system.mapper.MenuMapper.insertMenuAllColunmForNewTenant".equals(ms.getId())||
                		"com.xin.portal.system.mapper.DictMapper.insertDictAllColunmForNewTenant".equals(ms.getId())||
                        "com.xin.portal.system.mapper.ResourceTypeMapper.insertResourceTypeAll".equals(ms.getId())||
                		"com.xin.portal.system.mapper.ListManageMapper.insertListManageAllColunmForNewTenant".equals(ms.getId())||
                		"com.xin.portal.system.mapper.OrganizationMapper.selectListWithNoTenant".equals(ms.getId())||
                        "com.xin.portal.system.mapper.RoleMapper.selectByCode".equals(ms.getId())||
                        "com.xin.portal.system.mapper.OrganizationMapper.selectByExtId".equals(ms.getId())||
                        "com.xin.portal.system.mapper.UserMapper.insertUserTD".equals(ms.getId())||
                        "com.xin.portal.system.mapper.RoleUserMapper.insertRoleUserTD".equals(ms.getId())||
                        "com.xin.portal.system.mapper.UserInfoMapper.insertUserInfoTD".equals(ms.getId())||
                        "com.xin.portal.system.mapper.DataPermissionTypeMapper.insertDataPermissionTypeAllColunmForNewTenant".equals(ms.getId())||
                        "com.xin.portal.system.mapper.UserOrganizationMapper.insertUserOrganizationForNewTenant".equals(ms.getId())
                		) {
                	
                    return true;
                }
                return false;
            }
        });
        paginationInterceptor.setDialectType(dataSource.getDbType());
        return paginationInterceptor;
    }
	
	@Bean
    public MybatisSqlSessionFactoryBean mybatisSqlSessionFactoryBean() {
        MybatisSqlSessionFactoryBean mybatisPlus = new MybatisSqlSessionFactoryBean();
        mybatisPlus.setDataSource(dataSource);
        mybatisPlus.setVfs(SpringBootVFS.class);
//        if (StringUtils.hasText(this.properties.getConfigLocation())) {
//            mybatisPlus.setConfigLocation(this.resourceLoader.getResource(this.properties.getConfigLocation()));
//        }
        if (!ObjectUtils.isEmpty(this.interceptors)) {
            mybatisPlus.setPlugins(this.interceptors);
        }
        MybatisConfiguration mc = new MybatisConfiguration();
        mc.setDefaultScriptingLanguage(MybatisXMLLanguageDriver.class);
        //mc.setLogImpl(StdOutImpl.class);
        mybatisPlus.setConfiguration(mc);
        mybatisPlus.setDatabaseIdProvider(getDatabaseIdProvider());
//        if (StringUtils.hasLength(this.properties.getTypeAliasesPackage())) {
//            mybatisPlus.setTypeAliasesPackage(this.properties.getTypeAliasesPackage());
//        }
//        if (StringUtils.hasLength(this.properties.getTypeHandlersPackage())) {
//            mybatisPlus.setTypeHandlersPackage(this.properties.getTypeHandlersPackage());
//        }
        if (!ObjectUtils.isEmpty(this.properties.resolveMapperLocations())) {
            mybatisPlus.setMapperLocations(this.properties.resolveMapperLocations());
        }
        return mybatisPlus;
    }
	
	@Bean
    public DatabaseIdProvider getDatabaseIdProvider() {
	    DatabaseIdProvider databaseIdProvider = new VendorDatabaseIdProvider();
	    Properties providerProp = new Properties();
	    providerProp.setProperty("Oracle", "oracle");
	    providerProp.setProperty("MySQL", "mysql");
	    //providerProp.setProperty("SQL Server", "sqlserver");
	    providerProp.setProperty("SQL Server", "sqlserver2005");
	    databaseIdProvider.setProperties(providerProp);
	    return databaseIdProvider;
    }
	
	

}
