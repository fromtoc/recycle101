package com.xin.portal.system.util.datasource;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.xin.portal.system.mapper.DataPermissionMapper;
import com.xin.portal.system.mapper.DatasourceMapper;
import com.xin.portal.system.mapper.DictMapper;
import com.xin.portal.system.model.DataPermission;
import com.xin.portal.system.model.Datasource;
import com.xin.portal.system.model.Dict;
import com.xin.portal.system.model.PromptRel;
import com.xin.portal.system.util.SessionUtil;

@Component
public class DataSourceUtils {
	
	@Autowired
	private DictMapper dictMapper;
	@Autowired
	private DatasourceMapper datasourceMapper;
	@Autowired
	private DataPermissionMapper dataPermissionMapper;
	
	public static DataSourceUtils dataSourceUtils;
	
	@PostConstruct
    public void init() {
		dataSourceUtils = this;
		dataSourceUtils.dictMapper = this.dictMapper;
		dataSourceUtils.datasourceMapper = this.datasourceMapper;
		dataSourceUtils.dataPermissionMapper = this.dataPermissionMapper;
    }

	public static Map<String, Object> getDictDatasourceValue(String dictCode) {
		//根据code获取数据源信息
		Dict query = new Dict();
		query.setDictCode(dictCode);
		Dict dict = dataSourceUtils.dictMapper.find(query);
		//根据数据源id获取数据源信息
		Datasource datasource = dataSourceUtils.datasourceMapper.selectById(dict.getSourceId());
		//通过数据源信息获取数据
		return DataSourceDaoUtil.getMapBySql(datasource, dict.getQuerySql());
	}
	
	public static Map<String, Object> getDictDatasourceValueWithTenantId(String dictCode, String tenantId) {
		//根据code获取数据源信息
		Dict query = new Dict();
		query.setDictCode(dictCode);
		query.setTenantId(tenantId);
		Dict dict = dataSourceUtils.dictMapper.findWithTenantId(query);
		//根据数据源id获取数据源信息
		Datasource datasource = dataSourceUtils.datasourceMapper.selectWithTenantId(dict.getSourceId(), tenantId);
		//通过数据源信息获取数据
		return DataSourceDaoUtil.getMapBySql(datasource, dict.getQuerySql());
	}
	
