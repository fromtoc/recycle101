package com.xin.portal.system.interceptor;

import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.HandlerMapping;
import org.springframework.web.servlet.ModelAndView;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.io.ByteStreams;
import com.linecorp.bot.model.event.CallbackRequest;
import com.linecorp.bot.model.objectmapper.ModelObjectMapper;
import com.linecorp.bot.servlet.LineBotCallbackException;
import com.xin.portal.system.config.LineBotServerArgumentProcessor;
import com.xin.portal.system.service.ThirdAppParamService;
import com.xin.portal.system.util.line.annotaion.LineBotMessages;
import com.xin.portal.system.util.line.factory.LineBotCallbackRequestParserFactory;
import com.xin.portal.system.util.line.factory.LinePropertiesFactory;
import com.xin.portal.system.util.line.factory.LineSignatureValidatorFactory;

@Component
public class LineBotServerInterceptor implements HandlerInterceptor {

	@Autowired
	private LineSignatureValidatorFactory lineSignatureValidatorFactory;
	@Autowired
	private LinePropertiesFactory linePropertiesFactory;
	@Autowired
	private LineBotCallbackRequestParserFactory lineBotCallbackRequestParserFactory;
	@Autowired
	private ThirdAppParamService thirdAppParamService;

    private  final Logger log = LoggerFactory.getLogger(LineBotServerInterceptor.class);

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }

        final HandlerMethod hm = (HandlerMethod) handler;
        final MethodParameter[] methodParameters = hm.getMethodParameters();

        for (MethodParameter methodParameter : methodParameters) {
            if (methodParameter.getParameterAnnotation(LineBotMessages.class) == null) {
                continue;
            }
            try {
            	@SuppressWarnings("unchecked")
				Map<String, String> map = (Map<String, String>) request.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);
                String agentId = map.get("agentId");
                String tenantId = map.get("tenantId");
                linePropertiesFactory.setIdsAboutLine(tenantId, agentId);
                linePropertiesFactory.setLineBotProperties(thirdAppParamService.getAllLineProperties());
                lineSignatureValidatorFactory.setLineBotProperties(linePropertiesFactory.getObject());
                lineBotCallbackRequestParserFactory.setLineSignatureValidator(lineSignatureValidatorFactory.getObject());
                //final CallbackRequest callbackRequest = lineBotCallbackRequestParserFactory.getObject().handle(request);

            	ObjectMapper objectMapper = ModelObjectMapper.createNewObjectMapper();
                final byte[] json = ByteStreams.toByteArray(request.getInputStream());
                String jsonString = new String(json, StandardCharsets.UTF_8);
                final byte[] jsons = jsonString.getBytes(StandardCharsets.UTF_8);
                final CallbackRequest callbackRequest = objectMapper.readValue(jsons, CallbackRequest.class);
                
                LineBotServerArgumentProcessor.setValue(request, callbackRequest);
                return true;
            } catch (LineBotCallbackException e) {
                log.info("LINE Bot callback exception: {}", e.getMessage());
                response.sendError(HttpStatus.BAD_REQUEST.value());
                try (PrintWriter writer = response.getWriter()) {
                    writer.println(e.getMessage());
                }
                return false;
            }
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
                           ModelAndView modelAndView) throws Exception {
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler,
                                Exception ex) throws Exception {
    }
}
