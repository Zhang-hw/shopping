package com.bajie.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public enum ExceptionEnum {

    KEYWORD_NOT_FOUND(400, "搜索关键字为空"),
    CATEGORY_NOT_FOUND(400, "商品分类为空"),
    BRAND_NOT_FOUND(400, "没有该品牌"),
    BRAND_SAVE_ERROR(500, "添加品牌失败"),
    IMAGE_UPLOAD_ERROR(500, "图片上传失败"),
    FILE_TYPE_NOT_SUPPORT(400, "不支持的文件类型"),
    CATEGORY_NOT_FOUND_BY_BRAND(400, "该品牌没有所属分类"),
    UPDATE_BRAND_ERROR(500, "修改品牌出错"),
    DELETE_BRAND_ERROR(500, "删除品牌出错");
    private int code;
    private String msg;

}
