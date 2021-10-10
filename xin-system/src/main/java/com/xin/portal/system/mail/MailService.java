package com.xin.portal.system.mail;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.toolkit.StringUtils;
import com.sun.mail.util.MailSSLSocketFactory;
import com.xin.portal.system.bean.AttrModel;
import com.xin.portal.system.model.Config;
import com.xin.portal.system.service.ConfigService;
import com.xin.portal.system.util.Constant.ConfigKeys;

import freemarker.template.Template;

@Service
//@ConditionalOnProperty(name = "mail.enabled")
public class MailService {
	
	private final Logger logger = LoggerFactory.getLogger(MailService.class);
	
	private Map<String, String> mailInfoMap = null;
    
	@Autowired
    private FreeMarkerConfigurer freeMarker;// 邮件模板
	@Autowired
	private ConfigService configService; 
      
    public boolean send(MailModel mailModel, boolean isHtml) throws Exception {
        MimeMessage message = null;
        boolean flag = true;
        try {
            message = getMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "utf-8");
            //设置自定义发件人昵称  
            String nick="";  
            try {  
                nick=javax.mail.internet.MimeUtility.encodeText(mailInfoMap.get(ConfigKeys.MAIL_ACCOUNT));  
            } catch (UnsupportedEncodingException e) {  
                e.printStackTrace();  
            }   
            helper.setFrom(new InternetAddress(nick+" <"+(StringUtils.isEmpty(mailModel.getFromAddress()) ? mailInfoMap.get(ConfigKeys.MAIL_FROM) : mailModel.getFromAddress())+">"));
            helper.setTo(mailModel.getToAddress());
            helper.setSubject(mailModel.getSubject());
            helper.setText(mailModel.getContent(), isHtml);
            // 开始发送
//            mailSender.send(message);
         //   message.saveChanges();
            Transport.send(message);
        } catch (Exception e) {
            flag = false;
            e.printStackTrace();
        }
        return flag;
    }
    
    public boolean send(MailModel mailModel, List<Config> configList, boolean isHtml) throws Exception {
        MimeMessage message = null;
        boolean flag = true;
        try {
            message = getMimeMessage(configList);
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "utf-8");
            //设置自定义发件人昵称  
            String nick="";  
            try {  
                nick=javax.mail.internet.MimeUtility.encodeText(mailInfoMap.get(ConfigKeys.MAIL_ACCOUNT));  
            } catch (UnsupportedEncodingException e) {  
                e.printStackTrace();  
            }   
            helper.setFrom(new InternetAddress(nick+" <"+(StringUtils.isEmpty(mailModel.getFromAddress()) ? mailInfoMap.get(ConfigKeys.MAIL_FROM) : mailModel.getFromAddress())+">"));
            helper.setTo(mailModel.getToAddress());
            helper.setSubject(mailModel.getSubject());
            helper.setText(mailModel.getContent(), isHtml);
            // 开始发送
//            mailSender.send(message);
         //   message.saveChanges();
            Transport.send(message);
        } catch (Exception e) {
            flag = false;
            e.printStackTrace();
        }
        return flag;
    }
    
    private MimeMessage getMimeMessage(List<Config> configList) {
    	getMainInfo(configList);
    	if (prop==null) {
    		prop = new Properties();
    		prop.setProperty("mail.transport.protocol", mailInfoMap.get("MAIL_PROTOCOL"));
    		//服务器
    		prop.setProperty("mail.smtp.host", mailInfoMap.get(ConfigKeys.MAIL_HOST));
    		//端口
    		prop.setProperty("mail.smtp.port", mailInfoMap.get(ConfigKeys.MAIL_PORT));
    		//使用smtp身份验证
    		prop.setProperty("mail.smtp.auth", mailInfoMap.get("MAIL_AUTH"));
    		//协议
    		//使用SSL，企业邮箱必需！
    		//开启安全协议
    		MailSSLSocketFactory sf = null;
    		try {
    			sf = new MailSSLSocketFactory();
    			sf.setTrustAllHosts(true);
    		} catch (GeneralSecurityException e) {
    			e.printStackTrace();
    		}
    		prop.put("mail.smtp.ssl.enable", "true");
    		prop.put("mail.smtp.ssl.socketFactory", sf);
    	}
        Session session = Session.getDefaultInstance(prop, new MyAuthenricator(mailInfoMap.get(ConfigKeys.MAIL_FROM), mailInfoMap.get(ConfigKeys.MAIL_PASSWORD)));
        session.setDebug(Boolean.valueOf(mailInfoMap.get("MAIL_DEBUG")));
        return new MimeMessage(session);
	}

	private Map<String, String> getMainInfo(List<Config> configList) {
		//从数据库中获取到邮箱信息
    	Map<String, String> map = new HashMap<>();
    	if(configList!=null && configList.size()>0){
    		for (Config config : configList) {
    			map.put(config.getCode(), config.getValue());
    		}
    		map.put("MAIL_AUTH", "true");
    		map.put("MAIL_PROTOCOL", "smtp");
    		map.put("MAIL_DEBUG", "true");
    		mailInfoMap = map;
    		return map;
    	}
    	return null;
	}

	public boolean sendTemplateMail(MailModel mailModel, String templatePath, Map<String, Object> data) throws Exception {
        MimeMessage message = null;
        boolean flag = true;
        try {
            message = getMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "utf-8");
            String nick="";  
            try {  
                nick=javax.mail.internet.MimeUtility.encodeText(mailInfoMap.get(ConfigKeys.MAIL_ACCOUNT));  
            } catch (UnsupportedEncodingException e) {  
                e.printStackTrace();  
            }  
            helper.setFrom(new InternetAddress(nick+" <"+(StringUtils.isEmpty(mailModel.getFromAddress()) ? mailInfoMap.get(ConfigKeys.MAIL_FROM) : mailModel.getFromAddress())+">"));
            helper.setTo(mailModel.getToAddress());
            helper.setSubject(mailModel.getSubject());
            data.put("content", mailModel.getContent());
            data.put("hasImg", mailModel.getImgs()==null ? false : true);
            // 读取邮件 html 模板
            Template template = freeMarker.getConfiguration().getTemplate(templatePath,"utf-8");
            String html = FreeMarkerTemplateUtils.processTemplateIntoString(template, data);
            logger.info("===邮件内容===");
            logger.info(html);
            
            helper.setText(html, true);
            /*List<String> imgs = mailModel.getImgs();
            if (imgs!=null) {
            	data.put("imgs", imgs);
            	for (int i=0;i<imgs.size();i++) {
            		FileSystemResource img = new FileSystemResource(new File(imgs.get(i)));
            		helper.addInline("img_"+i, img);
            	}
            }*/
            
            List<AttrModel> attrs = mailModel.getAttrs();
            if (attrs!=null) {
            	for (AttrModel attrModel : attrs) {
            		FileSystemResource attr = new FileSystemResource(new File(attrModel.getSavedPath()));
            		helper.addAttachment(attrModel.getNameBefore(), attr);
            	}
            }
            // 开始发送
            //message.saveChanges();
            Transport.send(message);
            //mailSender.send(message);
            logger.info("===邮件发送完成===");
        } catch (Exception e) {
            flag = false;
            e.printStackTrace();
        }
        return flag;
    }
    
    private Properties prop;
    
    private MimeMessage getMimeMessage() {
    	getMainInfo();
    	if (prop==null) {
    		prop = new Properties();
    		prop.setProperty("mail.transport.protocol", mailInfoMap.get("MAIL_PROTOCOL"));
    		//服务器
    		prop.setProperty("mail.smtp.host", mailInfoMap.get(ConfigKeys.MAIL_HOST));
    		//端口
    		prop.setProperty("mail.smtp.port", mailInfoMap.get(ConfigKeys.MAIL_PORT));
    		//使用smtp身份验证
    		prop.setProperty("mail.smtp.auth", mailInfoMap.get("MAIL_AUTH"));
    		//协议
    		//使用SSL，企业邮箱必需！
    		//开启安全协议
    		MailSSLSocketFactory sf = null;
    		try {
    			sf = new MailSSLSocketFactory();
    			sf.setTrustAllHosts(true);
    		} catch (GeneralSecurityException e) {
    			e.printStackTrace();
    		}
    		prop.put("mail.smtp.ssl.enable", "true");
    		prop.put("mail.smtp.ssl.socketFactory", sf);
    	}
        Session session = Session.getDefaultInstance(prop, new MyAuthenricator(mailInfoMap.get(ConfigKeys.MAIL_FROM), mailInfoMap.get(ConfigKeys.MAIL_PASSWORD)));
        session.setDebug(Boolean.valueOf(mailInfoMap.get("MAIL_DEBUG")));
        return new MimeMessage(session);
    }
    
    private Map<String, String> getMainInfo(){
    	//从数据库中获取到邮箱信息
    	EntityWrapper<Config> ewc = new EntityWrapper<>();
    	ewc.in("code", "MAIL_PASSWORD,MAIL_PORT,MAIL_FROM,MAIL_ACCOUNT,MAIL_HOST");
    	List<Config> selectList = configService.selectList(ewc);
    	Map<String, String> map = new HashMap<>();
    	if(selectList!=null && selectList.size()>0){
    		for (Config config : selectList) {
    			map.put(config.getCode(), config.getValue());
    		}
    		map.put("MAIL_AUTH", "true");
    		map.put("MAIL_PROTOCOL", "smtp");
    		map.put("MAIL_DEBUG", "true");
    		mailInfoMap = map;
    		return map;
    	}
    	return null;
    }

    //用户名密码验证，需要实现抽象类Authenticator的抽象方法PasswordAuthentication
    static class MyAuthenricator extends Authenticator {
        String fromAddress = null;
        String password = null;

        public MyAuthenricator(String fromAddress, String password) {
            this.fromAddress = fromAddress;
            this.password = password;
        }

        @Override
        public PasswordAuthentication getPasswordAuthentication() {
            return new PasswordAuthentication(fromAddress, password);
        }
    }
    private void mian() {
		// TODO Auto-generated method stub
	}

}
