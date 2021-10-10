package com.xin.portal.system.util.line.factory;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.stereotype.Component;

import com.linecorp.bot.client.ChannelTokenSupplier;
import com.linecorp.bot.client.FixedChannelTokenSupplier;
import com.xin.portal.system.util.line.bean.LineBotProperties;

@Component
public class ChannelTokenSupplierFactory implements FactoryBean<ChannelTokenSupplier> {

    private LineBotProperties lineBotProperties;

    @Override
    public ChannelTokenSupplier getObject() throws Exception {
        return FixedChannelTokenSupplier.of(lineBotProperties.getChannelToken());
    }

    @Override
    public Class<?> getObjectType() {
        return LineBotProperties.class;
    }

    @Override
    public boolean isSingleton() {
        return false;
    }

    public void setLineBotProperties(LineBotProperties lineBotProperties) {
        this.lineBotProperties = lineBotProperties;
    }

}
