package com.MyDisk.project.entity;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Date;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
/**
 * 上传的文件信息
 */
public class File {

    private Integer fileId;
    private Integer fileStoreId;
    private String fileName;
    // 文件大小K计算
    private String fileType;
    private Double fileSize;
    private Date createTime;
    private Integer recycle;
    private Date recycleTime;

}
