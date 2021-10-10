package com.xin.portal.system.util.line.factory;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.linecorp.bot.client.ChannelManagementSyncClient;
import com.linecorp.bot.client.ChannelTokenSupplier;
import com.linecorp.bot.client.FixedChannelTokenSupplier;
import com.linecorp.bot.client.LineMessagingClient;
import com.linecorp.bot.client.LineSignatureValidator;
import com.linecorp.bot.servlet.LineBotCallbackRequestParser;
import com.xin.portal.system.util.line.bean.LineBotProperties;
import com.xin.portal.system.mapper.ThirdAppParamMapper;
import com.xin.portal.system.model.ThirdAppParam;

public class LineMessageFactory implements FactoryBean<LineBotProperties>{
    //最终目的创建LineClient的实例
    //private LineMessagingClient lineMessagingClient;
    //
    //private static LineBotProperties lineBotProperties;
    
    private static Map<String, LineBotProperties> LinePropertiesMap = new HashMap<String, LineBotProperties>();
    
   // private ChannelTokenSupplier channelTokenSupplier;
    
    //private LineBotCallbackRequestParser lineBotCallbackRequestParser;
    //
    //private ChannelManagementSyncClient channelManagementSyncClient;
	
	@Autowired
	private ThirdAppParamMapper thirdAppParamMapper;
	
	//获取properties
	public Map<String, LineBotProperties> initLineBotProperties(String tenantId){
		if (LinePropertiesMap.isEmpty()) {
			EntityWrapper<ThirdAppParam> ew = new EntityWrapper<>();
			ew.eq("tenant_id", tenantId);
			ew.eq("app_type", ThirdAppParam.APP_TYPE_LINE);
			List<ThirdAppParam> selectList = thirdAppParamMapper.selectList(ew);
			LineBotProperties line = null;
			if (selectList != null && selectList.size() > 0) {
	            for (ThirdAppParam thirdAppParam : selectList) {
	            	line = new LineBotProperties();
	            	line.setChannelSecret(thirdAppParam.getAppSecret());
	            	line.setChannelToken(thirdAppParam.getAppToken());
	            	line.setChannelId(thirdAppParam.getAppId());
	            	LinePropertiesMap.put(thirdAppParam.getAppId(), line);
				}
			}
		}
//    	LineBotProperties lineBotProperties = new LineBotProperties();
//    	lineBotProperties.setChannelSecret("54dfe2d9279a9c4cebe2ee47edc8b0ed");
//		lineBotProperties.setChannelToken("Vr6SN2Z2tgI+pscI0v7qAJLesGOCSYtKSZxD1350XG/lMr3yhqGpvFFs0xtecls+M8ou4ZctLADqS4iJl+G4+cy+kkNIcnUWtx6oJ6Mwo8O14ZRa3Sq61FMZZTfWnGNzqDN2dWmExd41vf9aik9vGAdB04t89/1O/w1cDnyilFU=");
    	return LinePropertiesMap;
    }
	
	public Map<String, LineBotProperties> initLineBotProperties(){
		if (!LinePropertiesMap.isEmpty()) {
			EntityWrapper<ThirdAppParam> ew = new EntityWrapper<>();
			ew.eq("app_type", ThirdAppParam.APP_TYPE_LINE);
			List<ThirdAppParam> selectList = thirdAppParamMapper.selectList(ew);
			LineBotProperties line = null;
			if (selectList != null && selectList.size() > 0) {
	            for (ThirdAppParam thirdAppParam : selectList) {
	            	line = new LineBotProperties();
	            	line.setChannelSecret(thirdAppParam.getAppSecret());
	            	line.setChannelToken(thirdAppParam.getAppToken());
	            	line.setChannelId(thirdAppParam.getAppId());
	            	LinePropertiesMap.put(thirdAppParam.getAppId()+"_"+thirdAppParam.getTenantId(), line);
				}
			}
		}
//    	LineBotProperties lineBotProperties = new LineBotProperties();
//    	lineBotProperties.setChannelSecret("54dfe2d9279a9c4cebe2ee47edc8b0ed");
//		lineBotProperties.setChannelToken("Vr6SN2Z2tgI+pscI0v7qAJLesGOCSYtKSZxD1350XG/lMr3yhqGpvFFs0xtecls+M8ou4ZctLADqS4iJl+G4+cy+kkNIcnUWtx6oJ6Mwo8O14ZRa3Sq61FMZZTfWnGNzqDN2dWmExd41vf9aik9vGAdB04t89/1O/w1cDnyilFU=");
    	return LinePropertiesMap;
    }
	
	
	//完成其他的实例
	public LineBotCallbackRequestParser lineBotCallbackRequestParser(String agintId, String tenantId) {
		LineSignatureValidator lineSignatureValidator =  lineSignatureValidator(agintId, tenantId);
		return new LineBotCallbackRequestParser(lineSignatureValidator);
	}
	
