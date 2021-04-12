package com.bajie.search.client;


import com.bajie.item.api.CategoryApi;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(value = "bj-item")

public interface CategoryClient extends CategoryApi {
}
