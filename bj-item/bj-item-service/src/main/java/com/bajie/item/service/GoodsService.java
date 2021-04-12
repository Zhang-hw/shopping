package com.bajie.item.service;

import com.bajie.common.vo.PageResult;
import com.bajie.item.bo.SpuBo;
import com.bajie.item.mapper.*;
import com.bajie.item.pojo.*;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import tk.mybatis.mapper.entity.Example;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class GoodsService {
    /* @Autowired
     private Logger logger;*/
    @Autowired
    private AmqpTemplate amqpTemplate;
    @Autowired
    private StockMapper stockMapper;
    @Autowired
    private SpuMapper spuMapper;
    @Autowired
    private SkuMapper skuMapper;
    @Autowired
    private SpuDetailMapper spuDetailMapper;
    @Autowired
    private BrandMapper brandMapper;
    @Autowired
    private CategoryMapper categoryMapper;

    public PageResult<SpuBo> querySpuByPage(Integer page, Integer rows, Boolean saleable, String key) {
        PageHelper.startPage(page, rows);
        Example example = new Example(Spu.class);
        Example.Criteria criteria = example.createCriteria();

        // 是否过滤上下架
        if (saleable != null) {
            criteria.orEqualTo("saleable", saleable);
        }
        // 是否模糊查询
        if (StringUtils.isNotBlank(key)) {
            criteria.andLike("title", "%" + key + "%");
        }

        Page<Spu> pageInfo = (Page) spuMapper.selectByExample(example);

        List<SpuBo> list1 = pageInfo.stream().map((spu) -> {
            SpuBo spuBo = new SpuBo();
            BeanUtils.copyProperties(spu, spuBo);
            //设置分类表示
            spuBo.setValid(true);
            List<Category> categories = categoryMapper.selectByIdList(Arrays.asList(spuBo.getCid1(), spuBo.getCid2(), spuBo.getCid3()));
            StringBuilder sb = new StringBuilder();
            for (Category c : categories
            ) {

                sb.append(c.getName() + "/");
            }
            spuBo.setCname(sb.toString());//手机/手机通讯/手机/
            //设置品牌名称
            Brand brand = brandMapper.selectByPrimaryKey(spuBo.getBrandId());
            spuBo.setBname(brand.getName());
            return spuBo;
        }).collect(Collectors.toList());
        return new PageResult<>(pageInfo.getTotal(), list1);
    }

    public SpuDetail querySpuDetailsById(Long spuId) {

        return spuDetailMapper.selectByPrimaryKey(spuId);
    }

    public List<Sku> querySkusById(Long id) {

        return skuMapper.selectSkusBySpuid(id);
    }

    /**
     * 新增商品
     *
     * @param
     */
    @Transactional
    public void saveGoods(SpuBo spu) {
        spu.setSaleable(true);
        spu.setValid(true);
        spu.setCreateTime(new Date());
        spu.setLastUpdateTime(spu.getCreateTime());
        spuMapper.insert(spu);

        spu.getSpuDetail().setSpuId(spu.getId());
        this.spuDetailMapper.insert(spu.getSpuDetail());
        saveSkuAndStock(spu.getSkus(), spu.getId());

        sendMessage(spu.getId(), "insert");


    }

    private void saveSkuAndStock(List<Sku> skus, Long spuId) {
        for (Sku sku : skus) {
            if (!sku.getEnable()) {
                continue;
            }
            // 保存sku
            sku.setSpuId(spuId);
            // 初始化时间
            sku.setCreateTime(new Date());
            sku.setLastUpdateTime(sku.getCreateTime());
            this.skuMapper.insert(sku);


            // 保存库存信息
            Stock stock = new Stock();
            stock.setSkuId(sku.getId());
            stock.setStock(sku.getStock());
            this.stockMapper.insert(stock);
        }

    }

    @Transactional
    public void update(SpuBo spu) {
        // 查询以前sku
        List<Sku> skus = this.querySkusById(spu.getId());
        // 如果以前存在，则删除
        if (!CollectionUtils.isEmpty(skus)) {
            List<Long> ids = skus.stream().map(s -> s.getId()).collect(Collectors.toList());
            // 删除以前库存
            Example example = new Example(Stock.class);
            example.createCriteria().andIn("skuId", ids);
            this.stockMapper.deleteByExample(example);

            // 删除以前的sku
            Sku record = new Sku();
            record.setSpuId(spu.getId());
            this.skuMapper.delete(record);
        }
        // 新增sku和库存
        saveSkuAndStock(spu.getSkus(), spu.getId());

        // 更新spu
        spu.setLastUpdateTime(new Date());
        spu.setCreateTime(null);
        spu.setValid(null);
        spu.setSaleable(null);
        this.spuMapper.updateByPrimaryKeySelective(spu);
        // 更新spu详情
        this.spuDetailMapper.updateByPrimaryKeySelective(spu.getSpuDetail());
        sendMessage(spu.getId(), "update");
    }


    public Spu querySpuById(Long id) {
        return this.spuMapper.selectByPrimaryKey(id);
    }


    private void sendMessage(Long id, String type) {
        // 发送消息
        try {
            this.amqpTemplate.convertAndSend("item." + type, id);
        } catch (Exception e) {
            //logger.error("{}商品消息发送异常，商品id：{}", type, id, e);
            System.out.println("异常");
        }
    }

    public Sku querySkuById(Long id) {
        return skuMapper.selectByPrimaryKey(id);
    }
}