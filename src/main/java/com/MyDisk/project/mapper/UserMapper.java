package com.MyDisk.project.mapper;

import com.MyDisk.project.entity.User;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface UserMapper {

    int insert(User user);

    int deleteById(Integer userId);

    User queryById(Integer userId);

    User queryByEmailAndPwd(String email,String password);

    User getUserByEmail(String email);
    int update(User user);

    Integer queryFileStoreIdByUserId(Integer userId);

    Integer queryUserIdByUserName(String username);

    Integer updatePdByUserId(Integer userId, String password);

    Integer updateFileStoreIdByUserId(Integer userId, Integer fileStoreId);

}
