package com.xin.portal.system.util.line.factory;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.stereotype.Component;

import com.linecorp.bot.client.ChannelManagementSyncClient;
import com.linecorp.bot.client.ChannelTokenSupplier;
import com.xin.portal.system.util.line.bean.LineBotProperties;

@Component
public class ChannelManagementSyncClientFactory implements FactoryBean<ChannelManagementSyncClient> {

    private ChannelTokenSupplier channelTokenSupplier;

    @Override
    public ChannelManagementSyncClient getObject() throws Exception {
        return ChannelManagementSyncClient.builder(channelTokenSupplier)
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

    public void setChannelTokenSupplier(ChannelTokenSupplier channelTokenSupplier) {
        this.channelTokenSupplier = channelTokenSupplier;
    }

}