	public static List<PromptRel> DatasourceValueFunction(List<PromptRel> promptRelList){
		//初始化，下拉框，单选，复选等，数据来源是数据源的
		//根据字典获取字典信息
		if(promptRelList != null && promptRelList.size() > 0){
			for (PromptRel promptRel : promptRelList) {
				if (promptRel.getType() != null 
						&& (promptRel.getType() == 4 || promptRel.getType() == 5 
							|| promptRel.getType() == 6 || promptRel.getType() == 10)) {
					if(promptRel.getPromptType()!=null && "4".equals(promptRel.getPromptType())){//组织筛选
						//使用组织数据权限作为内容
						//根据用户和用户组织获取当前的数据权限内容
						List<DataPermission> dpList = dataSourceUtils.dataPermissionMapper.
								selectDPByUserId(SessionUtil.getUserInfo().getId(), promptRel.getSpecial());
						promptRel.setItems(dpList);
						List<String> dpCode = dpList.stream().map(DataPermission -> DataPermission.
								getExtcode()).collect(Collectors.toList());
						if(promptRel.getType() != null && (promptRel.getType() == 4 
								||promptRel.getType() == 5 )){//单选
							//判断是否有默认值,没有-直接使用第一个权限回答; 有-使用默认值 
							//获取全部数据权限 --特殊字段+组织id --> 数据权限
							if(promptRel.getDefaultValue1() ==null || promptRel.getDefaultValue1().length()<=0){
								//没有默认值，
								if(dpCode != null && dpCode.size() > 0){
									promptRel.setDefaultValue1(dpCode.get(0));
								}else{
									promptRel.setDefaultValue1("");//设置空
								}
							}else{
								//有默认值 默认值是否属于 数据权限中，不属于使用第一个，属于使用默认值
								String defaultValue = promptRel.getDefaultValue1();
								defaultValue = defaultBelongDataPer(dpCode, defaultValue, false);
								promptRel.setDefaultValue1(defaultValue);
							}
						}else if(promptRel.getType() != null && (promptRel.getType() == 10 
								|| promptRel.getType() == 6 )){//下拉多选，复选框
							if(promptRel.getDefaultValue1() ==null || promptRel.getDefaultValue1().length()<=0){
								//没有默认值，
								if(dpCode != null && dpCode.size() > 0){
									promptRel.setDefaultValue1(StringUtils.join(dpCode, ","));
								}else{
									promptRel.setDefaultValue1("");//设置空
								}
							}else{
								//有默认值 默认值是否属于 数据权限中，不属于使用全部，属于使用默认值
								String defaultValue = promptRel.getDefaultValue1();
								defaultValue = defaultBelongDataPer(dpCode, defaultValue, true);
								if(defaultValue == null || defaultValue.trim().length() ==0){
									promptRel.setDefaultValue1(StringUtils.join(dpCode, ","));//设置全部
								}else{
									promptRel.setDefaultValue1(defaultValue);
								}
							}
						}
					}else{
						//其他的使用 字典或者 数据源
						//下拉单选,单选按钮,复选框,下拉多选
						//初始化选项值，根据dictCode 获取字典值，如果dictCode是数据源来源，则从数据源中获取数据
						Dict query = new Dict();
						query.setDictCode(promptRel.getDictCode());
						Dict dict = dataSourceUtils.dictMapper.find(query);
						if (dict.getIsSource() != null && dict.getIsSource() == 1) {
							//从数据源中获取
							//根据数据源id获取数据源信息
							Datasource datasource = dataSourceUtils.datasourceMapper.selectById(dict.getSourceId());
							List<Dict> list = DataSourceDaoUtil.getListValueAsDictBySql(datasource, dict.getQuerySql());
							promptRel.setItems(list);
						}else{
							//直接从选项值中获取
							List<Dict> list = dataSourceUtils.dictMapper.findItemList(query);
							promptRel.setItems(list);
						}
						//判断difault1
						if(promptRel.getDefaultValue1() != null && promptRel.getDefaultValue1().trim().length() > 0){
							//非空，说明有默认值 //TODO 先不作处理，有默认值就直接使用默认值回答，所以暂时不需要其他设置
							
						}
					}
				}
				if (promptRel.getType() != null 
						&& promptRel.getType() == 7) {//数字区间//TODO 暂时不实现
					//判断default1  和default2
					if(promptRel.getDefaultValue1() != null && promptRel.getDefaultValue1().trim().length() > 0){
						//开始非空，说明有默认值 
					}
					if(promptRel.getDefaultValue3() != null && promptRel.getDefaultValue3().trim().length() > 0){
						//非空，说明有默认值
					}
				}
			}
		}
		return promptRelList;
	}
	
	public static boolean isLegal(String sql){
		//检测SQL语句是否有update delete drop alter truncate create grant revoke 等不合法词汇
		boolean isLegal = true;
		if(sql != null && sql.trim().length() > 0){
			//先去掉注释的符号，然后将SQL转换为小写，获取不合法词汇
			String replaceSql = sql.replaceAll("#", "").replaceAll("--", "").replaceAll("/\\*", "")
			.replaceAll("//", "").replaceAll("\\*/", "");
			replaceSql = replaceSql.toLowerCase();
			System.out.println(replaceSql);
			if(replaceSql == null || replaceSql.trim().length() == 0){
				isLegal = true;
			}else if(replaceSql.indexOf("update") > -1 || replaceSql.indexOf("delete") > -1 
					|| replaceSql.indexOf("drop") > -1 || replaceSql.indexOf("alter") > -1 
					|| replaceSql.indexOf("truncate") > -1 || replaceSql.indexOf("create") > -1 
					|| replaceSql.indexOf("grant") > -1 || replaceSql.indexOf("revoke") > -1 ){
				isLegal = false;
			}
		}
		return isLegal;
	}
	
	private static String defaultBelongDataPer(List<String> dpList, String defaultValue, boolean isMultiple) {
		if(isMultiple){//多
			if(dpList != null && dpList.size() > 0){
				if(defaultValue.indexOf(",") > -1){
					List<String> res = new ArrayList<String>();
					String[] defaults = defaultValue.split(",");
					for (String value : defaults) {
						if(dpList.contains(value)){
							res.add(value);
						}
					}
					return StringUtils.join(res, ",");
				}else{
					if(dpList.contains(defaultValue)){
						return defaultValue;
					}
				}
			}
		}else{//单
			if(dpList != null && dpList.size() > 0){
				if(dpList.contains(defaultValue)){
					return defaultValue;
				}
			}
		}
		return null;
	}

