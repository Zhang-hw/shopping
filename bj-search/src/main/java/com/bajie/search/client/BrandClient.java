package com.bajie.search.client;


import com.bajie.item.api.BrandApi;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(value = "bj-item")
public interface BrandClient extends BrandApi {
}
