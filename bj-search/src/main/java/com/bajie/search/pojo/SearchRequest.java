package com.bajie.search.pojo;

import java.util.Map;

public class SearchRequest {
    private String key;// 搜索条件
    private String sortBy;
    private Boolean descending;
    private Integer page;// 当前页
    private Map<String, String> filter;

    private static final Integer DEFAULT_SIZE = 20;// 每页大小，不从页面接收，而是固定大小
    private static final Integer DEFAULT_PAGE = 1;// 默认页

    public Map<String, String> getFilter() {
        return filter;
    }

    public void setFilter(Map<String, String> filter) {
        this.filter = filter;
    }

    public void setDescending(Boolean descending) {
        this.descending = descending;
    }

    public Boolean getDescending() {
        return descending;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public void setSortBy(String sortBy) {
        this.sortBy = sortBy;
    }

    public String getSortBy() {
        return sortBy;
    }

    public static Integer getDefaultPage() {
        return DEFAULT_PAGE;
    }

    public static Integer getDefaultSize() {
        return DEFAULT_SIZE;
    }

    public Integer getPage() {
        if (page == null) {
            return DEFAULT_PAGE;
        }
        // 获取页码时做一些校验，不能小于1
        return Math.max(DEFAULT_PAGE, page);
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Integer getSize() {
        return DEFAULT_SIZE;
    }


}
