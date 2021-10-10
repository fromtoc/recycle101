package com.xin.portal.system.config.freemarker;

import com.jagregory.shiro.freemarker.ShiroTags;
import com.xin.portal.system.config.LocaleTags;
import freemarker.template.Configuration;
import freemarker.template.TemplateModelException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class FreemarkerTagConfig {
	
	@Autowired  
    protected Configuration  configuration;  
	@Autowired
	private LocaleTags localeTags;
	@Autowired
	private PathTag pathTag;
	
	  @PostConstruct
	  public void setSharedVariable() throws TemplateModelException {
	    configuration.setSharedVariable("shiro", new ShiroTags());
	    configuration.setSharedVariable("locale", localeTags);
	    configuration.setSharedVariable("navPath", pathTag);
	  }
}
