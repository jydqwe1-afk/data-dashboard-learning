package com.archive.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("departments")
public class Department {

    @TableId(type = IdType.AUTO)
    private Long id;

    private String name;

    private String description;

    private LocalDateTime createdAt;
}
