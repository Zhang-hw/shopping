package com.bajie.item.service.impl;

import com.bajie.item.mapper.CategoryMapper;
import com.bajie.item.pojo.Category;
import com.bajie.item.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryMapper categoryMapper;

    @Override
    public List<Category> queryCategoryListByParentId(Long pid) {
        Category record = new Category();
        record.setParentId(pid);
        return this.categoryMapper.select(record);
    }

    @Override
    public List<Category> queryByBrandId(Long bid) {
        return categoryMapper.queryByBrandId(bid);
    }

    @Override
    public List<String> queryNamesByIds(List<Long> ids) {
        return categoryMapper.selectByIdList(ids).stream()
                .map(Category::getName).collect(Collectors.toList());
    }


}
