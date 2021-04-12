package com.bajie.item.controller;

import com.bajie.common.vo.PageResult;
import com.bajie.item.pojo.Brand;
import com.bajie.item.service.BrandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/brand")
public class BrandController {

    @Autowired
    private BrandService brandService;

    @RequestMapping("/page")
    public ResponseEntity<PageResult<Brand>> queryBrandByPage(@RequestParam(value = "page", defaultValue = "1") Integer page,
                                                              @RequestParam(value = "rows", defaultValue = "5") Integer rows,
                                                              @RequestParam(value = "sortBy", required = false) String sortBy,
                                                              @RequestParam(value = "desc", defaultValue = "false") Boolean desc,
                                                              @RequestParam(value = "key", required = false) String key) {
        PageResult<Brand> result = brandService.queryBrandByPageAndSort(page, rows, sortBy, desc, key);
        if (result == null || result.getItems().size() == 0) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(result);
    }

    // 删除品牌
    @RequestMapping(method = RequestMethod.DELETE)
    public ResponseEntity deleteBrandById(Long id) {
        brandService.deleteBrandById(id);
        return ResponseEntity.ok(null);
    }

    // 新增品牌
    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity saveBrand(Brand brand, @RequestParam("cids") List<Long> cids) {
        brandService.saveBrand(brand, cids);
        return ResponseEntity.ok(null);
    }

    // 修改品牌
    @RequestMapping(method = RequestMethod.PUT)
    public ResponseEntity editBrand(Brand brand, @RequestParam("cids") List<Long> cids) {
        brandService.deleteBrandById(brand.getId());
        brandService.deleteCategoryByBrandId(brand.getId());
        brandService.saveBrand(brand, cids);
        return ResponseEntity.ok(null);
    }

    @GetMapping("/cid/{cid}")
    public ResponseEntity<List<Brand>> queryBrandsByCid(@PathVariable("cid") Long cid) {
        List<Brand> brands = brandService.queryBrandsByCid(cid);
        return ResponseEntity.ok(brands);
    }

    @GetMapping("{bid}")
    public ResponseEntity<Brand> queryBrandByBid(@PathVariable("bid") Long bid) {
        return ResponseEntity.ok(brandService.queryBrandsByBid(bid));
    }

    @GetMapping("list")
    public ResponseEntity<List<Brand>> queryBrandByIds(@RequestParam("ids") List<Long> ids) {
        List<Brand> list = this.brandService.queryBrandByIds(ids);
        if (list == null) {
            new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(list);
    }


}
