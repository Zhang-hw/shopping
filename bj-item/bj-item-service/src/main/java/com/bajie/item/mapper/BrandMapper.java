package com.bajie.item.mapper;

import com.bajie.item.pojo.Brand;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.additional.idlist.SelectByIdListMapper;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface BrandMapper extends Mapper<Brand>, SelectByIdListMapper<Brand, Long> {

    @Insert("insert into tb_category_brand(category_id,brand_id) values(#{cid},#{id})")
    void insertCategoryByBrand(@Param("cid") Long cid, @Param("id") Long id);

    @Delete("delete from tb_category_brand where brand_id=#{id}")
    void deleteCategoryByBrandId(Long id);

    @Select("select * from tb_brand where id in (select brand_id from tb_category_brand where category_id =#{id})")
    List<Brand> queryBrandsByCid(Long cid);
}
