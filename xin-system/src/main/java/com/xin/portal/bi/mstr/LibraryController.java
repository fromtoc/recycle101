/**
 * Copyright (C), 2008-2019, dod123456
 * FileName: LibraryController
 * Author:   dod123456
 * Date:     2019/3/29 14:12
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.xin.portal.bi.mstr;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.xin.portal.system.model.BiServer;
import com.xin.portal.system.service.BiServerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

/**
 * <br> 
 * 〈〉
 *
 * @author dod123456
 * @create 2019/3/29
 * @since 1.0.0
 */
@Controller
@RequestMapping("library")
public class LibraryController {

    @Autowired
    private BiServerService biServerService;

    @RequestMapping("demo/{page}")
    public String demo(@PathVariable String page, Model model){
        EntityWrapper<BiServer> ew = new EntityWrapper<>();
        ew.eq("type",BiServer.TYPE_MSTR_LIBRARY);
        List<BiServer> list = biServerService.selectList(ew);
        model.addAttribute("servers",list);
        return "library/"+page;
    }

}
