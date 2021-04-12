package com.bajie.item.mapper;

import com.bajie.item.pojo.Sku;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface SkuMapper extends Mapper<Sku> {
    @Select("select * from tb_sku where spu_id=#{id}")
    List<Sku> selectSkusBySpuid(Long id);

}
