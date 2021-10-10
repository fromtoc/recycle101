package com.xin.portal.system.controller;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.xin.portal.system.ueditor.ActionEnter;
	
@WebServlet(name="UeditorServlet", urlPatterns = "/UEditor")
public class UeditorServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	public void doGet(HttpServletRequest request,HttpServletResponse response) throws ServletException,IOException{
		doPost(request, response);
	}
		
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding( "utf-8" );  
		response.setHeader("Content-Type" , "text/html"); 
		String con = request.getSession().getServletContext().getRealPath("/");
		String webappPath = con.substring(0, con.lastIndexOf("\\"));
		String rootPath = webappPath.substring(0, webappPath.lastIndexOf("\\"));
		String configPaht = request.getSession().getServletContext().getRealPath("/")+"WEB-INF"+File.separator+"classes/config.json";
		try {
			String exec = new ActionEnter(request, rootPath,configPaht).exec();
			PrintWriter writer = response.getWriter();
			writer.write(exec);
			writer.flush();
			writer.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	 
}
