package com.archive.controller;

import com.archive.common.Result;
import com.archive.entity.AccessApplication;
import com.archive.service.ApplicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/applications")
public class ApplicationController {
    @Autowired
    private ApplicationService appService;

    @GetMapping("/mine")
    public Result<?> mine(@RequestParam(defaultValue = "1") int page,
                          @RequestParam(defaultValue = "10") int size,
                          Authentication auth) {
        Long userId = (Long) auth.getDetails();
        return Result.ok(appService.myApplications(userId, page, size));
    }

    @PostMapping
    public Result<?> create(@RequestBody AccessApplication app, Authentication auth) {
        Long userId = (Long) auth.getDetails();
        app.setApplicantId(userId);
        appService.create(app);
        return Result.ok();
    }

    @GetMapping("/reviews")
    public Result<?> reviews(@RequestParam(defaultValue = "1") int page,
                             @RequestParam(defaultValue = "10") int size) {
        return Result.ok(appService.pendingReviews(null, page, size));
    }

    @PutMapping("/{id:\\d+}/review")
    public Result<?> review(@PathVariable Long id, @RequestBody Map<String, Object> body, Authentication auth) {
        Long userId = (Long) auth.getDetails();
        boolean approved = "approve".equals(body.get("action"));
        String comment = (String) body.getOrDefault("comment", "");
        appService.review(id, userId, approved, comment);
        return Result.ok();
    }
}
