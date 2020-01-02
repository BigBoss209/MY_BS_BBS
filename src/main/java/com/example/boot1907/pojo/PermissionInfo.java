package com.example.boot1907.pojo;

import lombok.Data;

@Data
public class PermissionInfo {
    private Integer perId;
    private String perName;
    private String perInfo;
    private Integer perParentId;
    private Integer perLevel;
    private Integer valid;
}