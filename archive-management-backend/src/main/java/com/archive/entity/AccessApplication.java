package com.archive.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("access_applications")
public class AccessApplication {

    @TableId(type = IdType.AUTO)
    private Long id;

    private Long applicantId;

    private Long archiveId;

    private String purpose;

    private Integer status;

    private Long reviewerId;

    private String reviewComment;

    private LocalDateTime createdAt;

    private LocalDateTime reviewedAt;
}
