package com.xin.portal.system.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.plugins.Page;
import com.xin.portal.system.bean.PageModel;
import com.xin.portal.system.mapper.IssueMapper;
import com.xin.portal.system.mapper.OrganizationMapper;
import com.xin.portal.system.mapper.SysReleaseMapper;
import com.xin.portal.system.mapper.UserInfoMapper;
import com.xin.portal.system.mapper.UserOrganizationMapper;
import com.xin.portal.system.model.Issue;
import com.xin.portal.system.model.Organization;
import com.xin.portal.system.model.SysRelease;
import com.xin.portal.system.model.UserInfo;
import com.xin.portal.system.service.IssueService;
import com.xin.portal.system.util.SessionUtil;
import com.xin.portal.system.util.WebUtil;

/**
 *  服务实现类
 *
 * @author zhoujun
 * @since 2018-12-03
 */
@Service
public class IssueServiceImpl extends ServiceImpl<IssueMapper, Issue> implements IssueService {
	
	@Autowired
	private IssueMapper mapper;
	@Autowired
	private OrganizationMapper orgMapper;
	@Autowired
	private UserInfoMapper userInfoMapper;
	@Autowired
	private UserOrganizationMapper userOrganizationMapper;
	@Value("${system.name:DataWindow}")
	private String name;
	@Value("${system.version:1.0.0}")
	private String version;
	
	@Override
	public PageModel<Issue> page(Issue query) {
		Page<Issue> page = new Page<Issue>(query.getPageNumber(), query.getPageSize());
		page.setRecords(mapper.findPageList(page,query));
		return new PageModel<Issue>(page);
	}

	@Override
	public Issue getEnvironmentInfo(HttpServletRequest request) {
		//获取系统授权、系统版本、创建人、创建组织、ip
		Issue issue = new Issue();
		String userId = SessionUtil.getUserId();
		UserInfo userInfo = userInfoMapper.selectById(userId);
		//组织code、name数据库中查询，
		List<Organization> userOrgs = userOrganizationMapper.selectOrgbyUserId(userId);
		String createName = userInfo.getRealname();//创建人
		String ip = WebUtil.getIpAddr(request);
		//获取系统授权、系统版本、创建人、创建组织、ip
		issue.setSysImpower(name);
		issue.setSysVersion(version);
		issue.setCreateName(createName);
		issue.setCreater(userId);
		issue.setOrgId(userInfo.getOrgId());
		issue.setUserOrgs(userOrgs);
		issue.setIp(ip);
		return issue;
	}

	@Override
	public Issue selectIssueById(String id) {
		Issue result = new Issue();
		Issue query = new Issue();
		query.setId(id);
		List<Issue> list = mapper.selectByQuery(query);
		if (list!=null && list.size()>0) {
			result = list.get(0);
			//查询组织-用户关系
			List<Organization> userOrgs = userOrganizationMapper.selectOrgbyUserId(result.getCreater());
			result.setUserOrgs(userOrgs);
		}
		return result;
	}
	
	public String getOrgAllName(String orgCode){
		String orgName = "";
		if(orgCode!=null && orgCode.length()>0){
			int num = orgCode.length();
			if(num%3==0){
				List<String> orgList = new ArrayList<>();
				for (int i = 3; i <= orgCode.length(); i+=3) {
					String orgs = orgCode.substring(0, i);
					orgList.add(orgs);
				}
				for (String org : orgList) {
					Organization organization = new Organization();
					organization.setCode(org);
					orgName += orgMapper.selectOne(organization).getName()+"/";
				}
				orgName = orgName.substring(0, orgName.length()-1);
			}
		}
		return orgName;
	}

	@Override
	public Issue updatebyQuery(Issue record) {
		//修改状态、人、时间
		String userId = SessionUtil.getUserId();
		record.setUpdater(userId);
		record.setUpdateName(SessionUtil.getUserInfo().getRealname());
		record.setState(3);
		record.setUpdateTime(new Date());
		Integer update = mapper.updateById(record);
		return update>0?record:null;
	}

	@Override
	public boolean updateByIssue(Issue issue){
		String userId = SessionUtil.getUserId();
		issue.setUpdater(userId);
		issue.setUpdateName(SessionUtil.getUserInfo().getRealname());
		issue.setUpdateTime(new Date());
		return mapper.updateByIssue(issue)==1?true:false;
	}
	
}
