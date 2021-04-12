package com.bajie.item.api;


import com.bajie.item.pojo.Category;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@RequestMapping("category")
public interface CategoryApi {
    @GetMapping("list")
    public List<Category> queryByParentId(@RequestParam(value = "pid", defaultValue = "0") Long pid);

    @GetMapping("names")
    public List<String> queryNameByIds(@RequestParam("ids") List<Long> ids);

    @GetMapping("bid/{bid}")
    public List<Category> queryByBrandId(@PathVariable("bid") Long bid);
}
