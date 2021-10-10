package com.xin.portal.aa.contrller;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.customer.servlet.EntryServlet;
import com.xin.portal.system.util.SessionUtil;

@Controller
@RequestMapping("/form")
public class formController {

	private static final String path = "aa/";

	@RequestMapping(value = "/{APIname}")
	public String DataSourceList(Model model, @PathVariable String APIname) throws JSONException {
		Map<String, String> paramMap = new HashMap<String, String>();

        //获取当前接口参数
		String patternID = APIname;
		//获取数窗中当前登录对象的id
		String username = SessionUtil.getUserId();

		JSONObject json = EntryServlet.executeGezEntry(patternID, username, paramMap);

		String href = json.getString("href");

		String url = json.getString("url");
		JSONObject params = json.getJSONObject("params");
		model.addAttribute("params", params);
		model.addAttribute("href", href);
		model.addAttribute("url", url);

		return path + "demo";
	}

}
