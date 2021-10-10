package com.xin.portal.system.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.async.DeferredResult;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.service.VendorExtension;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;

@Configuration
@EnableSwagger2
@ConditionalOnProperty(name = "swagger.enabled")
public class Swagger2Config {

//	@Bean
//	public Docket createRestApi() {
//		return new Docket(DocumentationType.SWAGGER_2)
//				.apiInfo(apiInfo())
//				.select()
//				.apis(RequestHandlerSelectors.basePackage("com.xin.portal"))
//				.paths(PathSelectors.any())
//				.build();
//	}
//	
//	private ApiInfo apiInfo() {
//		return new ApiInfoBuilder()
//				.title("portal api文档")
////				.description("简单优雅的restfun风格，http://blog.csdn.net/saytime")
////				.termsOfServiceUrl("http://blog.csdn.net/saytime")
//				.version("1.0")
//				.build();
//	}
	
	
	
	@Bean
	public Docket ProductApi() {
		return new Docket(DocumentationType.SWAGGER_2)
				.genericModelSubstitutes(DeferredResult.class)
				.useDefaultResponseMessages(false)
				.forCodeGeneration(false)
				.pathMapping("/")
				.select()
				.build()
				.apiInfo(productApiInfo());
	}

	private ApiInfo productApiInfo() {
        //ApiInfo apiInfo = new ApiInfo(title, description, version, termsOfServiceUrl, contact, license, licenseUrl, vendorExtensions)
        Contact contact = new Contact("zhoujun","www.dataondemand.cn","zhoujun@dataondemand.cn");
        ApiInfo apiInfo = new ApiInfo("某API接口",//大标题
                "REST风格API",//小标题
                "0.1",//版本
                "www.baidu.com",
                contact,//作者
                "主页",//链接显示文字
                "",//网站链接
                new ArrayList<VendorExtension>()
        );
		return apiInfo;
	}
}