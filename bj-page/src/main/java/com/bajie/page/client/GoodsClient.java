package com.bajie.page.client;

import com.bajie.item.api.GoodsApi;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(value = "bj-item")
public interface GoodsClient extends GoodsApi {

}
