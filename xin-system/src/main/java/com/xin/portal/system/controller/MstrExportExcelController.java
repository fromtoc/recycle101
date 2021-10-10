package com.xin.portal.system.controller;

import java.io.*;

import com.xin.portal.system.model.Config;
import com.xin.portal.system.service.ConfigService;
import com.xin.portal.system.util.*;
import com.xin.portal.system.util.i18n.LanguageParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;
import org.apache.http.client.CookieStore;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.impl.client.BasicCookieStore;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import com.xin.portal.system.model.UserInfo;

@Controller
@RequestMapping("/mstrExport")
public class MstrExportExcelController {

	private static Logger logger = LoggerFactory.getLogger(MstrExportExcelController.class);


	@Autowired
	private ConfigService configService;
	@Value("${fileDownloadPath}")
	private String filesPath;
	@Value("${mstrExportUrl}")
	private String mstrExportUrl;

	@RequestMapping("excel")
	public void downloadFile(String exportUrl,String watermarkText,String type,HttpServletResponse response)
			throws UnsupportedEncodingException, Exception {

		//以用户账号作为对应的下载路径，防止文件重复
		UserInfo userInfo = SessionUtil.getUserInfo();
		String filePath = filesPath + userInfo.getUsername();
		//删除当前目录下所有文件
		deleteDir(new File(filePath));
		String lang = "zh_CN";

		Config config = configService.findByCode(Constant.ConfigKeys.CONFIG_LOCALE, SessionUtil.getUserInfo().getTenantId());
		if(config!=null){
			lang = config.getValue();
		}
		logger.info(LangTransform.getLocaleLang(lang, LanguageParam.LOGGERINFO_22) + "[{}]\\",filePath);
		logger.info(LangTransform.getLocaleLang(lang, LanguageParam.LOGGERINFO_23) +"[{}]", exportUrl);
		/*logger.info("文件下载位置是：" + filePath + "\\");
		logger.info("导出链接：" + exportUrl);*/
		//服务器请求mstr创建session
		String exportUrl1 = exportUrl.replace("token", "uid");
		// 保持session
		HttpClientContext clientContext = HttpClientContext.create();
		CookieStore cookieStore = new BasicCookieStore();
		clientContext.setCookieStore(cookieStore);
		String filename = getExport(exportUrl1, filePath, clientContext,type);
		logger.info(LangTransform.getLocaleLang(lang, LanguageParam.LOGGERINFO_24) +"[{}]", filename);
		//logger.info("第一次请求mstr获取文件：" + filename);
		//再次请求mstr获取excel文件（一般第二次即可获得文件，但最多请求不能超过5次，防止服务器崩溃）
		if(filename == null || filename.equals("")) {
			for (int i = 0; i < 5; i++) {
				filename = getExport(exportUrl, filePath, clientContext,type);
				logger.info(LangTransform.getLocaleLang(lang, LanguageParam.LOGGERINFO_25) +"[{}]", filename);
				//logger.info("再次请求mstr获取文件：" + filename);
				if(filename != null) {
					break;
				}
			}
		}
		ExcelWaterRemarkUtils ewru = new ExcelWaterRemarkUtils();
		logger.info(LangTransform.getLocaleLang(lang, LanguageParam.LOGGERINFO_26) +"[{}]", watermarkText);
		//logger.info("水印名称：" + watermarkText);
		//给文件添加水印
		ewru.waterRemark(filePath + "\\", watermarkText,
				filePath + "\\" + filename);
		File file = new File(filePath + "\\" + filename);
		// 如果文件名存在，则进行下载
		if (file.exists()) {
			// 配置文件下载
			response.setHeader("content-type", "application/octet-stream");
			response.setContentType("application/octet-stream");
			// 下载文件能正常显示中文
			response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(filename, "UTF-8"));
			// 实现文件下载
			byte[] buffer = new byte[1024];
			FileInputStream fis = null;
			BufferedInputStream bis = null;
			try {
				fis = new FileInputStream(file);
				bis = new BufferedInputStream(fis);
				OutputStream os = response.getOutputStream();
				int i = bis.read(buffer);
				while (i != -1) {
					os.write(buffer, 0, i);
					i = bis.read(buffer);
				}
				logger.info(LangTransform.getLocaleLang(lang, LanguageParam.LOGGERINFO_27));
				//logger.info("文件下载成功！");
			} catch (Exception e) {
				logger.info(LangTransform.getLocaleLang(lang, LanguageParam.LOGGERINFO_28));
				//logger.info("文件下载失败！");
			} finally {
				if (bis != null) {
					try {
						bis.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				if (fis != null) {
					try {
						fis.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}


	/**
	 * 获取mstr导出文件
	 * @param exportUrl：mstr导出连接
	 * @param filePath：服务器文件下载路径
	 * @param clientContext：客户端连接
	 * @return
	 */
	public String getExport(String exportUrl, String filePath, HttpClientContext clientContext,String type) {
		try {
			// 获取下载HTML页面
			String doGet = HttpRequestUtils.httpGet(exportUrl, null, null, 3000, false, null, clientContext);
			Document doc = Jsoup.parse(doGet);
			Element firstFrom = doc.select("FORM").first();
			String action = firstFrom.attr("ACTION");
			if("1".equals(type)){//document 和 dossier
				// 重下载页面中获取form表单参数
				String url = mstrExportUrl + action.substring(action.indexOf("/") + 1, action.length());
				Map<String, Object> formList = getForm(firstFrom);
				// 模拟提交form表单提交并获取文件
				String doPost = HttpRequestUtils.httpPost(url, null, formList, 3000, true, filePath, clientContext);
				return doPost;
			}else{//report类型的下载特殊处理
				String url = mstrExportUrl + action.substring(action.indexOf("/") + 1, action.length());
				Map<String, Object> formList = getForm(firstFrom);
				// 模拟提交form表单提交并获取文件
				String Post = HttpRequestUtils.httpPost(url, null, formList, 3000, false, filePath, clientContext);


				Document doc1 = Jsoup.parse(Post);
				Element firstFrom1 = doc1.select("FORM").first();
				String action1 = firstFrom1.attr("ACTION");
				url = mstrExportUrl + action1.substring(action1.indexOf("/") + 1, action1.length());
				Map<String, Object> formList1 = getForm(firstFrom1);
				String doPost = HttpRequestUtils.httpPost(url, null, formList1, 3000, true, filePath, clientContext);
				return doPost;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static Map<String, Object> getForm(Element from) {
		Map<String, Object> map = new HashMap<>();
		for (Element etr : from.select("input")) {
			String name = etr.attr("name");
			String value = etr.attr("value");
			map.put(name, value);
		}
		return map;
	}

	private static boolean deleteDir(File dir) {
		if (dir.isDirectory()) {
			String[] children = dir.list();
			for (int i = 0; i < children.length; i++) {
				boolean success = deleteDir(new File(dir, children[i]));
				if (!success) {
					return false;
				}
			}
		}
		// 目录此时为空，可以删除
		return dir.delete();
	}

}
