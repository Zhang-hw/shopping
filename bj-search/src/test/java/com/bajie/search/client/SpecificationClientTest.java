package com.bajie.search.client;

import com.bajie.common.vo.PageResult;
import com.bajie.item.bo.SpuBo;
import com.bajie.search.SearchApp;
import com.bajie.search.pojo.Goods;
import com.bajie.search.repository.GoodsRepository;
import com.bajie.search.service.IndexService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = SearchApp.class)
public class SpecificationClientTest {
    @Autowired
    private IndexService indexService;
    @Autowired
    private CategoryClient categoryClient;
    @Autowired
    private ElasticsearchTemplate template;
    @Autowired
    private GoodsRepository goodsRepository;
    @Autowired
    private GoodsClient goodsClient;

    @Test
    public void testQueryCategories() {
        List<String> names = this.categoryClient.queryNameByIds(Arrays.asList(1L, 2L, 3L));
        names.forEach(System.out::println);
    }


    @Test
    public void createIndex() {
        template.createIndex(Goods.class);
        template.putMapping(Goods.class);
    }

    @Test
    public void upload() throws IOException {
        int page = 1;
        int rows = 100;
        int size = 0;
        do {
            PageResult<SpuBo> result = goodsClient.querySpuByPage(page, rows, null, null);
            List<SpuBo> spus = result.getItems();
            List<Goods> goods = new ArrayList<>();
                /*List<Goods> goods = spus.stream().map(spu ->{return indexService.buildGoods(spu);})
                        .collect(Collectors.toList());*/
            for (SpuBo s : spus
            ) {
                Goods good = indexService.buildGoods(s);
                goods.add(good);
            }
            goodsRepository.saveAll(goods);

            size = spus.size();
            page++;


        } while (size == 100);
    }

}