package com.xin.portal.system.util.line.factory;

import java.util.Map;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.stereotype.Component;

import com.xin.portal.system.util.line.bean.LineBotProperties;

@Component
public class LinePropertiesFactory implements FactoryBean<LineBotProperties> {

    private LineBotProperties lineBotProperties;

    private static String tenantId = null;//租户id

    private static String agentId = null;//应用id

    private static String channelId;

    private static String channelToken;

    private static String channelSecret;

    @Override
    public LineBotProperties getObject() throws Exception {
        if (lineBotProperties != null) {
            return lineBotProperties;
        }
        return new LineBotProperties(channelId, channelToken, channelSecret);
    }

    @Override
    public Class<?> getObjectType() {
        return LineBotProperties.class;
    }

    @Override
    public boolean isSingleton() {
        return false;
    }

    public void setIdsAboutLine(String tenantId, String agentId) {
        LinePropertiesFactory.tenantId = tenantId;
        LinePropertiesFactory.agentId = agentId;
    }

    public void setLineBotProperties(Map<String, LineBotProperties> map) {
        if (LinePropertiesFactory.tenantId != null && LinePropertiesFactory.agentId != null) {
            lineBotProperties = map.get(LinePropertiesFactory.tenantId 
            		+ "_" + LinePropertiesFactory.agentId);
        }
    }

    public void setLineBotProperties(String channelId, String channelToken, String channelSecret) {
        LinePropertiesFactory.channelId = channelId;
        LinePropertiesFactory.channelToken = channelToken;
        LinePropertiesFactory.channelSecret = channelSecret;
    }

}
