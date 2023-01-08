package com.MyDisk.project.entity.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class ShareDTO {

    private String fileName;
    private String fileType;
    private String shareDate;
//    private String shareUser;
    private String sharedUser;

}
