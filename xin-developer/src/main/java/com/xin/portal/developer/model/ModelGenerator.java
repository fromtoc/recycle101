package com.xin.portal.developer.model;

public class ModelGenerator {
	
	public static String TYPE_VUE = "vue";
	public static String TYPE_HTML_VUE = "html_vue";
	public static String TYPE_HTML_LAYUI = "html_layui";
	public static String TYPE_HTML_MATERIAL = "html_material";
	
	private String name;
	private String outputDir;
	private String author;
	private String dbType;
	private String dbUsername;
	private String dbPassword;
	private String dbUrl;
	private String tablePrefix;
	private String tableName;
	private String superController;
	private boolean restControllerStyle;
	private String packageDir;
	private boolean page;
	private boolean api;//是否添加接口注释
	private String webFileType;
	private String frontType;//前端页面类型：1-不生成 2-html 3-vue
	private String frontDir;//前端页面地址
	private String module;
	private boolean hasSave;//
	private boolean hasUpdate;//
	private boolean hasDelete;//
	
	
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getOutputDir() {
		return outputDir;
	}
	public void setOutputDir(String outputDir) {
		this.outputDir = outputDir;
	}
	public String getAuthor() {
		return author;
	}
	public void setAuthor(String author) {
		this.author = author;
	}
	
	public String getDbType() {
		return dbType;
	}
	public void setDbType(String dbType) {
		this.dbType = dbType;
	}
	public String getDbUsername() {
		return dbUsername;
	}
	public void setDbUsername(String dbUsername) {
		this.dbUsername = dbUsername;
	}
	public String getDbPassword() {
		return dbPassword;
	}
	public void setDbPassword(String dbPassword) {
		this.dbPassword = dbPassword;
	}
	public String getDbUrl() {
		return dbUrl;
	}
	public void setDbUrl(String dbUrl) {
		this.dbUrl = dbUrl;
	}
	public String getTablePrefix() {
		return tablePrefix;
	}
	public void setTablePrefix(String tablePrefix) {
		this.tablePrefix = tablePrefix;
	}
	public String getTableName() {
		return tableName;
	}
	public void setTableName(String tableName) {
		this.tableName = tableName;
	}
	public String getSuperController() {
		return superController;
	}
	public void setSuperController(String superController) {
		this.superController = superController;
	}
	public boolean isRestControllerStyle() {
		return restControllerStyle;
	}
	public void setRestControllerStyle(boolean restControllerStyle) {
		this.restControllerStyle = restControllerStyle;
	}
	public String getPackageDir() {
		return packageDir;
	}
	public void setPackageDir(String packageDir) {
		this.packageDir = packageDir;
	}
	public boolean isPage() {
		return page;
	}
	public void setPage(boolean page) {
		this.page = page;
	}
	public String getWebFileType() {
		return webFileType;
	}
	public void setWebFileType(String webFileType) {
		this.webFileType = webFileType;
	}
	public Boolean getApi() {
		return api;
	}
	public void setApi(Boolean api) {
		this.api = api;
	}
	public String getFrontType() {
		return frontType;
	}
	public void setFrontType(String frontType) {
		this.frontType = frontType;
	}
	public String getFrontDir() {
		return frontDir;
	}
	public void setFrontDir(String frontDir) {
		this.frontDir = frontDir;
	}
	public String getModule() {
		return module;
	}
	public void setModule(String module) {
		this.module = module;
	}
	public boolean isHasSave() {
		return hasSave;
	}
	public void setHasSave(boolean hasSave) {
		this.hasSave = hasSave;
	}
	public boolean isHasUpdate() {
		return hasUpdate;
	}
	public void setHasUpdate(boolean hasUpdate) {
		this.hasUpdate = hasUpdate;
	}
	public boolean isHasDelete() {
		return hasDelete;
	}
	public void setHasDelete(boolean hasDelete) {
		this.hasDelete = hasDelete;
	}
	
	
	

}
