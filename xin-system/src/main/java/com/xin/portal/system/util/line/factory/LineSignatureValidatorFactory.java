package com.xin.portal.system.util.line.factory;

import java.nio.charset.StandardCharsets;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.stereotype.Component;

import com.linecorp.bot.client.LineSignatureValidator;
import com.xin.portal.system.util.line.bean.LineBotProperties;

@Component
public class LineSignatureValidatorFactory implements FactoryBean<LineSignatureValidator> {

    private LineBotProperties lineBotProperties;

    @Override
    public LineSignatureValidator getObject() throws Exception {
        return new LineSignatureValidator(
                lineBotProperties.getChannelSecret().getBytes(StandardCharsets.US_ASCII));
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
