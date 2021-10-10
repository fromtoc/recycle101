package com.xin.portal.system.util;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.xin.portal.system.bean.AllBean;
import com.xin.portal.system.mail.MailModel;
import com.xin.portal.system.mail.MailService;
import com.xin.portal.system.model.ServiceState;
import com.xin.portal.system.model.ServiceStateRecord;
import com.xin.portal.system.service.impl.ServiceStateRecordServiceImpl;
import com.xin.portal.system.service.impl.ServiceStateServiceImpl;

public class ServiceStateUtil implements Runnable{
	
	private ServiceState serviceState;
	
	ServiceStateServiceImpl serviceStateService = (ServiceStateServiceImpl) AllBean.getBean("serviceStateServiceImpl");
	ServiceStateRecordServiceImpl serviceStateRecordService = (ServiceStateRecordServiceImpl) AllBean.getBean("serviceStateRecordServiceImpl");
	MailService mailService = (MailService) AllBean.getBean("mailService");
	
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	public boolean exit = false;
	
	public ServiceStateUtil(ServiceState serviceState) {
		this.serviceState = serviceState;
	}
	
	@Override
	public void run() {
		if(serviceState.getState()==1) {
			try {
				Thread.sleep(serviceState.getRefreshTime()*60*1000);
				//System.out.println("设置线程睡眠:"+serviceState.getRefreshTime()*60*1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			ServiceStateRecord ssr = new ServiceStateRecord();
			if(serviceState.getRecordType()==1) {
				if(!isHostConnectable(serviceState.getIp(),Integer.parseInt(serviceState.getPort()))){
					ssr.setServiceState(0);
					ssr.setServiceId(serviceState.getId());
					ssr.setCreateTime(new Date());
					//System.out.println("插入数据");
					ssr.insert();
					String[] arr = serviceState.getEmail().split(";");
					for (int i = 0; i < arr.length; i++) {
						MailModel mailModel = new MailModel();
						mailModel.setToAddress(arr[i]);
						mailModel.setContent("监控服务名称: "+serviceState.getName()+"监控服务IP地址: "+serviceState.getIp()+"监控端口: "+serviceState.getPort()+"状态异常");
						mailModel.setSubject("服务异常提醒");
						try {
							//System.out.println("发送邮件");
							mailService.send(mailModel, true);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}
			} else if(serviceState.getRecordType()==2){
				if(!isHostConnectable(serviceState.getIp(),Integer.parseInt(serviceState.getPort()))){
					ssr.setServiceState(0);
					String[] arr = serviceState.getEmail().split(";");
					for (int i = 0; i < arr.length; i++) {
						MailModel mailModel = new MailModel();
						mailModel.setToAddress(arr[i]);
						mailModel.setContent("监控服务名称: "+serviceState.getName()+"监控服务IP地址: "+serviceState.getIp()+"监控端口: "+serviceState.getPort()+"状态异常");
						mailModel.setSubject("服务异常提醒");
						try {
							//System.out.println("发送邮件");
							mailService.send(mailModel, true);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				} else {
					ssr.setServiceState(1);
				}
				ssr.setServiceId(serviceState.getId());
				ssr.setCreateTime(new Date());
				//System.out.println("插入数据");
				ssr.insert();
			}
			Calendar calendar = Calendar.getInstance();
			calendar.add(Calendar.DATE, -serviceState.getRetentionTime());
			ssr.setServiceId(serviceState.getId());
			ssr.setCreateTime(calendar.getTime());
			serviceStateRecordService.deleteRecord(ssr);
			//System.out.println("删除数据");
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

}
