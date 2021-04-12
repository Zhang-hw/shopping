package com.bajie.item.api;


import com.bajie.common.vo.PageResult;
import com.bajie.item.pojo.Brand;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@RequestMapping("brand")
public interface BrandApi {
    @GetMapping("page")
    public PageResult<Brand> queryBrandByPage(
            @RequestParam(value = "page", defaultValue = "1") Integer page,
            @RequestParam(value = "rows", defaultValue = "5") Integer rows,
            @RequestParam(value = "sortBy", required = false) String sortBy,
            @RequestParam(value = "desc", defaultValue = "false") Boolean desc,
            @RequestParam(value = "key", required = false) String key);

    @GetMapping("cid/{cid}")
    public List<Brand> queryBrandsByCid(@PathVariable("cid") Long cid);

    @GetMapping("{bid}")
    public Brand queryBrandByBid(@PathVariable("bid") Long bid);

    @GetMapping("list")
    List<Brand> queryBrandByIds(@RequestParam("ids") List<Long> ids);

}
