package com.xin.portal.system.controller;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.xin.portal.system.bean.BaseController;
import com.xin.portal.system.util.EchartData;
import com.xin.portal.system.util.Series;

@Controller
@RequestMapping("/analysis")
public class AnalysisController extends BaseController {

	private static Logger logger = LoggerFactory.getLogger(AnalysisController.class);

	private static String DIR = "analysis/";
	private int sel = 0;
	private int sel1 = 0;
	private int sel2 = 0;
	private EchartData ed = null;
	private List<Map> lm = null;
	private EchartData ed2 = null;

	String URL = "jdbc:mysql://101.200.160.86:3306/zentao?useUnicode=true&amp;characterEncoding=utf-8";
	String USER = "XDF";
	String PASSWORD = "xdfpassword";
	
	// 查询所有项目
	String project = "SELECT id,name FROM zt_project";
	

	// 查询某个项目下的所有模块
	String model = "select DISTINCT zm.id mid,zm.name mname from zt_taskestimate ztt left join zt_task zt on ztt.task=zt.id left join zt_module zm on zm.id = zt.module left join zt_module zmp on zm.parent = zmp.id left join zt_project zp on zp.id = zt.project left join zt_user zu on zu.account = ztt.account where zt.project= ? and zt.deleted='0' and ztt.consumed>0 order by zmp.order, zt.module,zt.id";

	// 查询某个项目下所有模块的人物
	String user = "select DISTINCT zu.id uid,zu.realname uname from zt_taskestimate ztt left join zt_task zt on ztt.task=zt.id left join zt_module zm on zm.id = zt.module left join zt_module zmp on zm.parent = zmp.id left join zt_project zp on zp.id = zt.project left join zt_user zu on zu.account = ztt.account where zt.project= ? and zt.deleted='0' and ztt.consumed>0 order by zmp.order, zt.module,zt.id";

	// 查询某个项目下所有模块的人物的耗时
	String user_time = "select DISTINCT zm.id mid,zm.name mname,zt.id tid,zt.name tname,zu.id uid,zu.realname uname,ztt.consumed ,ztt.date from zt_taskestimate ztt left join zt_task zt on ztt.task=zt.id left join zt_module zm on zm.id = zt.module left join zt_module zmp on zm.parent = zmp.id left join zt_project zp on zp.id = zt.project left join zt_user zu on zu.account = ztt.account where zt.project= pid and zt.deleted='0' and ztt.consumed>0 AND zm.id = ? AND zu.id = ! and ztt.date >= 'date1' and ztt.date <= 'date2' order by zmp.order, zt.module,zt.id";

	// 查询某个项目下某个人的耗时
	String user_tolTime = "select DISTINCT zm.id mid,zm.name mname,zt.id tid,zt.name tname,zu.id uid,zu.realname uname,ztt.consumed ,ztt.`date` from zt_taskestimate ztt left join zt_task zt on ztt.task=zt.id left join zt_module zm on zm.id = zt.module left join zt_module zmp on zm.parent = zmp.id left join zt_project zp on zp.id = zt.project left join zt_user zu on zu.account = ztt.account where zt.project= pid  and zt.deleted='0' and ztt.consumed>0 and zu.id = ? and ztt.date >= 'date1' and ztt.date <= 'date2' order by zmp.`order`, zt.module,zt.id";

	// 查询某个人的每天的耗时
	String user_tolTime_day = "select DISTINCT zm.id mid,zm.name mname,zt.id tid,zt.name tname,zu.id uid,zu.realname uname,ztt.consumed ,ztt.`date` from zt_taskestimate ztt left join zt_task zt on ztt.task=zt.id left join zt_module zm on zm.id = zt.module left join zt_module zmp on zm.parent = zmp.id left join zt_project zp on zp.id = zt.project left join zt_user zu on zu.account = ztt.account where zt.project= pid and zt.deleted='0' and ztt.consumed>0 AND zu.id = ? AND ztt.date = '@' order by ztt.`date`,zmp.`order`, zm.id ,zt.id";

	@RequestMapping("/index")
	public String index(Model model) throws ClassNotFoundException, SQLException {
		List<Map> list = new ArrayList<>();
		Class.forName("com.mysql.jdbc.Driver");
		Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
		Statement st = conn.createStatement();
		ResultSet rs = st.executeQuery(project);
		while (rs.next()) {
			Map<String, String> map = new HashMap<>();
			map.put("id", rs.getString("id"));
			map.put("name", rs.getString("name"));
			list.add(map);
		}
		rs.close();
		st.close();
		conn.close();
		model.addAttribute("project", list);
		return DIR + "index";
	}

	// 查询某个项目下所有模块
	public List<Map> projectModel(String pid) throws ClassNotFoundException, SQLException {
		List<Map> list = new ArrayList<>();
		Class.forName("com.mysql.jdbc.Driver");
		Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
		Statement st = conn.createStatement();
		String sql = model.replace("?", pid);
		ResultSet rs = st.executeQuery(sql);
		while (rs.next()) {
			Map<String, String> map = new HashMap<>();
			map.put("mid", rs.getString("mid"));
			map.put("mname", rs.getString("mname"));
			list.add(map);
		}
		rs.close();
		st.close();
		conn.close();
		return list;
	}

	// 查询某个项目下所有模块的人物
	public List<Map> moduleUser(String pid) throws ClassNotFoundException, SQLException {
		List<Map> list = new ArrayList<>();
		Class.forName("com.mysql.jdbc.Driver");
		Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
		Statement st = conn.createStatement();
		String sql = user.replace("?", pid);
		ResultSet rs = st.executeQuery(sql);
		while (rs.next()) {
			Map<String, String> map = new HashMap<>();
			map.put("uid", rs.getString("uid"));
			map.put("uname", rs.getString("uname"));
			list.add(map);
		}
		rs.close();
		st.close();
		conn.close();
		return list;
	}

