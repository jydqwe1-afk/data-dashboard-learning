package com.archive.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

@Data
@TableName("security_levels")
public class SecurityLevel {

    @TableId(type = IdType.AUTO)
    private Long id;

    private String name;

    private Integer levelOrder;
}
