package com.bajie.cart.client;

import com.bajie.item.api.GoodsApi;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient("bj-item")
public interface GoodsClient extends GoodsApi {
}
