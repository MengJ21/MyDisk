package com.MyDisk.project.controller;

import com.MyDisk.project.entity.File;
import com.MyDisk.project.entity.Share;
import com.MyDisk.project.mapper.UserMapper;
import com.MyDisk.project.service.FileService;
import com.alibaba.fastjson.JSONArray;
import com.MyDisk.project.entity.dto.FileShow;
import com.MyDisk.project.entity.dto.RecycleDTO;
import com.MyDisk.project.mapper.FileMapper;
import com.MyDisk.project.mapper.ShareMapper;
import com.MyDisk.project.util.FileTypeUtils;
import com.MyDisk.project.util.MyDiskUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.*;

@RestController
@CrossOrigin
public class FileController {

    @Resource
    private UserMapper userMapper;

    @Resource
    private FileMapper fileMapper;

    @Resource
    private ShareMapper shareMapper;

    @Resource
    private FileService fileService;

    /**
     * 文件上传：默认上传至用户ID文件夹下面
     */
    @PostMapping("/upload/{userId}")
    public String uploadFile(@PathVariable("userId") Integer userId, MultipartFile multipartFile) {
        System.out.println("OK===================" + multipartFile.getOriginalFilename() + "userId === " + userId);
        if (!multipartFile.isEmpty()){
            try {
                //上传的文件需要保存的路径和文件名称，路径需要存在，否则报错
                multipartFile.transferTo(new java.io.File(MyDiskUtils.ROOTPATH + userId + "\\" + multipartFile.getOriginalFilename()));
            } catch (IllegalStateException | IOException e){
                e.printStackTrace();
                return MyDiskUtils.getJSONString(400, "上传失败");
            }
        } else {
            return MyDiskUtils.getJSONString(200, "请上传文件");
        }
        // 创建file数据
        File file = new File();
        file.setFileName(multipartFile.getOriginalFilename());
        file.setFileType(FileTypeUtils.getFileType(MyDiskUtils.ROOTPATH + userId + "\\" + multipartFile.getOriginalFilename()));
        file.setRecycle(0);
        file.setFileStoreId(userMapper.queryFileStoreIdByUserId(userId));
        file.setCreateTime(new Date());
        file.setFileSize((double) multipartFile.getSize());
        fileMapper.insertFile(file);

        List<FileShow> list = fileService.getFileInfo(userId);
        Map<String, Object> map = new HashMap<>();
        map.put("data", list);

        return MyDiskUtils.getJSONString(200, "文件上传成功", map);
    }

    @GetMapping("/getRecycles/{userId}")
    public String getRecycles(@PathVariable("userId") Integer userId) {
        List<RecycleDTO> list = fileService.getBinInfo(userId);
        Map<String, Object> map = new HashMap<>();
        map.put("data", list);
        return MyDiskUtils.getJSONString(200, "回收站信息", map);
    }

    @GetMapping("/getFiles/{userId}")
    public String getFiles(@PathVariable("userId") Integer userId) {
        List<FileShow> list = fileService.getFileInfo(userId);
        Map<String, Object> map = new HashMap<>();
        map.put("data", list);
        return MyDiskUtils.getJSONString(200, "文件信息", map);
    }

    @PostMapping("/deleteFiles/{userId}")
    public String deleteFiles(@PathVariable("userId") Integer userId, @RequestBody String data) {
        List<FileShow> list = JSONArray.parseArray(data,FileShow.class);
        System.out.println("userId == " + userId + "; data === " + data);
        Integer fileStoreId = userMapper.queryFileStoreIdByUserId(userId);
        for (FileShow fileShow : list) {
            Integer fileId = fileMapper.queryFileIdByFileName(fileShow.getFileName());
            fileMapper.deleteFileByFileIdAndFileStoreId(fileId, fileStoreId, 1, new Date());
        }
        List<FileShow> list1 = fileService.getFileInfo(userId);
        Map<String, Object> map = new HashMap<>();
        map.put("data", list1);
        return MyDiskUtils.getJSONString(200, "删除文件成功！", map);
    }

