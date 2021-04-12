package com.bajie.item.service;

import com.bajie.item.pojo.Category;

import java.util.List;

public interface CategoryService {
    List<Category> queryCategoryListByParentId(Long pid);

    List<Category> queryByBrandId(Long bid);

    List<String> queryNamesByIds(List<Long> ids);
}
