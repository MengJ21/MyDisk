package com.MyDisk.project.service;


import com.MyDisk.project.entity.User;
import java.util.List;

/**
 * @InterfaceName: MyFileService
 * @Description: 用户业务层接口
 **/
public interface UserService {

    /**
     * @Description 添加User
     */
    boolean insert(User user);

    /**
     * @Description 删除User
     */
    boolean deleteById(Integer userId);

    /**
     * @Description 查询单条数据
     */
    User queryById(Integer userId);


    /**
     * @Description 通过邮箱和密码查询单条数据
     */
    User queryByEmailAndPwd(String email, String password);

    /**
     * @Description 修改数据，哪个属性不为空就修改哪个属性
     */
    boolean update(User user);


}