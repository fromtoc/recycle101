package com.xin.portal.system.util;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.*;
import com.xin.portal.system.bean.AllBean;
import com.xin.portal.system.mail.MailModel;
import com.xin.portal.system.mail.MailService;
import com.xin.portal.system.model.ServiceState;
import com.xin.portal.system.model.ServiceStateRecord;
import com.xin.portal.system.service.impl.ServiceStateRecordServiceImpl;
import com.xin.portal.system.service.impl.ServiceStateServiceImpl;

public class ServiceStaticTaskTest extends TimerTask{
	
	//获取Bean
	ServiceStateServiceImpl serviceStateService = (ServiceStateServiceImpl) AllBean.getBean("serviceStateServiceImpl");
	ServiceStateRecordServiceImpl serviceStateRecordService = (ServiceStateRecordServiceImpl) AllBean.getBean("serviceStateRecordServiceImpl");
	MailService mailService = (MailService) AllBean.getBean("mailService");

    @Override
    public void run() {
 		List<ServiceState> list = serviceStateService.selectList(null);
		for(ServiceState serviceState:list) {
			if(serviceState.getState()==1) {
				ServiceStateRecord ssr = new ServiceStateRecord();
				if(serviceState.getRecordType()==1) {
					//通过ip以及port判断服务器是否正常
					if(!isHostConnectable(serviceState.getIp(),Integer.parseInt(serviceState.getPort()))){
						ssr.setServiceState(0);
						ssr.setServiceId(serviceState.getId());
						ssr.setCreateTime(new Date());
						ssr.insert();
						try{
							//判断在服务异常时是否发送过邮件
							if(serviceStateService.selectById(serviceState.getId()).getIsSentMail()==0)
								sendMail(serviceState);
						}catch(Exception e){
							e.printStackTrace();
						}
					}else{
						//在服务器正常时，将isSentMail设置为0
						serviceStateService.updateIsSentMail(0,serviceState.getId());
					}
				} else if(serviceState.getRecordType()==2){
					//通过ip以及port判断服务器是否正常
					if(!isHostConnectable(serviceState.getIp(),Integer.parseInt(serviceState.getPort()))){
						ssr.setServiceState(0);
							try{
								//判断在服务异常时是否发送过邮件
								if(serviceStateService.selectById(serviceState.getId()).getIsSentMail()==0)
								sendMail(serviceState);
							}catch(Exception e){
								e.printStackTrace();
							}
					} else {
						//在服务器正常时，将isSentMail设置为0
						serviceStateService.updateIsSentMail(0,serviceState.getId());
						ssr.setServiceState(1);
					}
					ssr.setServiceId(serviceState.getId());
					ssr.setCreateTime(new Date());
					ssr.insert();
				}
				Calendar calendar = Calendar.getInstance();
				calendar.add(Calendar.DATE, -serviceState.getRetentionTime());
				ssr.setServiceId(serviceState.getId());
				ssr.setCreateTime(calendar.getTime());
				serviceStateRecordService.deleteRecord(ssr);
			}
		}
    }
    
    public static boolean isHostConnectable(String host, int port) {
        Socket socket = new Socket();
        try {
            socket.connect(new InetSocketAddress(host, port));
        } catch (IOException e) {
            return false;
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return true;
    }

    public void sendMail(ServiceState serviceState)throws Exception{

		Timer timer = new Timer();
		TimerTask task = new TimerTask() {
			private int mailCount = serviceStateService.selectById(serviceState.getId()).getMailCount();
			@Override
			public void run() {
				this.mailCount--;
				ServiceState result = serviceStateService.selectById(serviceState.getId());
				//判断该服务监控设定发送邮件次数是否已经发送完或该服务器是否正常
				if(mailCount == 0){
					timer.cancel();// 停止定时器
				}else if(isHostConnectable(result.getIp(),Integer.parseInt(result.getPort()))){
					timer.cancel();// 停止定时器
					//在服务器正常时，将isSentMail设置为0
					serviceStateService.updateIsSentMail(0,result.getId());
				}
				String[] arr = serviceState.getEmail().split(";");
				for (int i = 0; i < arr.length; i++) {
					MailModel mailModel = new MailModel();
					mailModel.setToAddress(arr[i]);
					mailModel.setContent("监控服务名称: "+result.getName()+"===="+"监控服务IP地址: "+result.getIp()+"===="+"监控端口: "+result.getPort()+"状态异常");
					mailModel.setSubject("服务异常提醒");
					try {
						mailService.send(mailModel, true);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}

			}
		};
		int mailFrequency = serviceStateService.selectById(serviceState.getId()).getMailFrequency();
		//在服务器异常时，启用发送邮件的定时器，将isSentMail设置为1
		serviceStateService.updateIsSentMail(1,serviceState.getId());
		timer.schedule(task, 0, mailFrequency*60*1000);
	}

}
