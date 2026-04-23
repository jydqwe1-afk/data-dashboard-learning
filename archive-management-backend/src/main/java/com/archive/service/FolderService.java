package com.archive.service;

import com.archive.entity.Folder;
import com.archive.mapper.FolderMapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class FolderService {
    @Autowired
    private FolderMapper folderMapper;

    public List<Folder> listAll() {
        return folderMapper.selectList(new LambdaQueryWrapper<Folder>().orderByAsc(Folder::getId));
    }

    public void create(Folder folder) { folderMapper.insert(folder); }
    public void update(Folder folder) { folderMapper.updateById(folder); }
    public void delete(Long id) { folderMapper.deleteById(id); }
}
