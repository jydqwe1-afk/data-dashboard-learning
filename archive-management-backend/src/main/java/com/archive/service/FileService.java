package com.archive.service;

import com.archive.entity.ArchiveFile;
import com.archive.mapper.ArchiveFileMapper;
import com.archive.mq.LogProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@Service
public class FileService {
    @Autowired
    private ArchiveFileMapper fileMapper;
    @Autowired
    private LogProducer logProducer;
    @Value("${archive.upload-dir}")
    private String uploadDir;

    public ArchiveFile upload(MultipartFile file, Long archiveId, Long userId) throws IOException {
        String datePath = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));
        String dir = uploadDir + "/" + datePath;
        File dirFile = new File(dir);
        if (!dirFile.exists()) dirFile.mkdirs();

        String originalName = file.getOriginalFilename();
        String ext = originalName != null && originalName.contains(".")
                ? originalName.substring(originalName.lastIndexOf(".")) : "";
        String savedName = UUID.randomUUID().toString() + ext;
        String savedPath = dir + "/" + savedName;

        file.transferTo(new File(savedPath));

        ArchiveFile af = new ArchiveFile();
        af.setArchiveId(archiveId);
        af.setFileName(originalName);
        af.setFilePath(savedPath);
        af.setFileType(ext.replace(".", ""));
        af.setFileSize(file.getSize());
        af.setUploadedBy(userId);
        fileMapper.insert(af);

        try { logProducer.sendLog(userId, archiveId, "upload", "上传文件: " + originalName); } catch (Exception ignored) {}
        return af;
    }

    public ArchiveFile getFile(Long fileId) {
        return fileMapper.selectById(fileId);
    }
}
