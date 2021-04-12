package com.bajie.item.api;


import com.bajie.common.vo.PageResult;
import com.bajie.item.bo.SpuBo;
import com.bajie.item.pojo.Sku;
import com.bajie.item.pojo.Spu;
import com.bajie.item.pojo.SpuDetail;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@RequestMapping
public interface GoodsApi {
    @GetMapping("spu/page")
    public PageResult<SpuBo> querySpuByPage(
            @RequestParam(value = "page", defaultValue = "1") Integer page,
            @RequestParam(value = "rows", defaultValue = "5") Integer rows,
            @RequestParam(value = "saleable", required = false) Boolean saleable,
            @RequestParam(value = "key", required = false) String key);

    @GetMapping("/spu/detail/{id}")
    public SpuDetail querySpuDetailById(@PathVariable("id") Long id);

    @GetMapping("/sku/list")
    public List<Sku> querySkuBySpuId(@RequestParam("id") Long id);

    @GetMapping("/spu/{id}")
    Spu querySpuById(@PathVariable("id") Long id);

    @GetMapping("/sku")
    Sku querySkuById(@RequestParam("id") Long id);

}
