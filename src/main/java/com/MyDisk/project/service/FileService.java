package com.MyDisk.project.service;

import com.MyDisk.project.entity.dto.FileShow;
import com.MyDisk.project.entity.dto.ShareDTO;
import com.MyDisk.project.entity.dto.SharedDTO;

import java.util.List;

public interface FileService {

    List<FileShow> getFileInfo(Integer userId);
    List getBinInfo(Integer userId);
    List<ShareDTO> getShareInfo(Integer userId);
    List<SharedDTO> getSharedInfo(Integer userId);
}
