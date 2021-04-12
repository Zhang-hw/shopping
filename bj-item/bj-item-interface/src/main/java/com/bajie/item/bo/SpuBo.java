package com.bajie.item.bo;

import com.bajie.item.pojo.Sku;
import com.bajie.item.pojo.Spu;
import com.bajie.item.pojo.SpuDetail;
import lombok.Data;

import javax.persistence.Transient;
import java.io.Serializable;
import java.util.List;

@Data
public class SpuBo extends Spu implements Serializable {
    String cname;// 商品分类名称

    String bname;// 品牌名称
    @Transient
    SpuDetail spuDetail;// 商品详情
    @Transient
    List<Sku> skus;// sku列表

}