	public static List<PromptRel> DatasourceValueFunctionWithTenantId(List<PromptRel> promptRelList, String tenantId) {
		//初始化，下拉框，单选，复选等，数据来源是数据源的
		//根据字典获取字典信息
		if(promptRelList != null && promptRelList.size() > 0){
			for (PromptRel promptRel : promptRelList) {
				if (promptRel.getType() != null 
						&& (promptRel.getType() == 4 || promptRel.getType() == 5 
							|| promptRel.getType() == 6 || promptRel.getType() == 10)) {
					if(promptRel.getPromptType()!=null && "4".equals(promptRel.getPromptType())){//组织筛选
						//使用组织数据权限作为内容
						//根据用户获取当前的数据权限内容--用户组织关系多组织，用户自身
						List<DataPermission> dpList = dataSourceUtils.dataPermissionMapper.
								selectDPByUserIdWithTenantId(SessionUtil.getUserInfo().getId(),
										promptRel.getSpecial(),tenantId);
						promptRel.setItems(dpList);
						List<String> dpCode = dpList.stream().map(DataPermission -> DataPermission.
								getExtcode()).collect(Collectors.toList());
						if(promptRel.getType() != null && (promptRel.getType() == 4 
								||promptRel.getType() == 5 )){//单选
							//判断是否有默认值,没有-直接使用第一个权限回答; 有-使用默认值 
							//获取全部数据权限 --特殊字段+组织id --> 数据权限
							if(promptRel.getDefaultValue1() ==null || promptRel.getDefaultValue1().length()<=0){
								//没有默认值，
								if(dpCode != null && dpCode.size() > 0){
									promptRel.setDefaultValue1(dpCode.get(0));
								}else{
									promptRel.setDefaultValue1("");//设置空
								}
							}else{
								//有默认值 默认值是否属于 数据权限中，不属于使用第一个，属于使用默认值
								String defaultValue = promptRel.getDefaultValue1();
								defaultValue = defaultBelongDataPer(dpCode, defaultValue, false);
								promptRel.setDefaultValue1(defaultValue);
							}
						}else if(promptRel.getType() != null && (promptRel.getType() == 10 
								|| promptRel.getType() == 6 )){//下拉多选，复选框
							if(promptRel.getDefaultValue1() ==null || promptRel.getDefaultValue1().length()<=0){
								//没有默认值，
								if(dpCode != null && dpCode.size() > 0){
									promptRel.setDefaultValue1(StringUtils.join(dpCode, ","));
								}else{
									promptRel.setDefaultValue1("");//设置空
								}
							}else{
								//有默认值 默认值是否属于 数据权限中，不属于使用全部，属于使用默认值
								String defaultValue = promptRel.getDefaultValue1();
								defaultValue = defaultBelongDataPer(dpCode, defaultValue, true);
								if(defaultValue == null || defaultValue.trim().length() ==0){
									promptRel.setDefaultValue1(StringUtils.join(dpCode, ","));//设置全部
								}else{
									promptRel.setDefaultValue1(defaultValue);
								}
							}
						}
					}else{
						//其他的使用 字典或者 数据源
						//下拉单选,单选按钮,复选框,下拉多选
						//初始化选项值，根据dictCode 获取字典值，如果dictCode是数据源来源，则从数据源中获取数据
						Dict query = new Dict();
						query.setDictCode(promptRel.getDictCode());
						query.setTenantId(tenantId);
						Dict dict = dataSourceUtils.dictMapper.findWithTenantId(query);
						if (dict.getIsSource() != null && dict.getIsSource() == 1) {
							//从数据源中获取
							//根据数据源id获取数据源信息
							Datasource datasource = dataSourceUtils.datasourceMapper.selectWithTenantId(dict.getSourceId(), tenantId);
							List<Dict> list = DataSourceDaoUtil.getListValueAsDictBySql(datasource, dict.getQuerySql());
							promptRel.setItems(list);
						}else{
							//直接从选项值中获取
							List<Dict> list = dataSourceUtils.dictMapper.findItemListWithTenantId(query);
							promptRel.setItems(list);
						}
						//判断difault1
						if(promptRel.getDefaultValue1() != null && promptRel.getDefaultValue1().trim().length() > 0){
							//非空，说明有默认值 //TODO 先不作处理，有默认值就直接使用默认值回答，所以暂时不需要其他设置
							
						}
					}
				}
				if (promptRel.getType() != null 
						&& promptRel.getType() == 7) {//数字区间//TODO 暂时不实现
					//判断default1  和default2
					if(promptRel.getDefaultValue1() != null && promptRel.getDefaultValue1().trim().length() > 0){
						//开始非空，说明有默认值 
					}
					if(promptRel.getDefaultValue3() != null && promptRel.getDefaultValue3().trim().length() > 0){
						//非空，说明有默认值
					}
				}
			}
		}
		return promptRelList;
	}

}
