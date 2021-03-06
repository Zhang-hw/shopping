package com.bajie.search.service;

import com.bajie.common.enums.ExceptionEnum;
import com.bajie.common.exception.LyException;
import com.bajie.item.bo.SpuBo;
import com.bajie.item.pojo.*;
import com.bajie.search.client.BrandClient;
import com.bajie.search.client.CategoryClient;
import com.bajie.search.client.GoodsClient;
import com.bajie.search.client.SpecificationClient;
import com.bajie.search.pojo.Goods;
import com.bajie.search.pojo.SearchRequest;
import com.bajie.search.pojo.SearchResult;
import com.bajie.search.repository.GoodsRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.Operator;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.terms.LongTerms;
import org.elasticsearch.search.aggregations.bucket.terms.StringTerms;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.aggregation.AggregatedPage;
import org.springframework.data.elasticsearch.core.query.FetchSourceFilter;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;

@Service
public class IndexService {
    @Autowired
    private ElasticsearchTemplate template;
    @Autowired
    private GoodsRepository goodsRepository;
    public static ObjectMapper objectMapper = new ObjectMapper();
    @Autowired
    private BrandClient brandClient;
    @Autowired
    private SpecificationClient specificationClient;
    @Autowired
    private CategoryClient categoryClient;
    @Autowired
    private GoodsClient goodsClient;

    public Goods buildGoods(SpuBo spu) throws IOException {
        Long id = spu.getId();
        Goods goods = new Goods();
        goods.setId(spu.getId());
        goods.setCid1(spu.getCid1());
        goods.setCid2(spu.getCid2());
        goods.setCid3(spu.getCid3());
        goods.setBrandId(spu.getBrandId());
        goods.setCreateTime(spu.getCreateTime());
        goods.setSubTitle(spu.getSubTitle());

        List<Sku> skus = goodsClient.querySkuBySpuId(spu.getId());
        List<Long> price = new ArrayList<>();
        List<Map<String, Object>> skuList = new ArrayList<>();

        for (Sku sku : skus) {
            price.add(sku.getPrice());
            Map<String, Object> map = new HashMap<>();
            map.put("id", sku.getId());
            map.put("title", sku.getTitle());
            map.put("image", StringUtils.isBlank(sku.getImages()) ? "" : sku.getImages().split(",")[0]);
            map.put("price", sku.getPrice());
            skuList.add(map);
        }
        String skusJson = objectMapper.writeValueAsString(skuList);
        // ????????????
        List<String> names = this.categoryClient.queryNameByIds(Arrays.asList(spu.getCid1(), spu.getCid2(), spu.getCid3()));
        Brand brand = brandClient.queryBrandByBid(spu.getBrandId());

        List<SpecParam> specParams = specificationClient.querySpecParams(null, spu.getCid3(), true, null);
        Map<String, Object> specs = new HashMap<>();
        SpuDetail spuDetail = goodsClient.querySpuDetailById(id);
        String genericSpec = spuDetail.getGenericSpec();
        String specialSpec = spuDetail.getSpecialSpec();
        Map<String, Object> genericSpecMap = objectMapper.readValue(genericSpec, new TypeReference<Map<String, Object>>() {
        });
        Map<String, Object> specialSpecMap = objectMapper.readValue(specialSpec, new TypeReference<Map<String, Object>>() {
        });
        for (SpecParam s : specParams
        ) {
            if (s.getGeneric()) {
                String value = genericSpecMap.get(s.getId().toString()).toString();
                if (s.getNumeric()) {
                    // ???????????????????????????????????????
                    value = chooseSegment(value, s);
                }

                specs.put(s.getName(), value);
            } else {
                specs.put(s.getName(), specialSpecMap.get(s.getId() + ""));
            }

        }


        //all????????????????????????????????????????????????????????????????????????
        goods.setAll(spu.getTitle() + " " + StringUtils.join(names, " ") + " " + brand.getName());


        goods.setSkus(skusJson);
        goods.setSpecs(specs);

        goods.setPrice(price);
        return goods;
    }

    private String chooseSegment(String value, SpecParam p) {
        double val = NumberUtils.toDouble(value);
        String result = "??????";
        // ???????????????
        for (String segment : p.getSegments().split(",")) {
            String[] segs = segment.split("-");
            // ??????????????????
            double begin = NumberUtils.toDouble(segs[0]);
            double end = Double.MAX_VALUE;
            if (segs.length == 2) {
                end = NumberUtils.toDouble(segs[1]);
            }
            // ????????????????????????
            if (val >= begin && val < end) {
                if (segs.length == 1) {
                    result = segs[0] + p.getUnit() + "??????";
                } else if (begin == 0) {
                    result = segs[1] + p.getUnit() + "??????";
                } else {
                    result = segment + p.getUnit();
                }
                break;
            }
        }
        return result;
    }

