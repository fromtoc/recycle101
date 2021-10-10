package com.xin.portal.system.util.line.factory;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.stereotype.Component;

import com.linecorp.bot.client.LineSignatureValidator;
import com.linecorp.bot.servlet.LineBotCallbackRequestParser;
import com.xin.portal.system.util.line.bean.LineBotProperties;

@Component
public class LineBotCallbackRequestParserFactory implements FactoryBean<LineBotCallbackRequestParser> {

    private LineSignatureValidator lineSignatureValidator;

    @Override
    public LineBotCallbackRequestParser getObject() throws Exception {
        return new LineBotCallbackRequestParser(lineSignatureValidator);
    }

    @Override
    public Class<?> getObjectType() {
        return LineBotProperties.class;
    }

    @Override
    public boolean isSingleton() {
        return false;
    }

    public void setLineSignatureValidator(LineSignatureValidator lineSignatureValidator) {
        this.lineSignatureValidator = lineSignatureValidator;
    }

}
