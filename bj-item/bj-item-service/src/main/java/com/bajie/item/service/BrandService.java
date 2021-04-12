package com.bajie.item.service;

import com.bajie.common.vo.PageResult;
import com.bajie.item.pojo.Brand;

import java.util.List;

public interface BrandService {

    PageResult<Brand> queryBrandByPageAndSort(Integer page, Integer rows, String sortBy, Boolean desc, String key);

    void deleteBrandById(Long id);

    void saveBrand(Brand brand, List<Long> cids);

    void deleteCategoryByBrandId(Long id);

    List<Brand> queryBrandsByCid(Long cid);

    Brand queryBrandsByBid(Long bid);

    List<Brand> queryBrandByIds(List<Long> ids);
}