    public SearchResult search(SearchRequest request) {

        String key = request.getKey();
        if (StringUtils.isBlank(key)) {
            // ?????????????????????????????????????????????????????????????????????null
            throw new LyException(ExceptionEnum.KEYWORD_NOT_FOUND);
        }

        NativeSearchQueryBuilder queryBuilder = new NativeSearchQueryBuilder();
        queryBuilder.withSourceFilter(new FetchSourceFilter(new String[]{"id", "skus", "subTitle"}, null));
        QueryBuilder query = buildBasicQueryWithFilter(request);
        searchWithPageAndSort(queryBuilder, request);
        queryBuilder.withQuery(query);
        // 1.3?????????
        String categoryAggName = "category"; // ????????????????????????
        String brandAggName = "brand"; // ??????????????????
        // ???????????????????????????
        queryBuilder.addAggregation(AggregationBuilders.terms(categoryAggName).field("cid3"));
        queryBuilder.addAggregation(AggregationBuilders.terms(brandAggName).field("brandId"));

        AggregatedPage<Goods> pageInfo = (AggregatedPage<Goods>) this.goodsRepository.search(queryBuilder.build());

        Long total = pageInfo.getTotalElements();
        Long totalPage = (total.longValue() + request.getSize() - 1) / request.getSize();
        List<Category> categories = getCategoryAggResult(pageInfo.getAggregation(categoryAggName));
// 3.3????????????????????????
        List<Brand> brands = getBrandAggResult(pageInfo.getAggregation(brandAggName));
        List<Map<String, Object>> specs = null;
        if (categories.size() == 1) {
            specs = getSpecs(categories.get(0).getId(), query);
        }

        SearchResult searchResult = new SearchResult(total, totalPage, pageInfo.getContent(), categories, brands, specs);
        return searchResult;
    }

    private QueryBuilder buildBasicQueryWithFilter(SearchRequest request) {
        BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();
        // ??????????????????
        queryBuilder.must(QueryBuilders.matchQuery("all", request.getKey()).operator(Operator.AND));
        // ?????????????????????
        BoolQueryBuilder filterQueryBuilder = QueryBuilders.boolQuery();
        // ??????????????????
        Map<String, String> filter = request.getFilter();
        for (Map.Entry<String, String> entry : filter.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            // ????????????????????????????????????
            if (key != "cid3" && key != "brandId") {
                key = "specs." + key + ".keyword";
            }
            // ????????????????????????term??????
            filterQueryBuilder.must(QueryBuilders.termQuery(key, value));
        }
        // ??????????????????
        queryBuilder.filter(filterQueryBuilder);
        return queryBuilder;

    }

    private List<Map<String, Object>> getSpecs(Long id, QueryBuilder query) {
        List<SpecParam> specParams = this.specificationClient.querySpecParams(null, id, true, null);
        List<Map<String, Object>> specs = new ArrayList<>();
        NativeSearchQueryBuilder queryBuilder = new NativeSearchQueryBuilder();
        queryBuilder.withQuery(query);
        specParams.forEach(p -> {
            String key = p.getName();
            queryBuilder.addAggregation(AggregationBuilders.terms(key).field("specs." + key + ".keyword"));
        });
        Map<String, Aggregation> aggs = this.template.query(queryBuilder.build(),
                SearchResponse::getAggregations).asMap();
        specParams.forEach(p -> {
            Map<String, Object> spec = new HashMap<>();
            String key = p.getName();
            spec.put("k", key);
            StringTerms terms = (StringTerms) aggs.get(key);
            spec.put("options", terms.getBuckets().stream().map(StringTerms.Bucket::getKeyAsString));
            specs.add(spec);
        });

        return specs;
    }

    private List<Brand> getBrandAggResult(Aggregation aggregation) {
        LongTerms brandAgg = (LongTerms) aggregation;
        List<Long> bids = new ArrayList<>();
        for (LongTerms.Bucket bucket : brandAgg.getBuckets()) {
            bids.add(bucket.getKeyAsNumber().longValue());
        }
        List<Brand> brands = brandClient.queryBrandByIds(bids);
        return brands;

    }

    private List<Category> getCategoryAggResult(Aggregation aggregation) {
        List<Category> categories = new ArrayList<>();
        LongTerms categoryAgg = (LongTerms) aggregation;
        List<Long> cids = new ArrayList<>();
        for (LongTerms.Bucket bucket : categoryAgg.getBuckets()) {
            cids.add(bucket.getKeyAsNumber().longValue());
        }
        List<String> names = this.categoryClient.queryNameByIds(cids);
        for (int i = 0; i < names.size(); i++) {
            Category c = new Category();
            c.setId(cids.get(i));
            c.setName(names.get(i));
            categories.add(c);
        }
        return categories;
    }

    private void searchWithPageAndSort(NativeSearchQueryBuilder queryBuilder, SearchRequest request) {
        Integer page = request.getPage() - 1;// page ???0??????
        Integer size = request.getSize();
        final String sortBy = request.getSortBy();
        final Boolean descending = request.getDescending();
        queryBuilder.withPageable(PageRequest.of(page, 20));
        if (StringUtils.isNotBlank(sortBy)) {
            queryBuilder.withSort(SortBuilders.fieldSort(sortBy).order(descending ? SortOrder.DESC : SortOrder.ASC));
        }

    }


    public void createIndex(Long id) throws IOException {

        SpuBo spu = (SpuBo) this.goodsClient.querySpuById(id);
        // ????????????
        Goods goods = this.buildGoods(spu);
        // ????????????????????????
        this.goodsRepository.save(goods);
    }

    public void deleteIndex(Long id) {
        this.goodsRepository.deleteById(id);
    }
}
