package com.MyDisk.project.entity.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class SharedDTO {

    private String shareName;
    private String fileName;
    private String fileType;
    private String shareTime;

}
