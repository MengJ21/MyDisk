package com.MyDisk.project.entity.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class RecycleDTO {

    private String fileName;
    private String filePath;
    private Double fileSize;
    private String recycleTime;

}