	public ChannelTokenSupplier channelTokenSupplier(String agintId){
		LineBotProperties lineBotProperties = LinePropertiesMap.get(agintId);
		return FixedChannelTokenSupplier.of(lineBotProperties.getChannelToken());
	}
	
	//
	public ChannelManagementSyncClient channelManagementClient(String agintId){
		ChannelTokenSupplier channelTokenSupplier = channelTokenSupplier(agintId);
        return ChannelManagementSyncClient.builder(channelTokenSupplier).build();  
	}
	//获取clent
	public LineMessagingClient getLineMessagingClient(String agintId, String tenantId){
		LineBotProperties lineBotProperties = LinePropertiesMap.get(agintId+"_"+tenantId);
		ChannelTokenSupplier channelTokenSupplier = FixedChannelTokenSupplier.of(lineBotProperties.getChannelToken());
	    return LineMessagingClient
	            .builder(channelTokenSupplier)
	            .apiEndPoint(lineBotProperties.getApiEndPoint())
	            .connectTimeout(lineBotProperties.getConnectTimeout())
	            .readTimeout(lineBotProperties.getReadTimeout())
	            .writeTimeout(lineBotProperties.getWriteTimeout())
	            .build();
	}
	
	//获取密钥
	public LineSignatureValidator lineSignatureValidator(String agintId, String tenantId) {
		LineBotProperties lineBotProperties = LinePropertiesMap.get(agintId+"_"+tenantId);
		return new LineSignatureValidator(
        		lineBotProperties.getChannelSecret().getBytes(StandardCharsets.US_ASCII));
	}

	public List<LineSignatureValidator> lineSignatureValidator() {
		List<LineSignatureValidator> list = new ArrayList<>();
		List<LineBotProperties> lineBotProperties = get();
		LineSignatureValidator validator = null;
		if(!lineBotProperties.isEmpty()){
			for (LineBotProperties line : lineBotProperties) {
				validator = new  LineSignatureValidator(
						line.getChannelSecret().getBytes(StandardCharsets.US_ASCII));
				list.add(validator);
			}
		}
		return list;
	}
	
	public List<LineBotProperties> getByanintId(String agintId){
		List<LineBotProperties> list = new ArrayList<LineBotProperties>();
		if(!LinePropertiesMap.isEmpty()){
			for(Map.Entry<String, LineBotProperties> line:LinePropertiesMap.entrySet()){
				if (line.getKey().indexOf(agintId) > -1) {
					list.add(line.getValue());
				}
			}
		}
		return list;
	}
	
	public List<LineBotProperties> getBytenantId(String tenantId){
		List<LineBotProperties> list = new ArrayList<LineBotProperties>();
		if(!LinePropertiesMap.isEmpty()){
			for(Map.Entry<String, LineBotProperties> line:LinePropertiesMap.entrySet()){
				if (line.getKey().indexOf(tenantId) > -1) {
					list.add(line.getValue());
				}
			}
		}
		return list;
	}
	
	public List<LineBotProperties> get(){
		List<LineBotProperties> list = new ArrayList<LineBotProperties>();
		if(!LinePropertiesMap.isEmpty()){
			for(Map.Entry<String, LineBotProperties> line:LinePropertiesMap.entrySet()){
				list.add(line.getValue());
			}
		}
		return list;
	}
	
	private LineBotProperties lineBotProperties;
	
	private String tenantId = null;//租户id
	private String agentId = null;//应用id

	@Override
	public LineBotProperties getObject() throws Exception {
		return lineBotProperties;
	}

	@Override
	public Class<?> getObjectType() {
		return LineBotProperties.class;
	}
	
	@Override
    public boolean isSingleton() {
        return true;
    }
	
	public void getLineBotProperties(String tenantId,String agentId){
		this.tenantId = tenantId;
		this.agentId = agentId;
		queryByIds(this.tenantId, this.agentId);
	}
	
	private LineBotProperties queryByIds(String tenantId,String agentId){
		EntityWrapper<ThirdAppParam> ew = new EntityWrapper<>();
		ew.eq("tenant_id", tenantId);
		ew.eq("app_id", agentId);
		ew.eq("app_type", ThirdAppParam.APP_TYPE_LINE);
		List<ThirdAppParam> selectList = thirdAppParamMapper.selectList(ew);
		if (selectList != null && selectList.size() > 0) {
        	lineBotProperties = new LineBotProperties();
        	lineBotProperties.setChannelSecret(selectList.get(0).getAppSecret());
        	lineBotProperties.setChannelToken(selectList.get(0).getAppToken());
        	lineBotProperties.setChannelId(selectList.get(0).getAppId());
		}
		return lineBotProperties;
	}
	
}
