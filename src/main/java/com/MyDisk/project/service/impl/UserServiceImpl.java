package com.MyDisk.project.service.impl;

import com.MyDisk.project.mapper.UserMapper;
import com.MyDisk.project.service.UserService;
import com.MyDisk.project.entity.User;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
* @ClassName UserServiceImpl
* @Description (User)表服务实现类
**/
@Service
public class UserServiceImpl implements UserService {

    @Resource
    private UserMapper userMapper;

   /**
    * @Description 添加User
    */
   @Override
   public boolean insert(User user) {
       if(userMapper.insert(user) == 1){
           return true;
       }
       return false;
   }

   /**
    * @Description 删除User
    */
   @Override
   public boolean deleteById(Integer userId) {
       if(userMapper.deleteById(userId) == 1){
           return true;
       }
       return false;
   }

   /**
    * @Description 查询单条数据
    */
   @Override
   public User queryById(Integer userId) {
       return userMapper.queryById(userId);
   }


    /**
     * @Description 通过邮箱和密码查询单条数据
     */
    @Override
    public User queryByEmailAndPwd(String email, String password) {
        return userMapper.queryByEmailAndPwd(email,password);
    }

   /**
    * @Description 修改数据，哪个属性不为空就修改哪个属性
    */
   @Override
   public boolean update(User user) {
       if(userMapper.update(user) == 1){
           return true;
       }
       return false;
   }



}