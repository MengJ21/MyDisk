package com.MyDisk.project.controller;


import com.MyDisk.project.entity.dto.SharedDTO;
import com.MyDisk.project.mapper.UserMapper;
import com.MyDisk.project.service.FileService;
import com.alibaba.fastjson.JSONArray;
import com.MyDisk.project.entity.dto.ShareDTO;
import com.MyDisk.project.mapper.FileMapper;
import com.MyDisk.project.mapper.ShareMapper;
import com.MyDisk.project.util.MyDiskUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.*;

@RestController
public class ShareFileController {

    @Resource
    private ShareMapper shareMapper;

    @Resource
    private UserMapper userMapper;

    @Resource
    private FileMapper fileMapper;

    @Resource
    private FileService fileService;

    /**
     * 获取我分享的文件
     * @param userId
     * @return
     */

    @GetMapping("/getShares/{userId}")
    public String getShares(@PathVariable("userId") Integer userId) {
        System.out.println("==========" + userId);
        // 查询用户分享
        List<ShareDTO> list = fileService.getShareInfo(userId);
        System.out.println(list);
        Map<String, Object> map = new HashMap<>();
        map.put("data", list);
        return MyDiskUtils.getJSONString(200, "分享文件信息", map);
    }

    /**
     * 获取别人分享给我的文件
     * @param userId
     * @return
     */
    @GetMapping("/getShareds/{userId}")
    public String getShareds(@PathVariable("userId") Integer userId) {
        System.out.println("==========" + userId);
        // 本用户作为被分享用户
        List<SharedDTO> list = fileService.getSharedInfo(userId);
        System.out.println(list);
        Map<String, Object> map = new HashMap<>();
        map.put("data", list);
        return MyDiskUtils.getJSONString(200, "被分享记录", map);
    }

    /**
     * 取消分享文件
     * @param userId
     * @param data
     * @return
     */

    @PostMapping("/cancelShares/{userId}")
    public String cancelShares(@PathVariable("userId") Integer userId, @RequestBody String data) {
        System.out.println(userId + "===this is data =====" + data);
        List<ShareDTO> list = JSONArray.parseArray(data, ShareDTO.class);

        for (ShareDTO shareDTO : list) {
            // 查询文件id
            Integer fileId = fileMapper.queryFileIdByFileName(shareDTO.getFileName());
            Integer sharedUserId = userMapper.queryUserIdByUserName(shareDTO.getSharedUser());
            // 通过用户Id和文件Id删除分享信息
            shareMapper.deleteShareByUserIdAndFileId(userId, sharedUserId ,fileId);
        }
        List<ShareDTO> list1 = fileService.getShareInfo(userId);
        Map<String, Object> map = new HashMap<>();
        map.put("data", list1);
        return MyDiskUtils.getJSONString(200, "删除成功！", map);
    }

}
