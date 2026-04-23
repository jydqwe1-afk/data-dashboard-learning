package com.archive.controller;

import com.archive.common.Result;
import com.archive.entity.ArchiveFile;
import com.archive.mq.LogProducer;
import com.archive.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

@RestController
@RequestMapping("/files")
public class FileController {
    @Autowired
    private FileService fileService;
    @Autowired
    private LogProducer logProducer;

    @PostMapping("/upload")
    public Result<?> upload(@RequestParam("file") MultipartFile file,
                            @RequestParam("archiveId") Long archiveId,
                            Authentication auth) throws Exception {
        Long userId = (Long) auth.getDetails();
        ArchiveFile af = fileService.upload(file, archiveId, userId);
        return Result.ok(af);
    }

    @GetMapping("/{id:\\d+}/download")
    public ResponseEntity<FileSystemResource> download(@PathVariable Long id, Authentication auth) throws UnsupportedEncodingException {
        ArchiveFile af = fileService.getFile(id);
        File file = new File(af.getFilePath());
        Long userId = (Long) auth.getDetails();
        try { logProducer.sendLog(userId, af.getArchiveId(), "download", "下载文件: " + af.getFileName()); } catch (Exception ignored) {}

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentDispositionFormData("attachment",
                URLEncoder.encode(af.getFileName(), "UTF-8"));
        return ResponseEntity.ok().headers(headers).body(new FileSystemResource(file));
    }
}