    @PostMapping("/recoverFile/{userId}")
    public String recoverFile(@PathVariable("userId") Integer userId, @RequestBody String data) {
        System.out.println(userId + "===this is data =====" + data);
        List<RecycleDTO> list = JSONArray.parseArray(data, RecycleDTO.class);
        System.out.println(list);
        // 恢复文件： 将recycle值置为0，通过文件ID和userId
        for (RecycleDTO recycleDTO : list) {
            Integer fileId = fileMapper.queryFileIdByFileName(recycleDTO.getFilePath()+ recycleDTO.getFileName());
            Integer fileStoreId = userMapper.queryFileStoreIdByUserId(userId);
            fileMapper.updateRecycleByFileIdAndFileStoreId(fileId,fileStoreId, 0);
        }
        List<RecycleDTO> list1 = fileService.getBinInfo(userId);
        Map<String, Object> map = new HashMap<>();
        map.put("data", list1);
        return MyDiskUtils.getJSONString(200, "回收站信息", map);
    }


    @PostMapping("/createDir/{userId}")
    public String createDir(@PathVariable("userId") Integer userId, @RequestBody String data) {
        System.out.println("data ============= " + data.substring(0, data.length()-1));
        String path = data.substring(0, data.length()-1);
        String[] dirPath = path.split("/");
        System.out.println("创建的文件夹：" + dirPath[0]);
        String path_ = MyDiskUtils.ROOTPATH + userId;
        java.io.File file = new java.io.File(path_);
        System.out.println("用户文件夹路径：" + path_);
        System.out.println("用户文件夹存在?" + file.exists());
        if (!file.exists()) {
            System.out.println("用户文件夹不存在，创建文件夹");
            file.mkdir();
        }

        for (int i = 0;i < dirPath.length; i++) {
            path_ += "\\" + dirPath[i] + "\\";
            System.out.println(path_);
            System.out.println("新建文件夹路径：" + path_);
            file = new java.io.File(path_);
            if (i == dirPath.length-1 && file.exists()) {
                return MyDiskUtils.getJSONString(400, "文件夹已存在");
            }
            if (!file.exists()) {
                System.out.println("文件夹不存在，创建文件夹");
                file.mkdir();
            }
        }
        return MyDiskUtils.getJSONString(200, "创建文件夹成功");
    }

    /**
     * 分享文件
     */
    @PostMapping("/shareFiles/{userId}")
    public String shareFiles(@PathVariable("userId") Integer userId, @RequestBody Map<String, Object> data) {
        String userData = (String) data.get("value");
        if (userData == null || "".equals(userData)) {
            return MyDiskUtils.getJSONString(400, "分享用户为空");
        }
        String fileData = (String) data.get("tableData");
        String[] users = new String[1];
        // 解析用户名
        if (userData.contains(";")) {
            users = userData.split(";");
        }else if (userData.contains("；")) {
            users = userData.split("；");
        } else {
            users[0] = userData;
        }
        // 解析fileName
        System.out.println("users ===== " +  users.toString());
        for (int i = 0;i < users.length; i++) System.out.println(users[i]);

        String[] fileNames = fileData.split("-----");
        System.out.println(fileNames.toString()+"================");
        for (int i = 1;i < fileNames.length; i++) System.out.println(fileNames[i]);

        String userError = "";

        for (int i = 0;i < fileNames.length; i++) {
            Integer fileId = fileMapper.queryFileIdByFileName(fileNames[i]);
            if (fileId == null) continue;
            for (int j = 0;j < users.length; j++) {
                Integer shareId = userMapper.queryUserIdByUserName(users[j]);
                if (shareId == null) {
                    if ("".equals(userError)) {
                        userError += "用户不存在：";
                    } else {
                        userError += users[j] + ";";
                    }
                    continue;
                }
                Share share = new Share();
                share.setLike(0);
                share.setShareTime(new Date());
                share.setFileId(fileId);
                share.setUserId(userId);
                share.setSharedUserId(shareId);
                shareMapper.insertShare(share);
            }
        }
        if ("".equals(userError)) {
            return MyDiskUtils.getJSONString(200, "分享成功");
        }
        return MyDiskUtils.getJSONString(400, userError);
    }

