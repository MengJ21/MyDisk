package com.MyDisk.project.mapper;

import com.MyDisk.project.entity.File;
import org.apache.ibatis.annotations.Mapper;

import java.util.Date;
import java.util.List;

@Mapper
public interface FileMapper {

    int insertFile(File file);

    int updateRecycle(Integer fileId, Integer recycle);

    int deleteFile(Integer fileId);

    int updateFileName(Integer fileId, String fileName);

    // 查询被回收的文件
    List<File> queryByFileStoreIdAndRecycle(Integer fileStoreId, Integer recycle);

    List<File> queryFilesByFileStoreId(Integer fileStoreId);

    File queryById(Integer fileId);

    Integer queryFileIdByFileName(String fileName);

    int updateRecycleByFileIdAndFileStoreId(Integer fileId, Integer fileStoreId, Integer recycle);

    int deleteFileByFileIdAndFileStoreId(Integer fileId, Integer fileStoreId, Integer recycle, Date recycleTime);

    int updateFileNameByFileIdAndFileStoreId(Integer fileId, Integer fileStoreId, String fileName);


}
