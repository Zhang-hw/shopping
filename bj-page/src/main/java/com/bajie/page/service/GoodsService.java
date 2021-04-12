package com.bajie.page.service;

import com.bajie.item.pojo.*;
import com.bajie.page.client.BrandClient;
import com.bajie.page.client.CategoryClient;
import com.bajie.page.client.GoodsClient;
import com.bajie.page.client.SpecificationClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class GoodsService {
    @Autowired
    private GoodsClient goodsClient;

    @Autowired
    private CategoryClient categoryClient;

    @Autowired
    private BrandClient brandClient;

    @Autowired
    private SpecificationClient specificationClient;


    public Map<String, Object> loadModel(Long id) {
        try {
            // 模型数据
            Map<String, Object> modelMap = new HashMap<>();

            // 查询spu
            Spu spu = this.goodsClient.querySpuById(id);
            // 查询spuDetail
            SpuDetail detail = this.goodsClient.querySpuDetailById(id);
            // 查询sku
            List<Sku> skus = this.goodsClient.querySkuBySpuId(id);

            // 装填模型数据
            modelMap.put("spu", spu);
            modelMap.put("spuDetail", detail);
            modelMap.put("skus", skus);

            // 准备商品分类
            List<Category> categories = getCategories(spu);
            if (categories != null) {
                modelMap.put("categories", categories);
            }

            // 准备品牌数据
            List<Brand> brands = this.brandClient.queryBrandByIds(
                    Arrays.asList(spu.getBrandId()));
            modelMap.put("brand", brands.get(0));

            // 查询规格组及组内参数
            List<SpecGroup> groups = this.specificationClient.querySpecsByCid(spu.getCid3());
            modelMap.put("groups", groups);

            // 查询商品分类下的特有规格参数
            List<SpecParam> params =
                    this.specificationClient.querySpecParams(null, spu.getCid3(), null, false);
            // 处理成id:name格式的键值对
            Map<Long, String> paramMap = new HashMap<>();
            for (SpecParam param : params) {
                paramMap.put(param.getId(), param.getName());
            }
            modelMap.put("paramMap", paramMap);
            return modelMap;

        } catch (Exception e) {
        }
        return null;
    }

    private List<Category> getCategories(Spu spu) {
        try {
            List<String> names = this.categoryClient.queryNameByIds(
                    Arrays.asList(spu.getCid1(), spu.getCid2(), spu.getCid3()));
            Category c1 = new Category();
            c1.setName(names.get(0));
            c1.setId(spu.getCid1());

            Category c2 = new Category();
            c2.setName(names.get(1));
            c2.setId(spu.getCid2());

            Category c3 = new Category();
            c3.setName(names.get(2));
            c3.setId(spu.getCid3());

            return Arrays.asList(c1, c2, c3);
        } catch (Exception e) {
        }
        return null;
    }
}
