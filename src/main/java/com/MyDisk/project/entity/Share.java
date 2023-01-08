package com.MyDisk.project.entity;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
/**
 * 分享文件
 */
public class Share {

    private Integer id;
    private Integer userId;
    private Integer fileId;
    private Integer sharedUserId;
    private Date shareTime;
    // 是否被收藏
    private Integer like;
    private Date likeTime;

}
