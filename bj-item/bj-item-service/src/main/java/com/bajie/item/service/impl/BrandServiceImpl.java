package com.bajie.item.service.impl;

import com.bajie.common.vo.PageResult;
import com.bajie.item.mapper.BrandMapper;
import com.bajie.item.pojo.Brand;
import com.bajie.item.service.BrandService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

@Service
public class BrandServiceImpl implements BrandService {

    @Autowired
    private BrandMapper brandMapper;

    @Override
    public PageResult<Brand> queryBrandByPageAndSort(Integer page, Integer rows, String sortBy, Boolean desc, String key) {
        // 开启分页
        PageHelper.startPage(page, rows);
        // 过滤
        Example example = new Example(Brand.class);
        if (StringUtils.isNotBlank(key)) {
            example.createCriteria().andLike("name", "%" + key + "%")
                    .orEqualTo("letter", key);
        }
        if (StringUtils.isNotBlank(sortBy)) {
            // 排序
            String orderByClause = sortBy + (desc ? " DESC" : " ASC");
            example.setOrderByClause(orderByClause);
        }
        // 查询
        Page<Brand> pageInfo = (Page<Brand>) brandMapper.selectByExample(example);
        // 返回结果
        return new PageResult<>(pageInfo.getTotal(), pageInfo);
    }

    @Override
    public void deleteBrandById(Long id) {
        brandMapper.deleteByPrimaryKey(id);
    }

    @Override
    public void saveBrand(Brand brand, List<Long> cids) {
        brandMapper.insertSelective(brand);
        for (Long cid : cids
        ) {
            brandMapper.insertCategoryByBrand(cid, brand.getId());
        }
    }

    @Override
    public void deleteCategoryByBrandId(Long id) {
        brandMapper.deleteCategoryByBrandId(id);
    }

    public List<Brand> queryBrandsByCid(Long cid) {

        return brandMapper.queryBrandsByCid(cid);
    }

    @Override
    public Brand queryBrandsByBid(Long bid) {
        return brandMapper.selectByPrimaryKey(bid);
    }

    @Override
    public List<Brand> queryBrandByIds(List<Long> ids) {
        return this.brandMapper.selectByIdList(ids);
    }
}
