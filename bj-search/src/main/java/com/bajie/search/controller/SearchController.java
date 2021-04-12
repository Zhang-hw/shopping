package com.bajie.search.controller;

import com.bajie.search.pojo.SearchRequest;
import com.bajie.search.pojo.SearchResult;
import com.bajie.search.service.IndexService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping
public class SearchController {

    /**
     * 搜索商品
     *
     * @param request
     * @return
     */
    @Autowired
    private IndexService indexService;

    @PostMapping("page")
    public ResponseEntity<SearchResult> search(@RequestBody SearchRequest request) {
        SearchResult searchResult = indexService.search(request);
        return ResponseEntity.ok(searchResult);
    }


}