package com.MyDisk.project.entity.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@ToString
@NoArgsConstructor
@AllArgsConstructor
@Data
public class SaveDTO {

    private String fileName;
    private String filePath;
    private Double fileSize;
    private String shareUser;
    private String saveTime;

}
