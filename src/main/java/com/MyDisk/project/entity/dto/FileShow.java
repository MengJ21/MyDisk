package com.MyDisk.project.entity.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class FileShow {

    private String fileName;
    private Double fileSize;
    private String zTime;

}
