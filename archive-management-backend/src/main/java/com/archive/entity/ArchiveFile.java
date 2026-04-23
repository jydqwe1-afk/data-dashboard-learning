package com.archive.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("archive_files")
public class ArchiveFile {

    @TableId(type = IdType.AUTO)
    private Long id;

    private Long archiveId;

    private String fileName;

    private String filePath;

    private String fileType;

    private Long fileSize;

    private Long uploadedBy;

    private LocalDateTime uploadedAt;
}