    /**
     * 文件下载功能
     */
    @GetMapping("/download/{shareUser}")
    public void download(@RequestParam("path") String path, @PathVariable("shareUser")String shareUser,
                         HttpServletResponse response) {
        System.out.println("path ================ " + path + " shareUser == " + shareUser);
        Integer userId = userMapper.queryUserIdByUserName(shareUser);
        System.out.println("用户名：" + shareUser);
        if (userId == null) {
            return ;
        }
        System.out.println("---------------------------------");
        java.io.File file = null;
        if ("\\".equals(path.substring(0,1))) {
            file = new java.io.File(MyDiskUtils.ROOTPATH + userId + path);
        } else {
            file = new java.io.File(MyDiskUtils.ROOTPATH + userId + "\\" + path);
        }
        System.out.println("----------------------------------");
        System.out.println("文件名称：" + file.getPath());
        byte[] buffer = new byte[1024];
        BufferedInputStream bis = null;
        OutputStream os = null;
        try {
            //文件是否存在
            if (file.exists()) {
                //设置响应
                response.setContentType("application/octet-stream;charset=UTF-8");
                response.setCharacterEncoding("UTF-8");
                os = response.getOutputStream();
                bis = new BufferedInputStream(new FileInputStream(file));
                while (bis.read(buffer) != -1) {
                    os.write(buffer);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (bis != null) {
                    bis.close();
                }
                if (os != null) {
                    os.flush();
                    os.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 文件移动
     */
    @PostMapping("/moveFile/{userId}")
    public String moveFile(@PathVariable("userId") Integer userId, @RequestBody Map<String, String> data) {
        System.out.println("=================== value = " + data.get("value") + "  tableData ==== " + data.get("tableData"));
        if (data.get("value") == null || "".equals(data.get("value"))) {
            return MyDiskUtils.getJSONString(200, "文件移动的路径为空！");
        }
        // 检查文件路径是否存在
        String rootPath = MyDiskUtils.ROOTPATH + userId;
        String errorPath = "";
        String[] paths = data.get("value").split("\\\\");
        for (int i = 0;i < paths.length; i++) {
            System.out.println(paths[i]);
            rootPath += "\\" + paths[i];
            errorPath += "\\" + paths[i];
            java.io.File file = new java.io.File(rootPath);
            if (!file.exists()) {
                return MyDiskUtils.getJSONString(400, "文件夹 " + errorPath + " 不存在！");
            }
        }
        // 通过-----拆分多个文件
        String root = MyDiskUtils.ROOTPATH + userId;
        String noExistError = "";
        String[] files = data.get("tableData").split("-----");
        for (int i = 0;i < files.length; i++) {
            System.out.println("filename ==== " + files[i]);
            java.io.File file = new java.io.File(root + "\\" + files[i]);
            if (!file.exists()) {
                noExistError += files[i]+";";
                continue;
            }
            String newFile = data.get("value") + "\\" + files[i].substring(files[i].lastIndexOf("\\")+1);
            String destinationFile = root + newFile;
            System.out.println("移动之后的文件路径：" + destinationFile);
            if (file.renameTo(new java.io.File(destinationFile))) {
                // 修改文件的路径
                Integer fileId = fileMapper.queryFileIdByFileName(files[i]);
                fileMapper.updateFileName(fileId, newFile);
            }
        }
        if ("".equals(noExistError)) {
            List<FileShow> fileShows = fileService.getFileInfo(userId);
            Map<String, Object> map = new HashMap<>();
            map.put("data", fileShows);
            return MyDiskUtils.getJSONString(200, "文件移动成功", map);
        }
        return MyDiskUtils.getJSONString(400, "文件 " + noExistError + "不存在！");
    }

    /**
     * 修改文件名
     */
    @PostMapping("/updateFileName/{userId}")
    public String updateFileName(@PathVariable("userId") Integer userId, @RequestBody Map<String, String> data) {
        System.out.println("userId === " + userId + " oldName === " + data.get("oldName") + " newName === " + data.get("newName"));
        String oldName = data.get("oldName");
        String newName = data.get("newName");
        Integer fileId = fileMapper.queryFileIdByFileName(oldName);
        Integer fileStoreId = userMapper.queryFileStoreIdByUserId(userId);
        // 修改数据库
        if (fileMapper.updateFileNameByFileIdAndFileStoreId(fileId, fileStoreId, newName) != 1) {
            return MyDiskUtils.getJSONString(400, "1修改文件名失败！");
        }
        // 修改真实文件名
        String rootPath = MyDiskUtils.ROOTPATH + userId;
        if (oldName.substring(0,1).equals("\\")) {
            java.io.File oldFile = new java.io.File(rootPath + oldName);
            if (!oldFile.renameTo(new java.io.File(rootPath + newName))) {
                return MyDiskUtils.getJSONString(400, "2修改文件名失败！");
            }
        } else {
            java.io.File oldFile = new java.io.File(rootPath + "\\" + oldName);
            if (!oldFile.renameTo(new java.io.File(rootPath + "\\" + newName))) {
                return MyDiskUtils.getJSONString(400, "3修改文件名失败！");
            }
        }
        return MyDiskUtils.getJSONString(200, "修改文件名成功");
    }




}
