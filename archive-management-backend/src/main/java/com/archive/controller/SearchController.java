package com.archive.controller;

import com.archive.common.Result;
import com.archive.service.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/archives/search")
public class SearchController {
    @Autowired
    private SearchService searchService;

    @GetMapping
    public Result<?> search(@RequestParam(defaultValue = "1") int page,
                            @RequestParam(defaultValue = "10") int size,
                            @RequestParam(required = false) String keyword,
                            @RequestParam(required = false) String securityLevel,
                            @RequestParam(required = false) String creator,
                            @RequestParam(required = false) String startDate,
                            @RequestParam(required = false) String endDate) {
        return Result.ok(searchService.search(page, size, keyword, securityLevel, creator, startDate, endDate));
    }
}
