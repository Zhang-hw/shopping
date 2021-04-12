package com.bajie.order.service.api;

import com.bajie.item.api.GoodsApi;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(value = "bj-item")
public interface GoodsService extends GoodsApi {
}
