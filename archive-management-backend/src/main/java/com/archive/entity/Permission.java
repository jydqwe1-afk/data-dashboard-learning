package com.archive.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

@Data
@TableName("permissions")
public class Permission {

    @TableId(type = IdType.AUTO)
    private Long id;

    private Long userId;

    private Long archiveId;

    private String permissionType;
}
