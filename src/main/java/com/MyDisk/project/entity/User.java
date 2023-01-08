package com.MyDisk.project.entity;

import lombok.*;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class User {

    private Integer userId;
    private Integer fileStoreId;
    // 用户名不可重复
    private String userName;
    private String email;
    private String password;
    private Date registerTime;
    private String imagePath;
    private Integer role;
    private Date birthday;
    private String sex;
    private String myPage;
    private String phone;
    private String other;

}