	// 查询某个项目下所有模块的人物的耗时
	public double userTime(String sql) throws ClassNotFoundException, SQLException {
		Class.forName("com.mysql.jdbc.Driver");
		Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
		Statement st = conn.createStatement();
		ResultSet rs = st.executeQuery(sql);
		double num = 0;
		while (rs.next()) {
			String consumed = rs.getString("consumed");
			num += Double.parseDouble(consumed);
		}
		rs.close();
		st.close();
		conn.close();
		return num;
	}

	// 查询某个项目下所有模块的人物的每日耗时
	public List<Double> userTimeDay(String sql,String date1,String date2,String username) throws ClassNotFoundException, SQLException, ParseException {
		List<Double> data = new ArrayList<>();
		Class.forName("com.mysql.jdbc.Driver");
		Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
		Statement st = conn.createStatement();
		while(!date1.equals(date2)) {
			sql = sql.replace("@", date1);
			ResultSet rs = st.executeQuery(sql);
			sql = sql.replace(date1, "@");
			double num = 0;
			if (rs.next()) {
				rs.previous();
				while (rs.next()) {
					String consumed = rs.getString("consumed");
					num += Double.parseDouble(consumed);
				}
				data.add(num);
				num = 0;
			}
			else {
				data.add(0.0);
			}
			rs.close();
			date1 = addDateOneDay(date1);
		}
		st.close();
		conn.close();
		return data;
	}

	@RequestMapping("/data")
	@ResponseBody
	public EchartData Data(int pid,String date1,String date2) throws ClassNotFoundException, SQLException {
		List<String> legend = new ArrayList<String>();
		List<Series> series = new ArrayList<Series>();
		List<String> xAxis = new ArrayList<String>();
		List<Map> mlist = projectModel(String.valueOf(pid));
		List<Map> ulist = moduleUser(String.valueOf(pid));
		for (int i = 0; i < ulist.size(); i++) {
			List<Double> data = new ArrayList<>();
			for (int j = 0; j < mlist.size(); j++) {
				if (i == 0) {
					xAxis.add((String) mlist.get(j).get("mname"));
				}
				String sql = user_time.replace("?", (String) mlist.get(j).get("mid"));
				sql = sql.replace("!", (String) ulist.get(i).get("uid"));
				sql = sql.replace("pid", String.valueOf(pid));
				sql = sql.replace("date1", date1);
				sql = sql.replace("date2", date2);
				Integer mid = Integer.parseInt((String) mlist.get(j).get("mid"));
				Integer uid = Integer.parseInt((String) ulist.get(i).get("uid"));
				double num = userTime(sql);
				data.add(num);
			}
			series.add(new Series((String) ulist.get(i).get("uname"),"bar","total","{normal: {show: true,position: 'insideRight'}}",null, data));
		}
		EchartData data = new EchartData(legend, xAxis, series);
		return data;
	}

	@RequestMapping("/pieData")
	@ResponseBody
	public List<Map> pieData(int pid,String date1,String date2) throws ClassNotFoundException, SQLException {
		List<Map> list = new ArrayList<>();
		List<Map> ulist = moduleUser(String.valueOf(pid));
		for (int i = 0; i < ulist.size(); i++) {
			Map<String, String> map = new HashMap<>();
			String sql = user_tolTime.replace("?", (String) ulist.get(i).get("uid"));
			sql = sql.replace("pid", String.valueOf(pid));
			sql = sql.replace("date1", date1);
			sql = sql.replace("date2", date2);
			double num = userTime(sql);
			map.put("name", (String) ulist.get(i).get("uname"));
			map.put("value", num + "");
			list.add(map);
		}
		return list;
	}

	@RequestMapping("/riverData")
	@ResponseBody
	public EchartData riverData(int pid,String date1,String date2) throws ClassNotFoundException, SQLException, ParseException {
		List<String> legend = new ArrayList<String>();
		List<Series> series = new ArrayList<Series>();
		List<String> xAxis = new ArrayList<String>();
		List<Map> ulist = moduleUser(String.valueOf(pid));
		int days = differentDaysByMillisecond(date1,date2);
		String date = date1;
		for (int i = 0; i < days; i++) {
			xAxis.add(date);
			date = addDateOneDay(date);
		}
		for (int i = 0; i < ulist.size(); i++) {
			String sql = user_tolTime_day.replace("?", (String) ulist.get(i).get("uid"));
			sql = sql.replace("pid", String.valueOf(pid));
			List<Double> data = userTimeDay(sql,date1,date2,(String)ulist.get(i).get("uname"));
			series.add(new Series((String) ulist.get(i).get("uname"),"line","total",null,"{normal: {}}", data));
		}
		EchartData echartData = new EchartData(legend, xAxis, series);
		return echartData;
	}
	
	public String addDateOneDay(String date) throws ParseException {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		Date d = formatter.parse(date);
        if (null == date) {
        	String dateString = formatter.format(new Date());
            return dateString;
        }  
        Calendar c = Calendar.getInstance();  
        c.setTime(d);   //设置当前日期  
        c.add(Calendar.DATE, 1); //日期加1天  
        //c.add(Calendar.DATE, -1); //日期减1天  
        d = c.getTime();
        String dateString = formatter.format(d);
        return dateString;  
    }
	
	public int differentDaysByMillisecond(String date1,String date2) throws ParseException {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		Date d1 = formatter.parse(date1);
		Date d2 = formatter.parse(date2);
        int days = (int) ((d2.getTime() - d1.getTime()) / (1000*3600*24));
        return days;
    }

}
