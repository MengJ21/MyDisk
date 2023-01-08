package com.MyDisk.project.service.impl;

import com.MyDisk.project.entity.File;
import com.MyDisk.project.entity.Share;
import com.MyDisk.project.entity.dto.FileShow;
import com.MyDisk.project.entity.dto.RecycleDTO;
import com.MyDisk.project.entity.dto.ShareDTO;
import com.MyDisk.project.entity.dto.SharedDTO;
import com.MyDisk.project.mapper.FileMapper;
import com.MyDisk.project.mapper.ShareMapper;
import com.MyDisk.project.mapper.UserMapper;
import com.MyDisk.project.service.FileService;
import com.MyDisk.project.service.UserService;
import com.MyDisk.project.util.MyDiskUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;


@Service
public class FileServiceImpl implements FileService {

    @Resource
    UserMapper userMapper;
    @Resource
    FileMapper fileMapper;
    @Resource
    ShareMapper shareMapper;
    /**
     * 获取回收站信息
     * @param userId
     * @return
     */
    public List getBinInfo(Integer userId) {
        Integer fileStoreId = userMapper.queryFileStoreIdByUserId(userId);
        System.out.println("=========userId: " + userId + ",  fileStoreId: " + fileStoreId);
        List<File> files = fileMapper.queryByFileStoreIdAndRecycle(fileStoreId, 1);
        System.out.println("files === " + files);
        List<RecycleDTO> list = new ArrayList<>();
        for (File file : files) {
            RecycleDTO recycleDTO = new RecycleDTO();
            recycleDTO.setFileName(file.getFileName().substring(file.getFileName().lastIndexOf("/")+1));
            recycleDTO.setFilePath(file.getFileName().substring(0, file.getFileName().lastIndexOf("/")+1));
            recycleDTO.setFileSize(file.getFileSize());
            recycleDTO.setRecycleTime(MyDiskUtils.dateToString(file.getRecycleTime()));
            list.add(recycleDTO);
        }
        return list;
    }

    /**
     * 获取文件信息
     */
    public List<FileShow> getFileInfo(Integer userId) {
        Integer fileStoreId = userMapper.queryFileStoreIdByUserId(userId);
        System.out.println("userId === " + userId + "  ;" + "fileStoreId === " + fileStoreId);

        List<File> files = fileMapper.queryByFileStoreIdAndRecycle(fileStoreId, 0);
        List<FileShow> list = new ArrayList<>();
        for (File file : files) {
            FileShow fileShow = new FileShow();
            fileShow.setFileSize(file.getFileSize());
            fileShow.setFileName(file.getFileName());
            fileShow.setZTime(MyDiskUtils.dateToString(file.getCreateTime()));
            list.add(fileShow);
        }
        return list;
    }


    /**
     * 通过用户ID查询分享的文件
     * @param userId
     * @return
     */
    public List<ShareDTO> getShareInfo(Integer userId) {
        List<Share> shares = shareMapper.queryByUserId(userId);
        System.out.println(shares);
        List<ShareDTO> list = new ArrayList<>();
        for (Share share : shares) {
            ShareDTO shareDTO = new ShareDTO();
            File file = fileMapper.queryById(share.getFileId());
            shareDTO.setFileName(file.getFileName());
            shareDTO.setFileType(file.getFileType());
            shareDTO.setShareDate(MyDiskUtils.dateToString(share.getShareTime()));
            shareDTO.setSharedUser(userMapper.queryById(share.getSharedUserId()).getUserName());
            list.add(shareDTO);
        }
        return list;
    }
    public List<SharedDTO> getSharedInfo(Integer userId) {
        List<Share> shares = shareMapper.queryBySharedUserIdAndLike(userId, 0);
        List<SharedDTO> list = new ArrayList<>();
        for (Share share: shares) {
            SharedDTO sharedReturn = new SharedDTO();
            File file = fileMapper.queryById(share.getFileId());
            sharedReturn.setShareName(userMapper.queryById(share.getUserId()).getUserName());
            sharedReturn.setFileName(file.getFileName());
            sharedReturn.setFileType(file.getFileType());
            sharedReturn.setShareTime(MyDiskUtils.dateToString(share.getShareTime()));
            list.add(sharedReturn);
        }
        return list;
    }
}
