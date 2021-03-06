package com.bajie.search.pojo;

import com.bajie.common.vo.PageResult;
import com.bajie.item.pojo.Brand;
import com.bajie.item.pojo.Category;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class SearchResult extends PageResult<Goods> {
    private List<Category> categories;
    private List<Map<String, Object>> specs; // 规格参数过滤条件
    private List<Brand> brands;

    public SearchResult(Long total, Long totalPage, List<Goods> items, List<Category> categories, List<Brand> brands, List<Map<String, Object>> specs) {
        super(total, totalPage, items);
        this.categories = categories;
        this.brands = brands;
        this.specs = specs;
    }
}
