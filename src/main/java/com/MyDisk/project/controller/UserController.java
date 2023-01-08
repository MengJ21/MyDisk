package com.MyDisk.project.controller;

import com.MyDisk.project.mapper.UserMapper;
import com.MyDisk.project.service.UserService;
import com.MyDisk.project.entity.User;
import com.MyDisk.project.util.MyDiskUtils;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@RestController
public class UserController {

    @Resource
    private UserService userService;

    @Resource
    private UserMapper userMapper;

    @RequestMapping(path = "/register", method = RequestMethod.POST)
    public String register(@RequestBody Map<String, String> requestData) {
        System.out.println(requestData + "@@@@@@@@@@");
        String username = requestData.get("username");
        String email = requestData.get("email");
        String password = requestData.get("password");
        if (username == null || "".equals(username) || email == null && "".equals(email) || password == null || "".equals(password)) {
            return MyDiskUtils.getJSONString(400, "参数不能为空！");
        }
        User user = userService.queryByEmailAndPwd(email, password);
        User tUser = new User();
        if (user != null) {
            return MyDiskUtils.getJSONString(400, "用户已注册！");
        } else {
            tUser.setUserName(username);
            tUser.setPassword(password);
            tUser.setRegisterTime(new Date());
            tUser.setImagePath("https://p.qpic.cn/qqconnect/0/app_101851241_1582451550/100?max-age=2592000&t=0");
            tUser.setEmail(email);
            tUser.setRole(1);
            userService.insert(tUser);
        }
        Integer user_id = userMapper.getUserByEmail(email).getUserId();
        userMapper.updateFileStoreIdByUserId(user_id, user_id);
        // 为用户创建文件夹：用userId命名
        String filePath = MyDiskUtils.ROOTPATH + user_id;
        java.io.File myFolderPath = new java.io.File(filePath);
        if (!myFolderPath.exists()) {
            myFolderPath.mkdir();
        }

        HashMap<String, Object> map = new HashMap<>();
        map.put("data", tUser);
        return MyDiskUtils.getJSONString(200,"注册成功！", map);
    }

    @RequestMapping(path = "/login", method = RequestMethod.POST)
    public String login(@RequestBody Map<String, String> requestData, HttpServletResponse response) {
        String email = requestData.get("email");
        String password = requestData.get("password");
        System.out.println(email + " " + password);
        User user = userService.queryByEmailAndPwd(email, password);
        if (user == null) {
            return MyDiskUtils.getJSONString(400, "邮箱或密码错误！");
        }
        HashMap<String, Object> map = new HashMap<>();
        map.put("data", user);
        return MyDiskUtils.getJSONString(200, "登录成功！", map);
    }

    // 修改用户的个人信息
    @PostMapping("/updateUserInfo/{userId}")
    public String updateUserInfo(@PathVariable("userId") Integer userId,@RequestBody Map<String,String> userInfo) {
        System.out.println(userInfo);
        User user = new User();
        user.setUserId(userId);
        user.setUserName(userInfo.get("username"));
        user.setBirthday(MyDiskUtils.stringToDate(userInfo.get("birthday")));
        if ("男".equals(userInfo.get("sex"))) {
            user.setSex("male");
        } else if ("女".equals(userInfo.get("sex"))){
            user.setSex("female");
        }
        user.setOther(userInfo.get("other"));
        user.setEmail(userInfo.get("email"));
        user.setMyPage(userInfo.get("myPath"));
        user.setPhone(userInfo.get("phone"));
        userService.update(user);
        User userQuery = userService.queryById(userId);
        System.out.println(userQuery);
        Map<String, Object> map = new HashMap<>();
        map.put("username", userQuery.getUserName());
        map.put("birthday", MyDiskUtils.dateToString_(userQuery.getBirthday()));
        if (userQuery.getSex().equals("male")) {
            map.put("sex", "男");
        } else {
            map.put("sex", "女");
        }
        map.put("other", userQuery.getOther());
        map.put("email", userQuery.getEmail());
        map.put("myPath", userQuery.getMyPage());
        map.put("phone", userQuery.getPhone());
        return MyDiskUtils.getJSONString(200, "更新用户信息成功！", map);
    }

    // 获取用户信息
    @GetMapping("/getUser/{userId}")
    public String getUserInfo(@PathVariable("userId") Integer userId)  {
        User user = userService.queryById(userId);
        System.out.println(user);
        Map<String, Object> map = new HashMap<>();
        map.put("username", user.getUserName());
        map.put("birthday", MyDiskUtils.dateToString_(user.getBirthday()));
        if (user.getSex().equals("male")) {
            map.put("sex", "男");
        } else {
            map.put("sex", "女");
        }
        map.put("other", user.getOther());
        map.put("email", user.getEmail());
        map.put("myPath", user.getMyPage());
        map.put("phone", user.getPhone());
        return MyDiskUtils.getJSONString(200, "获取用户信息", map);
    }

    @PostMapping("/updatePd/{userId}")
    public String updatePd(@PathVariable("userId") Integer userId,@RequestBody Map<String, String> pd) {
        System.out.println(pd);
        userMapper.updatePdByUserId(userId, pd.get("password"));
        return MyDiskUtils.getJSONString(200, "修改密码成功！");
    }
}
