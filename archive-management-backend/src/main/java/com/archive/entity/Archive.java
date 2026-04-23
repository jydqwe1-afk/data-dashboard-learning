package com.archive.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("archives")
public class Archive {

    @TableId(type = IdType.AUTO)
    private Long id;

    private String title;

    private Long categoryId;

    private Long securityLevelId;

    private Long folderId;

    private Long departmentId;

    private Long createdBy;

    private Integer status;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
