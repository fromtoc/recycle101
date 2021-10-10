package com.xin.portal.system.config;

import javax.servlet.http.HttpServletRequest;

import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import com.linecorp.bot.model.event.CallbackRequest;
import com.xin.portal.system.util.line.annotaion.LineBotMessages;

@Component
public class LineBotServerArgumentProcessor implements HandlerMethodArgumentResolver {
    private static final String PROPERTY_NAME = "com.linecorp.bot.spring.callbackRequest";

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(LineBotMessages.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest, WebDataBinderFactory binderFactory)
            throws Exception {
        return webRequest.getAttribute(PROPERTY_NAME, RequestAttributes.SCOPE_REQUEST);
    }

    public static void setValue(HttpServletRequest request, CallbackRequest callbackRequest) {
        request.setAttribute(PROPERTY_NAME, callbackRequest.getEvents());
    }
}

