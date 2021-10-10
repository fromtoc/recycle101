package com.xin.portal.system.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.xin.portal.system.model.FileModel;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface FileModelMapper extends BaseMapper<FileModel> {
	
    int delete(String string);

    int save(FileModel record);

    FileModel find(FileModel record);

    List<FileModel> findList(FileModel record);

    int update(FileModel record);
    
    FileModel findByNameAfter(String nameAfter);

	boolean deleteAllPic();

	List<FileModel> findBannerAndThumbnail(FileModel fileModel);

	int insertWithTenantId(FileModel file);

	List<FileModel> selectByCreateTime();
}