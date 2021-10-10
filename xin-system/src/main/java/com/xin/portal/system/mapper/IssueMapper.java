package com.xin.portal.system.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.xin.portal.system.model.Issue;

/**
 *  Mapper 接口
 *
 * @author zhoujun
 * @since 2018-12-03
 */
@Mapper
public interface IssueMapper extends BaseMapper<Issue> {

	List<Issue> findPageList(Page<Issue> page, Issue query);

	List<Issue> selectByQuery(Issue query);

	Integer updateByIssue(Issue issue);

}
