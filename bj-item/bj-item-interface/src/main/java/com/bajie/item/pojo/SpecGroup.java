package com.bajie.item.pojo;

import lombok.Data;
import tk.mybatis.mapper.annotation.KeySql;

import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.io.Serializable;
import java.util.List;

@Data
@Table(name = "tb_spec_group")
public class SpecGroup implements Serializable {
    @Id
    @KeySql(useGeneratedKeys = true)
    private Long id;

    private Long cid;

    private String name;
    @Transient
    private List<SpecParam> params; //该组下所有的参数集合
}
