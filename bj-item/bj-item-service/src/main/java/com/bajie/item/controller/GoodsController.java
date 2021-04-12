package com.bajie.item.controller;

import com.bajie.common.vo.PageResult;
import com.bajie.item.bo.SpuBo;
import com.bajie.item.pojo.Sku;
import com.bajie.item.pojo.Spu;
import com.bajie.item.pojo.SpuDetail;
import com.bajie.item.service.GoodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping
public class GoodsController {
    @Autowired
    private GoodsService goodsService;

    @GetMapping("/spu/page")
    public ResponseEntity<PageResult<SpuBo>> querySpuByPage(
            @RequestParam(value = "page", defaultValue = "1") Integer page,
            @RequestParam(value = "rows", defaultValue = "5") Integer rows,
            @RequestParam(value = "saleable", required = false) Boolean saleable,
            @RequestParam(value = "key", required = false) String key) {
        PageResult<SpuBo> pageResult = goodsService.querySpuByPage(page, rows, saleable, key);
        return ResponseEntity.ok(pageResult);
    }

    @GetMapping("/spu/detail/{id}")
    public ResponseEntity<SpuDetail> querySpuByPage(@PathVariable("id") Long spuId) {
        SpuDetail spuDetail = goodsService.querySpuDetailsById(spuId);
        return ResponseEntity.ok(spuDetail);
    }

    @GetMapping("/sku/list")
    public ResponseEntity<List<Sku>> querySkusById(Long id) {
        List<Sku> skus = goodsService.querySkusById(id);
        return ResponseEntity.ok(skus);
    }

    @PostMapping("/goods")
    public ResponseEntity<Void> saveGoods(@RequestBody SpuBo spuBo) {
        try {
            this.goodsService.saveGoods(spuBo);
            return new ResponseEntity<>(HttpStatus.CREATED);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * 新增商品
     *
     * @param spu
     * @return
     */
    @PutMapping("/goods")
    public ResponseEntity<Void> updateGoods(@RequestBody SpuBo spu) {
        try {
            this.goodsService.update(spu);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/spu/{id}")
    public ResponseEntity<Spu> querySpuById(@PathVariable("id") Long id) {
        Spu spu = this.goodsService.querySpuById(id);
        if (spu == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(spu);
    }

    @GetMapping("/sku")
    public ResponseEntity<Sku> querySkuById(@RequestParam("id") Long id) {
        Sku sku = this.goodsService.querySkuById(id);
        if (sku == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(sku);
    }

}
