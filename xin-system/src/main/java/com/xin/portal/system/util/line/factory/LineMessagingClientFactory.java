package com.xin.portal.system.util.line.factory;


import org.springframework.beans.factory.FactoryBean;
import org.springframework.stereotype.Component;

import com.linecorp.bot.client.ChannelTokenSupplier;
import com.linecorp.bot.client.LineMessagingClient;
import com.xin.portal.system.util.line.bean.LineBotProperties;

@Component
public class LineMessagingClientFactory implements FactoryBean<LineMessagingClient> {

    private LineBotProperties lineBotProperties;

    private ChannelTokenSupplier channelTokenSupplier;

    @Override
    public LineMessagingClient getObject() throws Exception {
        return LineMessagingClient
                .builder(channelTokenSupplier)
                .apiEndPoint(lineBotProperties.getApiEndPoint())
                .connectTimeout(lineBotProperties.getConnectTimeout())
                .readTimeout(lineBotProperties.getReadTimeout())
                .writeTimeout(lineBotProperties.getWriteTimeout())
                .build();
    }

    @Override
    public Class<?> getObjectType() {
        return LineBotProperties.class;
    }

    @Override
    public boolean isSingleton() {
        return false;
    }

    public void setLineBotPropertiesAndChannelTokenSupplier(LineBotProperties lineBotProperties,
            ChannelTokenSupplier channelTokenSupplier) {
        this.lineBotProperties = lineBotProperties;
        this.channelTokenSupplier =channelTokenSupplier;
    }

}
