package com.archive.service;

import com.archive.entity.Department;
import com.archive.mapper.DepartmentMapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class DepartmentService {
    @Autowired
    private DepartmentMapper deptMapper;

    public List<Department> listAll() {
        return deptMapper.selectList(new LambdaQueryWrapper<Department>().orderByAsc(Department::getId));
    }

    public Department getById(Long id) { return deptMapper.selectById(id); }
    public void create(Department dept) { deptMapper.insert(dept); }
    public void update(Department dept) { deptMapper.updateById(dept); }
    public void delete(Long id) { deptMapper.deleteById(id); }
}
